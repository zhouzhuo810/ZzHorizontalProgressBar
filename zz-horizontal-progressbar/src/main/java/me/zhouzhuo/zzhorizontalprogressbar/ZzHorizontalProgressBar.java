package me.zhouzhuo.zzhorizontalprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;


/**
 * 水平进度条
 *
 * Created by 周卓 on 2016/9/22.
 */
public class ZzHorizontalProgressBar extends View {
    
    public static final int SHOW_MODE_ROUND = 0;
    public static final int SHOW_MODE_RECT = 1;
    public static final int SHOW_MODE_ROUND_RECT = 2;
    
    public static final int SHAPE_POINT = 0;
    public static final int SHAPE_LINE = 1;
    
    private int mMax;
    private int mProgress;
    private int mBgColor;
    private int mProgressColor;
    private int mPadding;
    private boolean mOpenGradient;
    private int mGradientFrom;
    private int mGradientTo;
    private boolean mShowSecondProgress;
    private int mSecondProgress;
    private int mSecondProgressShape;
    private boolean mShowZeroPoint;
    
    private Paint mSecondProgressPaint;
    private Paint mSecondGradientPaint;
    private Paint mProgressPaint;
    private Paint mGradientPaint;
    private Paint mBgPaint;
    private boolean mOpenSecondGradient;
    private int mSecondGradientFrom;
    private int mSecondGradientTo;
    private int mSecondProgressColor;
    
    private int mRadius;
    private boolean mDrawBorder = false;
    private int mBorderColor;
    private int mBorderWidth;
    
    private int mShowMode = 0;
    private Paint mBorderPaint;
    
    @IntDef({SHOW_MODE_ROUND, SHOW_MODE_RECT, SHOW_MODE_ROUND_RECT})
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ShowMode {
    }
    
    @IntDef({SHAPE_POINT, SHAPE_LINE})
    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SecondProgressShape {
    }
    
    private OnProgressChangedListener mOnProgressChangedListener;
    
    public interface OnProgressChangedListener {
        void onProgressChanged(ZzHorizontalProgressBar progressBar, int max, int progress);
        
        void onSecondProgressChanged(ZzHorizontalProgressBar progressBar, int max, int progress);
    }
    
    public ZzHorizontalProgressBar(Context context) {
        super(context);
        init(context, null);
    }
    
    public ZzHorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public ZzHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaths();
    }
    
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZzHorizontalProgressBar);
        mMax = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_max, 100);
        mProgress = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_progress, 0);
        mBgColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_bg_color, 0xff3F51B5);
        mProgressColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_pb_color, 0xffFF4081);
        mSecondProgressColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_pb_color, 0xffFF4081);
        mPadding = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_padding, 0);
        mShowZeroPoint = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_show_zero_point, false);
        mShowSecondProgress = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_show_second_progress, false);
        mSecondProgress = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_second_progress, 0);
        mSecondProgressShape = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_show_second_point_shape, SHAPE_POINT);
        mOpenGradient = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_open_gradient, false);
        mGradientFrom = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_gradient_from, 0xffFF4081);
        mGradientTo = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_gradient_to, 0xffFF4081);
        mOpenSecondGradient = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_open_second_gradient, false);
        mShowMode = a.getInt(R.styleable.ZzHorizontalProgressBar_zpb_show_mode, SHOW_MODE_ROUND);
        mSecondGradientFrom = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_gradient_from, 0xffFF4081);
        mSecondGradientTo = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_gradient_to, 0xffFF4081);
        mRadius = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_round_rect_radius, 20);
        mDrawBorder = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_draw_border, false);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_border_width, 1);
        mBorderColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_border_color, 0xffff001f);
        a.recycle();
    }
    
    private void initPaths() {
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);
        
        mSecondProgressPaint = new Paint();
        mSecondProgressPaint.setColor(mSecondProgressColor);
        mSecondProgressPaint.setStyle(Paint.Style.FILL);
        mSecondProgressPaint.setAntiAlias(true);
        
        mGradientPaint = new Paint();
        mGradientPaint.setStyle(Paint.Style.FILL);
        mGradientPaint.setAntiAlias(true);
        
        mSecondGradientPaint = new Paint();
        mSecondGradientPaint.setStyle(Paint.Style.FILL);
        mSecondGradientPaint.setAntiAlias(true);
        
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setAntiAlias(true);
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mShowMode) {
            case SHOW_MODE_ROUND:
                //half circle
                drawBackgroundCircleMode(canvas);
                drawProgressCircleMode(canvas);
                drawBorderCircleMode(canvas);
                break;
            case SHOW_MODE_RECT:
                //rect
                drawBackgroundRectMode(canvas);
                drawProgressRectMode(canvas);
                drawBorderRectMode(canvas);
                break;
            case SHOW_MODE_ROUND_RECT:
                //custom radius
                drawBackgroundRoundRectMode(canvas);
                drawProgressRoundRectMode(canvas);
                drawBorderRoundRect(canvas);
                break;
        }
    }
    
    /**
     * 绘制半圆形进度
     */
    private void drawProgressCircleMode(Canvas canvas) {
        int width = getWidth();
        float percent = 0;
        if (mMax != 0) {
            percent = mProgress * 1.0f / mMax;
        }
        int progressHeight = getHeight() - mPadding * 2;
        if (mOpenGradient) {
            int progressWidth = width - mPadding * 2;
            float dx = progressWidth * percent;
            
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = mGradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = mGradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                mPadding + progressHeight / 2.0f, mPadding, mPadding + progressHeight / 2.0f + dx, mPadding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            mGradientPaint.setShader(shader);
            
            int radius = width > getHeight() ? getHeight() / 2 : width / 2;
            if (dx < getHeight()) {
                //left circle
                if (mProgress == 0) {
                    if (mShowZeroPoint) {
                        canvas.drawCircle(mPadding + progressHeight / 2.0f, mPadding + progressHeight / 2.0f, progressHeight / 2.0f, mGradientPaint);
                    }
                } else {
                    canvas.drawCircle(mPadding + progressHeight / 2.0f, mPadding + progressHeight / 2.0f, progressHeight / 2.0f, mGradientPaint);
                }
                
            } else {
                //progress line
                RectF rectF = new RectF(mPadding, mPadding, mPadding + dx, mPadding + progressHeight);
                canvas.drawRoundRect(rectF, radius, radius, mGradientPaint);
            }
            
        } else {
            int progressWidth = width - mPadding * 2 - progressHeight;
            float dx = progressWidth * percent;
            mProgressPaint.setColor(mProgressColor);
            float left = mPadding + progressHeight / 2.0f;
            //left circle
            if (mProgress == 0) {
                if (mShowZeroPoint) {
                    canvas.drawCircle(left, left, progressHeight / 2.0f, mProgressPaint);
                }
            } else {
                canvas.drawCircle(left, left, progressHeight / 2.0f, mProgressPaint);
            }
            //right circle
            if (mProgress == 0) {
                if (mShowZeroPoint) {
                    canvas.drawCircle(left + dx, left, progressHeight / 2.0f, mProgressPaint);
                }
            } else {
                canvas.drawCircle(left + dx, left, progressHeight / 2.0f, mProgressPaint);
            }
            //middle line
            RectF rectF = new RectF(left, mPadding, left + dx, mPadding + progressHeight);
            canvas.drawRect(rectF, mProgressPaint);
        }
        
        //draw second progress
        if (mShowSecondProgress) {
            float secondPercent = 0;
            if (mMax != 0) {
                secondPercent = mSecondProgress * 1.0f / mMax;
            }
            int secondProgressHeight = getHeight() - mPadding * 2;
            if (mOpenSecondGradient) {
                int secondProgressWidth = width - mPadding * 2;
                float secondDx = secondProgressWidth * secondPercent;
                
                int[] secondColors = new int[2];
                float[] secondPositions = new float[2];
                //from color
                secondColors[0] = mSecondGradientFrom;
                secondPositions[0] = 0;
                //to color
                secondColors[1] = mSecondGradientTo;
                secondPositions[1] = 1;
                LinearGradient secondShader = new LinearGradient(
                    mPadding + secondProgressHeight / 2.0f, mPadding, mPadding + secondProgressHeight / 2.0f + secondDx, mPadding + secondProgressHeight,
                    secondColors,
                    secondPositions,
                    Shader.TileMode.MIRROR);
                //gradient
                mSecondGradientPaint.setShader(secondShader);
                
                int secondRadius = width > getHeight() ? getHeight() / 2 : width / 2;
                if (secondDx < getHeight()) {
                    //left circle
                    if (mSecondProgress == 0) {
                        if (mShowZeroPoint) {
                            canvas.drawCircle(mPadding + secondProgressHeight / 2.0f, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondGradientPaint);
                        }
                    } else {
                        canvas.drawCircle(mPadding + secondProgressHeight / 2.0f, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondGradientPaint);
                    }
                } else {
                    //progress line
                    RectF rectF = new RectF(mPadding, mPadding, mPadding + secondDx, mPadding + secondProgressHeight);
                    canvas.drawRoundRect(rectF, secondRadius, secondRadius, mSecondGradientPaint);
                }
            } else {
                //no gradient
                if (mSecondProgressShape == 0) {
                    //point shape
                    int secondProgressWidth = width - mPadding * 2;
                    float secondDx = secondProgressWidth * secondPercent;
                    //progress line
                    float px = mPadding + secondProgressHeight / 2.0f + secondDx;
                    if (px < width - mPadding - secondProgressHeight / 2.0f) {
                        if (mSecondProgress == 0) {
                            if (mShowZeroPoint) {
                                canvas.drawCircle(px, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                            }
                        } else {
                            canvas.drawCircle(px, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(px - secondProgressHeight, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                    }
                    
                } else {
                    //line shape
                    int secondProgressWidth = width - mPadding * 2 - secondProgressHeight;
                    float dx = secondProgressWidth * secondPercent;
                    mSecondProgressPaint.setColor(mSecondProgressColor);
                    //left circle
                    if (mSecondProgress == 0) {
                        if (mShowZeroPoint) {
                            canvas.drawCircle(mPadding + secondProgressHeight / 2.0f, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(mPadding + secondProgressHeight / 2.0f, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                    }
                    //right circle
                    if (mSecondProgress == 0) {
                        if (mShowZeroPoint) {
                            canvas.drawCircle(mPadding + secondProgressHeight / 2.0f + dx, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(mPadding + secondProgressHeight / 2.0f + dx, mPadding + secondProgressHeight / 2.0f, secondProgressHeight / 2.0f, mSecondProgressPaint);
                    }
                    //middle line
                    RectF rectF = new RectF(mPadding + secondProgressHeight / 2.0f, mPadding, mPadding + secondProgressHeight / 2.0f + dx, mPadding + secondProgressHeight);
                    canvas.drawRect(rectF, mSecondProgressPaint);
                }
            }
        }
        
    }
    
    /**
     * 绘制方形进度
     */
    private void drawProgressRectMode(Canvas canvas) {
        int width = getWidth();
        float percent = 0;
        if (mMax != 0) {
            percent = mProgress * 1.0f / mMax;
        }
        int progressHeight = getHeight() - mPadding * 2;
        if (mOpenGradient) {
            int progressWidth = width - mPadding * 2;
            float mDx = progressWidth * percent;
            
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = mGradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = mGradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                mPadding + progressHeight / 2.0f, mPadding, mPadding + progressHeight / 2.0f + mDx, mPadding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            mGradientPaint.setShader(shader);
            
            //progress line
            RectF rectF = new RectF(mPadding, mPadding, mPadding + mDx, mPadding + progressHeight);
            canvas.drawRect(rectF, mGradientPaint);
            
        } else {
            int progressWidth = width - mPadding * 2;
            float dx = progressWidth * percent;
            mProgressPaint.setColor(mProgressColor);
            RectF rectF = new RectF(mPadding, mPadding, mPadding + dx, mPadding + progressHeight);
            canvas.drawRect(rectF, mProgressPaint);
        }
        
        //draw second progress
        if (mShowSecondProgress) {
            float secondPercent = 0;
            if (mMax != 0) {
                secondPercent = mSecondProgress * 1.0f / mMax;
            }
            int secondProgressHeight = getHeight() - mPadding * 2;
            if (mOpenSecondGradient) {
                int secondProgressWidth = width - mPadding * 2;
                float secondDx = secondProgressWidth * secondPercent;
                
                int[] secondColors = new int[2];
                float[] secondPositions = new float[2];
                //from color
                secondColors[0] = mSecondGradientFrom;
                secondPositions[0] = 0;
                //to color
                secondColors[1] = mSecondGradientTo;
                secondPositions[1] = 1;
                LinearGradient secondShader = new LinearGradient(
                    mPadding + secondProgressHeight / 2.0f, mPadding, mPadding + secondProgressHeight / 2.0f + secondDx, mPadding + secondProgressHeight,
                    secondColors,
                    secondPositions,
                    Shader.TileMode.MIRROR);
                //gradient
                mSecondGradientPaint.setShader(secondShader);
                
                //progress line
                RectF rectF = new RectF(mPadding, mPadding, mPadding + secondDx, mPadding + secondProgressHeight);
                canvas.drawRect(rectF, mSecondGradientPaint);
            } else {
                //no gradient
                //line shape
                int secondProgressWidth = width - mPadding * 2;
                float dx = secondProgressWidth * secondPercent;
                mSecondProgressPaint.setColor(mSecondProgressColor);
                RectF rectF = new RectF(mPadding, mPadding, mPadding + dx, mPadding + secondProgressHeight);
                canvas.drawRect(rectF, mSecondProgressPaint);
            }
        }
        
    }
    
    /**
     * 绘制圆角矩形进度
     */
    private void drawProgressRoundRectMode(Canvas canvas) {
        int width = getWidth();
        float percent = 0;
        if (mMax != 0) {
            percent = mProgress * 1.0f / mMax;
        }
        int progressHeight = getHeight() - mPadding * 2;
        int progressWidth = width - mPadding * 2 - mBorderWidth;
        float dx = progressWidth * percent;
        if (mOpenGradient) {
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = mGradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = mGradientTo;
            positions[1] = 1;
            float left = mPadding + progressHeight / 2.0f;
            LinearGradient shader = new LinearGradient(
                left, mPadding, left + dx, mPadding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            mGradientPaint.setShader(shader);
            //progress line
            float rectLeft = mPadding + mBorderWidth / 2.0f;
            float rectTop = mPadding + mBorderWidth / 2.0f;
            RectF rectF = new RectF(rectLeft, rectTop, rectLeft + dx, getHeight() - rectTop);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mGradientPaint);
        } else {
            mProgressPaint.setColor(mProgressColor);
            float rectLeft = mPadding + mBorderWidth / 2.0f;
            float rectTop = mPadding + mBorderWidth / 2.0f;
            RectF rectF = new RectF(rectLeft, rectTop, rectLeft + dx, getHeight() - rectTop);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mProgressPaint);
        }
        
        //draw second progress
        if (mShowSecondProgress) {
            float secondPercent = 0;
            if (mMax != 0) {
                secondPercent = mSecondProgress * 1.0f / mMax;
            }
            int secondProgressHeight = getHeight() - mPadding * 2;
            int secondProgressWidth = width - mPadding * 2 - mBorderWidth;
            float secondDx = secondProgressWidth * secondPercent;
            if (mOpenSecondGradient) {
                int[] secondColors = new int[2];
                float[] secondPositions = new float[2];
                //from color
                secondColors[0] = mSecondGradientFrom;
                secondPositions[0] = 0;
                //to color
                secondColors[1] = mSecondGradientTo;
                secondPositions[1] = 1;
                float left = mPadding + secondProgressHeight / 2.0f;
                LinearGradient secondShader = new LinearGradient(
                    left, mPadding, left + secondDx, mPadding + secondProgressHeight,
                    secondColors,
                    secondPositions,
                    Shader.TileMode.MIRROR);
                //gradient
                mSecondGradientPaint.setShader(secondShader);
                
                //progress line
                float rectLeft = mPadding + mBorderWidth / 2.0f;
                float rectTop = mPadding + mBorderWidth / 2.0f;
                RectF rectF = new RectF(rectLeft, rectTop, rectLeft + secondDx, getHeight() - rectTop);
                canvas.drawRoundRect(rectF, mRadius, mRadius, mSecondGradientPaint);
            } else {
                //no gradient
                //line shape
                mSecondProgressPaint.setColor(mSecondProgressColor);
                float rectLeft = mPadding + mBorderWidth / 2.0f;
                float rectTop = mPadding + mBorderWidth / 2.0f;
                RectF rectF = new RectF(rectLeft, rectTop, rectLeft + secondDx, getHeight() - rectTop);
                canvas.drawRoundRect(rectF, mRadius, mRadius, mSecondProgressPaint);
            }
        }
        
    }
    
    /**
     * 绘制半圆形背景
     */
    private void drawBackgroundCircleMode(Canvas canvas) {
        int bgHeight = getHeight();
        int width = getWidth();
        //left circle
        canvas.drawCircle(bgHeight / 2.0f, bgHeight / 2.0f, bgHeight / 2.0f, mBgPaint);
        //right circle
        canvas.drawCircle(width - bgHeight / 2.0f, bgHeight / 2.0f, bgHeight / 2.0f, mBgPaint);
        //middle line
        RectF rectF = new RectF(bgHeight / 2.0f, 0, width - bgHeight / 2.0f, bgHeight);
        canvas.drawRect(rectF, mBgPaint);
    }
    
    /**
     * 绘制半圆形边框
     */
    private void drawBorderCircleMode(Canvas canvas) {
        if (mDrawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRoundRect(rect, bgHeight / 2.0f, bgHeight / 2.0f, mBorderPaint);
        }
    }
    
    /**
     * 绘制半方形边框
     */
    private void drawBorderRectMode(Canvas canvas) {
        if (mDrawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRect(rect, mBorderPaint);
        }
    }
    
    /**
     * 绘制圆角矩形边框
     */
    private void drawBorderRoundRect(Canvas canvas) {
        if (mDrawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(mBorderWidth / 2.0f, mBorderWidth / 2.0f, width - mBorderWidth / 2.0f, bgHeight - mBorderWidth / 2.0f);
            canvas.drawRoundRect(rect, mRadius, mRadius, mBorderPaint);
        }
    }
    
    /**
     * 绘制方形背景
     */
    private void drawBackgroundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        int width = getWidth();
        RectF rectF = new RectF(0, 0, width, bgHeight);
        canvas.drawRect(rectF, mBgPaint);
    }
    
    /**
     * 绘制圆角矩形背景
     */
    private void drawBackgroundRoundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        int width = getWidth();
        RectF rectF = new RectF(mBorderWidth / 2.0f, mBorderWidth / 2.0f, width - mBorderWidth / 2.0f, bgHeight - mBorderWidth / 2.0f);
        canvas.drawRoundRect(rectF, mRadius, mRadius, mBgPaint);
    }
    
    /**
     * 获取最大值
     *
     * @return 最大值
     */
    public int getMax() {
        return mMax;
    }
    
    /**
     * 设置最大值
     *
     * @param max 最大值
     */
    public void setMax(int max) {
        this.mMax = max;
        invalidate();
    }
    
    /**
     * 获取一级进度值
     *
     * @return 进度值
     */
    public int getProgress() {
        return mProgress;
    }
    
    /**
     * 设置一级进度值
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            this.mProgress = 0;
        } else if (progress > mMax) {
            this.mProgress = mMax;
        } else {
            this.mProgress = progress;
        }
        invalidate();
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onProgressChanged(this, mMax, this.mProgress);
        }
    }
    
    /**
     * 是否显示二级进度条
     *
     * @return 是/否
     */
    public boolean isShowSecondProgress() {
        return mShowSecondProgress;
    }
    
    /**
     * 设置是否显示二级进度条
     *
     * @param showSecondProgress 是/否
     */
    public void setShowSecondProgress(boolean showSecondProgress) {
        this.mShowSecondProgress = showSecondProgress;
        invalidate();
    }
    
    /**
     * 获取二级进度条进度
     *
     * @return 进度值
     */
    public int getSecondProgress() {
        return mSecondProgress;
    }
    
    /**
     * 设置二级进度条进度
     *
     * @param secondProgress 进度值
     */
    public void setSecondProgress(int secondProgress) {
        if (secondProgress < 0) {
            this.mSecondProgress = 0;
        } else if (secondProgress > mMax) {
            this.mSecondProgress = mMax;
        } else {
            this.mSecondProgress = secondProgress;
        }
        invalidate();
        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onSecondProgressChanged(this, mMax, this.mSecondProgress);
        }
    }
    
    /**
     * 获取二级进度条形状
     *
     * @return 形状，点：{@link #SHAPE_POINT} 线：{@link #SHAPE_LINE}
     */
    public int getSecondProgressShape() {
        return mSecondProgressShape;
    }
    
    /**
     * 设置二级进度条形状
     *
     * @param secondProgressShape 形状，点：{@link #SHAPE_POINT} 线：{@link #SHAPE_LINE}
     */
    public void setSecondProgressShape(@SecondProgressShape int secondProgressShape) {
        this.mSecondProgressShape = secondProgressShape;
        invalidate();
    }
    
    /**
     * 获取背景色
     *
     * @return 颜色值
     */
    public int getBgColor() {
        return mBgColor;
    }
    
    /**
     * 设置背景色
     *
     * @param bgColor 颜色值
     */
    public void setBgColor(@ColorInt int bgColor) {
        this.mBgColor = bgColor;
        mBgPaint.setColor(bgColor);
        invalidate();
    }
    
    /**
     * 获取二级渐变是否启用
     *
     * @return 是/否
     */
    public boolean isOpenSecondGradient() {
        return mOpenSecondGradient;
    }
    
    /**
     * 设置二级渐变是否启用
     *
     * @param openSecondGradient 是/否
     */
    public void setOpenSecondGradient(boolean openSecondGradient) {
        this.mOpenSecondGradient = openSecondGradient;
        invalidate();
    }
    
    public int getSecondGradientFrom() {
        return mSecondGradientFrom;
    }
    
    public int getSecondGradientTo() {
        return mSecondGradientTo;
    }
    
    /**
     * 获取二级进度条颜色
     *
     * @return 颜色值
     */
    public int getSecondProgressColor() {
        return mSecondProgressColor;
    }
    
    /**
     * 设置二级进度条颜色
     *
     * @param secondProgressColor 颜色值
     */
    public void setSecondProgressColor(@ColorInt int secondProgressColor) {
        this.mSecondProgressColor = secondProgressColor;
        mSecondProgressPaint.setColor(secondProgressColor);
        invalidate();
    }
    
    /**
     * 获取一级进度条颜色
     *
     * @return 颜色值
     */
    public int getProgressColor() {
        return mProgressColor;
    }
    
    /**
     * 设置一级进度条颜色
     *
     * @param progressColor 颜色值
     */
    public void setProgressColor(@ColorInt int progressColor) {
        this.mProgressColor = progressColor;
        mProgressPaint.setColor(progressColor);
        invalidate();
    }
    
    /**
     * 获取内边距
     *
     * @return 边距值
     */
    public int getPadding() {
        return mPadding;
    }
    
    /**
     * 设置内边距
     *
     * @param padding 边距值
     */
    public void setPadding(int padding) {
        this.mPadding = padding;
        invalidate();
    }
    
    /**
     * 设置显示模式
     *
     * @param showMode 显示模式，0：半圆，1：方形，2：圆角矩形
     */
    public void setShowMode(@ShowMode int showMode) {
        switch (showMode) {
            case SHOW_MODE_ROUND:
                this.mShowMode = 0;
                break;
            case SHOW_MODE_RECT:
                this.mShowMode = 1;
                break;
            case SHOW_MODE_ROUND_RECT:
                this.mShowMode = 2;
                break;
        }
        invalidate();
    }
    
    /**
     * 获取进度百分比，int类型
     *
     * @return percentage value
     */
    public int getPercentage() {
        if (mMax == 0) {
            return 0;
        }
        return (int) (mProgress * 100.0f / mMax + 0.5f);
    }
    
    /**
     * 获取进度百分比，float类型
     *
     * @return percentage value
     */
    public float getPercentageFloat() {
        if (mMax == 0) {
            return 0f;
        }
        return mProgress * 100.0f / mMax;
    }
    
    /**
     * 一级渐变色是否启用
     *
     * @return 是/否
     */
    public boolean isOpenGradient() {
        return mOpenGradient;
    }
    
    /**
     * 设置一级渐变色是否启用
     *
     * @param openGradient 是/否
     */
    public void setOpenGradient(boolean openGradient) {
        this.mOpenGradient = openGradient;
        invalidate();
    }
    
    public int getGradientFrom() {
        return mGradientFrom;
    }
    
    public int getGradientTo() {
        return mGradientTo;
    }
    
    /**
     * 设置边框颜色
     *
     * @param borderColor 颜色值
     */
    public void setBorderColor(@ColorInt int borderColor) {
        this.mBorderColor = borderColor;
        this.mBorderPaint.setColor(this.mBorderColor);
        invalidate();
    }
    
    /**
     * 设置一级进度条的渐变色
     *
     * @param from 起始颜色
     * @param to   结束颜色
     */
    public void setGradientColor(int from, int to) {
        this.mGradientFrom = from;
        this.mGradientTo = to;
        invalidate();
    }
    
    /**
     * 设置二级进度条的渐变色
     *
     * @param from 起始颜色
     * @param to   结束颜色
     */
    public void setSecondGradientColor(int from, int to) {
        this.mSecondGradientFrom = from;
        this.mSecondGradientTo = to;
        invalidate();
    }
    
    /**
     * 设置一级进度条的渐变色和边框颜色
     *
     * @param from        起始颜色
     * @param to          结束颜色
     * @param borderColor 边框颜色
     */
    public void setGradientColorAndBorderColor(int from, int to, int borderColor) {
        this.mGradientFrom = from;
        this.mGradientTo = to;
        this.mBorderColor = borderColor;
        this.mBorderPaint.setColor(this.mBorderColor);
        invalidate();
    }
    
    /**
     * 获取边框颜色
     *
     * @return 颜色值
     */
    public int getBorderColor() {
        return mBorderColor;
    }
    
    /**
     * 设置进度变化监听（包括一级和二级进度）
     *
     * @param onProgressChangedListener 进度值变化回调
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mOnProgressChangedListener = onProgressChangedListener;
    }
}
