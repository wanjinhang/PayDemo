package com.rikuo.paydemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.allinpay.unifypay.sdk.Allinpay;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author rikuo
 */
public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText etAmt;
    private static String appid;
    private static String cusid;
    private static String key;

    static {
        // 生产参数
        appid = "00010021";
//        orgid = "100581048160005";
        cusid = "276241015203896";
        key = "yxm123";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_result);
        etAmt = findViewById(R.id.et_amt);
        // 调试模式，提供日志打印
        Allinpay.debug();
    }

    /**
     * 跳转SDK支付选择界面
     *
     * @param view 视图
     */
    public void turnToPay(View view) {
        HashMap<String, String> params = new HashMap<>();
        params.put("trxamt", "1");
        String amt = etAmt.getText().toString();
        if (!TextUtils.isEmpty(amt)) {
            params.put("trxamt", new BigDecimal(amt).multiply(new BigDecimal(100)).toBigInteger().toString());
        }
        params.put("reqsn", String.valueOf(System.currentTimeMillis()));
        params.put("subject", "Android测试支付下单");
        params.put("trxreserve", "备注");
        params.put("validtime", "30");
        params.put("notifyurl", "http://www.xxx.com");
        params.put("appid", appid);
        params.put("cusid", cusid);
        params.put("sign", signParam(params));
        String jsonString = JSON.toJSONString(params);
        Log.v("报文", jsonString);
        Allinpay.openPay(this, params);
        textView.setText("");
    }

    /**
     * 签名订单信息
     *
     * @param params 订单参数
     * @return 密钥
     */
    private String signParam(HashMap<String, String> params) {
        try {
            String signtype = params.get("signtype");
            String rsa = "RSA";
            String md5Sign;
            if (!TextUtils.isEmpty(signtype) && rsa.equals(signtype)) {
                String blankData = EncryptUtil.getSignStr(params, null, true);
                md5Sign = EncryptUtil.rsa(blankData,
                        "此处设置RSA私钥");
            } else {
                params.put("key", key);
                String blankData = EncryptUtil.getSignStr(params, null, true);
                md5Sign = EncryptUtil.md5(blankData.getBytes(StandardCharsets.UTF_8));
                params.remove("key");
            }
            return md5Sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SDK直接下单支付
     *
     * @param view
     */
    public void pay(View view) {
        HashMap<String, String> params = new HashMap<>();
        params.put("trxamt", "1");
        params.put("reqsn", String.valueOf(System.currentTimeMillis()));
        params.put("subject", "Android测试支付下单");
        params.put("trxreserve", "备注");
        params.put("validtime", "30");
        params.put("notifyurl", "http://www.xxx.com");
        params.put("limitpay", "no_credit");
        params.put("appid", appid);
//        params.put("orgid", orgid);
        params.put("cusid", cusid);
//        params.put("signtype", "RSA");
        params.put("sign", signParam(params));
        // 支付方式，不用参与签名
        // 支付宝 Allinpay.PAY_TYPE_ALI
        // 中国银行 Allinpay.PAY_TYPE_BOC
        // 工商银行 Allinpay.PAY_TYPE_ICBC
        // 招商银行 Allinpay.PAY_TYPE_CMB
        params.put("paytype", Allinpay.PAY_TYPE_CMB);
        String jsonString = JSON.toJSONString(params);
        Log.v("报文", jsonString);

        Allinpay.createPayment(this, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Allinpay.REQUEST_CODE_PAY) {
            if (resultCode == RESULT_OK) {
                // 成功 Allinpay.PAY_RESULT_SUCC
                // 失败 Allinpay.PAY_RESULT_FAIL
                // 取消 Allinpay.PAY_RESULT_CANCEL
                // 未知 Allinpay.PAY_RESULT_UNKNOWN 一般在银行支付中返回
                int result = data.getIntExtra("code", Allinpay.ALLINPAY_SUCCESS);
                Toast.makeText(MainActivity.this, data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                textView.setText(result + ":" + data.getStringExtra("message"));
            }
        }
    }


}
