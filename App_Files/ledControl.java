package com.led.led;

import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnDis;
    ImageView switch1,switch2,switch3,switch4,timer1,timer2,timer3,timer4,myimage_view;
    String timer_op = null;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static String EXTRA_DATA = "Data";


    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device


        //view of the ledControl
        setContentView(R.layout.activity_led_control);

       myimage_view = (ImageView)findViewById(R.id.background_image);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        myimage_view.setImageDrawable(wallpaperDrawable);


        //call the widgtes
        switch1 = (ImageView)findViewById(R.id.imageview1);
        switch2 = (ImageView)findViewById(R.id.imageview2);
        switch3 = (ImageView)findViewById(R.id.imageview3);
        switch4 = (ImageView)findViewById(R.id.imageview4);

        timer1 = (ImageView)findViewById(R.id.timer1);
        timer2 = (ImageView)findViewById(R.id.timer2);
        timer3 = (ImageView)findViewById(R.id.timer3);
        timer4 = (ImageView)findViewById(R.id.timer4);

        btnDis = (Button)findViewById(R.id.button_dis);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        switch1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tosw1();      //method to turn on
            }
        });

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tosw2();   //method to turn off
            }
        });
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tosw3();   //method to turn off
            }
        });
        switch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tosw4();   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        timer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmr1();      //method to turn on
            }
        });

        timer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmr2();      //method to turn on
            }
        });

        timer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmr3();      //method to turn on
            }
        });

        timer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmr4();      //method to turn on
            }
        });

    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void tosw1()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("#1".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void tosw2()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("#2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void tosw3()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("#3".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void tosw4()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("#4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }



    private void tmr1()
    {

     Intent i  = new Intent(getApplicationContext(),Timer.class);
     startActivityForResult(i, 1);// Activity is started with requestCode 1
    }

    private void tmr2()
    {
        Intent i  = new Intent(getApplicationContext(),Timer.class);
        startActivityForResult(i, 2);// Activity is started with requestCode 2

    } private void tmr3()
    {
        Intent i  = new Intent(getApplicationContext(),Timer.class);
        startActivityForResult(i, 3);// Activity is started with requestCode 3
    }
    private void tmr4()
    {
        Intent i  = new Intent(getApplicationContext(),Timer.class);
        startActivityForResult(i, 4);// Activity is started with requestCode 4

    }
    private void sleep()
    {
        Intent i  = new Intent(getApplicationContext(),Timer.class);
        i.putExtra("requestCode","5");
        startActivityForResult(i, 5);// Activity is started with requestCode s

    }


    // Call Back method  to get the Message form other Activity    override the method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1) { // fetch the message String
            timer_op = data.getStringExtra("MESSAGE");

            // check if the request code is same as what is passed  here it is 2
            if (requestCode == 1) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write(("@1:"+timer_op).toString().getBytes());
                        Toast.makeText(getBaseContext(), ("@1:"+timer_op).toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
            if (requestCode == 2) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write(("@2:"+timer_op).toString().getBytes());
                        Toast.makeText(getBaseContext(), ("@2:" + timer_op).toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
            if (requestCode == 3) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write(("@3:"+timer_op).toString().getBytes());
                        Toast.makeText(getBaseContext(), ("@3:" + timer_op).toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
            if (requestCode == 4) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write(("@4:"+timer_op).toString().getBytes());
                        Toast.makeText(getBaseContext(), ("@4:"+timer_op).toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }

            if (requestCode == 5) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write(("@s:"+timer_op).toString().getBytes());
                        Toast.makeText(getBaseContext(), ("@s:"+timer_op).toString(), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }

        }
    }


    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
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

        if (id == R.id.action_tfa) {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("!tfa".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
        if (id == R.id.action_toa) {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("!toa".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }
        if (id == R.id.action_auto) {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("!auto".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }if (id == R.id.action_secure) {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("!secure".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
        }


        if(id == R.id.action_sleep)
        sleep();


        if (id == R.id.action_data) {

            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("!data".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }

           String incomingdata = "data from arduino\n\ndata from arduino\tdata from arduino";    //data sent by arduino

           //some command recive data in async mid from arduino...


            // Make an intent to start next activity.
            Intent i = new Intent(getApplicationContext(), Data.class);

            //Change the activity.
            i.putExtra(EXTRA_DATA,incomingdata); //this will be received at ledControl (class) Activity
            startActivity(i);
        }

        if (id == R.id.action_about_maker) {
            Toast.makeText(getBaseContext(), "Ashutosh Mishra", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "MAIT/IP Univ.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "ashutosh.m812@gmail.com", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
