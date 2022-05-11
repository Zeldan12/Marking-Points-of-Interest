package ipl.ei.mpoi.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest implements Parcelable {

    private String name;
    private LatLng position;
    private Double altitude;
    private String category;
    private String description;
    private String classification;

    public PointOfInterest(String name, LatLng position, Double altitude, String category, String description, String classification) {
        this.name = name;
        this.position = position;
        this.altitude = altitude;
        this.category = category;
        this.description = description;
        this.classification = classification;
    }

    public PointOfInterest(Parcel in) {
        name = in.readString();
        category = in.readString();
        description = in.readString();
        classification = in.readString();
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        position = new LatLng(lat,lng);
        altitude = in.readDouble();
    }

    public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {
        public PointOfInterest createFromParcel(Parcel in) {
            return new PointOfInterest(in);
        }

        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(classification);
        dest.writeDouble(position.latitude);
        dest.writeDouble(position.longitude);
        dest.writeDouble(altitude);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }


}
