package com.mikepenz.materialdrawer.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes

/**
 * Created by mikepenz on 13.07.15.
 */
open class StringHolder {
    var textString: CharSequence? = null
        internal set
    var textRes = -1
        internal set

    constructor(text: CharSequence?) {
        this.textString = text
    }

    constructor(@StringRes textRes: Int) {
        this.textRes = textRes
    }

    open fun applyTo(textView: TextView?) {
        when {
            textString != null -> textView?.text = textString
            textRes != -1 -> textView?.setText(textRes)
            else -> textView?.text = ""
        }
    }

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

    open fun getText(ctx: Context): String? {
        if (textString != null) {
            return textString.toString()
        } else if (textRes != -1) {
            return ctx.getString(textRes)
        }
        return null
    }

    companion object {
        fun applyTo(text: StringHolder?, textView: TextView?) {
            text?.applyTo(textView)
        }

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
