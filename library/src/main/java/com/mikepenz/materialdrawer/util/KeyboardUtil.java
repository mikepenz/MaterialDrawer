package com.mikepenz.materialdrawer.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mikepenz on 14.03.15.
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
public class KeyboardUtil {
    private View decorView;
    private View contentView;
    private float initialDpDiff = -1;

    public KeyboardUtil(Activity act, View contentView) {
        this.decorView = act.getWindow().getDecorView();
        this.contentView = contentView;
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    //a small helper to allow showing the editText focus
    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            decorView.getWindowVisibleDisplayFrame(r);

            //get the height diff as dp
            float heightDiffDp = UIUtils.convertPixelsToDp(decorView.getRootView().getHeight() - (r.bottom - r.top), decorView.getContext());

            //set the initialDpDiff at the beginning. (on my phone this was 73dp)
            if (initialDpDiff == -1) {
                initialDpDiff = heightDiffDp;
            }

            //if it could be a keyboard add the padding to the view
            if (heightDiffDp - initialDpDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                //Toast.makeText(MainActivity.this, "Hallo", Toast.LENGTH_SHORT).show();
                contentView.setPadding(0, 0, 0, (int) UIUtils.convertDpToPixel((heightDiffDp - initialDpDiff), decorView.getContext()));
            } else {
                contentView.setPadding(0, 0, 0, 0);
            }
        }
    };


    /**
     * Helper to hide the keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }
}
