package com.karan.churi.PermissionManager;

/**
 * Created by Karan Churi on 19-06-2017.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PermissionManager {
    private Activity activity;
    public  boolean checkAndRequestPermissions(Activity activity) {
        this.activity=activity;
        if(Build.VERSION.SDK_INT>=23) {
            List<String> listPermissionsNeeded = setPermission();
            List<String> listPermissionsAssign=new ArrayList<>();
            for(String per:listPermissionsNeeded){
                if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),per)!=PackageManager.PERMISSION_GRANTED){
                    listPermissionsAssign.add(per);
                }
            }

            if (!listPermissionsAssign.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsAssign.toArray(new String[listPermissionsAssign.size()]),1212);
                return false;
            }
        }
        return true;
    }

    public ArrayList<statusArray> getStatus(){
        ArrayList<statusArray> statusPermission=new ArrayList<>();
        ArrayList<String> grant=new ArrayList<>();
        ArrayList<String> deny=new ArrayList<>();
        List<String> listPermissionsNeeded = setPermission();
        for(String per:listPermissionsNeeded){
            if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),per)==PackageManager.PERMISSION_GRANTED){
                grant.add(per);
            }else{
                deny.add(per);
            }
        }
        statusArray stat =new statusArray(grant,deny);
        statusPermission.add(stat);
        return statusPermission;
    }

    public List<String>  setPermission() {
        List<String> per=new ArrayList<>();
        try {
            PackageManager pm = activity.getApplicationContext().getPackageManager();
            PackageInfo pi=pm.getPackageInfo(activity.getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            String permissionInfo[]=pi.requestedPermissions;
            for (String p : permissionInfo) {
                per.add(p);
            }
        } catch (Exception e) {

        }
        return per;
    }

    public void checkResult(int requestCode,String permissions[], int[] grantResults){
        switch (requestCode) {
            case 1212: {
                List<String> listPermissionsNeeded = setPermission();
                Map<String, Integer> perms = new HashMap<>();
                for(String permission:listPermissionsNeeded){
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    boolean isAllGranted=true;
                    for(String permission:listPermissionsNeeded){
                        if(perms.get(permission) == PackageManager.PERMISSION_DENIED){
                            isAllGranted=false;
                            break;
                        }
                    }
                    if(isAllGranted){

                    }else{
                        boolean shouldRequest=false;
                        for(String permission:listPermissionsNeeded){
                            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                                shouldRequest=true;
                                break;
                            }
                        }
                        if(shouldRequest){
                            ifCancelledAndCanRequest(activity);
                        }else{
                            ifCancelledAndCannotRequest(activity);
                        }
                    }
                }
            }
        }
    }

    public void ifCancelledAndCanRequest(final Activity activity) {

        showDialogOK(activity,"Some Permission required for this app, please grant permission for the same",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                checkAndRequestPermissions(activity);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // proceed with logic by disabling the related features or quit the app.
                                break;
                        }
                    }
                });
    }
    public void ifCancelledAndCannotRequest(Activity activity){
        Toast.makeText(activity.getApplicationContext(), "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
    }
    private void showDialogOK(Activity activity,String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    public class statusArray{
        statusArray(ArrayList<String> granted,ArrayList<String> denied){
            this.denied=denied;
            this.granted=granted;
        }
        public ArrayList<String> granted;
        public ArrayList<String> denied;
    }
}

