package com.example.se_project;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<String>();
    Geocoder geocoder;
    List<Address> list = null;
    MapPoint[] mapPoints = new MapPoint[2];
    double[] points = new double[4];

    Button btn_quit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btn_quit = (Button)findViewById(R.id.btn_back);


        geocoder = new Geocoder(this);
        MapView mapView = new MapView(this);
        Toast toast = Toast.makeText(MapActivity.this, "지도 성공", Toast.LENGTH_SHORT);
        toast.show();
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Intent data = getIntent();
        if(data != null) {
            Bundle bundle = data.getExtras();
            items = bundle.getStringArrayList("items");

        }

        System.out.println(items);
        float a = 0, b = 0;
        float sum_x = 0, sum_y = 0;
        float avg_x = 0;
        float avg_y = 0;

        for (int i = 0; i < items.size(); i++) {

            try {
                list = geocoder.getFromLocationName(items.get(i), 1); // 읽을 개수
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (list != null) {
                if (list.size() == 0) {

                } else {
                    a = (float) list.get(0).getLatitude();        // 위도
                    b = (float) list.get(0).getLongitude();    // 경도

                    if(points[0] == 0 || points[0] < a){
                        points[0] = a;
                    }
                    if(points[1] == 0 || points[1] < b){
                        points[1] = b;
                    }
                    if(points[2] == 0 || points[2] > a){
                        points[2] = a;
                    }
                    if(points[3] == 0 || points[3] > b){
                        points[3] = b;
                    }
                }
            }

            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(i+1 +"번 사용자 위치");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(a, b));
            marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
            mapView.addPOIItem(marker);

            System.out.println(a);
            System.out.println(b);

            sum_x = sum_x + a;
            sum_y = sum_y + b;

        }


        avg_x = sum_x / items.size();
        avg_y = sum_y / items.size();

        System.out.println(avg_x);
        System.out.println(avg_y);



        MapCircle circle1 = new MapCircle(
                MapPoint.mapPointWithGeoCoord(avg_x, avg_y), // center
                500, // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(128, 255, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mapView.addCircle(circle1);

        MapPointBounds[] mapPointBoundsArray = { circle1.getBound()};
        MapPointBounds mapPointBounds = new MapPointBounds(mapPointBoundsArray);
        int padding = 50; // px

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("중간지점");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(avg_x, avg_y));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        mapView.addPOIItem(marker);
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        mapPoints[0] = MapPoint.mapPointWithGeoCoord(points[0], points[1]); // 최저점
        mapPoints[1] = MapPoint.mapPointWithGeoCoord(points[2], points[3]); // 최고점

        mapView.fitMapViewAreaToShowMapPoints(mapPoints);

        MapPOIItem subway0 = new MapPOIItem();
        subway0.setItemName("가천대역");
        subway0.setMapPoint(MapPoint.mapPointWithGeoCoord(37.448605, 127.126697));
        subway0.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        mapView.addPOIItem(subway0);

        MapPOIItem subway1 = new MapPOIItem();
        subway1.setItemName("잠실역");
        subway1.setMapPoint(MapPoint.mapPointWithGeoCoord(37.51395, 127.102234));
        subway1.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        mapView.addPOIItem(subway1); // 지하철 역 (SPOT) 마커로 지정 (예시로 2개)

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
