package com.algolia.search.filter

import com.algolia.search.model.Attribute

internal fun Group.key(filter: Filter): Group.Key {
    val key = when (this) {
        is GroupOr -> when (filter) {
            is FilterFacet -> Group.Type.OrFacet
            is FilterNumeric -> Group.Type.OrNumeric
            is FilterTag -> Group.Type.OrTag
        }
        is GroupAnd -> Group.Type.And
    }
    return Group.Key(name, key)
}

internal typealias GroupMap<T> = MutableMap<Group.Key, MutableSet<T>>

internal fun <T : Filter> GroupMap<T>.add(group: Group, vararg filters: T) {
    filters.forEach { filter ->
        val key = group.key(filter)

        getOrElse(key) { mutableSetOf() }.let {
            it += filter
            this[key] = it
        }
    }
}

internal fun <T : Filter> GroupMap<T>.remove(group: Group, vararg filters: Filter): Boolean {
    return filters.any {
        val key = group.key(it)
        val set = get(key)
        if (set != null) {
            if (set.remove(it)) {
                if (set.isEmpty()) remove(key)
                true
            } else false
        } else false
    }
}

internal fun <T : Filter> GroupMap<T>.contains(group: Group, filter: Filter): Boolean {
    return get(group.key(filter))?.contains(filter) ?: false
}

internal fun <T : Filter> GroupMap<T>.contains(filter: Filter): Boolean {
    return any { it.value.contains(filter) }
}

internal fun <T : Filter> GroupMap<T>.clear(group: Group, attribute: Attribute?) {
    filterKeys { it.name == group.name }.forEach { (key, value) ->
        if (attribute != null) {
            value.removeAll { it.attribute == attribute }
        } else {
            value.clear()
        }
        if (value.isEmpty()) remove(key)
    }
}

internal fun <T : Filter> GroupMap<T>.replace(group: Group, filter: T, replacement: T): Boolean {
    if (filter::class != replacement::class) throw IllegalArgumentException("Can't replace filters of different types. [$filter, $replacement]")
    return get(group.key(filter))?.let {
        if (it.remove(filter)) {
            it += replacement
            true
        } else false
    } ?: false
}

internal fun <T : Filter> GroupMap<T>.move(origin: Group, destination: Group, filter: T): Boolean {
    return if (remove(origin, filter)) {
        add(destination, filter)
        true
    } else false
}

internal fun <T : Filter> GroupMap<T>.toggle(group: Group, filter: T): Boolean {
    val contains = contains(filter)
    if (contains) remove(group, filter) else add(group, filter)
    return contains
}

internal inline fun <reified T : Filter> GroupMap<T>.replaceAttribute(
    group: Group,
    attribute: Attribute,
    replacement: Attribute
) {
    filterKeys { it.name == group.name }.forEach { (_, value) ->
        val found = value.filter { it.attribute == attribute }

        value -= found
        value += found.map { it.modifyAttribute<T>(replacement) }
    }
}

internal fun <T : Filter> GroupMap<T>.get(group: Group, attribute: Attribute?): Set<T> {
    val filters = filterKeys { it.name == group.name }.flatMap { it.value }

    return if (attribute != null) {
        filters.filter { it.attribute == attribute }
    } else {
        filters
    }.toSet()
}

internal fun <T : Filter> GroupMap<T>.get(attribute: Attribute?): Set<T> {
    return if (attribute != null) {
        values.flatMap { set -> set.filter { it.attribute == attribute } }
    } else {
        values.flatten()
    }.toSet()
}

private inline fun <reified T> Filter.modifyAttribute(attribute: Attribute): T {
    return when (this) {
        is FilterComparison -> copy(attribute = attribute)
        is FilterTag -> this
        is FilterFacet -> copy(attribute = attribute)
        is FilterRange -> copy(attribute = attribute)
    } as T
}