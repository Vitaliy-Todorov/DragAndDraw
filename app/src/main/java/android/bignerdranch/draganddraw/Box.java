package android.bignerdranch.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    private float mDegrees;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public float getDegrees() {
        return mDegrees;
    }

    public void setDegrees(float degrees) {
        mDegrees = degrees;
    }

    @Override
    public int describeContents() {                                                             //describeContents() описывает различного рода специальные объекты, описывающие интерфейс. (ничего не понятно короче)
        return 0;
    }

    private Box(Parcel in) {                                                                    //Box - используется в createFromParcel
        mOrigin.readFromParcel(in);
        mCurrent.readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {                                          //writeToParcel() - упаковывает объект для передачи. (Запихиваем в Parcel то, что нам нужно передать)
        mOrigin.writeToParcel(out, flags);
        mCurrent.writeToParcel(out, flags);
    }

    public static final Parcelable.Creator<Box> CREATOR = new Parcelable.Creator<Box>() {       //Parcelable.Creator<T> CREATOR - генерирует объект класса-передатчика. (Упаковывает Parcelable (в данном случаи в коллекцию))

        @Override
        public Box createFromParcel(Parcel in) {                                                //createFromParcel - на вход нам дается Parcel, а вернуть мы должны готовый MyObject (Box)
            return new Box(in);                                                                 //Box() - метод прописанный выше
        }

        @Override
        public Box[] newArray(int size) {                                                       //newArray - создайте новый с элементами класса Parcelable. size - размер массива.
            return new Box[size];
        }
    };
}
