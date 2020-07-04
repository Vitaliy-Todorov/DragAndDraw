package android.bignerdranch.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String PARENT_STATE_KEY = "ParentStateKey";
    private static final String BOXEN_KEY = "BoxenKey";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Прямоугольники рисуются полупрозрачным красным цветом (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Фон закрашивается серовато-белым цветом
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {                                                      //onDraw - запускается когда View изначально нарисован. Всякий раз, когда invalidate () вызывается в представлении
        // Заполнение фона
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);                       //Класс Mathсодержит методы для выполнения basic числовые операции, такие как элементарная экспонента, логарифм, квадратный корень и тригонометрические функции.
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            Log.i(TAG, "degrees" + box.getDegrees());
            canvas.save();                                                                      //После вызова restore(), canvas перейдёт в сохранённое в save() состояние, с сохранением предыдущего изображения.
            canvas.rotate(box.getDegrees(), box.getCurrent().x, box.getCurrent().y);            //rotate(fi, x, y) - повернуть на fi, покруг (x, y)
            canvas.drawRect(left, top, right, bottom, mBoxPaint);                               //drawRect(…) рисует красный прямоугольник на экране
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {                                            //MotionEvent — класса, описывающего событие касания, включая его позицию и действие.
        float y1 = 0;
        float x1 = 0;
        float y2 = 0;
        float x2 = 0;
        float degrees = 0;
        String action = "";

        PointF current1 = null;
        PointF current2 = null;

        //По поводу id и индекса касания смотри https://startandroid.ru/ru/uroki/vse-uroki-spiskom/168-urok-103-multitouch-obrabotka-mnozhestvennyh-kasanij.html
        for (int i = 0; i < event.getPointerCount(); i++) {                                     //getPointerId(i) - возвращает id касание, id касания не меняется если вы не отрываите пальца. То есть к пальцу привязан свой id
            //Далее привязываем к id касания свой "попловок" PointF. Это нужно, что бы разные касания меняли свой PointF
            if (event.getPointerId(i) == 0) {
                current1 = new PointF(event.getX(i), event.getY(i));                                //PointF - содержит две координаты поплавка. getX(i) - здесь i это индекс касания, если одно убрать первое касание все остальные уменьшатся на единицу.
            } else if (event.getPointerId(i) == 1) {                                            //Обработка прикосновения второго пальца
                current2 = new PointF(event.getX(i), event.getY(i));
            }
        }

        /*При каждом получении события ACTION_DOWN в поле mCurrentBox сохраняется новый объект Box с
        базовой точкой, соответствующей позиции события. Этот объект Box добавляется в массив
        прямоугольников (в следующем разделе, когда мы займемся прорисовкой, BoxDrawingView будет
        выводить каждый объект Box из массива.)

        В процессе перемещения пальца по экрану приложение обновляет mCurrentBox.mCurrent. Затем,
        когда касание отменяется или палец не касается экрана, поле mCurrentBox обнуляется для
        завершения операции. Объект Box завершен; он сохранен в массиве и уже не будет обновляться
        событиями перемещения.*/
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:                                                       //ACTION_DOWN - первое касание
                action = "ACTION_DOWN";
                // Сброс текущего состояния
                mCurrentBox = new Box(current1);
                mBoxen.add(mCurrentBox);
                break;
            /*case MotionEvent.ACTION_BUTTON_PRESS:                                               //последующие касания
                Log.i(TAG, "event.getPointerCount()" + event.getPointerCount());
                break;*/
            case MotionEvent.ACTION_MOVE:                                                       //ACTION_MOVE - Пользователь перемещает палец по экрану
                action = "ACTION_MOVE";

                if (mCurrentBox != null) {
                    if (current1 != null) {
                        mCurrentBox.setCurrent(current1);
                    }
                    if (current2 != null) {
                        y1 = mCurrentBox.getOrigin().y;                                         //Получаем координаты откуда начали рисовать прямоугольник
                        x1 = mCurrentBox.getOrigin().x;
                        y2 = current2.y;                                                        //Получаем координаты вторго пальца
                        x2 = current2.x;
                        degrees = (float) Math.atan2(y2-y1, x2-x1) ;                            //Находим arctg dy/dx
                        mCurrentBox.setDegrees(degrees*360/(float) Math.PI);                    //Сохраняем угол на который произошёл поворот
                    }
                    invalidate();                                                               //invalidate() - запускает onDraw(). в случае ACTION_MOVE, он заставляет BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник в процессе перетаскивания. Это заставляет его перерисовать себя и приводит к повторному вызову onDraw(Canvas).
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

        //Log.i(TAG, action + " at x=" + current1.x + ", y=" + current1.y);

        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {                                                //onSaveInstanceState - Вызывается для извлечения состояния каждого экземпляра из действия перед его уничтожением так что состояние может быть восстановлено в onCreate(Bundle)или onRestoreInstanceState(Bundle). У используемого view обязательно должно быть id (в XML файле0
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATE_KEY, super.onSaveInstanceState());
        bundle.putParcelableArray(BOXEN_KEY, mBoxen.toArray(new Box[mBoxen.size()]));           //toArray - переганяет из Array в массив (коллекцию)

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {                                   //onRestoreInstanceState() - ловит то, что выслали через onSaveInstanceState()

        if (state instanceof Bundle) {                                                          //instanceof - Проверка принадлежности к классу
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(PARENT_STATE_KEY));
            Box[] boxes = (Box[]) bundle.getParcelableArray(BOXEN_KEY);
            mBoxen = new ArrayList<>(Arrays.asList(boxes));                                     //asList - перевращает коллекцию boxes в массив. Array - Этот класс содержит различные методы для манипулирования массивами.
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
