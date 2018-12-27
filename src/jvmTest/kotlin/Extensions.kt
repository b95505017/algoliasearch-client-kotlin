
import client.Index
import client.data.Attribute
import client.query.helper.*


internal fun groupMap(): GroupMap<Filter> = mutableMapOf()
internal fun set(vararg filters: Filter) = mutableSetOf(*filters)

internal const val nameA = "nameA"
internal const val nameB = "nameB"
internal const val unknown = "unknown"
internal const val int = 0
internal const val boolean = true
internal const val string = "string"
internal const val all = "*"
internal val attributeA = Attribute("attributeA")
internal val attributeB = Attribute("attributeB")
internal val attributeC = Attribute("attributeC")
internal val indexA = Index("indexA")
internal val indexB = Index("indexB")
internal val groupOrA = GroupOr(nameA)
internal val groupOrB = GroupOr(nameB)
internal val groupAndA = GroupAnd(nameA)
internal val groupAndB = GroupAnd(nameB)
internal val attributeAll = Attribute("*")
internal val attributes = listOf(attributeA, attributeB)
internal val facetA = FilterFacet(attributeA, "facetA")
internal val facetB = FilterFacet(attributeB, false)
internal val comparisonA = FilterComparison(attributeA, NumericOperator.Greater, 5.0)
internal val comparisonB = FilterComparison(attributeB, NumericOperator.NotEquals, 10.0)
internal val rangeA = FilterRange(attributeA, 0.0, 5.0)
internal val rangeB = FilterRange(attributeB, 5.0, 10.0)
internal val tagA = FilterTag("tagA")
internal val tagB = FilterTag("tagB")

internal fun FilterBuilder.buildTest() = build().replace("\"", "")