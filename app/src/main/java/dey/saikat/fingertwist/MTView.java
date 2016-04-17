package dey.saikat.fingertwist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MTView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int MAX_TOUCHPOINTS = 10;
    private static int MAX_TOUCH_SUPPORTED=0;
    private static final String START_TEXT = "Touch the screen to test this device's maximum touch support";

    private Paint textPaint = new Paint();
    private Paint touchPaints[] = new Paint[MAX_TOUCHPOINTS];
    private int colors[] = new int[MAX_TOUCHPOINTS];

    private int width, height;
    private float scale = 1.0f;

    private SharedPreferences.Editor editor;

    public MTView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // make sure we get key events
        setFocusableInTouchMode(true); // make sure we get touch events
        init();
    }

    public MTView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // make sure we get key events
        setFocusableInTouchMode(true); // make sure we get touch events
        init();
    }

    public MTView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context,attributeSet,defStyle);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // make sure we get key events
        setFocusableInTouchMode(true); // make sure we get touch events
        init();
    }

    private void init() {
        textPaint.setColor(Color.WHITE);
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;
        colors[2] = Color.GREEN;
        colors[3] = Color.YELLOW;
        colors[4] = Color.CYAN;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.DKGRAY;
        colors[7] = Color.WHITE;
        colors[8] = Color.LTGRAY;
        colors[9] = Color.GRAY;
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            touchPaints[i] = new Paint();
            touchPaints[i].setColor(colors[i]);
        }
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        if (pointerCount > MAX_TOUCHPOINTS) {
            pointerCount = MAX_TOUCHPOINTS;
        }
        if(pointerCount>MAX_TOUCH_SUPPORTED){
            MAX_TOUCH_SUPPORTED=pointerCount;
            Log.i("MyApp",String.valueOf(MAX_TOUCH_SUPPORTED));
            editor.putString("MaxTouch",Integer.toString(MAX_TOUCH_SUPPORTED));
            editor.commit();
        }
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);
            if (event.getAction() == MotionEvent.ACTION_UP) {

            } else {
                for (int i = 0; i < pointerCount; i++) {
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCrosshairsAndText(x, y, touchPaints[id], i, id, c);
                }
                for (int i = 0; i < pointerCount; i++) {
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCircle(x, y, touchPaints[id], c);
                }
            }
            getHolder().unlockCanvasAndPost(c);
        }
        return true;
    }

    private void drawCrosshairsAndText(int x, int y, Paint paint, int ptr, int id, Canvas c) {
        c.drawLine(0, y, width, y, paint);
        c.drawLine(x, 0, x, height, paint);
        int textY = (int)((15 + 20 * ptr) * scale);
        c.drawText("Maximum Touch Supported = "+MAX_TOUCH_SUPPORTED, 10 * scale, textY, textPaint);
        //c.drawText("y" + ptr + "=" + y, 70 * scale, textY, textPaint);
        c.drawText("id" + ptr + "=" + id, width - 55 * scale, textY, textPaint);
    }

    private void drawCircle(int x, int y, Paint paint, Canvas c) {
        c.drawCircle(x, y, 40 * scale, paint);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        if (width > height) {
            this.scale = width / 480f;
        } else {
            this.scale = height / 480f;
        }
        textPaint.setTextSize(14 * scale);
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
// clear screen
            c.drawColor(Color.BLACK);
            float tWidth = textPaint.measureText(START_TEXT);
            c.drawText(START_TEXT, width / 2 - tWidth / 2, height / 2, textPaint);
            getHolder().unlockCanvasAndPost(c);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}