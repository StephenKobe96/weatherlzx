package com.mh.piety.mweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.piety.mweather.Utils.PositionUtil;

import java.util.ArrayList;


public class PositionActivity extends AppCompatActivity {
    private Context context=this;
    private TextView text_title;
    private ListView list_province,list_city,list_position;
    private String province,city;
    private String Province_Id,City_Id;
    private ArrayList<String> L_province,L_city,L_position;
    private ArrayAdapter<String> change_province,change_city,change_position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position);

        try {
            ConnectivityManager conm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = conm.getActiveNetworkInfo();
            if(net==null||net.getState()!= NetworkInfo.State.CONNECTED){
                Toast.makeText(context,"当前网络连接不可用",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        text_title= (TextView) findViewById(R.id.text_title);
        list_province= (ListView) findViewById(R.id.list_province);
        list_city= (ListView) findViewById(R.id.list_city);
        list_position= (ListView) findViewById(R.id.list_position);

        ImageView image_btn = (ImageView) findViewById(R.id.image_btn);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                L_province = new ArrayList<>();
                L_province= PositionUtil.getProvince();
                change_province=new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,L_province);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list_province.setAdapter(change_province);
                    }
                });
            }
        }).start();
        list_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_position.setAdapter(null) ;
                String str_item = parent.getItemAtPosition(position).toString();
                Province_Id=str_item.split("-")[0];
                province=str_item.split("-")[1];
                text_title.setText(province);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        L_city=new ArrayList<>();
                        L_city=PositionUtil.getCity(Province_Id);
                        change_city=new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,L_city);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list_city.setAdapter(change_city);
                            }
                        });
                    }
                }).start();
            }
        });
        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_item = parent.getItemAtPosition(position).toString();
                City_Id=str_item.split("-")[0];
                city=str_item.split("-")[1];
                text_title.setText(province+"-"+city);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        L_position=new ArrayList<>();
                        L_position=PositionUtil.getLocation(Province_Id,City_Id);
                        change_position=new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,L_position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list_position.setAdapter(change_position);
                            }
                        });
                    }
                }).start();
            }
        });
        list_position.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_item = parent.getItemAtPosition(position).toString();
                MainActivity.position=province+"-"+str_item;
                finish();
            }
        });

    }
}
