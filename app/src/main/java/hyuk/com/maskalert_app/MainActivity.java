package hyuk.com.maskalert_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import hyuk.com.maskalert_app.adapter.StoreAdapter;
import hyuk.com.maskalert_app.connection.APIHelper;
import hyuk.com.maskalert_app.connection.NetworkResultListener;
import hyuk.com.maskalert_app.connection.RestURL;
import hyuk.com.maskalert_app.connection.raa.GetMaskRAA;
import hyuk.com.maskalert_app.object.Loc;
import hyuk.com.maskalert_app.object.Store;

public class MainActivity extends AppCompatActivity{
    // 위치 정보
    private static final int LOCATION_PERMISSION_REQUEST = 1000;
    private FusedLocationSource locationSource;
    LatLng coord;

    // 네이버 맵
    private NaverMap map;
    private ImageView locationViewOFF;
    private ImageView locationViewON;
    private LocationOverlay locationOverlay;
    StoreAdapter storeAdapter;
    private boolean fixMap = true;

    private List<Store> stores;

    // 액티비티 객체
    EditText bornYear;
    EditText range;
    Button search;
    ImageButton notice;
    TextView MON;
    TextView TUE;
    TextView WED;
    TextView THU;
    TextView FRI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createObject();
        actionObject();
        resetWeekColor();

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(naverMap -> map = naverMap);

        // GPS
        locationViewOFF.setVisibility(View.GONE);
        locationViewON.setVisibility(View.VISIBLE);

        // 좌표 생성
        storeAdapter = new StoreAdapter(getApplicationContext());
        stores = new ArrayList<Store>();

        // 현재 위치 받아오기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST);
        locationSource.activate(new LocationSource.OnLocationChangedListener() {
            @Override
            public void onLocationChanged(@Nullable Location location) {
                LocationEvent(location);
            }
        });

        // 공지
        Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
        startActivity(intent);
    }

    void createObject() {
        bornYear = (EditText) findViewById(R.id.bornYear);
        range = (EditText) findViewById(R.id.range);
        search = (Button) findViewById(R.id.search);
        MON = (TextView) findViewById(R.id.MON);
        TUE = (TextView) findViewById(R.id.TUE);
        WED = (TextView) findViewById(R.id.WED);
        THU = (TextView) findViewById(R.id.THU);
        FRI = (TextView) findViewById(R.id.FRI);

        locationViewOFF = (ImageView)findViewById(R.id.locationOFF);
        locationViewON = (ImageView)findViewById(R.id.locationON);
        notice = (ImageButton)findViewById(R.id.notice);
    }

    void resetWeekColor() {
        int colorDeafult = Color.argb(0, 0, 0, 0);
        MON.setBackgroundColor(colorDeafult);
        TUE.setBackgroundColor(colorDeafult);
        WED.setBackgroundColor(colorDeafult);
        THU.setBackgroundColor(colorDeafult);
        FRI.setBackgroundColor(colorDeafult);
    }

    void LocationEvent(@NonNull Location location){
        // gps 이벤트
        if (map == null || location == null) {
            return;
        }

        coord = new LatLng(location);

        locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(coord);
        locationOverlay.setBearing(location.getBearing());

        if(fixMap) {
            map.moveCamera(CameraUpdate.scrollTo(coord));
            fixMap = false;
        }
        new BackgroundTask().execute();
    }

    void actionObject() {
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        bornYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String Syear = bornYear.getText().toString();

                if (Syear.length() == 4) {
                    Toast.makeText(MainActivity.this, "요일 변경", Toast.LENGTH_SHORT).show();

                    String colorActivated = "#FFFFFF";
                    int year = Integer.parseInt(Syear);
                    resetWeekColor();
                    switch (year % 10) {
                        case 1:
                        case 6:
                            MON.setBackgroundColor(Color.parseColor(colorActivated));
                            break;
                        case 2:
                        case 7:
                            TUE.setBackgroundColor(Color.parseColor(colorActivated));
                            break;
                        case 3:
                        case 8:
                            WED.setBackgroundColor(Color.parseColor(colorActivated));
                            break;
                        case 4:
                        case 9:
                            THU.setBackgroundColor(Color.parseColor(colorActivated));
                            break;
                        case 5:
                        case 0:
                            FRI.setBackgroundColor(Color.parseColor(colorActivated));
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        range.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String Srange = range.getText().toString();
                if (Srange.length() == 0) {
                    search.setEnabled(false);
                    return;
                }
                if (Integer.parseInt(Srange) < 1 || Integer.parseInt(Srange) > 10) {
                    Toast.makeText(MainActivity.this, "1 ~ 10 사이로 지정해 주세요.", Toast.LENGTH_LONG).show();
                    search.setEnabled(false);
                } else search.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        locationViewOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "현재 위치 탐색 시작", Toast.LENGTH_SHORT).show();
                if (locationSource != null) {
                    locationSource.activate(new LocationSource.OnLocationChangedListener() {
                        @Override
                        public void onLocationChanged(@Nullable Location location) {
                            fixMap = true;
                            LocationEvent(location);
                        }
                    });

                    locationViewOFF.setVisibility(View.GONE);
                    locationViewON.setVisibility(View.VISIBLE);
                }
            }
        });
        locationViewON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationSource != null){
                    locationOverlay.setVisible(false);
                    locationSource.deactivate();
                    Toast.makeText(MainActivity.this, "현재 위치 탐색 중지", Toast.LENGTH_SHORT).show();

                    locationViewON.setVisibility(View.GONE);
                    locationViewOFF.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationSource != null) {
            locationSource.deactivate();
        }
    }

    private long lastTimeBackPressed;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this, "한 번 더 눌러 종료", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = RestURL.storesByGeo +
                        "?lat=" + URLEncoder.encode(coord.latitude + "", "utf-8")
                +"&lng=" + URLEncoder.encode(coord.longitude+"", "utf-8")
                +"&m=" + URLEncoder.encode("2500", "utf-8");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String tmp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((tmp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tmp + "\n");
                }
                bufferedReader.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int result = jsonObject.getInt("count");
                if (result < 1) {
                    Toast.makeText(MainActivity.this, "주변에 판매처가 없어요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                stores.clear();
                storeAdapter.clearMarkers();

                JSONArray jsonArray = jsonObject.getJSONArray("stores");
                int count = 0;

                String code;
                String name;
                String addr;
                String type;
                double _lat;
                double _lng;
                String stock_at;
                String remain_stat;
                String created_at;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    code = object.getString("code");
                    name = object.getString("name");
                    addr = object.getString("addr");
                    type = object.getString("type");
                    _lat = object.getDouble("lat");
                    _lng = object.getDouble("lng");
                    stock_at = object.getString("stock_at");
                    remain_stat = object.getString("remain_stat");
                    created_at = object.getString("created_at");

                    stores.add(new Store(code, name, addr, type, _lat, _lng, stock_at, remain_stat, created_at));
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 마커 등록
            storeAdapter.setMarkers(stores, map);
        }
    }
}