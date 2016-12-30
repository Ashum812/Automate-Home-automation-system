package com.led.led;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;


public class Timer extends ActionBarActivity {

    public int tlapse = 0;     //minutes
    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar;
    private ToggleButton action_button;
    int timer_action = 0;
    Button clear_timer_button;
    Button send_button;
    String requestCode ;
    ImageView myimage_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        myimage_view = (ImageView)findViewById(R.id.background_image);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        myimage_view.setImageDrawable(wallpaperDrawable);

        Intent i  = getIntent();
        requestCode = i.getStringExtra("requestCode"); //receive the data from control activity

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) { // only for newer versions than gingerbread
          clear_timer_button  = (Button)findViewById (R.id.clear_timer);
          clear_timer_button.setTextSize(14);
        }
        if(requestCode == "5")
        {
            send_button  = (Button)findViewById (R.id.send);
            clear_timer_button  = (Button)findViewById (R.id.clear_timer);
            action_button = (ToggleButton)findViewById(R.id.toggleButton);
            send_button.setText("Set Alarm");
            clear_timer_button.setText("Stop Alarm");
            action_button.setVisibility(View.GONE);
            //action_button.isShown();
            setTitle("Sleep Alarm");
        }

        timePicker1 = (TimePicker) findViewById(R.id.timePicker);
        timePicker1.setIs24HourView(true);
        timePicker1.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                calendar = Calendar.getInstance();
                int chour = calendar.get(Calendar.HOUR_OF_DAY);
                int cmin = calendar.get(Calendar.MINUTE);

                if ((arg1 - chour) >= 0) {
                    if ((arg2 - cmin) >= 0) {
                        tlapse = ((arg1 - chour) * 60 + (arg2 - cmin));
                        time.setText("" + (arg1 - chour) + " : " + (arg2 - cmin));
                    }

                    if ((arg2 - cmin) < 0) {
                        tlapse = ((arg1 - chour) * 60 + (arg2 - cmin + 60));
                        time.setText("" + (arg1 - chour) + " : " + (arg2 - cmin + 60));
                    }
                } else if ((arg1 - chour) <= 0) {
                    if ((arg2 - cmin) >= 0) {
                        tlapse = ((arg1 - chour + 24) * 60 + (arg2 - cmin));
                        time.setText("" + (arg1 - chour + 24) + " : " + (arg2 - cmin));
                    }
                    if ((arg2 - cmin) < 0) {
                        tlapse = ((arg1 - chour + 24) * 60 + (arg2 - cmin));
                        time.setText("" + (arg1 - chour + 24) + " : " + (arg2 - cmin + 60));
                    }
                }

            }
        });

        time = (TextView) findViewById(R.id.timer_textView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendtime(View button) {
        int id = button.getId();
        //if(action_button.isChecked())
            timer_action = 1;
       // else timer_action = 0;

        if (id == R.id.send) {
            time.setText(new StringBuilder().append(tlapse));
            String output= (timer_action+":"+tlapse).toString();
            Intent i = new Intent(getApplicationContext(),ledControl.class);
            i.putExtra("MESSAGE", output);
            setResult(1, i);
            finish();
        }
        else  if (id == R.id.cancel) {
            time.setText(new StringBuilder().append(tlapse));
            Intent i = new Intent(getApplicationContext(), ledControl.class);
            setResult(0, i);
            finish();
        }
        else  if (id == R.id.clear_timer) {
            String output= (timer_action+":"+"-1").toString();
            Intent i = new Intent(getApplicationContext(),ledControl.class);
            i.putExtra("MESSAGE", output);
            setResult(1, i);
            finish();
        }



    }
}
