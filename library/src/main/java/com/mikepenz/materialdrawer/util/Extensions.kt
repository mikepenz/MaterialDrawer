package com.mikepenz.materialdrawer.util

/**
 * Does an action if it is not null
 */
internal inline infix fun <T> T?.ifNotNull(block: (T) -> Unit): Unit? {
    return if (this != null) {
        block(this)
    } else {
        null
    }
}

/**
 * Does an action if it is null
 */
internal inline infix fun <T> T?.otherwise(block: () -> Unit): Unit = ifNull(block)

/**
 * Does an action if it is null
 */
internal inline infix fun <T> T?.ifNull(block: () -> Unit): Unit {
    if (this == null) {
        block()
    }
}
