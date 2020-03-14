package hyuk.com.maskalert_app;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import hyuk.com.maskalert_app.adapter.StoreAdapter;
import hyuk.com.maskalert_app.connection.RestURL;
import hyuk.com.maskalert_app.object.Addr;
import hyuk.com.maskalert_app.object.Store;

public class MainActivity extends AppCompatActivity {
    // 인텐트
    private int REQUEST_CODE = 1;

    // 위치 정보
    private static final int LOCATION_PERMISSION_REQUEST = 1000;
    private FusedLocationSource locationSource;
    LatLng coord;
    GetByGeo getByGeo;
    Geocoding geocoding;

    // 네이버 맵
    private NaverMap map;
    private ImageView locationViewOFF;
    private ImageView locationViewON;
    private LocationOverlay locationOverlay;
    StoreAdapter storeAdapter;
    private boolean fixMap = true;

    private boolean soldOutVisible = true;
    private List<Store> stores;

    // 액티비티 객체
    EditText bornYear;
    EditText address;
    Button search;
    Button soldOut;
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
        locationViewOFF.setEnabled(false);
        locationViewOFF.setVisibility(View.GONE);
        locationViewON.setEnabled(true);
        locationViewON.setVisibility(View.VISIBLE);

        // 좌표 생성
        storeAdapter = new StoreAdapter(getApplicationContext());
        stores = new ArrayList<Store>();

        // 현재 위치 받아오기
        Toast.makeText(MainActivity.this, "위치 수신중..\n GPS 활성 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
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
        address = (EditText) findViewById(R.id.address);
        search = (Button) findViewById(R.id.search);
        soldOut = (Button) findViewById(R.id.soldOut);
        MON = (TextView) findViewById(R.id.MON);
        TUE = (TextView) findViewById(R.id.TUE);
        WED = (TextView) findViewById(R.id.WED);
        THU = (TextView) findViewById(R.id.THU);
        FRI = (TextView) findViewById(R.id.FRI);

        locationViewOFF = (ImageView) findViewById(R.id.locationOFF);
        locationViewON = (ImageView) findViewById(R.id.locationON);
        notice = (ImageButton) findViewById(R.id.notice);

        coord = new LatLng(37.5535582,126.9669724);
    }

    void resetWeekColor() {
        int colorDeafult = Color.argb(0, 0, 0, 0);
        MON.setBackgroundColor(colorDeafult);
        TUE.setBackgroundColor(colorDeafult);
        WED.setBackgroundColor(colorDeafult);
        THU.setBackgroundColor(colorDeafult);
        FRI.setBackgroundColor(colorDeafult);
    }

    void LocationEvent(@NonNull Location location) {
        // gps 이벤트
        if (map == null || location == null) {
            return;
        }

        coord = new LatLng(location);

        locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(coord);
        locationOverlay.setBearing(location.getBearing());

        if (fixMap) {
            map.moveCamera(CameraUpdate.scrollTo(coord));
            fixMap = false;
        }
        if (getByGeo != null) {
            try {
                if (getByGeo.getStatus() == AsyncTask.Status.RUNNING) {
                    getByGeo.cancel(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getByGeo = new GetByGeo();
        getByGeo.execute();
    }

    void LocationEvent(LatLng coord) {
        // gps 이벤트

        locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(coord);

        map.moveCamera(CameraUpdate.scrollTo(coord));

        if (getByGeo != null) {
            try {
                if (getByGeo.getStatus() == AsyncTask.Status.RUNNING) {
                    getByGeo.cancel(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getByGeo = new GetByGeo();
        getByGeo.execute();
    }

    void actionObject() {
        address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    search.callOnClick();
                    return true;
                }
                return false;
            }
        });
        soldOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soldOutVisible) {
                    soldOutVisible = false;
                    soldOut.setTextColor(Color.rgb(0x50, 0x50, 0x50));
                } else {
                    soldOutVisible = true;
                    soldOut.setTextColor(Color.rgb(0xFF, 0xFF, 0xFF));
                }
                storeAdapter.hideSoldOut(stores, map, !soldOutVisible);
            }
        });

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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationViewOFF.isEnabled())
                    locationViewON.callOnClick();

                try {
                    if (geocoding != null) {
                        if (geocoding.getStatus() == AsyncTask.Status.RUNNING) {
                            geocoding.cancel(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                geocoding = new Geocoding();
                geocoding.execute();
            }
        });

        locationViewOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "정보 업데이트 시작", Toast.LENGTH_SHORT).show();
                fixMap = true;
                if (locationSource != null) {
                    locationSource.activate(new LocationSource.OnLocationChangedListener() {
                        @Override
                        public void onLocationChanged(@Nullable Location location) {
                            LocationEvent(location);
                        }
                    });

                    locationViewOFF.setEnabled(false);
                    locationViewOFF.setVisibility(View.GONE);
                    locationViewON.setEnabled(true);
                    locationViewON.setVisibility(View.VISIBLE);
                }
            }
        });
        locationViewON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationOverlay == null){
                    Toast.makeText(MainActivity.this, "위치 수신중..\n GPS 활성 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (locationSource != null && locationOverlay != null) {
                    locationOverlay.setVisible(false);
                    locationSource.deactivate();
                    Toast.makeText(MainActivity.this, "정보 업데이트 중지", Toast.LENGTH_SHORT).show();

                    locationViewON.setEnabled(false);
                    locationViewON.setVisibility(View.GONE);
                    locationViewOFF.setEnabled(true);
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
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "한 번 더 눌러 종료", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        try {
            if (getByGeo != null) {
                if (getByGeo.getStatus() == AsyncTask.Status.RUNNING) {
                    getByGeo.cancel(true);
                }
            }
            if (geocoding != null) {
                if (geocoding.getStatus() == AsyncTask.Status.RUNNING) {
                    geocoding.cancel(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    class GetByGeo extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = RestURL.storesByGeo +
                        "?lat=" + URLEncoder.encode(coord.latitude + "", "utf-8")
                        + "&lng=" + URLEncoder.encode(coord.longitude + "", "utf-8")
                        + "&m=" + URLEncoder.encode("2500", "utf-8");
            } catch (Exception e) {
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
                    stringBuilder.append(tmp).append("\n");
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
            // thread err handling
            if(geocoding != null){
                if(geocoding.getStatus() == Status.RUNNING){
                    geocoding.cancel(true);
                }
            }

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

    class Geocoding extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            String query = address.getText().toString();
            try {
                target = RestURL.SearchPlace +
                        "?query=" + URLEncoder.encode(query, "utf-8") +
                "&x=" + URLEncoder.encode(coord.longitude+"", "utf-8") +
                "&y=" + URLEncoder.encode(coord.latitude+"", "utf-8"); // coordinate
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //String clientId = getText(R.string.NAVER_API_ID).toString();//애플리케이션 클라이언트 아이디값";
            String clientSecret = getText(R.string.Kakao_API_KEY).toString();//애플리케이션 클라이언트 시크릿값";
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
//                httpURLConnection.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                httpURLConnection.setRequestProperty("Authorization", clientSecret);
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
//                String status = jsonObject.getString("status");
//                if (!status.equals("OK")) return;

                JSONArray jsonArray = jsonObject.getJSONArray("documents");
                int count = 0;

                double _lat;
                double _lng;
                String jibunAddress;
                String buildingName;

                List<Addr> addrList = new ArrayList<Addr>();

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);

                    _lat = object.getDouble("y");
                    _lng = object.getDouble("x");
                    jibunAddress = object.getString("road_address_name");
                    buildingName = object.getString("place_name");;

                    addrList.add(new Addr(_lat, _lng, jibunAddress, buildingName));
                    count++;
                }
                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                intent.putExtra("addrList", (Serializable) addrList);
                startActivityForResult(intent, REQUEST_CODE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            // 마커 등록
            storeAdapter.setMarkers(stores, map);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                double lat = data.getDoubleExtra("lat", 0);
                double lng = data.getDoubleExtra("lng", 0);

                coord = new LatLng(lat, lng);

                locationOverlay = map.getLocationOverlay();
                locationOverlay.setVisible(true);
                locationOverlay.setPosition(coord);

                LocationEvent(coord);

                try {
                    if (getByGeo != null) {
                        if (getByGeo.getStatus() == AsyncTask.Status.RUNNING) {
                            getByGeo.cancel(true);
                        }
                        getByGeo = new GetByGeo();
                        getByGeo.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                locationViewOFF.callOnClick();
            }
        }
        onResume();
    }
}