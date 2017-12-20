package fr.bs_tech.vps;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Transport extends BaseActivity
{
    TableLayout tl;
    TableRow tr;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setActivityName("Transport");
        // Display toolbar and use it as an action bar
        setContentView(R.layout.activity_transport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fill in tables with current and future missions
        tl = (TableLayout) findViewById(R.id.mainTable);
        // getCurrentMissionFromServer()
        displayMission(true, tl, "25/12/2017", "2017122501-001", "Annie Cordy", "Marseille - Lille", "42");

        tl = (TableLayout) findViewById(R.id.otherTable);
        // getFutureMissionsFromServer()
        displayMission(false, tl, "30/11/2017", "2017113002-001", "Patrick Bruel", "Lille - Dunkerque", "4");
        for (int i = 1; i < 15; i++)
        {
            displayMission(false, tl, "02/12/2017", "2017120201-001", "Steve Jobs", "Berck plage - Bristol", "125");
        }

        TextView usernameText = (TextView) findViewById(R.id.txtUserName);
        usernameText.setText(curMiss.getLogin());


        // If it's the first launch,set status to "Resting"
        if (_launched == 0)
        {
            // Create directory for missions pictures
            File newdir = new File(_picsDir);
            newdir.mkdirs();

            _driverStatus = STRESTING;
            curMiss.setTvStatus(getString(R.string.pending));
            _launched = 1;
        }
    }

    // Create menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        _statusMenu = menu;

        getMenuInflater().inflate(R.menu.menu_transport, menu);
        updateStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Display current mission activity
    private void showCurrentMission(String ot, String dt, String dr, String jn)
    {
        curMiss.setTvOt(ot);
        curMiss.setTvDate(dt);
        curMiss.setTvConvoyLeader(dr);
        curMiss.setGlobalJourney(jn);

        Intent intent = new Intent(Transport.this, Mission.class);
        startActivity(intent);
    }

    // Display data for current mission
    // Input : TableLayout, date, transport number, driver name, journey, estimated duration in hours
    // Output : nothing
    public void displayMission(boolean clickable, TableLayout tl, final String date, final String ot, final String driver, final String journey, final String duration)
    {
        TextView[] txt = new TextView[5];

        tr = new TableRow(this);

        txt[0] = new TextView(this);
        txt[0].setText(date);
        txt[0].setTextColor(Color.BLACK);
        txt[0].setGravity(Gravity.LEFT);
        tr.addView(txt[0]);
        txt[1] = new TextView(this);
        txt[1].setText(ot);
        txt[1].setTextColor(Color.BLACK);
        txt[1].setGravity(Gravity.CENTER);
        tr.addView(txt[1]);
        txt[2] = new TextView(this);
        txt[2].setText(driver);
        txt[2].setTextColor(Color.BLACK);
        txt[2].setGravity(Gravity.CENTER);
        tr.addView(txt[2]);
        txt[3] = new TextView(this);
        txt[3].setText(journey);
        txt[3].setTextColor(Color.BLACK);
        txt[3].setGravity(Gravity.CENTER);
        tr.addView(txt[3]);
        txt[4] = new TextView(this);
        txt[4].setText(hours2days(duration));
        txt[4].setTextColor(Color.BLACK);
        txt[4].setGravity(Gravity.CENTER);
        tr.addView(txt[4]);
        tr.setClickable(clickable);
        if (clickable)
            tr.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showCurrentMission(ot, date, driver, journey);
                }
            });

        tl.addView(tr, new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    // Format duration from hours to days and hours
    public String hours2days(String duration)
    {
        int dur;
        int days;
        int hours;
        String d;
        String h = getString(R.string.hourabbrev);

        dur = Integer.parseInt(duration);
        if (dur < 24)
            return (duration.concat(" ").concat(h));
        days = dur / 24;
        d = (days > 1) ? getString(R.string.daynamesingle) : getString(R.string.daynameplural);

        hours = dur - (days * 24);

        return (String.format("%d %s %d %s", days, d, hours, h));
    }

}
