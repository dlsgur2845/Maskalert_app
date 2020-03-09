package hyuk.com.maskalert_app.connection.raa;

import hyuk.com.maskalert_app.connection.NetworkResultListener;
import hyuk.com.maskalert_app.connection.RestURL;
import hyuk.com.maskalert_app.object.Loc;

public class MaskRAA {
    public static RestApiAction Get_mask(Loc location, int range, NetworkResultListener networkResultListener){
        RestApiAction raa = new RestApiAction(RestURL.MASK_URL, networkResultListener);
        raa.parameters.put("", location.getLat()+"");
        raa.parameters.put("", location.getLon()+"");
        raa.parameters.put("", range+"");
        return raa;
    }
}
