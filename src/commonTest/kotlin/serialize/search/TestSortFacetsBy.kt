package serialize.search

import com.algolia.search.model.search.SortFacetsBy
import com.algolia.search.model.search.SortFacetsBy.*
import kotlinx.serialization.json.JsonLiteral
import serialize.TestSerializer
import unknown


internal class TestSortFacetsBy : TestSerializer<SortFacetsBy>(SortFacetsBy) {

    override val items = listOf(
        Alpha to JsonLiteral(Alpha.raw),
        Count to JsonLiteral(Count.raw),
        Other(unknown) to JsonLiteral(unknown)
    )
}