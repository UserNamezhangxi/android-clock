package mw.huawei.com.clockview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/5/21.
 */

@SuppressLint("AppCompatCustomView")
public class ClockView extends ImageView {

    private int mWidth,mHeight;
    private Paint outCirclePaint,degreePaint,APMPaint;
    private Paint hourPaint,minPaint,sPaint;
    private int mcount,hcount,scount;

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData(){
        outCirclePaint = new Paint();
        outCirclePaint.setStrokeWidth(2);
        outCirclePaint.setAntiAlias(true);
        outCirclePaint.setStyle(Paint.Style.STROKE);

        degreePaint = new Paint();
        degreePaint.setStrokeWidth(2);

        hourPaint = new Paint();
        hourPaint.setStrokeWidth(5);

        minPaint = new Paint();
        minPaint.setStrokeWidth(3);

        sPaint = new Paint();
        sPaint.setStrokeWidth(2);

        APMPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        /*第一步，画一个外面的表盘（圆）*/
        canvas.drawCircle(mWidth/2,mHeight/2,mWidth/2,outCirclePaint);

        /*画刻度,通过旋转画布的方法*/

        for (int i = 0; i <= 12;i++){
            if (i==3||i==6||i==9 || i==12){
                degreePaint.setStrokeWidth(3);
                degreePaint.setTextSize(30);
                canvas.drawLine(mWidth/2,mHeight/2-mWidth/2,mWidth/2,mHeight/2-mWidth/2+30,degreePaint);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        mWidth/2-degreePaint.measureText(degree)/2,
                        mHeight/2-mWidth/2 + 60,
                        degreePaint);
            }else{
                if (i!=0){
                    degreePaint.setStrokeWidth(2);
                    degreePaint.setTextSize(20);
                    canvas.drawLine(mWidth/2,mHeight/2-mWidth/2,mWidth/2,mHeight/2-mWidth/2+15,degreePaint);
                    String degree = String.valueOf(i);
                    canvas.drawText(degree,
                        mWidth/2-degreePaint.measureText(degree)/2,
                        mHeight/2-mWidth/2 + 40,
                        degreePaint);
                }
            }
            canvas.rotate(30,mWidth/2,mHeight/2);
        }

        /*画指针*/
        canvas.save();

        Calendar calendar = Calendar.getInstance();
        hcount = calendar.get(Calendar.HOUR_OF_DAY);
        mcount = calendar.get(Calendar.MINUTE);
        scount = calendar.get(Calendar.SECOND);

        //绘制 上午下午
        APMPaint.setTextSize(20);
        APMPaint.setStrokeWidth(2);
        canvas.rotate(-30,mWidth/2,mHeight/2);
        String apm ;
        if (hcount < 12){
            apm = "AM";
        }else{
            apm = "PM";
        }

        canvas.drawText(apm,
                mWidth/2-degreePaint.measureText(apm)/2,
                mHeight/2+100,
                APMPaint);

        int hx = (int) (70*Math.cos(Math.PI*(hcount%12-15) / 6));
        int hy = (int) (70*Math.sin(Math.PI*(hcount%12-15) / 6));
        int mx = (int) (90*Math.cos(Math.PI*(mcount-15) / 30));
        int my = (int) (90*Math.sin(Math.PI*(mcount-15) / 30));
        int sx = (int) (110*Math.cos(Math.PI*(scount-15) / 30));  // -15 是为了调整时差(角度差)
        int sy = (int) (110*Math.sin(Math.PI*(scount-15) / 30));

        canvas.translate(mWidth/2,mHeight/2);
        canvas.drawLine(0,0,hx,hy,hourPaint);
        canvas.drawLine(0,0,mx,my,minPaint);
        canvas.drawLine(0,0,sx,sy,sPaint);


        Toast.makeText(getContext(),hcount+":"+mcount+":"+scount,Toast.LENGTH_SHORT).show();
        postInvalidateDelayed(1000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }
}
