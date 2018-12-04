package me.zhouzhuo.zzhorizontalprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


/**
 * A Horizontal ProgressBar that is customized easily.
 * Created by zz on 2016/9/22.
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
    
    //    private boolean useRectMode = false;
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
                //half cicle
                drawBackground(canvas);
                drawProgress(canvas);
                drawBorder(canvas);
                break;
            case 1:
                //rect
                drawBackgroundRectMode(canvas);
                drawProgressRectMode(canvas);
                drawBorderRect(canvas);
                break;
            case 2:
                //custom radius
                drawBackgroundRoundRectMode(canvas);
                drawProgressRoundRectMode(canvas);
                drawBorderRoundRect(canvas);
                break;
        }
    }
    
    private void drawProgress(Canvas canvas) {
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        float percent = 0;
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (progressHeight % 2 != 0) {
            progressHeight = progressHeight - 1;
        }
        if (openGradient) {
            int progressWidth = width - padding * 2;
            float mDx = progressWidth * percent;
            
            int colors[] = new int[2];
            float positions[] = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                padding + progressHeight / 2, padding, padding + progressHeight / 2 + mDx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            
            int radius = width > getHeight() ? getHeight() / 2 : width / 2;
            if (mDx < getHeight()) {
                //left circle
                if (progress == 0) {
                    if (showZeroPoint) {
                        canvas.drawCircle(padding + progressHeight / 2, padding + progressHeight / 2, progressHeight / 2, gradientPaint);
                    }
                } else {
                    canvas.drawCircle(padding + progressHeight / 2, padding + progressHeight / 2, progressHeight / 2, gradientPaint);
                }
                
            } else {
                //progress line
                RectF rectF = new RectF(padding, padding, padding + mDx, padding + progressHeight);
                canvas.drawRoundRect(rectF, radius, radius, gradientPaint);
            }
            
        } else {
            int progressWidth = width - padding * 2 - progressHeight;
            float dx = progressWidth * percent;
            progressPaint.setColor(progressColor);
            //left circle
            if (progress == 0) {
                if (showZeroPoint) {
                    canvas.drawCircle(padding + progressHeight / 2, padding + progressHeight / 2, progressHeight / 2, progressPaint);
                }
            } else {
                canvas.drawCircle(padding + progressHeight / 2, padding + progressHeight / 2, progressHeight / 2, progressPaint);
            }
            //right circle
            if (progress == 0) {
                if (showZeroPoint) {
                    canvas.drawCircle(padding + progressHeight / 2 + dx, padding + progressHeight / 2, progressHeight / 2, progressPaint);
                }
            } else {
                canvas.drawCircle(padding + progressHeight / 2 + dx, padding + progressHeight / 2, progressHeight / 2, progressPaint);
            }
            //middle line
            RectF midRecf = new RectF(padding + progressHeight / 2, padding, padding + progressHeight / 2 + dx, padding + progressHeight);
            canvas.drawRect(midRecf, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float s_percent = 0;
            if (max != 0) {
                s_percent = secondProgress * 1.0f / max;
            }
            int s_progressHeight = getHeight() - padding * 2;
            if (s_progressHeight % 2 != 0) {
                s_progressHeight = s_progressHeight - 1;
            }
            if (openSecondGradient) {
                int s_progressWidth = width - padding * 2;
                float s_mDx = s_progressWidth * s_percent;
                
                int s_colors[] = new int[2];
                float s_positions[] = new float[2];
                //from color
                s_colors[0] = secondGradientFrom;
                s_positions[0] = 0;
                //to color
                s_colors[1] = secondGradientTo;
                s_positions[1] = 1;
                LinearGradient s_shader = new LinearGradient(
                    padding + s_progressHeight / 2, padding, padding + s_progressHeight / 2 + s_mDx, padding + s_progressHeight,
                    s_colors,
                    s_positions,
                    Shader.TileMode.MIRROR);
                //gradient
                secondGradientPaint.setShader(s_shader);
                
                int s_radius = width > getHeight() ? getHeight() / 2 : width / 2;
                if (s_mDx < getHeight()) {
                    //left circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2, padding + s_progressHeight / 2, s_progressHeight / 2, secondGradientPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2, padding + s_progressHeight / 2, s_progressHeight / 2, secondGradientPaint);
                    }
                } else {
                    //progress line
                    RectF rectF = new RectF(padding, padding, padding + s_mDx, padding + s_progressHeight);
                    canvas.drawRoundRect(rectF, s_radius, s_radius, secondGradientPaint);
                }
            } else {
                //no gradient
                if (secondProgressShape == 0) {
                    //point shape
                    int s_progressWidth = width - padding * 2;
                    float s_mDx = s_progressWidth * s_percent;
                    //progress line
                    float px = padding + s_progressHeight / 2 + s_mDx;
                    if (px < width - padding - s_progressHeight / 2) {
                        if (secondProgress ==0) {
                            if (showZeroPoint) {
                                canvas.drawCircle(px, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                            }
                        } else {
                            canvas.drawCircle(px, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(px - s_progressHeight, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                    }
                    
                } else {
                    //line shape
                    int s_progressWidth = width - padding * 2 - s_progressHeight;
                    float dx = s_progressWidth * s_percent;
                    secondProgressPaint.setColor(secondProgressColor);
                    //left circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                    }
                    //right circle
                    if (secondProgress == 0) {
                        if (showZeroPoint) {
                            canvas.drawCircle(padding + s_progressHeight / 2 + dx, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                        }
                    } else {
                        canvas.drawCircle(padding + s_progressHeight / 2 + dx, padding + s_progressHeight / 2, s_progressHeight / 2, secondProgressPaint);
                    }
                    //middle line
                    RectF midRecf = new RectF(padding + s_progressHeight / 2, padding, padding + s_progressHeight / 2 + dx, padding + s_progressHeight);
                    canvas.drawRect(midRecf, secondProgressPaint);
                }
            }
        }
        
    }
    
    private void drawProgressRectMode(Canvas canvas) {
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        float percent = 0;
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (progressHeight % 2 != 0) {
            progressHeight = progressHeight - 1;
        }
        if (openGradient) {
            int progressWidth = width - padding * 2;
            float mDx = progressWidth * percent;
            
            int colors[] = new int[2];
            float positions[] = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                padding + progressHeight / 2, padding, padding + progressHeight / 2 + mDx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            
            int radius = width > getHeight() ? getHeight() / 2 : width / 2;
            //progress line
            RectF rectF = new RectF(padding, padding, padding + mDx, padding + progressHeight);
            canvas.drawRect(rectF, gradientPaint);
            
        } else {
            int progressWidth = width - padding * 2;
            float dx = progressWidth * percent;
            progressPaint.setColor(progressColor);
            RectF midRecf = new RectF(padding, padding, padding + dx, padding + progressHeight);
            canvas.drawRect(midRecf, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float s_percent = 0;
            if (max != 0) {
                s_percent = secondProgress * 1.0f / max;
            }
            int s_progressHeight = getHeight() - padding * 2;
            if (s_progressHeight % 2 != 0) {
                s_progressHeight = s_progressHeight - 1;
            }
            if (openSecondGradient) {
                int s_progressWidth = width - padding * 2;
                float s_mDx = s_progressWidth * s_percent;
                
                int s_colors[] = new int[2];
                float s_positions[] = new float[2];
                //from color
                s_colors[0] = secondGradientFrom;
                s_positions[0] = 0;
                //to color
                s_colors[1] = secondGradientTo;
                s_positions[1] = 1;
                LinearGradient s_shader = new LinearGradient(
                    padding + s_progressHeight / 2, padding, padding + s_progressHeight / 2 + s_mDx, padding + s_progressHeight,
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
                RectF midRecf = new RectF(padding, padding, padding + dx, padding + s_progressHeight);
                canvas.drawRect(midRecf, secondProgressPaint);
            }
        }
        
    }
    
    private void drawProgressRoundRectMode(Canvas canvas) {
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        float percent = 0;
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (progressHeight % 2 != 0) {
            progressHeight = progressHeight - 1;
        }
        if (openGradient) {
            int progressWidth = width - padding * 2 - borderWidth;
            float mDx = progressWidth * percent;
            
            int colors[] = new int[2];
            float positions[] = new float[2];
            //from color
            colors[0] = gradientFrom;
            positions[0] = 0;
            //to color
            colors[1] = gradientTo;
            positions[1] = 1;
            LinearGradient shader = new LinearGradient(
                padding + progressHeight / 2, padding, padding + progressHeight / 2 + mDx, padding + progressHeight,
                colors,
                positions,
                Shader.TileMode.MIRROR);
            //gradient
            gradientPaint.setShader(shader);
            //progress line
            RectF rectF = new RectF(padding + borderWidth / 2, padding + borderWidth / 2, padding + mDx + borderWidth / 2, padding + progressHeight - borderWidth / 2);
            canvas.drawRoundRect(rectF, radius, radius, gradientPaint);
            
        } else {
            int progressWidth = width - padding * 2 - borderWidth;
            float dx = progressWidth * percent;
            progressPaint.setColor(progressColor);
            RectF rectF = new RectF(padding + borderWidth / 2, padding + borderWidth / 2, padding + dx + borderWidth / 2, padding + progressHeight - borderWidth / 2);
            canvas.drawRoundRect(rectF, radius, radius, progressPaint);
        }
        
        //draw second progress
        if (showSecondProgress) {
            float s_percent = 0;
            if (max != 0) {
                s_percent = secondProgress * 1.0f / max;
            }
            int s_progressHeight = getHeight() - padding * 2;
            if (s_progressHeight % 2 != 0) {
                s_progressHeight = s_progressHeight - 1;
            }
            if (openSecondGradient) {
                int s_progressWidth = width - padding * 2;
                float s_mDx = s_progressWidth * s_percent;
                
                int s_colors[] = new int[2];
                float s_positions[] = new float[2];
                //from color
                s_colors[0] = secondGradientFrom;
                s_positions[0] = 0;
                //to color
                s_colors[1] = secondGradientTo;
                s_positions[1] = 1;
                LinearGradient s_shader = new LinearGradient(
                    padding + s_progressHeight / 2 + borderWidth / 2, padding + borderWidth / 2, padding + s_progressHeight / 2 + s_mDx - borderWidth / 2, padding + s_progressHeight - borderWidth / 2,
                    s_colors,
                    s_positions,
                    Shader.TileMode.MIRROR);
                //gradient
                secondGradientPaint.setShader(s_shader);
                
                //progress line
                RectF rectF = new RectF(padding + borderWidth / 2, padding + borderWidth / 2, padding + s_mDx - borderWidth / 2, padding + s_progressHeight - borderWidth / 2);
                canvas.drawRoundRect(rectF, radius, radius, secondGradientPaint);
            } else {
                //no gradient
                //line shape
                int s_progressWidth = width - padding * 2;
                float dx = s_progressWidth * s_percent;
                secondProgressPaint.setColor(secondProgressColor);
                RectF midRecf = new RectF(padding + borderWidth / 2, padding + borderWidth / 2, padding + dx - borderWidth / 2, padding + s_progressHeight - borderWidth / 2);
                canvas.drawRoundRect(midRecf, radius, radius, secondProgressPaint);
            }
        }
        
    }
    
    private void drawBackground(Canvas canvas) {
        int bgHeight = getHeight();
        if (bgHeight % 2 != 0) {
            bgHeight = bgHeight - 1;
        }
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        
        //left circle
        canvas.drawCircle(bgHeight / 2, bgHeight / 2, bgHeight / 2, bgPaint);
        //right circle
        canvas.drawCircle(width - bgHeight / 2, bgHeight / 2, bgHeight / 2, bgPaint);
        //middle line
        RectF midRecf = new RectF(bgHeight / 2, 0, width - bgHeight / 2, bgHeight);
        canvas.drawRect(midRecf, bgPaint);
    }
    
    private void drawBorder(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            if (bgHeight % 2 != 0) {
                bgHeight = bgHeight - 1;
            }
            int width = getWidth();
            if (width % 2 != 0) {
                //Fix Me
                width = width - 1;
            }
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRoundRect(rect, bgHeight / 2, bgHeight / 2, borderPaint);
        }
    }
    
    private void drawBorderRect(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            if (bgHeight % 2 != 0) {
                bgHeight = bgHeight - 1;
            }
            int width = getWidth();
            if (width % 2 != 0) {
                //Fix Me
                width = width - 1;
            }
            RectF rect = new RectF(0, 0, width, bgHeight);
            canvas.drawRect(rect, borderPaint);
        }
    }
    
    private void drawBorderRoundRect(Canvas canvas) {
        if (drawBorder) {
            int bgHeight = getHeight();
            if (bgHeight % 2 != 0) {
                bgHeight = bgHeight - 1;
            }
            int width = getWidth();
            if (width % 2 != 0) {
                //Fix Me
                width = width - 1;
            }
            RectF rect = new RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, bgHeight - borderWidth / 2);
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        }
    }
    
    private void drawBackgroundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        if (bgHeight % 2 != 0) {
            bgHeight = bgHeight - 1;
        }
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        
        RectF midRecf = new RectF(0, 0, width, bgHeight);
        canvas.drawRect(midRecf, bgPaint);
    }
    
    private void drawBackgroundRoundRectMode(Canvas canvas) {
        int bgHeight = getHeight();
        if (bgHeight % 2 != 0) {
            bgHeight = bgHeight - 1;
        }
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width - 1;
        }
        
        RectF midRecf = new RectF(borderWidth / 2, borderWidth / 2, width - borderWidth / 2, bgHeight - borderWidth / 2);
        canvas.drawRoundRect(midRecf, radius, radius, bgPaint);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    
    public int getMax() {
        return max;
    }
    
    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
    
    public int getProgress() {
        return progress;
    }
    
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
    
    public boolean isShowSecondProgress() {
        return showSecondProgress;
    }
    
    public void setShowSecondProgress(boolean showSecondProgress) {
        this.showSecondProgress = showSecondProgress;
        invalidate();
    }
    
    public int getSecondProgress() {
        return secondProgress;
    }
    
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
    
    public int getSecondProgressShape() {
        return secondProgressShape;
    }
    
    public void setSecondProgressShape(int secondProgressShape) {
        this.secondProgressShape = secondProgressShape;
        invalidate();
    }
    
    public int getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bgPaint.setColor(bgColor);
        invalidate();
    }
    
    public boolean isOpenSecondGradient() {
        return openSecondGradient;
    }
    
    public void setOpenSecondGradient(boolean openSecondGradient) {
        this.openSecondGradient = openSecondGradient;
        invalidate();
    }
    
    public int getSecondGradientFrom() {
        return secondGradientFrom;
    }
    
    public void setSecondGradientFrom(int secondGradientFrom) {
        this.secondGradientFrom = secondGradientFrom;
        invalidate();
    }
    
    public int getSecondGradientTo() {
        return secondGradientTo;
    }
    
    public void setSecondGradientTo(int secondGradientTo) {
        this.secondGradientTo = secondGradientTo;
        invalidate();
    }
    
    public int getSecondProgressColor() {
        return secondProgressColor;
    }
    
    public void setSecondProgressColor(int secondProgressColor) {
        this.secondProgressColor = secondProgressColor;
        secondProgressPaint.setColor(secondProgressColor);
        invalidate();
    }
    
    public int getProgressColor() {
        return progressColor;
    }
    
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidate();
    }
    
    public int getPadding() {
        return padding;
    }
    
    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }
    
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
     * get the percentage value of progress and max.
     *
     * @return percentage value
     */
    public int getPercentage() {
        if (max == 0) {
            return 0;
        }
        return (int) (progress * 100.0 / max);
    }
    
    public boolean isOpenGradient() {
        return openGradient;
    }
    
    public void setOpenGradient(boolean openGradient) {
        this.openGradient = openGradient;
        invalidate();
    }
    
    public int getGradientFrom() {
        return gradientFrom;
    }
    
    public void setGradientFrom(int gradientFrom) {
        this.gradientFrom = gradientFrom;
        invalidate();
    }
    
    public int getGradientTo() {
        return gradientTo;
    }
    
    public void setGradientTo(int gradientTo) {
        this.gradientTo = gradientTo;
        invalidate();
    }
    
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        this.borderPaint.setColor(this.borderColor);
        invalidate();
    }
    
    public void setGradientColor(int from, int to) {
        this.gradientFrom = from;
        this.gradientTo = to;
        invalidate();
    }
    
    public void setSecondGradientColor(int from, int to) {
        this.secondGradientFrom = from;
        this.secondGradientTo = to;
        invalidate();
    }
    
    public void setGradientColorAndBorderColor(int from, int to, int borderColor) {
        this.gradientFrom = from;
        this.gradientTo = to;
        this.borderColor = borderColor;
        this.borderPaint.setColor(this.borderColor);
        invalidate();
    }
    
    public int getBorderColor() {
        return borderColor;
    }
    
    
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }
}
