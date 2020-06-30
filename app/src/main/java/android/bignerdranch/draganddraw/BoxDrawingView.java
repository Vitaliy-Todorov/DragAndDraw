package android.bignerdranch.draganddraw;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {                                            //MotionEvent — класса, описывающего событие касания, включая его позицию и действие.
        PointF current = new PointF(event.getX(), event.getY());                                //PointF - содержит две координаты поплавка
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:                                                       //ACTION_DOWN - Пользователь прикоснулся к экрану
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:                                                       //ACTION_MOVE - Пользователь перемещает палец по экрану
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:                                                         //ACTION_UP - Пользователь отводит палец от экрана
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:                                                     //ACTION_CANCEL - Родительское представление перехватило событие касания
                action = "ACTION_CANCEL";
                break;
        }

        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);

        return true;
    }
}
