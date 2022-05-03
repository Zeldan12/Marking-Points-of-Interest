package ipl.ei.mpoi.objects;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest {

    private String name;
    private LatLng position;
    private String category;
    private String description;
    private String classification;

    public PointOfInterest(String name, LatLng position, String category, String description, String classification) {
        this.name = name;
        this.position = position;
        this.category = category;
        this.description = description;
        this.classification = classification;
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
}
