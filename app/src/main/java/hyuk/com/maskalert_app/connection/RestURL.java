package hyuk.com.maskalert_app.connection;

public class RestURL {
    private RestURL() {

    }
    public static final String SERVER_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1";

    public static final String storesByGeo = SERVER_URL + "/storesByGeo/json";
    public static final String storesByAddr = SERVER_URL + "/storesByAddr/json";
}
