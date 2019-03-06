package serialize.settings

import com.algolia.search.model.settings.Distinct
import kotlinx.serialization.json.JsonLiteral
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import serialize.TestSerializer


@RunWith(JUnit4::class)
internal class TestDistinct : TestSerializer<Distinct>(Distinct) {

    override val items = listOf(
        Distinct.True to JsonLiteral(true),
        Distinct.False to JsonLiteral(false),
        Distinct.Value(2) to JsonLiteral(2),
        Distinct.Value(3) to JsonLiteral(3)
    )
}