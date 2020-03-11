package hyuk.com.maskalert_app.adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.List;

import hyuk.com.maskalert_app.object.Store;

public class StoreAdapter {
    private List<Marker> markers;
    private Context context;

    private List<InfoWindow> infoWindows;

    public StoreAdapter(Context context) {
        this.markers = new ArrayList<Marker>();
        this.infoWindows = new ArrayList<InfoWindow>();
        this.context = context;
    }

    public void setMarkers(List<Store> stores, NaverMap map){
        if(stores != null){
            for(int i=0;i<stores.size();i++){
                Store store = stores.get(i);

                Marker marker = new Marker();
                marker.setPosition(new LatLng(store.getLat(), store.getLng()));;
                marker.setWidth(80);
                marker.setHeight(85);
                marker.setAlpha(0.8f);
                marker.setCaptionText(store.getName());
                marker.setCaptionColor(Color.BLUE);

                // 정보 창
                InfoWindow infoWindow = new InfoWindow();
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(context) {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow) {
                        return "[" + store.getName()+ "]\n" + store.getAddr();
                    }
                });
                infoWindows.add(infoWindow);

                // 마커를 클릭하면:
                Overlay.OnClickListener listener = overlay -> {
                    if (marker.getInfoWindow() == null) {
                        // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                        infoWindow.open(marker);
                    } else {
                        // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                        infoWindow.close();
                    }
                    return true;
                };

                marker.setOnClickListener(listener);

                if(store.getRemain_stat().equals("empty")){
                    marker.setIcon(MarkerIcons.BLACK);
                }
                else if(store.getRemain_stat().equals("few")){
                    marker.setIcon(MarkerIcons.RED);
                }
                else if(store.getRemain_stat().equals("some")){
                    marker.setIcon(MarkerIcons.YELLOW);
                }
                else{ // plenty

                }
                marker.setMap(map);
            }
            // 지도를 클릭하면 정보 창을 닫음
            map.setOnMapClickListener((coord, point) -> {
                for(int i=0;i<infoWindows.size();i++){
                    infoWindows.get(i).close();
                }
            });
        }
    }

    public void clearMarkers(){
        for(int i=0;i<markers.size();i++) {
            markers.get(i).setMap(null);
        }
        markers.clear();
    }
}
