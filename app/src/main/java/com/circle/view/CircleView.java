package com.circle.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by xuqianqian on 2017/7/18.
 */

public class CircleView extends View {

    Bitmap bottomBitmap;
    Bitmap upBitmap;
    Paint circlePaint;
    PorterDuffXfermode mPorterMode;
    RectF rectF;

    /**
     * 结尾数
     */
    float endAngle = 0;
    /**
     * 开始角度
     */
    float startAngle = -90;
    float tempAngle = -90;

    float strokeWidth = 0;

    float mXmlStrokeWidth;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        //从xml里获取资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        //没有就用默认图
        int mBottomResId = typedArray.getResourceId(R.styleable.CircleView_bottomImage, R.drawable.circle_white);
        int mUpResId = typedArray.getResourceId(R.styleable.CircleView_upImage, R.drawable.circle_rainbow);

        bottomBitmap = BitmapFactory.decodeResource(getResources(), mBottomResId);
        upBitmap = BitmapFactory.decodeResource(getResources(), mUpResId);
        strokeWidth = upBitmap.getHeight() / 20f;
        Log.i("xx", "strokeWidth::" + strokeWidth);
        //mXmlStrokeWidth = typedArray.getDimension(R.styleable.CircleView_strokeWidth,strokeWidth);
        circlePaint = new Paint();
        rectF = new RectF();

        mPorterMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        circlePaint.setAntiAlias(true);

        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bottomBitmap, (getWidth() - bottomBitmap.getWidth()) / 2
                , (getHeight() - bottomBitmap.getHeight()) / 2, circlePaint);

        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        //设置属性
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setStrokeWidth(strokeWidth * 2);


        rectF.left = getWidth() / 2 - bottomBitmap.getWidth() / 2 + strokeWidth;
        rectF.right = getWidth() / 2 + bottomBitmap.getWidth() / 2 - strokeWidth;
        rectF.top = getHeight() / 2 - bottomBitmap.getHeight() / 2 + strokeWidth;
        rectF.bottom = getHeight() / 2 + bottomBitmap.getHeight() / 2 - strokeWidth;

        //画圆弧
        canvas.drawArc(rectF, startAngle, tempAngle, false, circlePaint);//SRC_IN
        circlePaint.setXfermode(mPorterMode);
        //画上面的圆 彩虹
        canvas.drawBitmap(upBitmap, (getWidth() - upBitmap.getWidth()) / 2
                , (getHeight() - upBitmap.getHeight()) / 2, circlePaint);

        circlePaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取xml里定义的大小
        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);

        //因为是一个圆，所以width 和 height 必须是一样的，如果xml中设置不一样，则取其中最小的一个
        float value = Math.min(width, height);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        //给出了确切的值 xml里
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            //图片应该按照xml里给定的尺寸进行图片的缩放

            upBitmap = getScaleBitmap(upBitmap, (int) value, (int) value);
            bottomBitmap = getScaleBitmap(bottomBitmap, (int) value, (int) value);

            strokeWidth = upBitmap.getHeight() / 20f;
        }
    }

    private Bitmap getScaleBitmap(Bitmap bitmap, float width, float height) {

        float scaleWidth = width / bitmap.getWidth();
        float scaleHeight = height / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        return newBit;
    }

    public void startAnimation() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float fraction = animation.getAnimatedFraction();
                // -90 0
                tempAngle = endAngle * fraction;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置步数
     *
     * @param dicration
     */
    public void setAngel(float dicration) {
        endAngle = dicration * 360f;
        tempAngle = endAngle;
        invalidate();
    }
}
