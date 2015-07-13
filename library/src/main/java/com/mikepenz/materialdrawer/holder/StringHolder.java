package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mikepenz on 13.07.15.
 */
public class StringHolder {
    private String mText;
    private int mTextRes = -1;

    public StringHolder(String text) {
        this.mText = text;
    }

    public StringHolder(int textRes) {
        this.mTextRes = textRes;
    }

    public void applyTo(TextView textView) {
        if (mText != null) {
            textView.setText(mText);
        } else if (mTextRes != -1) {
            textView.setText(mTextRes);
        } else {
            textView.setText("");
        }
    }

    public void applyToOrHide(TextView textView) {
        if (mText != null) {
            textView.setText(mText);
            textView.setVisibility(View.VISIBLE);
        } else if (mTextRes != -1) {
            textView.setText(mTextRes);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public String getText(Context ctx) {
        if (mText != null) {
            return mText;
        } else if (mTextRes != -1) {
            return ctx.getString(mTextRes);
        }
        return null;
    }


    public static void applyTo(StringHolder text, TextView textView) {
        if (text != null && textView != null) {
            text.applyTo(textView);
        }
    }

    public static void applyToOrHide(StringHolder text, TextView textView) {
        if (text != null && textView != null) {
            text.applyToOrHide(textView);
        } else if (textView != null) {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public String toString() {
        return mText;
    }
}
