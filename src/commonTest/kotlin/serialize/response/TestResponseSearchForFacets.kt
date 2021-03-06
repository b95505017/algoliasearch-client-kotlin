package serialize.response

import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Facet
import com.algolia.search.serialize.*
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import serialize.TestSerializer
import unknown


internal class TestResponseSearchForFacets : TestSerializer<ResponseSearchForFacets>(
    ResponseSearchForFacets.serializer()
) {

    override val items = listOf(
        ResponseSearchForFacets(
            facets = listOf(
                Facet(unknown, 0, "hello")
            ),
            exhaustiveFacetsCount = true,
            processingTimeMS = 0
        ) to json {
            KeyFacetHits to jsonArray {
                +json {
                    KeyValue to unknown
                    KeyCount to 0
                    KeyHighlighted to "hello"
                }
            }
            KeyExhaustiveFacetsCount to true
            KeyProcessingTimeMS to 0
        }
    )
}