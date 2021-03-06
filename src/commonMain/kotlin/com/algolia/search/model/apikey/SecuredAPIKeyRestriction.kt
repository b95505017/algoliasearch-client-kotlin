package com.algolia.search.model.apikey

import com.algolia.search.model.APIKey
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken
import com.algolia.search.model.search.Query
import com.algolia.search.serialize.toJsonNoDefaults
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.content


/**
 * Create restrictions for an [APIKey].
 *
 * @param query A mapping of search parameters that will be forced at query time.
 * @param restrictIndices List of [IndexName] that can be queried.
 * @param restrictSources IPv4 network allowed to use the generated key. This is used for more protection against
 * [APIKey] leaking and reuse.
 * @param validUntil A Unix timestamp used to define the expiration date of the [APIKey].
 * @param userToken Specify a user identifier. This is often used with rate limits. By default, rate limits will only
 * use the IP. This can be an issue when several of your end users are using the same IP. To avoid that, you can set a
 * userToken query parameter when generating the key. When set, a unique user will be identified by his IP + user_token
 * instead of only by his IP. This allows you to restrict a single user to performing a maximum of N API calls per hour,
 * even if he shares his IP with another user.
 */
public data class SecuredAPIKeyRestriction(
    val query: Query? = null,
    val restrictIndices: List<IndexName>? = null,
    val restrictSources: List<String>? = null,
    val validUntil: Long? = null,
    val userToken: UserToken? = null
) {

    internal fun buildRestrictionString(): String {
        return Parameters.build {
            query?.let { query ->
                query.toJsonNoDefaults().forEach { (key, element) ->
                    when (element) {
                        is JsonArray -> appendAll(key, element.content.map { it.content })
                        else -> append(key, element.content)
                    }
                }
            }
            restrictIndices?.let { append("restrictIndices", it.joinToString(";") { indexName -> indexName.raw }) }
            restrictSources?.let { append("restrictSources", it.joinToString(";")) }
            userToken?.let { append("userToken", it.raw) }
            validUntil?.let { append("validUntil", it.toString()) }
        }.formUrlEncode()
    }
}