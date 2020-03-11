package hyuk.com.maskalert_app.connection.raa;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hyuk.com.maskalert_app.connection.NetworkResultListener;
import hyuk.com.maskalert_app.connection.RestURL;
import hyuk.com.maskalert_app.object.Loc;

public class GetMaskRAA {
    public static RestApiAction Get_mask_geo(Loc location, int range, NetworkResultListener networkResultListener){
        try {
            RestApiAction raa = new RestApiAction(RestURL.storesByGeo, networkResultListener);
            raa.parameters.put("lat", URLEncoder.encode(Double.toString(location.getLat()), "utf-8"));
            raa.parameters.put("lng", URLEncoder.encode(Double.toString(location.getLng()), "utf-8"));
            raa.parameters.put("m", URLEncoder.encode(Integer.toString(1000), "utf-8"));
            return raa;
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }

    public static RestApiAction Geocoding(String addr, NetworkResultListener networkResultListener){
        try {
            RestApiAction raa = new RestApiAction(RestURL.Geocoding, networkResultListener);
            raa.parameters.put("userquery", URLEncoder.encode(addr, "utf-8"));
            return raa;
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }
}
