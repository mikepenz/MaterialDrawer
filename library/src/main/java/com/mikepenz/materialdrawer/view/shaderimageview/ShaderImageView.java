package com.mikepenz.materialdrawer.view.shaderimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.view.shaderimageview.util.CircleShader;
import com.mikepenz.materialdrawer.view.shaderimageview.util.RoundedShader;
import com.mikepenz.materialdrawer.view.shaderimageview.util.ShaderHelper;


@SuppressWarnings("WeakerAccess")
public class ShaderImageView extends ImageView {

    private final static boolean DEBUG = false;
    private ShaderHelper pathHelper;

    public ShaderImageView(Context context) {
        super(context);
        setup(context, null, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs, defStyle);
    }

    private void setup(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null && createImageViewHelper() == null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView, defStyle, 0);
            int type = typedArray.getInt(R.styleable.ShaderImageView_siType, 0);
            if (type == 0) {
                //circular
                pathHelper = new CircleShader();
            } else {
                //rounded (square = round with radius 1)
                pathHelper = new RoundedShader();
            }
            typedArray.recycle();
        }

        getPathHelper().init(context, attrs, defStyle);
    }

    protected ShaderHelper getPathHelper() {
        if (pathHelper == null) {
            pathHelper = createImageViewHelper();
        }
        return pathHelper;
    }

    /**
     * overwrite this to define a custom shader
     *
     * @return
     */
    public ShaderHelper createImageViewHelper() {
        return null;
    }


    public void disableTouchFeedback(boolean disable) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getPathHelper().isSquare()) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int dimen = Math.min(width, height);
            setMeasuredDimension(dimen, dimen);
        }
    }

    //Required by path helper
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        getPathHelper().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (!(drawable instanceof TransitionDrawable)) {
            getPathHelper().onImageDrawableReset(getDrawable());
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        getPathHelper().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageURI(Uri uri) {
        if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            DrawerImageLoader.getInstance().setImage(this, uri);
        } else {
            super.setImageURI(uri);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getPathHelper().onSizeChanged(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (DEBUG) {
            canvas.drawRGB(10, 200, 200);
        }

        if (!getPathHelper().onDraw(canvas)) {
            super.onDraw(canvas);
        }

    }

}