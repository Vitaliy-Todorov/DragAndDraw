package android.bignerdranch.draganddraw;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();

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

        /*При каждом получении события ACTION_DOWN в поле mCurrentBox сохраняется новый объект Box с
        базовой точкой, соответствующей позиции события. Этот объект Box добавляется в массив
        прямоугольников (в следующем разделе, когда мы займемся прорисовкой, BoxDrawingView будет
        выводить каждый объект Box из массива.)

        В процессе перемещения пальца по экрану приложение обновляет mCurrentBox.mCurrent. Затем,
        когда касание отменяется или палец не касается экрана, поле mCurrentBox обнуляется для
        завершения операции. Объект Box завершен; он сохранен в массиве и уже не будет обновляться
        событиями перемещения.*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:                                                       //ACTION_DOWN - Пользователь прикоснулся к экрану
                action = "ACTION_DOWN";
                // Сброс текущего состояния
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:                                                       //ACTION_MOVE - Пользователь перемещает палец по экрану
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();                                                               //invalidate() в случае ACTION_MOVE, он заставляет BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник в процессе перетаскивания.
                }
                break;
            case MotionEvent.ACTION_UP:                                                         //ACTION_UP - Пользователь отводит палец от экрана
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:                                                     //ACTION_CANCEL - Родительское представление перехватило событие касания
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);

        return true;
    }
}
