package com.example.hua.wms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zxing.activity.CaptureActivity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import utils.AccessToken;

public class Reveiving3Activity extends AppCompatActivity {
    private  TextView sn;   //SN号
    private TextView order_tv; //订单号
    private TextView  commodity_tv;//商品名称
    private  TextView meterage_tv;//计量规格
    private  TextView vid_tv;//有效期至
    private  TextView  upplier_tv;//供货单位
    private  TextView  norms_tv; //规格
    private  TextView   basenumber_tv;//库号
    private  TextView receivable_tv;//应收
    private  TextView receipts_tv;  //实收
    private  TextView received_tv;  //已收
    private  TextView due_tv;   //代收
    String scanResult;//扫描结果
    Intent intent=new Intent();
    String myToken = AccessToken.getInstance().getToken();
    final OkHttpClient client = new OkHttpClient();
    String sn_barcodes="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveiving3);
        order_tv= (TextView) findViewById(R.id.order_tv);
        commodity_tv= (TextView) findViewById(R.id.commodity_tv);
        meterage_tv= (TextView) findViewById(R.id.meterage_tv);
        vid_tv= (TextView) findViewById(R.id.vid_tv);
        upplier_tv= (TextView) findViewById(R.id.upplier_tv);
        norms_tv= (TextView) findViewById(R.id.norms_tv);
        basenumber_tv= (TextView) findViewById(R.id.basenumber_tv);
        receivable_tv= (TextView) findViewById(R.id.receivable_tv);
        receipts_tv= (TextView) findViewById(R.id.receipts_tv);
        received_tv= (TextView) findViewById(R.id.received_tv);
        due_tv= (TextView) findViewById(R.id.due_tv);
        sn=(TextView)findViewById(R.id.sn);

    }
    /*调用扫一扫*/
    public void scan_bt(View view){
       intent.setClass(Reveiving3Activity.this,CaptureActivity.class);
        Reveiving3Activity.this.startActivityForResult(intent,0);

    }
    /*监听返回按钮*/
    public  void  return_bt( View view){
        intent.setClass(Reveiving3Activity.this,Reveiving2Activity.class);
        Reveiving3Activity.this.startActivity(intent);

    }
    /*监听取消收货按钮*/
    public void cancel_bt(View view){
        setContentView(R.layout.activity_reveiving3);
    }
/* 扫一扫返回的扫描结果*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){		//此处就是用result来区分,是谁返回的数据
            Bundle bundle = data.getExtras();
           //这就获取了扫描的内容了
//            Toast.makeText(Reveiving3Activity.this,scanResult,Toast.LENGTH_SHORT).show();
            scanResult = bundle.getString("result");
            if (scanResult.contains("SN")){
                if (sn_barcodes.isEmpty()){
                    sn_barcodes=scanResult;
                    getRequest();
                }else {
                    sn_barcodes();
                }
            }else if(scanResult.contains("HAPL")){
                postRequest(sn_barcodes,scanResult,"1",myToken);
            }

        }
    }
    public  void sn_barcodes(){
        if (sn_list.contains(scanResult)){
            if (sn_barcodes.contains(scanResult)){
                Toast.makeText(Reveiving3Activity.this,"请不要重复扫描SN！",Toast.LENGTH_SHORT).show();
            }else{
                sn_barcodes+=","+scanResult;
            }

        }else{
            Toast.makeText(Reveiving3Activity.this,"SN不合法！",Toast.LENGTH_SHORT).show();
        }

    }
    /*扫描SN联网发送的get请求！*/
    public  void getRequest(){
       final Request request = new Request.Builder()
               .get()
               .url("http://wms-test-api.joyoio.net/api/v1/binding_tray/scan_sn?barcode="+scanResult+"&token="+myToken)
                .build();
       Log.i("111111","111111111111111111");
        new Thread(new Runnable(){
            @Override
            public void run() {
                Response response;
                Log.i("stan","xxxxx");
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String message=response.body().string();
                        Log.i("4444444444444",message);
                        Message msg=handler.obtainMessage();
                        Log.i("4444444444444","44444444444444444444444");
                        msg.obj=message;
                        msg.what=1;
                        handler.sendMessage(msg);
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.i("22222222222","2222222222222");
    }
    /*
    *扫描托盘执行流程*/
    public void postRequest(String sn_barcode, String tray_barcode,String is_full_tray,String token ){
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormEncodingBuilder()
                .add("sn_barcodes",sn_barcodes)
                .add("tray_barcode",scanResult)
                .add("is_full_tray","1")
                .add("token",myToken)
                .build();
        final Request request = new Request.Builder()
                .url("http://wms-test-api.joyoio.net//api/v1/binding_tray")
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String message=response.body().string();
                        //JSONObject msgInfo= JSONObject.fromObject(message);
                        Message msg=handler.obtainMessage();
                        //msg.obj=msgInfo;
                        msg.obj=message;
                        msg.what=2;
                        handler.sendMessage(msg);
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /*接受子线程传递的数据*/
    String sn_list;
    JSONObject msgInfo;
    Handler handler=new Handler(){
        public void  handleMessage(Message msg){
            Log.i("5555555555","5555555555555");

             msgInfo= JSONObject.fromObject((String) msg.obj);
            if( msg.what==1) {
                getResponse();

            } else if (msg.what==2){
                postResponse();
            }
            else
                Toast.makeText(Reveiving3Activity.this,"联网失败！",Toast.LENGTH_SHORT).show();
        }
        public void getResponse(){
            if ("ok".equals(msgInfo.getString("status"))) {
            sn.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("sn"));
            order_tv.setText(msgInfo.getJSONObject("data").getString("receipt_order_code"));
            commodity_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("product_name"));
            meterage_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("measure_unit"));
            vid_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("expiry_date"));
            upplier_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("unit_name"));
            norms_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("specific"));
            basenumber_tv.setText(msgInfo.getJSONObject("data").getJSONObject("sn_info").getString("storehouse_no"));
            receivable_tv.setText(msgInfo.getJSONObject("data").getJSONObject("detail").getString("quantity"));
            receipts_tv.setText(msgInfo.getJSONObject("data").getJSONObject("detail").getString("quantity"));
            received_tv.setText(msgInfo.getJSONObject("data").getJSONObject("detail").getString("quantity"));
            due_tv.setText(msgInfo.getJSONObject("data").getJSONObject("detail").getString("quantity"));
            Map<String,String> info_=(Map<String,String>)((JSONArray)msgInfo.getJSONObject("data").get("detail_list")).get(0);
            sn_list=info_.get("sn");
        } else if("token超时或不存在,请重新登录".equals(msgInfo.get("message"))){
            intent.setClass(Reveiving3Activity.this,MainActivity.class);
            Reveiving3Activity.this.startActivity(intent);
        }else  if ("SN信息不存在".equals(msgInfo.get("message"))){
            Toast.makeText(Reveiving3Activity.this,"SN信息不存在！",Toast.LENGTH_SHORT).show();
        }else {
                Toast.makeText(Reveiving3Activity.this,"该SN不可绑定",Toast.LENGTH_SHORT).show();
            }
        }
        public void  postResponse(){
           String  status= msgInfo.getString("status");
            if ("ok".equals(status)){
               setContentView(R.layout.activity_reveiving3);
                Toast.makeText(Reveiving3Activity.this,"该托货品收货完成",Toast.LENGTH_SHORT).show();
            }else{
                setContentView(R.layout.activity_reveiving3);
                Toast.makeText(Reveiving3Activity.this,"收货异常！",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
