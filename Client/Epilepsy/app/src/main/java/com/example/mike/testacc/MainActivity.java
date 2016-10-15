package com.example.mike.testacc;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String BASE_URL = "http://<IP>/healthhack/index.php";
    private OkHttpClient client = new OkHttpClient();
    private String username = "tester";
    private long timestamp = 0L;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private TextView timeText,
            linXText, linYText, linZText;
    private Sensor myAcc, myGra, myLin;
    private SensorManager SM;

    private int counter = 0;
    private JSONObject json = new JSONObject();

    private final int sampNum = 250;
    private double interval = 1000L * 30;
    private double freq = 1000L / 50;
    private long lastTime = System.currentTimeMillis();
    private long currTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Linear Acceleration excluding Gravity
        myLin = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Register Sensor Listener
        SM.registerListener(this, myLin, SensorManager.SENSOR_DELAY_FASTEST);

        // Assign TextView
        linXText = (TextView)findViewById(R.id.linXText);
        linYText = (TextView)findViewById(R.id.linYText);
        linZText = (TextView)findViewById(R.id.linZText);

        timeText = (TextView)findViewById(R.id.currTime);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            currTime = System.currentTimeMillis();
            if (counter == 0) {
                timestamp = currTime;
            }
            if (counter < sampNum) {
                if (((currTime - lastTime) >= freq)) {
                    linXText.setText("X: " + event.values[0]);
                    linYText.setText("Y: " + event.values[1]);
                    linZText.setText("Z: " + event.values[2]);
                    JSONObject acc = new JSONObject();
                    try {
                        acc.put("X", event.values[0]);
                        acc.put("Y", event.values[1]);
                        acc.put("Z", event.values[2]);
                        json.put(Long.toString(currTime),acc);
                    } catch (JSONException e) {

                    }
                    lastTime = currTime;
                    counter++;
                }
            } else {
                if (currTime - timestamp >= interval) {
                    counter = 0;
                    FormBody body = new FormBody.Builder()
                            .add("action", "insert")
                            .add("username", "tester")
                            .add("timestamp", Long.toString(timestamp))
                            .add("json", json.toString())
                            .build();
                    String url = BASE_URL;
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("ERROR", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.v("RES", response.toString());
                        }
                    });

                    json = new JSONObject();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not is use
    }
}
