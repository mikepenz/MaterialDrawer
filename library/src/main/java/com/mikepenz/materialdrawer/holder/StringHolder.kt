package com.mikepenz.materialdrawer.holder

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.mikepenz.materialize.holder.StringHolder

/**
 * Created by mikepenz on 13.07.15.
 */
class StringHolder : com.mikepenz.materialize.holder.StringHolder {
    constructor(text: CharSequence?) : super(text)

    constructor(@StringRes textRes: Int) : super(textRes)

    companion object {
        fun applyTo(text: StringHolder?, textView: TextView?) {
            StringHolder.applyTo(text, textView)
        }

        fun applyToOrHide(text: StringHolder?, textView: TextView?): Boolean {
            return StringHolder.applyToOrHide(text, textView)
        }
    }
}

fun StringHolder?.applyTo(textView: TextView?) {
    this ?: return
    if (textView != null) {
        this.applyTo(textView)
    }
}

fun StringHolder?.applyToOrHide(textView: TextView?): Boolean {
    if (this != null && textView != null) {
        return this.applyToOrHide(textView)
    } else if (textView != null) {
        textView.visibility = View.GONE
        return false
    }
    return false
}

