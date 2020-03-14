package hyuk.com.maskalert_app.object;

import java.io.Serializable;

public class Addr implements Serializable {
    private double lat;
    private double lng;
    private String jibunAddress;
    private String buildingName;

    public Addr(double lat, double lng, String jibunAddress, String buildingName) {
        this.lat = lat;
        this.lng = lng;
        this.jibunAddress = jibunAddress;
        this.buildingName = buildingName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
