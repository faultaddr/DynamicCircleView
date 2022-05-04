package com.faultaddr.dynamiccircleview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;


/**
 * SweepGradient
 */
public class DynamicCircleView extends View {
    private static final int MSG_WHAT = 10086;
    private static final int DELAY_TIME = 20;

    private int mRadarRadius;  //雷达的半径

    private Paint mRadarPaint;//雷达画笔

    private int radarCircleCount = 4;//雷达圆圈的个数，默认4个

    private int mRadarLineColor = Color.WHITE; //雷达线条的颜色，默认为白色

    private int mRadarBgColor = Color.BLACK; //雷达圆圈背景色
    private Shader radarShader;  //paintShader
    // default background pic
    private int bgPic = R.drawable.main;
    private Bitmap mBitmap;

    private int mCount = 0;

    private Context mContext;

    //雷达扫描时候的起始和终止颜色
    private int startColor = 0x0000ff00;
    private int endColor = 0xaa00ff00;
    //旋转的角度
    private int rotateAngel = 0;
    private Matrix matrix;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            rotateAngel += 3;
            postInvalidate();

            matrix.reset();
            matrix.preRotate(rotateAngel, 0, 0);
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
        }
    };

    public DynamicCircleView(Context context, ViewConfig config) {
        super(context);
        this.mContext = context;
        this.startColor = config.startColor;
        this.endColor = config.endColor;
        this.mRadarBgColor = config.bgColor;
        this.mRadarLineColor = config.lineColor;
        this.bgPic = config.bgPic;
        this.mCount = config.count;
        boolean isConfig = true;
        setParams();
        initPaint();
    }


    public DynamicCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DynamicCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setParams();
        init(context, attrs);
        initPaint();
    }

    //初始化，拓展可设置参数供布局使用
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DynamicCircleView);
            startColor = ta.getColor(R.styleable.DynamicCircleView_startColor, startColor);
            endColor = ta.getColor(R.styleable.DynamicCircleView_endColor, endColor);
            mRadarBgColor = ta.getColor(R.styleable.DynamicCircleView_bgColor, mRadarBgColor);
            mRadarLineColor = ta.getColor(R.styleable.DynamicCircleView_lineColor, mRadarLineColor);
            radarCircleCount = ta.getInteger(R.styleable.DynamicCircleView_circleCount, radarCircleCount);
            mCount = ta.getInteger(R.styleable.DynamicCircleView_count, mCount);
            ta.recycle();
        }
    }

    public void initPaint() {
        mRadarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarPaint.setColor(mRadarLineColor);                  //画笔颜色
        mRadarPaint.setStyle(Paint.Style.STROKE);           //设置空心的画笔，只画圆边
        mRadarPaint.setStrokeWidth((float) 0.3);                      //画笔宽度


        //雷达底色画笔
        Paint mRadarBg = new Paint(Paint.ANTI_ALIAS_FLAG);     //设置抗锯齿
        mRadarBg.setColor(mRadarBgColor);                  //画笔颜色
        mRadarBg.setStyle(Paint.Style.FILL);           //设置空心的画笔，只画圆边


        radarShader = new SweepGradient(0, 0, startColor, endColor);

        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置默认宽高，雷达一般都是圆形，所以我们下面取宽高会去Math.min(宽,高)
        int DEFAULT_WIDTH = 50;
        int width = measureSize(1, DEFAULT_WIDTH, widthMeasureSpec);
        int DEFAULT_HEIGHT = 50;
        int height = measureSize(0, DEFAULT_HEIGHT, heightMeasureSpec);
        int measureSize = Math.max(width, height);   //取最大的 宽|高
        setMeasuredDimension(measureSize, measureSize);
    }

    /**
     * 测绘measure
     *
     * @param specType    1为宽， 其他为高
     * @param contentSize 默认值
     */
    private int measureSize(int specType, int contentSize, int measureSpec) {
        int result;
        //获取测量的模式和Size
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(contentSize, specSize);
        } else {
            result = contentSize;

            if (specType == 1) {
                // 根据传人方式计算宽
                result += (getPaddingLeft() + getPaddingRight());
            } else {
                // 根据传人方式计算高
                result += (getPaddingTop() + getPaddingBottom());
            }
        }

        return result;

    }

    private void setParams() {
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(bgPic);

        mBitmap = BitmapFactory.decodeStream(is).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadarRadius = Math.min(w / 2, h / 2);
        String TAG = "DynamicCircleView";
        Log.i(TAG, "onSizeChanged");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect mSrcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF mDestRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mRadarPaint);
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize((float) 80.0);
        String countText = String.valueOf(mCount);
        canvas.drawText(countText, mRadarRadius - textPaint.measureText(countText) / 2, mRadarRadius / 2, textPaint);
        textPaint.setTextSize((float) 40.0);
        String s = countText + "m|0K";
        canvas.drawText(s, mRadarRadius - textPaint.measureText(s) / 2, mRadarRadius / 2 + 80, textPaint);


        canvas.translate(mRadarRadius, mRadarRadius / 2);
        //将画板移动到屏幕的中心点

        mRadarPaint.setShader(radarShader);
        // canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);  //绘制底色(默认为黑色)，可以使雷达的线看起来更清晰
        canvas.concat(matrix);
        for (int i = 1; i <= radarCircleCount; i++) {     //根据用户设定的圆个数进行绘制
            for (int j = 0; j < 5; j++) {
                canvas.drawCircle(0, 0, (float) (mRadarRadius / 2 - 20 - j * 3), mRadarPaint);
                canvas.drawCircle(j, j * 2, (float) (mRadarRadius / 2 - 30), mRadarPaint);
            }
        }


    }

    public void startScan() {
        mHandler.removeMessages(MSG_WHAT);
        mHandler.sendEmptyMessage(MSG_WHAT);
    }

    public void stopScan() {
        mHandler.removeMessages(MSG_WHAT);
    }


    public static class ViewConfig {
        public int startColor;
        public int endColor;
        public int bgColor;
        public int circleCount;
        public int lineColor;
        public int bgPic;
        public int count;


        public ViewConfig startColor(int startColor) {
            this.startColor = startColor;
            return this;
        }


        public ViewConfig endColor(int endColor) {
            this.endColor = endColor;
            return this;
        }


        public ViewConfig bgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public ViewConfig circleCount(int circleCount) {
            this.circleCount = circleCount;
            return this;
        }


        public ViewConfig lineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }


        public ViewConfig bgPic(int bgPic) {
            this.bgPic = bgPic;
            return this;
        }

        public ViewConfig count(int count) {
            this.count = count;
            return this;
        }

        public DynamicCircleView config(Context context) {
            return new DynamicCircleView(context, this);
        }

    }

}