package documentation.guides.data

import com.algolia.search.serialize.Json
import documentation.index
import java.io.File
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObjectSerializer
import kotlinx.serialization.builtins.list
import org.junit.Ignore
import org.junit.Test

@Ignore
internal class GuideExportToFile {

    @Test
    fun snippet1() {
        runBlocking {
            val records = index.browseObjects().flatMap { response ->
                response.hits.map { it.json }
            }
            val json = Json.stringify(JsonObjectSerializer.list, records)

            File("your_filename.json").writeText(json)
        }
    }
}
