package fr.bs_tech.vps;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.bs_tech.vps.bindings.CurrentMission;

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

    protected static final String _picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MissionPhotos/";

    // Needed to change menu items
    protected Menu _statusMenu;
    // Mission current _driverStatus
    protected static int _driverStatus;
    // App already _launched once
    protected static int _launched = 0;

    public static CurrentMission curMiss;

    // Status methods

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
    }

    @Override
    public void onBackPressed()
    {
        String fDate = new SimpleDateFormat("yyyyMMddHHmmss").
                format(Calendar.getInstance().getTime());
        String file = _picsDir + fDate + ".jpg";
        File newfile = new File(file);
        try
        {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }
        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, PHOTOTAKEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }
        catch (FileNotFoundException e)
        {
            Log.d("Bitmap", "File not found: " + e.getMessage());
            return 0;
        }
        catch (IOException e)
        {
            Log.d("Bitmap", "Error accessing file: " + e.getMessage());
            return 0;
        }
    }

    public File getOutputMediaFile(String prefix)
    {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MissionPictures/"+curMiss.getTvOt());
        File mediaFile;

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        if(prefix.equals(""))
        {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").
                    format(Calendar.getInstance().getTime());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    curMiss.getLogin() + timeStamp + ".png");
        }
        else
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    curMiss.getLogin() + prefix + ".png");
        }

        return mediaFile;
    }

}
