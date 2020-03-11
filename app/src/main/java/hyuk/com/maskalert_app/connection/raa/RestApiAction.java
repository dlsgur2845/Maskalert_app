package hyuk.com.maskalert_app.connection.raa;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import hyuk.com.maskalert_app.connection.NetworkResultListener;

public class RestApiAction extends StringRequest {
    protected String URL;
    protected Map<String, String> parameters;

    public RestApiAction(String url, final NetworkResultListener networkResultListener) {
        super(Method.GET, url, response -> networkResultListener.onResponse(response), error -> networkResultListener.onError(error));
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
