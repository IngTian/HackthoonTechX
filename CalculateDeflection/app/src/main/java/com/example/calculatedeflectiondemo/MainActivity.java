package com.example.calculatedeflectiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.calculatedeflectiondemo.R;
import com.example.calculatedeflectiondemo.util.Coordinate;
import com.example.calculatedeflectiondemo.util.DeflectionUtil;

import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView currentCoordinate, destinationCoordinate, deflectionAngle;
    private int axis_x, axis_y;
    private SensorManager sensorManager;
    private Activity activity;
    private float[] accelerValues = new float[3];
    private float[] magneticValues = new float[3];
    private double destToCurAngle;

    private final SensorEventListener sensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {

                // 深度复制，可避免部分设备下 SensorManager.getRotationMatrix 返回false的问题
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        accelerValues[i] = event.values[i];
                    }

                    double deflectionFromTrueSouth = 360.0 - destToCurAngle;
                    double deflection = 180.0 - getAngle();

                    String t = "Deflection: " + (deflection - deflectionFromTrueSouth);

                    deflectionAngle.setText(t.toCharArray(), 0, t.length());

                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    for (int i = 0; i < 3; i++) {
                        magneticValues[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        currentCoordinate = (TextView) findViewById(R.id.currentCoordinate);
        destinationCoordinate = (TextView) findViewById(R.id.destinationCoordinate);
        deflectionAngle = (TextView) findViewById(R.id.deflectionAngle);

        /*
        Set Text
         */
        double[] curCor = new double[]{121.442342, 31.188235}, destCor = new double[]{121.442498, 31.197395};
        String c = "(" + curCor[0] + "," + curCor[1] + ")",
                d = "(" + destCor[0] + "," + destCor[1] + ")";
        currentCoordinate.setText(c.toCharArray(), 0, c.length());
        destinationCoordinate.setText(d.toCharArray(), 0, d.length());
        destToCurAngle = DeflectionUtil.getAngle(new Coordinate(destCor[0], destCor[1]), new Coordinate(curCor[0], curCor[1]));

        this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        // 注册三个传感器监听，加速度传感器、磁场传感器、陀螺仪传感器。
        // 陀螺仪传感器是可选的，也没有实际用处，但是在部分设备上，如果没注册它，会导致
        // SensorManager.getRotationMatrix 返回false
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_UI);

        initIn();
    }


    private void initIn() {

        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向），而应用是横屏时，
        // 需要将相对设备自然方向的方位角转换为相对水平方向的方位角。

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display dis = wm.getDefaultDisplay();
        int rotation = dis.getRotation();

        axis_x = SensorManager.AXIS_X;
        axis_y = SensorManager.AXIS_Y;
        switch (rotation) {
            case Surface.ROTATION_0:
                break;
            case Surface.ROTATION_90:
                axis_x = SensorManager.AXIS_Y;
                axis_y = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                axis_x = SensorManager.AXIS_X;
                axis_y = SensorManager.AXIS_MINUS_Y;
                break;
            case Surface.ROTATION_270:
                axis_x = SensorManager.AXIS_MINUS_Y;
                axis_y = SensorManager.AXIS_X;
                break;

            default:
                break;
        }
    }

    /**
     * 取方位角
     *
     * @return
     */
    private float getAngle() {

        float[] inR = new float[9];
        // 第一个是方向角度参数，第二个参数是倾斜角度参数
        SensorManager.getRotationMatrix(inR, null, accelerValues, magneticValues);

        float[] outR = new float[9];
        float[] orientationValues = new float[3];

        // 根据当前上下文的屏幕方向调整与自然方向的相对关系。
        // 当设备的自然方向是竖直方向（比如，理论上说所有手机的自然方向都是都是是竖直方向，而有些平板的自然方向是水平方向），而应用是横屏时，
        // 需要将相对设备自然方向的方位角转换为相对水平方向的方位角。
        SensorManager.remapCoordinateSystem(inR, axis_x, axis_y, outR);
        SensorManager.getOrientation(outR, orientationValues);
        return (float) Math.toDegrees(orientationValues[0]);
    }
}