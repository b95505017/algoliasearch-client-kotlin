package suite

import com.algolia.search.model.rule.QueryRule
import com.algolia.search.model.settings.AttributeForFaceting
import com.algolia.search.model.settings.Settings
import com.algolia.search.model.synonym.Synonym
import com.algolia.search.model.task.Task
import com.algolia.search.model.task.TaskStatus
import com.algolia.search.serialize.KeyObjectID
import com.algolia.search.toAttribute
import com.algolia.search.toObjectID
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.json
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import shouldBeTrue
import shouldEqual


@RunWith(JUnit4::class)
internal class TestSuiteCopyIndex {

    private val suffix = "copy_index"
    private val indexName = testSuiteIndexName(suffix)
    private val index = clientAdmin1.getIndex(indexName)
    private val company = "company".toAttribute()
    private val indexNameSettings = indexName.copy(indexName.raw + "_settings")
    private val indexNameRules = indexName.copy(indexName.raw + "_rules")
    private val indexNameSynonyms = indexName.copy(indexName.raw + "_synonyms")
    private val indexNameFullCopy = indexName.copy(indexName.raw + "_full_copy")
    private val queryRuleID = "company_auto_faceting".toObjectID()

    private val objects = listOf(
        json {
            KeyObjectID to "one"
            company.raw to "apple"
        },
        json {
            KeyObjectID to "two"
            company.raw to "algolia"
        }
    )
    private val settings = Settings(attributesForFaceting = listOf(AttributeForFaceting.Default(company)))

    @Before
    fun clean() {
        cleanIndex(clientAdmin1, suffix)
    }

    @Test
    fun test() {
        runBlocking {
            index.apply {
                val tasks = mutableListOf<Task>()
                val synonym = load(Synonym, "synonym_placeholder.json")
                val queryRule = load(QueryRule.serializer(), "query_rule_company.json")

                tasks += saveObjects(objects)
                tasks += setSettings(settings)
                tasks += saveSynonym(synonym)
                tasks += saveRule(queryRule)

                tasks.wait().all { it is TaskStatus.Published }.shouldBeTrue()
                tasks.clear()

                tasks += copySettings(indexNameSettings)
                tasks += copyRule(indexNameRules)
                tasks += copySynonyms(indexNameSynonyms)
                tasks += copyIndex(indexNameFullCopy)

                tasks.wait().all { it is TaskStatus.Published }.shouldBeTrue()

                clientAdmin1.getIndex(indexNameSettings).getSettings() shouldEqual getSettings()
                clientAdmin1.getIndex(indexNameRules).getRule(queryRuleID) shouldEqual getRule(queryRuleID)
                clientAdmin1.getIndex(indexNameSynonyms).getSynonym(synonym.objectID) shouldEqual getSynonym(synonym.objectID)
                clientAdmin1.getIndex(indexNameFullCopy).also {
                    it.getSettings() shouldEqual getSettings()
                    it.getRule(queryRuleID) shouldEqual getRule(queryRuleID)
                    it.getSynonym(synonym.objectID) shouldEqual getSynonym(synonym.objectID)
                }
            }
        }
    }
}