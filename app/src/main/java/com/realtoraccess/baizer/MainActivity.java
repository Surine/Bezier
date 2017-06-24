package com.realtoraccess.baizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.realtoraccess.baizer.libary.AnimationLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AnimationLayout animationLayout = (AnimationLayout) findViewById(R.id.click);
        animationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationLayout.thumbsUp();
            }
        });
    }
}
