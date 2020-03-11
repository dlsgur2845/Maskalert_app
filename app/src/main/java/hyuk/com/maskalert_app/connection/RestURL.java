package hyuk.com.maskalert_app.connection;

public class RestURL {
    private RestURL() {

    }
    // 공공데이터
    public static final String SERVER_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1";

    public static final String storesByGeo = SERVER_URL + "/storesByGeo/json";

    // 네이버
    public static final String Geocoding = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
}
