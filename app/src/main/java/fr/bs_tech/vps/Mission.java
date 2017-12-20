package fr.bs_tech.vps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.bs_tech.vps.databinding.ActivityMission2Binding;

public class Mission extends BaseActivity
{


    protected PopupWindow SignaturePopup;
    private String sigType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setActivityName("Mission");

        ActivityMission2Binding bindings = DataBindingUtil.setContentView(this, R.layout.activity_mission2);
        bindings.setCurrentmission(curMiss);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mission " + curMiss.getGlobalJourney());

        // Initialize signature type to nothing
        sigType = "";
        CheckBox cb = (CheckBox) findViewById(R.id.cbDischarge);
        cb.setEnabled(true);
        if (curMiss.isbSignedDischarge())
        {
            cb.setChecked(true);
            cb.setEnabled(false);
        }
        // Display back icon on toolbar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Prevent keyboard to pop up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // Callback for toolbar icons others than status ones
    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        String title = getString(R.string.mission_status);

        switch (item.getItemId())
        {
            case android.R.id.home:
                Toast.makeText(this, "Status : " + curMiss.getTvStatus(), Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.missionStart:
                if (!item.isChecked())
                {
                    new AlertDialog.Builder(this)
                            .setTitle(title)
                            .setMessage(R.string.start_curr_miss)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    curMiss.setCurrentMissionStatus(MISSIONSTARTED);
                                    curMiss.setTvStatus(getString(R.string.in_progress));
                                    item.setChecked(true);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    return true;
                } else
                    return false;

            case R.id.missionTransfert:
                if (!item.isChecked())
                {
                    new AlertDialog.Builder(this)
                            .setTitle(title)
                            .setMessage(R.string.switch_miss)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    curMiss.setCurrentMissionStatus(MISSIONSWITCHED);
                                    curMiss.setTvStatus(getString(R.string.finished));
                                    item.setChecked(true);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    return true;
                } else
                    return false;

            case R.id.missionStop:
                if (!item.isChecked())
                {
                    new AlertDialog.Builder(this)
                            .setTitle(title)
                            .setMessage(R.string.finish_curr_miss)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    curMiss.setCurrentMissionStatus(MISSIONFINISHED);
                                    curMiss.setTvStatus(getString(R.string.finished));
                                    item.setChecked(true);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    return true;
                } else
                    return false;
        }
        return super.onOptionsItemSelected(item);
    }

    // Callback for menu creation. Update status when menu is created.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        _statusMenu = menu;

        getMenuInflater().inflate(R.menu.menu_transport, menu);
        updateStatus();
        return true;
    }

    public void onCbClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.cbDischarge:
                if (((CheckBox) view).isChecked())
                {
                    sigType = "Discharge";
                    showSignaturePopup();
                    break;
                }
            case R.id.rb1:
                if(((RadioButton) view).isChecked())
                    curMiss.setCbCategory(1);
                break;
            case R.id.rb2:
                if(((RadioButton) view).isChecked())
                    curMiss.setCbCategory(2);
                break;
            case R.id.rb3:
                if(((RadioButton) view).isChecked())
                    curMiss.setCbCategory(3);
                break;
        }
    }

    protected void showSignaturePopup()
    {
        Context mContext;
        RelativeLayout parentlayout;
        final CaptureSignatureView mSig = new CaptureSignatureView(this, null);

        mContext = getApplicationContext();
        parentlayout = (RelativeLayout) findViewById(R.id.mission_layout);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup_signature, null);
        ViewGroup dlgView = (ViewGroup) customView.findViewById(R.id.siglayout);

        if (sigType.equals("Discharge"))
        {
            TextView tv = (TextView) customView.findViewById(R.id.tvJuridique);
            tv.setText("Ici sera présent un long texte à la con que personne ne lit, pas même " + curMiss.getTvConvoyLeader() + ". N'est-ce pas ?" +
                    "et on va rajouter un paquet de texte pour voir si le scroll fonctionne. Y'a intérêt sinon je lui démonte la tronche et je le disperse façon puzzle.");
        }

        // Use MATCH_PARENT to have a full-screen popup
        SignaturePopup = new PopupWindow(customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        SignaturePopup.setOutsideTouchable(false);

        Button okButton = (Button) customView.findViewById(R.id.btnOk);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                byte[] sig;

                SignaturePopup.dismiss();
                // TODO : check if signature is not blank
                sig = mSig.getBytes();
                if (storeImage(sig, sigType) == 1)
                {
                    if (sigType.equals("Discharge"))
                    {
                        curMiss.setbSignedDischarge(true);
                        CheckBox cb = (CheckBox) findViewById(R.id.cbDischarge);
                        cb.setChecked(true);
                        cb.setEnabled(false);
                        sigType = "";
                    }
                }
            }
        });

        ImageButton cancelButton = (ImageButton) customView.findViewById(R.id.ib_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SignaturePopup.dismiss();
                if (sigType.equals("Discharge"))
                {
                    curMiss.setbSignedDischarge(false);
                    CheckBox cb = (CheckBox) findViewById(R.id.cbDischarge);
                    cb.setChecked(false);
                    sigType = "";
                }
            }
        });

        dlgView.addView(mSig, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        SignaturePopup.showAtLocation(parentlayout, Gravity.CENTER, 0, 0);
    }

}

