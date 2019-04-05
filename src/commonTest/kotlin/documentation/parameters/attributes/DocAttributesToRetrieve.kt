package documentation.parameters.attributes

import com.algolia.search.dsl.attributesToRetrieve
import com.algolia.search.dsl.query
import com.algolia.search.dsl.settings
import documentation.index
import runBlocking
import kotlin.test.Ignore
import kotlin.test.Test


@Ignore
internal class DocAttributesToRetrieve {

//    // list of attributes to retrieve
//    settings {
//        attributesToRetrieve {
//            +"attribute1"
//            +"attribute2"
//        }
//    }
//
//    // retrieves all attributes
//    settings { attributesToRetrieve { +"*" } }
//
//    // retrieves all attributes except the one listed.
//    settings {
//        attributesToRetrieve {
//            +"attribute1"
//            +"attribute2"
//            excludeAttributes = true
//        }
//    }

    @Test
    fun defaultList() {
        runBlocking {
            val settings = settings {
                attributesToRetrieve {
                    +"author"
                    +"title"
                    +"content"
                }
            }

            index.setSettings(settings)
        }
    }

    @Test
    fun allAttributes() {
        runBlocking {
            val settings = settings {
                attributesToRetrieve { +"*" }
            }

            index.setSettings(settings)
        }
    }

    @Test
    fun currentSearch() {
        runBlocking {
            val query = query("query") {
                attributesToRetrieve {
                    +"title"
                    +"content"
                }
            }

            index.search(query)
        }
    }

    @Test
    fun doNotRetrieve() {
        runBlocking {
            val settings = settings {
                attributesToRetrieve {
                    +"sku"
                    +"internal_desc"
                    excludeAttributes = true
                }
            }

            index.setSettings(settings)
        }
    }
}