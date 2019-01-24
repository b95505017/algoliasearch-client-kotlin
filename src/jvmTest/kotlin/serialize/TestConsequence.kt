package serialize

import attributeA
import com.algolia.search.saas.data.*
import com.algolia.search.saas.serialize.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.list
import objectIDA
import objectIDB
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import unknown


@RunWith(JUnit4::class)
internal class TestConsequence : TestSerializer<Consequence>(Consequence) {

    private val edits = listOf(Edit(unknown))
    private val paramsQuery = Params(query = QueryOrEdits.Query(unknown))
    private val paramsQuerySerialized = paramsQuery.toJsonObject(Params.serializer()).encodeNoNulls()
    private val paramsEdits = Params(query = QueryOrEdits.Edits(edits))
    private val paramsEditsSerialized = paramsEdits.toJsonObject(Params.serializer()).encodeNoNulls()
    private val filters = listOf(AutomaticFacetFilters(attributeA, 1, true))
    private val objectIDs = listOf(objectIDA, objectIDB)
    private val promotions = listOf(Promotion(objectIDA, 0))
    private val promotionsSerialized = Json.plain.toJson(Promotion.serializer().list, promotions)
    private val params = paramsEdits.copy(automaticFacetFilters = filters, automaticOptionalFacetFilters = filters)
    private val paramsSerialized = params.toJsonObject(Params.serializer()).encodeNoNulls()
    private val userData = json { KeyUserData to unknown }

    override val items = listOf(
        Consequence() to json { },
        Consequence(paramsQuery) to json { KeyParams to paramsQuerySerialized },
        Consequence(paramsEdits) to json { KeyParams to paramsEditsSerialized },
        Consequence(params, promotions, objectIDs, userData) to json {
            KeyParams to paramsSerialized
            KeyPromote to promotionsSerialized
            KeyHide to jsonArray {
                +json { KeyObjectID to objectIDA.raw }
                +json { KeyObjectID to objectIDB.raw }
            }
            KeyUserData to userData
        }
    )
}