package com.rikuo.paydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ndktools.javamd5.Mademd5;

import java.util.Map;
import java.util.TreeMap;

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
    protected Map<String,String>  getSignStr(){

        TreeMap<String,String> params = new TreeMap<String, String>();
        params.put("cusid", "990440148166000");
        params.put("appid", "00000003");
        params.put("trxamt", "100");
        params.put("reqsn", "20200227001");
        params.put("subject", "testTile");
        params.put("validtime", "5");
        params.put("signtype", "MD5");

        params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
        byte[] bys = http.postParams(params, true);
        String result = new String(bys,"UTF-8");
        Map<String,String> map = handleResult(result);
        return map;


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
