package com.example.smswatchertradingand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class SMSTradingReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.INFO(TAG, "Intent recieved: " + intent.getAction());
        if (intent.getAction() == SMS_RECEIVED)
        {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for(SmsMessage message : smsMessages)
            {
                // Do whatever you want to do with SMS.
                //Log.Info(TAG, message.DisplayMessageBody);

                String orgAddress = message.getOriginatingAddress().toUpperCase(Locale.ROOT);
                Toast.makeText(context, "From:" + orgAddress + "\n Mgs:" + message.getDisplayMessageBody(), Toast.LENGTH_LONG).show();
                if(orgAddress.contains("BAKULJ") || orgAddress.contains("DSIJPL") ||
                        orgAddress.contains("9930311883") || orgAddress.contains("9892149099") ||
                        orgAddress.contains("9819596650") || orgAddress.contains("9930311866")
                || orgAddress.contains("9820559252") || orgAddress.contains("UTMFIN")) {
                    postModel(message.getDisplayMessageBody());
                }

            }

        }
    }

    private void postModel(String message)
    {
        //URL httpbinEndpoint;

        try {
            JSONObject SmsModel = new JSONObject();
            SmsModel.put("Message",message);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpPost httpPostObj = new OkHttpPost();
                    try {
                        httpPostObj.Post("https://devinvestortrading.azurewebsites.net/signal/submit",SmsModel.toString());
                    } catch (IOException e) {
                        //Toast.makeText(.m, "", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            });
            thread.start();

            //OkHttpPost httpPostObj = new OkHttpPost();
            //httpPostObj.Post("https://smsparserapi.azurewebsites.net/trading",SmsModel.toString());
           /* httpbinEndpoint = new URL("https://smsparserapi.azurewebsites.net/trading");
            HttpsURLConnection myConnection = (HttpsURLConnection) httpbinEndpoint.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            JSONObject SmsModel = new JSONObject();
            SmsModel.put("Message",message);
            DataOutputStream localDataOutputStream = new DataOutputStream(myConnection.getOutputStream());
            localDataOutputStream.writeBytes(SmsModel.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();*/

        } catch ( JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void postRequest() throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "https://cakeapi.trinitytuts.com/api/add";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", "aneh");
            postdata.put("password", "12345");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e(TAG, mMessage);
            }
        });
    }*/

}

