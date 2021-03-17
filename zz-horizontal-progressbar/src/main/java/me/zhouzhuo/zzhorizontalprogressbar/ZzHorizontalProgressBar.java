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


/**
 * 水平进度条
 *
 * Created by 周卓 on 2016/9/22.
 */
public class ZzHorizontalProgressBar extends View {
    
    private int max;
    private int progress;
    private int bgColor;
    private int progressColor;
    private int padding;
    private boolean openGradient;
    private int gradientFrom;
    private int gradientTo;
    private boolean showSecondProgress;
    private int secondProgress;
    private int secondProgressShape;
    private boolean showZeroPoint;
    
    private Paint secondProgressPaint;
    private Paint secondGradientPaint;
    private Paint progressPaint;
    private Paint gradientPaint;
    private Paint bgPaint;
    private boolean openSecondGradient;
    private int secondGradientFrom;
    private int secondGradientTo;
    private int secondProgressColor;
    
    private int radius;
    private boolean drawBorder = false;
    private int borderColor;
    private int borderWidth;
    
    private int showMode = 0;
    private Paint borderPaint;
    
    public static enum ShowMode {
        ROUND, RECT, ROUND_RECT
    }
    
    private OnProgressChangedListener onProgressChangedListener;
    
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
        max = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_max, 100);
        progress = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_progress, 0);
        bgColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_bg_color, 0xff3F51B5);
        progressColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_pb_color, 0xffFF4081);
        secondProgressColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_pb_color, 0xffFF4081);
        padding = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_padding, 0);
        showZeroPoint = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_show_zero_point, false);
        showSecondProgress = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_show_second_progress, false);
        secondProgress = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_second_progress, 0);
        secondProgressShape = a.getInteger(R.styleable.ZzHorizontalProgressBar_zpb_show_second_point_shape, 0);
        openGradient = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_open_gradient, false);
        gradientFrom = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_gradient_from, 0xffFF4081);
        gradientTo = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_gradient_to, 0xffFF4081);
        openSecondGradient = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_open_second_gradient, false);
        showMode = a.getInt(R.styleable.ZzHorizontalProgressBar_zpb_show_mode, 0);
        secondGradientFrom = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_gradient_from, 0xffFF4081);
        secondGradientTo = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_second_gradient_to, 0xffFF4081);
        radius = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_round_rect_radius, 20);
        drawBorder = a.getBoolean(R.styleable.ZzHorizontalProgressBar_zpb_draw_border, false);
        borderWidth = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_border_width, 1);
        borderColor = a.getColor(R.styleable.ZzHorizontalProgressBar_zpb_border_color, 0xffff001f);
        a.recycle();
    }
    
    private void initPaths() {
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);
        
        secondProgressPaint = new Paint();
        secondProgressPaint.setColor(secondProgressColor);
        secondProgressPaint.setStyle(Paint.Style.FILL);
        secondProgressPaint.setAntiAlias(true);
        
        gradientPaint = new Paint();
        gradientPaint.setStyle(Paint.Style.FILL);
        gradientPaint.setAntiAlias(true);
        
        secondGradientPaint = new Paint();
        secondGradientPaint.setStyle(Paint.Style.FILL);
        secondGradientPaint.setAntiAlias(true);
        
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);
        
        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setAntiAlias(true);
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (showMode) {
            case 0:
                //half circle
                drawBackgroundCircleMode(canvas);
                drawProgressCircleMode(canvas);
                drawBorderCircleMode(canvas);
                break;
            case 1:
                //rect
                drawBackgroundRectMode(canvas);
                drawProgressRectMode(canvas);
                drawBorderRectMode(canvas);
                break;
            case 2:
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
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (openGradient) {
            int progressWidth = width - padding * 2;
            float dx = progressWidth * percent;
            
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                padding + progressHeight / 2.0f, padding, padding + progressHeight / 2.0f + dx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            
            int radius = width > getHeight() ? getHeight() / 2 : width / 2;
            if (dx < getHeight()) {
                //left circle
                if (progress == 0) {
                    if (showZeroPoint) {
                        canvas.drawCircle(padding + progressHeight / 2.0f, padding + progressHeight / 2.0f, progressHeight / 2.0f, gradientPaint);
                    }
                } else {
                    canvas.drawCircle(padding + progressHeight / 2.0f, padding + progressHeight / 2.0f, progressHeight / 2.0f, gradientPaint);
                }
                
            } else {
                //progress line
                RectF rectF = new RectF(padding, padding, padding + dx, padding + progressHeight);
                canvas.drawRoundRect(rectF, radius, radius, gradientPaint);
            }
            
        } else {
            int progressWidth = width - padding * 2 - progressHeight;
            float dx = progressWidth * percent;
            progressPaint.setColor(progressColor);
            float left = padding + progressHeight / 2.0f;
            //left circle
            if (progress == 0) {
                if (showZeroPoint) {
                    canvas.drawCircle(left, left, progressHeight / 2.0f, progressPaint);
                }
            } else {
                canvas.drawCircle(left, left, progressHeight / 2.0f, progressPaint);
            }
            //right circle
            if (progress == 0) {
                if (showZeroPoint) {
                    canvas.drawCircle(left + dx, left, progressHeight / 2.0f, progressPaint);
                }
            } else {
                canvas.drawCircle(left + dx, left, progressHeight / 2.0f, progressPaint);
            }
            //middle line
            RectF rectF = new RectF(left, padding, left + dx, padding + progressHeight);
            canvas.drawRect(rectF, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float secondPercent = 0;
            if (max != 0) {
                secondPercent = secondProgress * 1.0f / max;
            }
            int s_progressHeight = getHeight() - padding * 2;
            if (openSecondGradient) {
                int secondProgressWidth = width - padding * 2;
                float secondDx = secondProgressWidth * secondPercent;
                
                int[] secondColors = new int[2];
                float[] secondPositions = new float[2];
                //from color
                secondColors[0] = secondGradientFrom;
                secondPositions[0] = 0;
                //to color
                secondColors[1] = secondGradientTo;
                secondPositions[1] = 1;
                LinearGradient s_shader = new LinearGradient(
                    padding + s_progressHeight / 2.0f, padding, padding + s_progressHeight / 2.0f + secondDx, padding + s_progressHeight,
                    secondColors,
                    secondPositions,
                    Shader.TileMode.MIRROR);
                //gradient
                secondGradientPaint.setShader(s_shader);
                
                int s_radius = width > getHeight() ? getHeight() / 2 : width / 2;
                if (secondDx < getHeight()) {
                    //left circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2.0f, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondGradientPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2.0f, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondGradientPaint);
                    }
                } else {
                    //progress line
                    RectF rectF = new RectF(padding, padding, padding + secondDx, padding + s_progressHeight);
                    canvas.drawRoundRect(rectF, s_radius, s_radius, secondGradientPaint);
                }
            } else {
                //no gradient
                if (secondProgressShape == 0) {
                    //point shape
                    int s_progressWidth = width - padding * 2;
                    float s_mDx = s_progressWidth * secondPercent;
                    //progress line
                    float px = padding + s_progressHeight / 2.0f + s_mDx;
                    if (px < width - padding - s_progressHeight / 2.0f) {
                        if (secondProgress == 0) {
                            if (showZeroPoint) {
                                canvas.drawCircle(px, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                            }
                        } else {
                            canvas.drawCircle(px, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(px - s_progressHeight, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                    }
                    
                } else {
                    //line shape
                    int s_progressWidth = width - padding * 2 - s_progressHeight;
                    float dx = s_progressWidth * secondPercent;
                    secondProgressPaint.setColor(secondProgressColor);
                    //left circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2.0f, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2.0f, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                    }
                    //right circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2.0f + dx, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2.0f + dx, padding + s_progressHeight / 2.0f, s_progressHeight / 2.0f, secondProgressPaint);
                    }
                    //middle line
                    RectF midRecf = new RectF(padding + s_progressHeight / 2.0f, padding, padding + s_progressHeight / 2.0f + dx, padding + s_progressHeight);
                    canvas.drawRect(midRecf, secondProgressPaint);
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
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (openGradient) {
            int progressWidth = width - padding * 2;
            float mDx = progressWidth * percent;
            
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                padding + progressHeight / 2.0f, padding, padding + progressHeight / 2.0f + mDx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            
            //progress line
            RectF rectF = new RectF(padding, padding, padding + mDx, padding + progressHeight);
            canvas.drawRect(rectF, gradientPaint);
            
        } else {
            int progressWidth = width - padding * 2;
            float dx = progressWidth * percent;
            progressPaint.setColor(progressColor);
            RectF rectF = new RectF(padding, padding, padding + dx, padding + progressHeight);
            canvas.drawRect(rectF, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float s_percent = 0;
            if (max != 0) {
                s_percent = secondProgress * 1.0f / max;
            }
            int s_progressHeight = getHeight() - padding * 2;
            if (openSecondGradient) {
                int s_progressWidth = width - padding * 2;
                float s_mDx = s_progressWidth * s_percent;
                
                int[] s_colors = new int[2];
                float[] s_positions = new float[2];
                //from color
                s_colors[0] = secondGradientFrom;
                s_positions[0] = 0;
                //to color
                s_colors[1] = secondGradientTo;
                s_positions[1] = 1;
                LinearGradient s_shader = new LinearGradient(
                    padding + s_progressHeight / 2.0f, padding, padding + s_progressHeight / 2.0f + s_mDx, padding + s_progressHeight,
                    s_colors,
                    s_positions,
                    Shader.TileMode.MIRROR);
                //gradient
                secondGradientPaint.setShader(s_shader);
                
                //progress line
                RectF rectF = new RectF(padding, padding, padding + s_mDx, padding + s_progressHeight);
                canvas.drawRect(rectF, secondGradientPaint);
            } else {
                //no gradient
                //line shape
                int s_progressWidth = width - padding * 2;
                float dx = s_progressWidth * s_percent;
                secondProgressPaint.setColor(secondProgressColor);
                RectF rectF = new RectF(padding, padding, padding + dx, padding + s_progressHeight);
                canvas.drawRect(rectF, secondProgressPaint);
            }
        }
        
    }
    
    /**
     * 绘制圆角矩形进度
     */
    private void drawProgressRoundRectMode(Canvas canvas) {
        int width = getWidth();
        float percent = 0;
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        int progressWidth = width - padding * 2 - borderWidth;
        float dx = progressWidth * percent;
        if (openGradient) {
            int[] colors = new int[2];
            float[] positions = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            float left = padding + progressHeight / 2.0f;
            LinearGradient shader = new LinearGradient(
                left, padding, left + dx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            //progress line
            float rectLeft = padding + borderWidth / 2.0f;
            float rectTop = padding + borderWidth / 2.0f;
            RectF rectF = new RectF(rectLeft, rectTop, rectLeft + dx, getHeight() - rectTop);
            canvas.drawRoundRect(rectF, radius, radius, gradientPaint);
        } else {
            progressPaint.setColor(progressColor);
            float rectLeft = padding + borderWidth / 2.0f;
            float rectTop = padding + borderWidth / 2.0f;
            RectF rectF = new RectF(rectLeft, rectTop, rectLeft + dx, getHeight() - rectTop);
            canvas.drawRoundRect(rectF, radius, radius, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float secondPercent = 0;
            if (max != 0) {
                secondPercent = secondProgress * 1.0f / max;
            }
            int secondProgressHeight = getHeight() - padding * 2;
            int secondProgressWidth = width - padding * 2 - borderWidth;
            float secondDx = secondProgressWidth * secondPercent;
            if (openSecondGradient) {
                int[] secondColors = new int[2];
                float[] secondPositions = new float[2];
                //from color
                secondColors[0] = secondGradientFrom;
                secondPositions[0] = 0;
                //to color
                secondColors[1] = secondGradientTo;
                secondPositions[1] = 1;
                float left = padding + secondProgressHeight / 2.0f;
                LinearGradient s_shader = new LinearGradient(
                    left, padding, left + secondDx, padding + secondProgressHeight,
                    secondColors,
                    secondPositions,
                    Shader.TileMode.MIRROR);
                //gradient
                secondGradientPaint.setShader(s_shader);
                
                //progress line
                float rectLeft = padding + borderWidth / 2.0f;
                float rectTop = padding + borderWidth / 2.0f;
                RectF rectF = new RectF(rectLeft, rectTop, rectLeft + secondDx, getHeight() - rectTop);
                canvas.drawRoundRect(rectF, radius, radius, secondGradientPaint);
            } else {
                //no gradient
                //line shape
                secondProgressPaint.setColor(secondProgressColor);
                float rectLeft = padding + borderWidth / 2.0f;
                float rectTop = padding + borderWidth / 2.0f;
                RectF rectF = new RectF(rectLeft, rectTop, rectLeft + secondDx, getHeight() - rectTop);
                canvas.drawRoundRect(rectF, radius, radius, secondProgressPaint);
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
        canvas.drawCircle(bgHeight / 2.0f, bgHeight / 2.0f, bgHeight / 2.0f, bgPaint);
        //right circle
        canvas.drawCircle(width - bgHeight / 2.0f, bgHeight / 2.0f, bgHeight / 2.0f, bgPaint);
        //middle line
        RectF rectF = new RectF(bgHeight / 2.0f, 0, width - bgHeight / 2.0f, bgHeight);
        canvas.drawRect(rectF, bgPaint);
    }
    
    /**
     * 绘制半圆形边框
     */
    private void drawBorderCircleMode(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRoundRect(rect, bgHeight / 2.0f, bgHeight / 2.0f, borderPaint);
        }
    }
    
    /**
     * 绘制半方形边框
     */
    private void drawBorderRectMode(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRect(rect, borderPaint);
        }
    }
    
    /**
     * 绘制圆角矩形边框
     */
    private void drawBorderRoundRect(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            int width = getWidth();
            RectF rect = new RectF(borderWidth / 2.0f, borderWidth / 2.0f, width - borderWidth / 2.0f, bgHeight - borderWidth / 2.0f);
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        }
    }
    
    /**
     * 绘制方形背景
     */
    private void drawBackgroundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        int width = getWidth();
        RectF rectF = new RectF(0, 0, width, bgHeight);
        canvas.drawRect(rectF, bgPaint);
    }
    
    /**
     * 绘制圆角矩形背景
     */
    private void drawBackgroundRoundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        int width = getWidth();
        RectF rectF = new RectF(borderWidth / 2.0f, borderWidth / 2.0f, width - borderWidth / 2.0f, bgHeight - borderWidth / 2.0f);
        canvas.drawRoundRect(rectF, radius, radius, bgPaint);
    }
    
    /**
     * 获取最大值
     *
     * @return 最大值
     */
    public int getMax() {
        return max;
    }
    
    /**
     * 设置最大值
     *
     * @param max 最大值
     */
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
    
    /**
     * 获取一级进度值
     *
     * @return 进度值
     */
    public int getProgress() {
        return progress;
    }
    
    /**
     * 设置一级进度值
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            this.progress = 0;
        } else if (progress > max) {
            this.progress = max;
        } else {
            this.progress = progress;
        }
        invalidate();
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged(this, max, this.progress);
        }
    }
    
    /**
     * 是否显示二级进度条
     *
     * @return 是/否
     */
    public boolean isShowSecondProgress() {
        return showSecondProgress;
    }
    
    /**
     * 设置是否显示二级进度条
     *
     * @param showSecondProgress 是/否
     */
    public void setShowSecondProgress(boolean showSecondProgress) {
        this.showSecondProgress = showSecondProgress;
        invalidate();
    }
    
    /**
     * 获取二级进度条进度
     *
     * @return 进度值
     */
    public int getSecondProgress() {
        return secondProgress;
    }
    
    /**
     * 设置二级进度条进度
     *
     * @param secondProgress 进度值
     */
    public void setSecondProgress(int secondProgress) {
        if (secondProgress < 0) {
            this.secondProgress = 0;
        } else if (secondProgress > max) {
            this.secondProgress = max;
        } else {
            this.secondProgress = secondProgress;
        }
        invalidate();
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onSecondProgressChanged(this, max, this.secondProgress);
        }
    }
    
    /**
     * 获取二级进度条形状
     *
     * @return 形状，0：点，1：线
     */
    public int getSecondProgressShape() {
        return secondProgressShape;
    }
    
    /**
     * 设置二级进度条形状
     *
     * @param secondProgressShape 形状，0：点，1：线
     */
    public void setSecondProgressShape(int secondProgressShape) {
        this.secondProgressShape = secondProgressShape;
        invalidate();
    }
    
    /**
     * 获取背景色
     *
     * @return 颜色值
     */
    public int getBgColor() {
        return bgColor;
    }
    
    /**
     * 设置背景色
     *
     * @param bgColor 颜色值
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bgPaint.setColor(bgColor);
        invalidate();
    }
    
    /**
     * 获取二级渐变是否启用
     *
     * @return 是/否
     */
    public boolean isOpenSecondGradient() {
        return openSecondGradient;
    }
    
    /**
     * 设置二级渐变是否启用
     *
     * @param openSecondGradient 是/否
     */
    public void setOpenSecondGradient(boolean openSecondGradient) {
        this.openSecondGradient = openSecondGradient;
        invalidate();
    }
    
    public int getSecondGradientFrom() {
        return secondGradientFrom;
    }
    
    public int getSecondGradientTo() {
        return secondGradientTo;
    }
    
    /**
     * 获取二级进度条颜色
     *
     * @return 颜色值
     */
    public int getSecondProgressColor() {
        return secondProgressColor;
    }
    
    /**
     * 设置二级进度条颜色
     *
     * @param secondProgressColor 颜色值
     */
    public void setSecondProgressColor(int secondProgressColor) {
        this.secondProgressColor = secondProgressColor;
        secondProgressPaint.setColor(secondProgressColor);
        invalidate();
    }
    
    /**
     * 获取一级进度条颜色
     *
     * @return 颜色值
     */
    public int getProgressColor() {
        return progressColor;
    }
    
    /**
     * 设置一级进度条颜色
     *
     * @param progressColor 颜色值
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidate();
    }
    
    /**
     * 获取内边距
     *
     * @return 边距值
     */
    public int getPadding() {
        return padding;
    }
    
    /**
     * 设置内边距
     *
     * @param padding 边距值
     */
    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }
    
    /**
     * 设置显示模式
     *
     * @param showMode 显示模式，0：半圆，1：方形，2：圆角矩形
     */
    public void setShowMode(ShowMode showMode) {
        switch (showMode) {
            case ROUND:
                this.showMode = 0;
                break;
            case RECT:
                this.showMode = 1;
                break;
            case ROUND_RECT:
                this.showMode = 2;
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
        if (max == 0) {
            return 0;
        }
        return (int) (progress * 100.0f / max + 0.5f);
    }
    
    /**
     * 获取进度百分比，float类型
     *
     * @return percentage value
     */
    public float getPercentageFloat() {
        if (max == 0) {
            return 0f;
        }
        return progress * 100.0f / max;
    }
    
    /**
     * 一级渐变色是否启用
     *
     * @return 是/否
     */
    public boolean isOpenGradient() {
        return openGradient;
    }
    
    /**
     * 设置一级渐变色是否启用
     *
     * @param openGradient 是/否
     */
    public void setOpenGradient(boolean openGradient) {
        this.openGradient = openGradient;
        invalidate();
    }
    
    public int getGradientFrom() {
        return gradientFrom;
    }
    
    public int getGradientTo() {
        return gradientTo;
    }
    
    /**
     * 设置边框颜色
     *
     * @param borderColor 颜色值
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        this.borderPaint.setColor(this.borderColor);
        invalidate();
    }
    
    /**
     * 设置一级进度条的渐变色
     *
     * @param from 起始颜色
     * @param to   结束颜色
     */
    public void setGradientColor(int from, int to) {
        this.gradientFrom = from;
        this.gradientTo = to;
        invalidate();
    }
    
    /**
     * 设置二级进度条的渐变色
     *
     * @param from 起始颜色
     * @param to   结束颜色
     */
    public void setSecondGradientColor(int from, int to) {
        this.secondGradientFrom = from;
        this.secondGradientTo = to;
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
        this.gradientFrom = from;
        this.gradientTo = to;
        this.borderColor = borderColor;
        this.borderPaint.setColor(this.borderColor);
        invalidate();
    }
    
    /**
     * 获取边框颜色
     *
     * @return 颜色值
     */
    public int getBorderColor() {
        return borderColor;
    }
    
    /**
     * 设置进度变化监听（包括一级和二级进度）
     *
     * @param onProgressChangedListener 进度值变化回调
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }
}
