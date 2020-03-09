package hyuk.com.maskalert_app.connection;

import com.android.volley.VolleyError;

public abstract class NetworkResultListener {
    public abstract void onResponse(String response);
    public abstract void onError(VolleyError error);

}
