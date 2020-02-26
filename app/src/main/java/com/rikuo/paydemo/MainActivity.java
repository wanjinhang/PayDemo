package com.rikuo.paydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ndktools.javamd5.Mademd5;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button payBtn = findViewById(R.id.paybtn);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"test",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *
     * @param signStr
     * @return
     */
    protected String sign(String signStr){

        Mademd5 md5 = new Mademd5();
        return (md5.toMd5(signStr));

    }
}
