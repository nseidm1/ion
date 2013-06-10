package com.koushikdutta.ion;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by koush on 6/8/13.
 */
public class IonDrawable extends Drawable {
    private Paint paint;
    private Bitmap bitmap;
    private IonBitmapRequestBuilder.ScaleMode scaleMode;

    private static final int DEFAULT_PAINT_FLAGS = Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;

    public IonDrawable() {
        paint = new Paint(DEFAULT_PAINT_FLAGS);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    int width;
    int height;
    public IonDrawable setBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == this.bitmap && this.width == width && this.height == height)
            return this;

        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        invalidateSelf();
        return this;
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        paint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        paint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public IonDrawable setScaleMode(IonBitmapRequestBuilder.ScaleMode scaleMode) {
        if (this.scaleMode == scaleMode)
            return this;
        this.scaleMode = scaleMode;
        invalidateSelf();
        return this;
    }

    Rect drawBounds = new Rect();
    @Override
    public void draw(Canvas canvas) {
        if (bitmap == null)
            return;


        if (scaleMode == IonBitmapRequestBuilder.ScaleMode.CenterCrop) {
            Rect bounds = getBounds();

            float ratio = (float)bounds.width() / (float)bounds.height();

            int newWidth;
            int newHeight;
            if (ratio < 1) {
                newWidth = bitmap.getWidth();
                newHeight = (int)(newWidth / ratio);
            }
            else {
                newHeight = bitmap.getHeight();
                newWidth = (int)(newHeight * ratio);
            }

            if (newWidth > bitmap.getWidth()) {
                ratio = (float)bitmap.getWidth() / (float)newWidth;
                newWidth = bitmap.getWidth();
                newHeight *= ratio;
            }

            if (newHeight > bitmap.getHeight()) {
                ratio = (float)bitmap.getHeight() / (float)newHeight;
                newHeight = bitmap.getHeight();
                newWidth *= ratio;
            }

            int clipx = (bitmap.getWidth() - newWidth) >> 1;
            int clipy = (bitmap.getHeight() - newHeight) >> 1;

            drawBounds.set(clipx, clipy, bitmap.getWidth() - clipx, bitmap.getHeight() - clipy);

            assert bounds.width() == 1080 && bounds.height() == 1080;

            canvas.drawBitmap(bitmap, drawBounds, bounds, paint);
        }
        else {

        }

    }

    @Override
    public void setAlpha(int alpha) {
       paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return (bitmap == null || bitmap.hasAlpha() || paint.getAlpha() < 255) ?
                PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
    }

    @Override
    public Drawable mutate() {
        throw new UnsupportedOperationException();
    }
}