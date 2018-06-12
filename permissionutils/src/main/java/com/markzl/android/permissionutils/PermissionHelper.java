package com.markzl.android.permissionutils;


import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * <pre>
 * @autor markzl
 * @date 2018/6/12
 * @desc Android6.0 检测权限获取,适配Android6.0运行时权限检测
 * </pre>
 */
public class PermissionHelper {

    //1.1 Object Fragment or Activity
    //1.2 int   请求吗
    //1.3 String[] 需要请求的权限

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermissions;

    private PermissionHelper(Object object) {
        this.mObject = object;
    }

    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermission(String... requestPermissions) {
        this.mRequestPermissions = requestPermissions;
        return this;
    }

    public void request() {
        if (!PermissionUtils.isOverMarshmallow()) {
            //不是6.0及以上，直接执行方法
            PermissionUtils.excuteSuccessMethod(mObject, mRequestCode);
            return;
        }

        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject,mRequestPermissions);

        if(deniedPermissions.size()==0){
            //权限全部授予过
            PermissionUtils.excuteSuccessMethod(mObject,mRequestCode);
        }else{
            //如果没有授予，申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                    deniedPermissions.toArray(new String[deniedPermissions.size()]),
                    mRequestCode);
        }
    }

    public static void requestPermissionsResult(Object object,int requestCode,String[] permissions){

        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object,permissions);
        if(deniedPermissions.size() == 0){
            PermissionUtils.excuteSuccessMethod(object,requestCode);
        }else {
            PermissionUtils.excuteFailedMethod(object,requestCode);
        }
    }
}
