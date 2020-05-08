package com.bawei.dianshangjin17;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

public class MainActivity extends AppCompatActivity {
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private TextView resultShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultShow = findViewById(R.id.result_show);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setApiKey("b70066b95d242e2ccbdb7ad3944e917a");
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
    }
    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    String address = amapLocation.getAddress();//地址
                    String country = amapLocation.getCountry();//国家信息
                    String province = amapLocation.getProvince();//省信息
                    String city = amapLocation.getCity();//城市信息
                    String district = amapLocation.getDistrict();//城区信息
                    //拼接
                    resultShow.setText("纬度：" + latitude + "\n经度：" + longitude + "\n" + address + "\n" + country + "  " + province + "  " +
                            city + "  " + district);
                } else {
                    resultShow.setText("定位错误，请检查！\n" + amapLocation.getErrorCode()+":"+amapLocation.getErrorInfo());
                }
            } else {
                resultShow.setText("定位失败，请检查！");
            }
            //结束定位
            mLocationClient.stopLocation();
        }
    };
    //点击事件
    public void click01(View view) {
        //权限申请
        //判断系统版本，高于API 23的需要手动获取权限
        if(Build.VERSION.SDK_INT >= 23){
            //checkSelfPermission方法就是检测是否有权限
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                //启动定位
                mLocationClient.startLocation();
                resultShow.setText("应用正在确定您的位置，请稍等...");
            }else {
                //没有权限，申请权限
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            }
        } else {
            //启动定位
            mLocationClient.startLocation();
            resultShow.setText("应用正在确定您的位置，请稍等...");
        }
    }
    //处理权限返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断
        if(requestCode == 200 && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            //启动定位
            mLocationClient.startLocation();
            resultShow.setText("应用正在确定您的位置，请稍等...");
        } else {
            Toast.makeText(MainActivity.this,"权限获取失败，应用无法定位！",Toast.LENGTH_LONG).show();
        }
    }
}
