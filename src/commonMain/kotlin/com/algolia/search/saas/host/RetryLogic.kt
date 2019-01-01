package com.algolia.search.saas.host

import com.algolia.search.saas.data.ApplicationId
import io.ktor.client.features.BadResponseStatusException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlinx.io.IOException
import kotlin.math.floor


internal class RetryLogic(
    applicationId: ApplicationId,
    type: Type,
    private val hostStatusExpirationDelay: Long = 1000L * 60L * 5L
) {

    enum class Type {
        Read,
        Write
    }

    private val host = when (type) {
        Type.Read -> applicationId.readHost
        Type.Write -> applicationId.writeHost
    }
    internal val hosts = listOf(host) + applicationId.computeHosts().randomize()
    internal val statuses = hosts.initialHostStatus()

    private suspend fun <T> retry(
        timeout: Long,
        path: String,
        attempt: Int,
        request: suspend (String) -> T
    ): T {
        if (statuses.areStatusExpired(hostStatusExpirationDelay)) {
            for (index in statuses.indices) {
                statuses[index] = HostStatus.Unknown to 0L
            }
        }
        val index = statuses.selectNextHostIndex()
        val host = hosts[index]

        return try {
            withTimeout(timeout * attempt) {
                val response = request("$host$path")
                statuses[index] = HostStatus.Up.getHostStatus()
                response
            }
        } catch (exception: TimeoutCancellationException) {
            statuses[index] = HostStatus.Down.getHostStatus()
            retry(timeout, path, attempt + 1, request)
        } catch (exception: BadResponseStatusException) {
            val code = exception.response.status.value
            val isSuccessful = floor(code / 100f) == 2f
            val isRetryable = floor(code / 100f) != 4f && !isSuccessful

            if (isRetryable) {
                statuses[index] = HostStatus.Down.getHostStatus()
                retry(timeout, path, attempt, request)
            } else throw exception
        } catch (exception: IOException) {
            statuses[index] = HostStatus.Down.getHostStatus()
            retry(timeout, path, attempt, request)
        }
    }

    suspend fun <T> retry(
        timeout: Long,
        path: String,
        request: suspend (String) -> T
    ): T {
        return retry(timeout, path, 1, request)
    }
}