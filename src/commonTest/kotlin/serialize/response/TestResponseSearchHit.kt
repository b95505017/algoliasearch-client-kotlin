package serialize.response

import attributeA
import attributeB
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.*
import com.algolia.search.serialize.*
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.json.json
import kotlinx.serialization.list
import serialize.TestSerializer
import serialize.search.TestHighlightResult
import unknown


internal class TestResponseSearchHit : TestSerializer<ResponseSearch.Hit>(ResponseSearch.Hit) {

    override val items = listOf(
        hit to json
    )

    companion object {

        private val highlights = mapOf(attributeA to TestHighlightResult.item)
        private val rankingInfo = RankingInfo(
            promoted = true,
            nbExactWords = 0,
            nbTypos = 1,
            geoDistance = 2,
            geoPrecision = 3,
            matchedGeoLocation = MatchedGeoLocation(Point(0f, 1f), 2),
            firstMatchedWord = 4,
            proximityDistance = 5,
            filters = 6,
            userScore = 7,
            words = 8
        )
        private val snippets = mapOf(
            attributeA to listOf(SnippetResult(unknown, MatchLevel.None)),
            attributeB to listOf(SnippetResult(unknown, MatchLevel.None))
        )
        val json = json {
            Key_DistinctSeqID to 0
            Key_HighlightResult to Json.toJson(HashMapSerializer(Attribute, HighlightResult.serializer()), highlights)
            Key_SnippetResult to Json.toJson(HashMapSerializer(Attribute, SnippetResult.serializer().list), snippets)
            attributeA to unknown
            attributeB to unknown
            Key_RankingInfo to Json.toJson(RankingInfo.serializer(), rankingInfo)
        }
        val hit = ResponseSearch.Hit(json)
    }
}