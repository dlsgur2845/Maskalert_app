package hyuk.com.maskalert_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

import hyuk.com.maskalert_app.object.Loc;
import hyuk.com.maskalert_app.object.Store;

public class MainActivity extends AppCompatActivity{
    // 위치 정보
    private static final int LOCATION_PERMISSION_REQUEST = 1000;
    private FusedLocationSource locationSource;

    // 네이버 맵
    private NaverMap map;
    private ImageView locationViewOFF;
    private ImageView locationViewON;
    private LocationOverlay locationOverlay;

    private ArrayList<Loc> locations;
    private ArrayList<Store> stores;

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

        LatLng coord = new LatLng(location);

        locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(coord);
        locationOverlay.setBearing(location.getBearing());

        map.moveCamera(CameraUpdate.scrollTo(coord));
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

        // 좌표 받아오기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST);
        locationSource.activate(new LocationSource.OnLocationChangedListener() {
            @Override
            public void onLocationChanged(@Nullable Location location) {
                LocationEvent(location);
            }
        });

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Loc loc = new Loc(locationSource.getLastLocation().getLatitude(), locationSource.getLastLocation().getLongitude());
//                        int range=0;
//
//                        // 마스크 api 호출
//                        APIHelper.CallAPI(getApplicationContext(), MaskRAA.Get_mask(loc, range, new NetworkResultListener() {
//                            @Override
//                            public void onResponse(String response) {
//                                try{
//
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(VolleyError error) {
//                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }));
//                        Thread.sleep(3000);
//                    }
//                    catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();

        Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
        startActivity(intent);
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
}