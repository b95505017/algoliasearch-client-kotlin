package documentation.parameters.rule

import com.algolia.search.dsl.query
import com.algolia.search.dsl.settings
import documentation.TestDocumentation
import runBlocking
import kotlin.test.Test


internal class DocEnableRules : TestDocumentation() {

//    enableRules: Boolean = true|false

    @Test
    fun settings() {
        runBlocking {
            val settings = settings {
                enableRules = true
            }

            index.setSettings(settings)
        }
    }

    @Test
    fun query() {
        runBlocking {
            val query = query("query") {
                enableRules = true
            }

            index.search(query)
        }
    }
}