package hyuk.com.maskalert_app.connection.raa;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import hyuk.com.maskalert_app.connection.NetworkResultListener;

public class RestApiAction extends StringRequest {
    protected String URL;
    protected Map<String, String> parameters;

    public RestApiAction(String url, final NetworkResultListener networkResultListener) {
        super(Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                networkResultListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkResultListener.onError(error);
            }
        });
        parameters = new HashMap<>();
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
