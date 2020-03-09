package hyuk.com.maskalert_app.connection;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import hyuk.com.maskalert_app.connection.raa.RestApiAction;

public class APIHelper {
    private APIHelper() {

    }

    public static void CallAPI(Context context, RestApiAction rapiAction){
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rapiAction);
    }
}
