package hyuk.com.maskalert_app.connection.raa;

import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hyuk.com.maskalert_app.connection.NetworkResultListener;
import hyuk.com.maskalert_app.connection.RestURL;
import hyuk.com.maskalert_app.object.Loc;

public class GetMaskRAA {
    public static RestApiAction Get_mask_geo(Loc location, int range, NetworkResultListener networkResultListener){
        RestApiAction raa = new RestApiAction(RestURL.storesByGeo, networkResultListener);
        try {
            raa.parameters.put("lat", URLEncoder.encode(Double.toString(location.getLat()), "utf-8"));
            raa.parameters.put("lng", URLEncoder.encode(Double.toString(location.getLng()), "utf-8"));
            raa.parameters.put("m", URLEncoder.encode(Integer.toString(1000), "utf-8"));
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return raa;
    }

    public static RestApiAction Get_mask_addr(String addr, NetworkResultListener networkResultListener){
        RestApiAction raa = new RestApiAction(RestURL.storesByAddr, networkResultListener);
        raa.parameters.put("address", addr);
        return raa;
    }
}
