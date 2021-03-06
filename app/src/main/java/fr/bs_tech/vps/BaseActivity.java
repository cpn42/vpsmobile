package fr.bs_tech.vps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fr.bs_tech.vps.bindings.CurrentMission;
import fr.bs_tech.vps.utils.Pair;
import fr.bs_tech.vps.utils.SendHTTPPost;

/**
 * Created by cpellerin on 01/12/2017.
 */

public class BaseActivity extends AppCompatActivity
{
    protected static final int STDRIVING = 1;
    protected static final int STWORKING = 2;
    protected static final int STONBOARD = 3;
    protected static final int STRESTING = 4;
    protected static final int PHOTOTAKEN = 10;
    protected static final int MISSIONSTARTED = 20;
    protected static final int MISSIONSWITCHED = 21;
    protected static final int MISSIONFINISHED = 22;

    private final int PERMALL = 1;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

    protected static final String _picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MissionPhotos/";

    // Needed to change menu items
    protected Menu _statusMenu;
    // Mission current _driverStatus
    protected static int _driverStatus;
    // App already _launched once
    protected static int _launched = 0;
    // Current activity (for menu)
    protected static String activityName;
    // Current mission instance
    public static CurrentMission curMiss;
    // Location manager
    public LocationManager locationManager;
    double latGPS, longGPS;
    ArrayList<Pair> values;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Deal with permissions for Android >= 6.0
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PERMISSIONS != null)
        {
            for (String perm : PERMISSIONS)
            {
                if (ActivityCompat.checkSelfPermission(this, perm)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMALL);
                }
            }
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        values = new ArrayList<Pair>();
    }

    /*********************************************************************************************/
    // Setters and getters
    /*********************************************************************************************/
    public static String getActivityName()
    {
        return activityName;
    }

    public static void setActivityName(String activityName)
    {
        BaseActivity.activityName = activityName;
    }

    public double getLatGPS()
    {
        return latGPS;
    }

    public double getLongGPS()
    {
        return longGPS;
    }

    /*********************************************************************************************/
    // Status methods

    /*********************************************************************************************/
    public void onClickStatusDriving(MenuItem item)
    {
        _driverStatus = STDRIVING;
        updateStatus();
    }

    public void onClickStatusWorking(MenuItem item)
    {
        _driverStatus = STWORKING;
        updateStatus();
    }

    public void onClickStatusOnboard(MenuItem item)
    {
        _driverStatus = STONBOARD;
        updateStatus();
    }

    public void onClickStatusResting(MenuItem item)
    {
        _driverStatus = STRESTING;
        updateStatus();
    }

    protected void resetMenuItems()
    {
        MenuItem item;

        item = _statusMenu.findItem(R.id.action_driving);
        item.setIcon(R.drawable.ic_steering_wheel);
        item = _statusMenu.findItem(R.id.action_working);
        item.setIcon(R.drawable.ic_icon_hammers);
        item = _statusMenu.findItem(R.id.action_onboard);
        item.setIcon(R.drawable.ic_square);
        item = _statusMenu.findItem(R.id.action_resting);
        item.setIcon(R.drawable.ic_bed);
    }

    protected void updateStatus()
    {
        MenuItem item;

        resetMenuItems();
        switch (_driverStatus)
        {
            case STDRIVING:
                item = _statusMenu.findItem(R.id.action_driving);
                item.setIcon(R.drawable.ic_steering_wheel_green);
                break;
            case STWORKING:
                item = _statusMenu.findItem(R.id.action_working);
                item.setIcon(R.drawable.ic_icon_hammers_green);
                break;
            case STONBOARD:
                item = _statusMenu.findItem(R.id.action_onboard);
                item.setIcon(R.drawable.ic_square_green);
                break;
            case STRESTING:
                item = _statusMenu.findItem(R.id.action_resting);
                item.setIcon(R.drawable.ic_bed_green);
                break;
        }
        if (getActivityName().equals("Mission"))
        {
            switch (curMiss.getCurrentMissionStatus())
            {
                case MISSIONSTARTED:
                    item = _statusMenu.findItem(R.id.missionStart);
                    item.setChecked(true);
                    break;
                case MISSIONSWITCHED:
                    item = _statusMenu.findItem(R.id.missionTransfert);
                    item.setChecked(true);
                    break;
                case MISSIONFINISHED:
                    item = _statusMenu.findItem(R.id.missionStop);
                    item.setChecked(true);
                    break;
            }
        } else
        {
            item = _statusMenu.findItem(R.id.action_settings);
            item.setEnabled(false);
        }
    }

    /*********************************************************************************************/
    // Back button management

    /*********************************************************************************************/
    @Override
    public void onBackPressed()
    {
        exportMission();
        return;
    }

    /*********************************************************************************************/
    // Camera methods

    /*********************************************************************************************/

    public void takePhotos()
    {
        String fDate = new SimpleDateFormat("yyyyMMddHHmmss").
                format(Calendar.getInstance().getTime());
        String file = _picsDir + fDate + ".jpg";
        File newfile = new File(file);
        try
        {
            newfile.createNewFile();
        } catch (IOException e)
        {
        }
        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, PHOTOTAKEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTOTAKEN && resultCode == RESULT_OK)
        {
            Toast.makeText(this, "Pic saved", Toast.LENGTH_SHORT).show();
        }
    }

    public int storeImage(byte[] image, String prefix)
    {
        File pictureFile = getOutputMediaFile(prefix);
        if (pictureFile == null)
        {
            Toast.makeText(this, R.string.error_file_sig, Toast.LENGTH_LONG).show();
            return 0;
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(image);
            fos.close();
            return 1;
        } catch (FileNotFoundException e)
        {
            Log.d("Bitmap", "File not found: " + e.getMessage());
            return 0;
        } catch (IOException e)
        {
            Log.d("Bitmap", "Error accessing file: " + e.getMessage());
            return 0;
        }
    }

    public File getOutputMediaFile(String prefix)
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MissionPictures/" + curMiss.getTvOt());
        File mediaFile;

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        if (prefix.equals(""))
        {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").
                    format(Calendar.getInstance().getTime());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    curMiss.getLogin() + timeStamp + ".png");
        } else
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    curMiss.getLogin() + prefix + ".png");
        }

        return mediaFile;
    }

    /*********************************************************************************************/
    // Serialization methods

    /*********************************************************************************************/

    public boolean exportMission_native()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("/sdcard/save_object.bin")
                    )
            ); //Select where you wish to save the file...
            oos.writeObject(curMiss); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
            return true;
        } catch (Exception ex)
        {
            Log.v("Serialization Error : ", ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean exportMission()
    {
        String json;
        Gson gson;
        SendHTTPPost post;
        File file;
        FileOutputStream fos;

        gson = new Gson();

        try
        {
            json = gson.toJson(curMiss);
            file = new File("/sdcard/curmiss.json");
            fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();

            post = new SendHTTPPost();
            post.execute("http://corpsnuds.pierrel.social:80/json", json);

            return true;
        } catch (Exception ex)
        {
            Log.v("Serialization Error : ", ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /*********************************************************************************************/
    //  Cryptographic methods

    /*********************************************************************************************/

    public String MD5(String md5)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e)
        {
        }
        return null;
    }

    /*********************************************************************************************/
    // Location methods

    /*********************************************************************************************/

    private boolean isLocationEnabled()
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkLocation()
    {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                    }
                });
        dialog.show();
    }

    private final LocationListener locationListenerGPS = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            Pair pair;
            longGPS = location.getLongitude();
            latGPS = location.getLatitude();
            pair = new Pair(latGPS, longGPS);
            values.add(pair);
            Log.d("GPS", "Location changed");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @SuppressWarnings({"MissingPermission"})
    public void toggleGPSUpdates(boolean onoff)
    {
        if(!checkLocation())
            return;
        if(!onoff)
        {
            locationManager.removeUpdates(locationListenerGPS);
        }
        else
        {
            Criteria crit = new Criteria();
            crit.setAccuracy(Criteria.ACCURACY_FINE);
            crit.setAltitudeRequired(false);
            crit.setCostAllowed(true);
            crit.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(crit, true);
            if(provider != null)
            {
                locationManager.requestLocationUpdates(provider, 10 * 1000,
                        0.0f, locationListenerGPS);
            }
        }
    }


}
