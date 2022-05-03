package ipl.ei.mpoi.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class PointMap implements Parcelable {
    private String name;
    private LinkedList<PointOfInterest> points;

    public PointMap(String name){
        this.name = name;
        points = new LinkedList<PointOfInterest>();
    }

    private PointMap(Parcel in){
        name = in.readString();
        points = new LinkedList<PointOfInterest>();
        in.readList(points,PointMap.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoint(PointOfInterest point){
        points.add(point);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(points);
    }

    public static final Parcelable.Creator<PointMap> CREATOR = new Parcelable.Creator<PointMap>() {
        public PointMap createFromParcel(Parcel in) {
            return new PointMap(in);
        }

        public PointMap[] newArray(int size) {
            return new PointMap[size];
        }
    };
}
