package serialize.search

import com.algolia.search.model.search.ExactOnSingleWordQuery
import com.algolia.search.model.search.ExactOnSingleWordQuery.*
import kotlinx.serialization.json.JsonLiteral
import serialize.TestSerializer
import unknown


internal class TestExactOnSingleWordQuery : TestSerializer<ExactOnSingleWordQuery>(
    ExactOnSingleWordQuery
) {

    override val items = listOf(
        Attribute to JsonLiteral(Attribute.raw),
        None to JsonLiteral(None.raw),
        Word to JsonLiteral(Word.raw),
        Other(unknown) to JsonLiteral(unknown)
    )
}