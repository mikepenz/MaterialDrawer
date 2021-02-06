package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes

/**
 * Defines a custom holder class to support providing strings either as string or resource. Does not require a [Context] and will resolve the value when applying.
 */
open class StringHolder {
    /** Defines the string text */
    var textString: CharSequence? = null
        internal set
    /** Defines the string resource */
    var textRes = -1
        internal set

    constructor(text: CharSequence?) {
        this.textString = text
    }

    constructor(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    /**
     * Applies the text to a [TextView]
     */
    open fun applyTo(textView: TextView?) {
        when {
            textString != null -> textView?.text = textString
            textRes != -1 -> textView?.setText(textRes)
            else -> textView?.text = ""
        }
    }

    /**
     * Applies the [TextView] if no text given, hide the textView
     */
    open fun applyToOrHide(textView: TextView?): Boolean {
        textView ?: return false
        return when {
            textString != null -> {
                textView.text = textString
                textView.visibility = View.VISIBLE
                true
            }
            textRes != -1 -> {
                textView.setText(textRes)
                textView.visibility = View.VISIBLE
                true
            }
            else -> {
                textView.visibility = View.GONE
                false
            }
        }
    }

    /**
     * Returns the text as [String]
     */
    open fun getText(ctx: Context): String? {
        if (textString != null) {
            return textString.toString()
        } else if (textRes != -1) {
            return ctx.getString(textRes)
        }
        return null
    }

    companion object {
        /**
         * Helper to apply the text to a [TextView]
         */
        fun applyTo(text: StringHolder?, textView: TextView?) {
            text?.applyTo(textView)
        }

        /**
         * Helper to apply the text to a [TextView] or hide if null
         */
        fun applyToOrHide(text: StringHolder?, textView: TextView?): Boolean {
            return if (text != null) {
                text.applyToOrHide(textView)
            } else {
                textView?.visibility = View.GONE
                false
            }
        }
    }
}
