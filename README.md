# PermissionManager
![ScreenShot](https://github.com/karanchuri/PermissionManager/blob/master/demo.gif?raw=true)


This Library automatically search for permission in androidmanifests file and request for the same. Also, if request is cancelled it shows a popup window with alert to grant permission. If the app can no longer request permission due to "Don't ask again" on permission it will toast a message to grant permission from settings. All the action performed after grant or deny can be customized by overriding method shown below. Custom permission can be checked instead of all permission in androidmanifests file.

Setup:
Add it in your root build.gradle at the end of repositories:


	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.karanchuri:PermissionManager:0.1.0'
	}



//For Automatic Permission request.

//It Dynamically search for apps permission and request for the same.

//Below code in onCreate function.

First Import below code:
```
import com.karan.churi.PermissionManager.PermissionManager;
```

```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	permission=new PermissionManager() {};
	permission.checkAndRequestPermissions(this);
    }	
```
//Below code in onRequestPermissionsResult function
```
@Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
    }
```


//Customized by overriding method shown below.
```
permission=new PermissionManager() {
            @Override
            public void ifCancelledAndCanRequest(Activity activity) {
                // Do Customized operation if permission is cancelled without checking "Don't ask again"
                // Use super.ifCancelledAndCanRequest(activity); or Don't override this method if not in use
            }

            @Override
            public void ifCancelledAndCannotRequest(Activity activity) {
                // Do Customized operation if permission is cancelled with checking "Don't ask again"
                // Use super.ifCancelledAndCannotRequest(activity); or Don't override this method if not in use
            }

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
                List<String> customPermission=new ArrayList<>();               
		customPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
                return customPermission;
            }
        };
```        
        //To initiate checking permission
```
        permission.checkAndRequestPermissions(this);
```




//To get Granted Permission and Denied Permission
```
@Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
        //To get Granted Permission and Denied Permission
        ArrayList<String> granted=permission.getStatus().get(0).granted;
        ArrayList<String> denied=permission.getStatus().get(0).denied;
    }
```
