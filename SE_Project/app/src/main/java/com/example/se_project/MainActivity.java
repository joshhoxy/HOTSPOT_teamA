package com.example.se_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button map_btn;
    Button addr_btn;
    Button del_btn;
    Button btn_quit;
    ListView listView;
    //TextView empty;

    String[] addr = {"경기도 성남시 수정구 복정동 582", "경기도 성남시 수정구 성남대로 지하 1332 (가천대역)", "태평역"};
    ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String address = data.getStringExtra("address");

        if (!address.equals("") || !address.equals(null)) {
            int index = address.indexOf(')');
            address = address.substring(index + 2);
            items.add(address);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, items);
            listView.setAdapter(adapter);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map_btn = (Button) findViewById(R.id.map_btn);
        addr_btn = (Button) findViewById(R.id.addr_btn);
        del_btn = (Button) findViewById(R.id.del_btn);
        btn_quit = (Button) findViewById(R.id.btn_quit);
        listView = (ListView) findViewById(R.id.listView);

        items.add("경기도 성남시 수정구 복정동 582");
        items.add("경기도 성남시 수정구 성남대로 지하 1332 (가천대역)");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, items);
        listView.setAdapter(adapter);

        map_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                Bundle mybundle = new Bundle();
                mybundle.putStringArrayList("items", items);
                intent.putExtras(mybundle);
                startActivity(intent);

            }
        });

        addr_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(intent, 1122);


            }
        });

        del_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listView.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        items.remove(checked) ;

                        // listview 선택 초기화.
                        listView.clearChoices();

                        // listview 갱신.
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }

        });

    }


}

