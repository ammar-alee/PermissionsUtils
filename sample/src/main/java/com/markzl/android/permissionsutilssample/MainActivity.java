package com.markzl.android.permissionsutilssample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.markzl.android.permissionutils.PermissionHelper;
import com.markzl.android.permissionutils.annotation.PermissionFailed;
import com.markzl.android.permissionutils.annotation.PermissionSuccessed;

public class MainActivity extends AppCompatActivity {

    private static final int CALL_PHONE_REQUEST_CODE = 0x0011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void phoneClick(View view) {
        PermissionHelper.with(this)
                .requestCode(CALL_PHONE_REQUEST_CODE)
                .requestPermission(Manifest.permission.CALL_PHONE)
                .request();
    }

    @PermissionSuccessed(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:15655657972");
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @PermissionFailed(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhoneFail(){
        Toast.makeText(this,"您拒绝了拨打电话", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions);
    }
}
