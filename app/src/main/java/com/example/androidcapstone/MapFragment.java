package com.example.androidcapstone;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.List;


public class MapFragment extends Fragment {
    // 지도 화면 구성해줄 예정.

    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;

    double latitude;
    double longitude;

    EditText mEditTextQuery;
    Button mButtonSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        //사용자의 위치 수신을 위한 세팅
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        //사용자의 현재 위치
        Location userLocation = getMyLocation();
        if( userLocation != null ) {
            latitude = userLocation.getLatitude();
            longitude = userLocation.getLongitude();
            Log.i("map", "latitude : "+latitude);
            Log.i("map", "longtitude : "+longitude);
        }


        // 지도
        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 중심점 변경 - 예제 좌표는 서울 남산 근처 일산빌딩 -> latitude, longtitude 햔재 위치로 바꿀 것.
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(4, true);

        //마커 찍기
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        mEditTextQuery = (EditText) v.findViewById(R.id.editTextQuery); // 검색창


        mButtonSearch = (Button) v.findViewById(R.id.buttonSearch); // 검색버튼
        /*
        mButtonSearch.setOnClickListener(new View.OnClickListener() { // 검색버튼 클릭 이벤트 리스너
            @Override
            public void onClick(View v) {
                String query = mEditTextQuery.getText().toString();
                //hideSoftKeyboard(); // 키보드 숨김
                MapPoint.GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
                int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                String apikey = MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY;

                Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
                searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<ClipData.Item> itemList) {
                        mapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        showResult(itemList); // 검색 결과 보여줌
                    }

                    @Override
                    public void onFail() {
                        Log.e("map", "API_KEY의 제한 트래픽이 초과되었습니다.");
                    }
                });
            }
        });

         */


        return v;
    }

    /**
     * 사용자의 위치를 수신
     */
    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("////////////사용자에게 권한을 요청해야함");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            System.out.println("////////////권한요청 안해도됨");

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
            }
        }
        return currentLocation;
    }


}