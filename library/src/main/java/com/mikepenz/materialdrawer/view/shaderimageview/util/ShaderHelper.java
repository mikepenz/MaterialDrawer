package com.mikepenz.materialdrawer.view.shaderimageview.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.mikepenz.materialdrawer.R;

@SuppressWarnings("WeakerAccess")
public abstract class ShaderHelper {
    private final static int ALPHA_MAX = 255;

    protected int viewWidth;
    protected int viewHeight;

    protected int borderColor = Color.BLACK;
    protected int borderWidth = 0;
    protected float borderAlpha = 1f;
    protected boolean square = false;

    protected final Paint borderPaint;
    protected final Paint imagePaint;
    protected BitmapShader shader;
    protected Drawable drawable;
    protected final Matrix matrix = new Matrix();

    public ShaderHelper() {
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
    }

    public abstract void draw(Canvas canvas, Paint imagePaint, Paint borderPaint);

    public abstract void reset();

    @SuppressWarnings("UnusedParameters")
    public abstract void calculate(int bitmapWidth, int bitmapHeight, float width, float height, float scale, float translateX, float translateY);


    @SuppressWarnings("SameParameterValue")
    protected final int dpToPx(DisplayMetrics displayMetrics, int dp) {
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public boolean isSquare() {
        return square;
    }

    public void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView, defStyle, 0);
            borderColor = typedArray.getColor(R.styleable.ShaderImageView_siBorderColor, borderColor);
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.ShaderImageView_siBorderWidth, borderWidth);
            borderAlpha = typedArray.getFloat(R.styleable.ShaderImageView_siBorderAlpha, borderAlpha);
            square = typedArray.getBoolean(R.styleable.ShaderImageView_siSquare, square);
            typedArray.recycle();
        }

        borderPaint.setColor(borderColor);
        borderPaint.setAlpha(Float.valueOf(borderAlpha * ALPHA_MAX).intValue());
        borderPaint.setStrokeWidth(borderWidth);
    }

    public boolean onDraw(Canvas canvas) {
        if (shader == null) {
            createShader();
        }
        if (shader != null && viewWidth > 0 && viewHeight > 0) {
            draw(canvas, imagePaint, borderPaint);
            return true;
        }

        return false;
    }

    public void onSizeChanged(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        if (isSquare()) {
            viewWidth = viewHeight = Math.min(width, height);
        }
        if (shader != null) {
            calculateDrawableSizes();
        }
    }

    public Bitmap calculateDrawableSizes() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            if (bitmapWidth > 0 && bitmapHeight > 0) {
                float width = Math.round(viewWidth - 2f * borderWidth);
                float height = Math.round(viewHeight - 2f * borderWidth);

                float scale;
                float translateX = 0;
                float translateY = 0;

                if (bitmapWidth * height > width * bitmapHeight) {
                    scale = height / bitmapHeight;
                    translateX = Math.round((width / scale - bitmapWidth) / 2f);
                } else {
                    scale = width / (float) bitmapWidth;
                    translateY = Math.round((height / scale - bitmapHeight) / 2f);
                }

                matrix.setScale(scale, scale);
                matrix.preTranslate(translateX, translateY);
                matrix.postTranslate(borderWidth, borderWidth);

                calculate(bitmapWidth, bitmapHeight, width, height, scale, translateX, translateY);

                return bitmap;
            }
        }

        reset();
        return null;
    }

    public final void onImageDrawableReset(Drawable drawable) {
        this.drawable = drawable;
        shader = null;
        imagePaint.setShader(null);
    }

    protected void createShader() {
        Bitmap bitmap = calculateDrawableSizes();
        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            imagePaint.setShader(shader);
        }
    }

    protected Bitmap getBitmap() {
        Bitmap bitmap = null;
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }
        }

        return bitmap;
    }
}