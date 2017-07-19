package com.circle.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar mCurrentSeekBar;
    CircleView mCircleView;
    TextView mStep;

    /**
     * 目标步数
     */
    public static float TARGET_STEP = 100f;

    private float currentStep = 30f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this,TestActivity.class));

        mCurrentSeekBar = (SeekBar) findViewById(R.id.currentBar);
        mCircleView = (CircleView) findViewById(R.id.circleView);
        mStep = (TextView) findViewById(R.id.current_and_target);

        float precent = currentStep/TARGET_STEP;
        mCircleView.setAngel(precent);
        mStep.setText((int) currentStep+" 步\n目标 100 步");
        mCurrentSeekBar.setProgress((int)currentStep);
        mCurrentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mStep.setText(progress+" 步\n目标 100 步");
                //当前步数占据目标步数的多少
                float precent = progress/TARGET_STEP;
                mCircleView.setAngel(precent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
