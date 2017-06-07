package com.mh.piety.mwe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.piety.mwe.Bean.LocalInfoBean;
import com.mh.piety.mwe.Bean.WeatherBean;
import com.mh.piety.mwe.Utils.WeatherUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String position="";
    private Context context = this;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Menu menu;
    private Toolbar toolbar;
    private TextView text_position,text_temp,text_felt,text_txt,text_dirr,text_scc,text_damp,text_seeing,text_day1Cond,text_day2Cond,text_day3Cond,text_day1Tmp,text_day2Tmp,text_day3Tmp;
    private TextView text_qlty,text_aqi,text_pm25,text_dbrff,text_dtxt,text_sbrff,text_stxt,text_tbrff,text_ttxt,text_update;
    private ImageView image_delete,image_icon,image_day1Icon,image_day2Icon,image_day3Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},3);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        text_position= (TextView) findViewById(R.id.text_position);
        image_delete= (ImageView) findViewById(R.id.image_delete);

        //天气信息相关控件
        text_temp= (TextView) findViewById(R.id.text_temp);
        text_felt= (TextView) findViewById(R.id.text_felt);
        text_txt= (TextView) findViewById(R.id.text_txt);
        text_dirr= (TextView) findViewById(R.id.text_dirr);
        text_scc= (TextView) findViewById(R.id.text_scc);
        text_damp= (TextView) findViewById(R.id.text_damp);
        text_seeing= (TextView) findViewById(R.id.text_seeing);
        image_icon= (ImageView) findViewById(R.id.image_icon);
        text_day1Cond= (TextView) findViewById(R.id.text_day1Cond);
        text_day2Cond= (TextView) findViewById(R.id.text_day2Cond);
        text_day3Cond= (TextView) findViewById(R.id.text_day3Cond);
        text_day1Tmp= (TextView) findViewById(R.id.text_day1Tmp);
        text_day2Tmp= (TextView) findViewById(R.id.text_day2Tmp);
        text_day3Tmp= (TextView) findViewById(R.id.text_day3Tmp);
        image_day1Icon= (ImageView) findViewById(R.id.image_day1Icon);
        image_day2Icon= (ImageView) findViewById(R.id.image_day2Icon);
        image_day3Icon= (ImageView) findViewById(R.id.image_day3Icon);
        text_qlty= (TextView) findViewById(R.id.text_qlty);
        text_aqi= (TextView) findViewById(R.id.text_aqi);
        text_pm25= (TextView) findViewById(R.id.text_pm25);
        text_dbrff= (TextView) findViewById(R.id.text_dbrff);
        text_dtxt= (TextView) findViewById(R.id.text_dtxt);
        text_sbrff= (TextView) findViewById(R.id.text_sbrff);
        text_stxt= (TextView) findViewById(R.id.text_stxt);
        text_tbrff= (TextView) findViewById(R.id.text_tbrff);
        text_ttxt= (TextView) findViewById(R.id.text_ttxt);
        text_update= (TextView) findViewById(R.id.text_update);

        //获取用户偏好地址

        final ArrayList<LocalInfoBean> list = new WeatherUtil().getPrePosition(context);
        if(!checkNet()) {
            Toast.makeText(context, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
            if(list.size()>0){
                updateMenu();
                text_position.setText(menu.getItem(0).getTitle());
                WeatherUtil wt = new WeatherUtil();
                wt.jsondata = list.get(0).we_info;
                text_position.setText(list.get(0).position);
                updateUI(setWeatherBean(wt, 1));
            }
        } else {
            if(list.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WeatherUtil wt = new WeatherUtil(list.get(0).position.split("-")[1]);
                    final WeatherBean we = setWeatherBean(wt, 0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_position.setText(list.get(0).position);
                            updateMenu();
                            updateUI(we);
                        }
                    });
                }
            }).start();}
            else {
                Intent intent = new Intent();
                intent.setClass(context,PositionActivity.class);
                startActivity(intent);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //浮动button添加地址
        FloatingActionButton feltoat_btn = (FloatingActionButton) findViewById(R.id.feltoat_btn);
        feltoat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,PositionActivity.class);
                startActivity(intent);
            }
        });

        //侧边栏监听item点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String str_position = item.getTitle().toString();
            text_position.setText(str_position);
            update(str_position);
            drawerLayout.closeDrawer(navigationView);
            return true;
            }
        });

        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new WeatherUtil().getPrePosition(context).size()>0){
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确认不看 "+text_position.getText().toString().split("-")[1]+" 的天气信息吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new WeatherUtil().deleteInfo(context,text_position.getText().toString());
                                updateMenu();
                                if(menu.size()>0){
                                    String str_position = menu.getItem(0).getTitle().toString();
                                    text_position.setText(str_position);
                                    update(str_position);
                                }
                                else{
                                    Toast.makeText(context,"添加地址！",Toast.LENGTH_SHORT).show();
                                    text_position.setText("");
                                    text_temp.setText("...°");
                                    text_felt.setText("体感温度：...°");
                                    text_txt.setText("...");
                                    text_dirr.setText("...");
                                    text_scc.setText("...");
                                    text_damp.setText("...%");
                                    text_seeing.setText("...km");
                                    image_icon.setImageResource(R.drawable.none);
                                    image_day1Icon.setImageResource(R.drawable.none);
                                    image_day2Icon.setImageResource(R.drawable.none);
                                    image_day3Icon.setImageResource(R.drawable.none);
                                    text_day1Cond.setText("...");
                                    text_day2Cond.setText("...");
                                    text_day3Cond.setText("...");
                                    text_day1Tmp.setText("...°/...°");
                                    text_day2Tmp.setText("...°/...°");
                                    text_day3Tmp.setText("...°/...°");
                                    text_qlty.setText("...");
                                    text_aqi.setText("...");
                                    text_pm25.setText("...");
                                    text_dbrff.setText("...");
                                    text_dtxt.setText("......");
                                    text_sbrff.setText("...");
                                    text_stxt.setText("......");
                                    text_tbrff.setText("...");
                                    text_ttxt.setText("......");
                                    text_update.setText("最后更新时间：");
                                    Intent intent = new Intent();
                                    intent.setClass(context,PositionActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (position!="") {
            final boolean isexist = (new WeatherUtil().queryInfo(context,position)!=null);
            text_position.setText(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String location=position.split("-")[1];
                        WeatherUtil wt = new WeatherUtil(location);
                        if(isexist){new WeatherUtil().updateInfo(context,position,wt.jsondata);}
                        else{new WeatherUtil().storeWeatherInfo(context,position,wt.jsondata);}
                        final WeatherBean we = setWeatherBean(wt,0);
                        MainActivity.position = "";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateMenu();
                                updateUI(we);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"未找到相关天气信息!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }
    private boolean checkNet(){
        try {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = conn.getActiveNetworkInfo();
            if(net.getState()== NetworkInfo.State.CONNECTED){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private void updateMenu(){
        menu=navigationView.getMenu();
        menu.clear();
        ArrayList<LocalInfoBean> list = new WeatherUtil().getPrePosition(context);
        int i=0;
        for(LocalInfoBean l:list){
            menu.add(Menu.NONE,Menu.FIRST+i,Menu.NONE,l.position);
            i++;
        }
    }
    private WeatherBean setWeatherBean(WeatherUtil wt,int type){;
        WeatherBean we = new WeatherBean();
        we.now = wt.getNowWeathre();
        we.day1 = wt.getDailyWeather(0);
        we.day2 = wt.getDailyWeather(1);
        we.day3 = wt.getDailyWeather(2);
        if(type==0){
        we.now_icon = wt.getIcon(we.now.condition.code);
        we.day1_icon = wt.getIcon(we.day1.condition.code_di);
        we.day2_icon = wt.getIcon(we.day2.condition.code_di);
        we.day3_icon = wt.getIcon(we.day3.condition.code_di);}
        else{
            we.now_icon = wt.getIconForLoc(we.now.condition.code);
            we.day1_icon = wt.getIconForLoc(we.day1.condition.code_di);
            we.day2_icon = wt.getIconForLoc(we.day2.condition.code_di);
            we.day3_icon = wt.getIconForLoc(we.day3.condition.code_di);
        }
        we.aqi=wt.getAQI();
        we.drsg=wt.getSuggestion("drsg");
        we.sport=wt.getSuggestion("sport");
        we.trav=wt.getSuggestion("trav");
        we.time=wt.getUpdateTime();
        return we;
    }

    //选择地址后从网络获取数据更新界面
    private void updateUI(WeatherBean we){
        text_temp.setText(we.now.temp + "°");
        text_felt.setText("体感温度：" + we.now.felt + "°");
        text_txt.setText(we.now.condition.txt);
        text_dirr.setText(we.now.wind.dirr);
        text_scc.setText(we.now.wind.scc.indexOf("风") != -1 ? we.now.wind.scc : we.now.wind.scc + "级");
        text_damp.setText(we.now.damp + "%");
        text_seeing.setText(we.now.seeing + "km");
        image_icon.setImageBitmap(we.now_icon);
        image_day1Icon.setImageBitmap(we.day1_icon);
        image_day2Icon.setImageBitmap(we.day2_icon);
        image_day3Icon.setImageBitmap(we.day3_icon);
        text_day1Cond.setText(we.day1.condition.txt_di.equals(we.day1.condition.txt_n) ? we.day1.condition.txt_di : we.day1.condition.txt_di + "转" + we.day1.condition.txt_n);
        text_day2Cond.setText(we.day2.condition.txt_di.equals(we.day2.condition.txt_n) ? we.day2.condition.txt_di : we.day2.condition.txt_di + "转" + we.day2.condition.txt_n);
        text_day3Cond.setText(we.day3.condition.txt_di.equals(we.day3.condition.txt_n) ? we.day3.condition.txt_di : we.day3.condition.txt_di + "转" + we.day3.condition.txt_n);
        text_day1Tmp.setText(we.day1.temp.min + "°/" + we.day1.temp.max + "°");
        text_day2Tmp.setText(we.day2.temp.min + "°/" + we.day2.temp.max + "°");
        text_day3Tmp.setText(we.day3.temp.min + "°/" + we.day3.temp.max + "°");
        if(we.aqi==null){
            text_qlty.setText("无");
            text_aqi.setText("无");
            text_pm25.setText("无");
        }
        else{
            text_qlty.setText(we.aqi.qlty);
            text_aqi.setText(we.aqi.aqi);
            text_pm25.setText(we.aqi.pm25);
        }
        text_dbrff.setText(we.drsg.brff);
        text_dtxt.setText(we.drsg.txt);
        text_sbrff.setText(we.sport.brff);
        text_stxt.setText(we.sport.txt);
        text_tbrff.setText(we.trav.brff);
        text_ttxt.setText(we.trav.txt);
        text_update.setText("最后更新时间：" + we.time);
    }
    //偏好地址的天气信息，如果有网络 从网络获取数据更新，否则从本地获取缓存的天气数据
    public void update(String position){
        final String str_position=position;
        if(checkNet()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                    WeatherUtil wt = new WeatherUtil(str_position.split("-")[1]);
                    final WeatherBean we = setWeatherBean(wt,0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI(we);
                        }
                    });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            WeatherUtil wt = new WeatherUtil();
            wt.jsondata=wt.queryInfo(context,str_position).we_info;
            WeatherBean we = setWeatherBean(wt,1);
            updateUI(we);
        }
    }
}
