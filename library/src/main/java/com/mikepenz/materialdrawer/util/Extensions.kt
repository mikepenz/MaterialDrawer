package com.mikepenz.materialdrawer.util

/**
 * Does an action if it is not null
 */
public inline infix fun <T> T?.ifNotNull(block: (T) -> Unit): Unit? {
    return if (this != null) {
        block(this)
    } else {
        null
    }
}

/**
 * Does an action if it is null
 */
public inline infix fun <T> T?.otherwise(block: () -> Unit): Unit = ifNull(block)

/**
 * Does an action if it is null
 */
public inline infix fun <T> T?.ifNull(block: () -> Unit): Unit {
    if (this == null) {
        block()
    }
}
