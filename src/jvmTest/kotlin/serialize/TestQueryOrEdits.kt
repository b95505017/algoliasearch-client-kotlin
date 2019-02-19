package serialize

import com.algolia.search.model.rule.*
import com.algolia.search.serialize.KeyEdits
import com.algolia.search.toObjectID
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.json
import kotlinx.serialization.list
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import shouldEqual
import suite.loadScratch
import unknown


@RunWith(JUnit4::class)
internal class TestQueryOrEdits : TestSerializer<QueryOrEdits>(QueryOrEdits) {

    private val edits = listOf(
        Edit(unknown),
        Edit(unknown, unknown)
    )

    override val items = listOf(
        QueryOrEdits.Edits(edits) to json { KeyEdits to Json.plain.toJson(Edit.list, edits) },
        QueryOrEdits.Query(unknown) to JsonLiteral(unknown)
    )

    @Test
    fun backwardCompatibility() {
        val queryRule = Json.parse(QueryRule.serializer(), loadScratch("query_rule_v1.json").readText())

        queryRule shouldEqual QueryRule(
            "query_edits".toObjectID(),
            Condition(
                Pattern.Literal("mobile phone"),
                Anchoring.Is
            ),
            Consequence(
                Params(
                    query = QueryOrEdits.Edits(listOf(Edit("mobile"), Edit("phone")))
                )
            )
        )
    }
}