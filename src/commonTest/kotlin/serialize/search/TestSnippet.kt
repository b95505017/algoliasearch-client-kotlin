package serialize.search

import attributeA
import attributeB
import com.algolia.search.model.search.Snippet
import kotlinx.serialization.json.JsonLiteral
import serialize.TestSerializer


internal class TestSnippet : TestSerializer<Snippet>(Snippet) {

    override val items = listOf(
        Snippet(attributeA) to JsonLiteral(
            Snippet(
                attributeA
            ).raw
        ),
        snippet to json
    )

    companion object {

        val snippet = Snippet(attributeB, 10)
        val json = JsonLiteral(snippet.raw)
    }
}