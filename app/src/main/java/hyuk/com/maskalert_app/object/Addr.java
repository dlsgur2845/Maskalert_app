package hyuk.com.maskalert_app.object;

import com.naver.maps.geometry.LatLng;

import java.io.Serializable;

public class Addr implements Serializable {
    private double lat;
    private double lng;
    private String jibunAddress;

    public Addr(double lat, double lng, String jibunAddress) {
        this.lat = lat;
        this.lng = lng;
        this.jibunAddress = jibunAddress;
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
}
