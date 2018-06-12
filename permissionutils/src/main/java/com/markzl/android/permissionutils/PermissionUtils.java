package com.markzl.android.permissionutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.markzl.android.permissionutils.annotation.PermissionFailed;
import com.markzl.android.permissionutils.annotation.PermissionSuccessed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * @autor markzl
 * @date 2018/6/12
 * @desc
 * </pre>
 */

public class PermissionUtils {

    private PermissionUtils() {
        throw new UnsupportedOperationException("不能实例化");
    }

    /**
     * 判断是否为Android6.0及以上机型
     *
     * @return boolean
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        }

        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }

        return null;
    }

    public static List<String> getDeniedPermissions(Object object, String[] reqeustPermissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission : reqeustPermissions) {
            if (ContextCompat.checkSelfPermission(getActivity(object), requestPermission) ==
                    PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }

    public static void excuteSuccessMethod(Object reflectObject, int requestCode) {

        //获取class中有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();
        //遍历标记的方法
        for (Method method : methods) {
            //获取该方法上面有没有打这个成功的标记
            PermissionSuccessed successedMethod = method.getAnnotation(PermissionSuccessed.class);
            if (successedMethod != null) {
                int methodCode = successedMethod.requestCode();
                if(methodCode == requestCode){
                    // 执行这个方法
                    executeMethod(reflectObject,method);
                }
            }
        }

    }

    public static void excuteFailedMethod(Object reflectObject,int requestCode){

        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        for (Method method:methods) {
            PermissionFailed failedMethod = method.getAnnotation(PermissionFailed.class);
            if (failedMethod!=null){
                int methodCode = failedMethod.requestCode();
                if(methodCode == requestCode){
                    executeMethod(reflectObject,method);
                }
            }
        }
    }

    private static void executeMethod(Object reflectObject, Method method) {

        try {
            method.setAccessible(true);

            method.invoke(reflectObject,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
