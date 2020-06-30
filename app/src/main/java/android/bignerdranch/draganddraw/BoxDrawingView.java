package android.bignerdranch.draganddraw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BoxDrawingView extends View {

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
