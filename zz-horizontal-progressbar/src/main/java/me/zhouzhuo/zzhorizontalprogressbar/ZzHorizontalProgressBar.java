package me.zhouzhuo.zzhorizontalprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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

    private Paint progressPaint;
    private Paint bgPaint;

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
        padding = a.getDimensionPixelSize(R.styleable.ZzHorizontalProgressBar_zpb_padding, 2);
        a.recycle();
    }

    private void initPaths() {
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);

        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width-1;
        }
        float percent = 0;
        if (max != 0) {
            percent = progress * 1.0f / max;
        }
        int progressHeight = getHeight() - padding * 2;
        if (progressHeight % 2 != 0) {
            progressHeight = progressHeight -1;
        }
        int progressWidth = width - padding * 2 - progressHeight;
        float dx =  progressWidth * percent;
        //left circle
        canvas.drawCircle(padding+progressHeight/2, padding+progressHeight/2, progressHeight/2, progressPaint);
        //right circle
        canvas.drawCircle(padding+progressHeight/2+dx, padding+progressHeight/2, progressHeight/2, progressPaint);
        //middle line
        RectF midRecf = new RectF(padding+progressHeight/2, padding,padding + progressHeight/2 + dx, padding+ progressHeight);
        canvas.drawRect(midRecf, progressPaint);
    }

    private void drawBackground(Canvas canvas) {
        int bgHeight = getHeight();
        if (bgHeight % 2 != 0) {
            bgHeight = bgHeight-1;
        }
        int width = getWidth();
        if (width % 2 != 0) {
            //Fix Me
            width = width-1;
        }

        //left circle
        canvas.drawCircle(bgHeight/2, bgHeight/2, bgHeight/2, bgPaint);
        //right circle
        canvas.drawCircle(width-bgHeight/2, bgHeight/2, bgHeight/2, bgPaint);
        //middle line
        RectF midRecf = new RectF(bgHeight/2, 0, width-bgHeight/2, bgHeight);
        canvas.drawRect(midRecf, bgPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        this.progress = progress;
        invalidate();
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    /**
     * get the percentage value of progress and max.
     * @return percentage value
     */
    public int getPercentage() {
        if (max == 0) {
            return 0;
        }
        return (int) (progress*100.0/max);
    }

}
