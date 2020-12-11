package www.starcom.com.jualanpraktis.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;


public class ServiceTask extends Service  {


    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
       // displayNotification(this,"test service");
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("SHE Inspection")
                    .setContentText("Service Running").build();

            startForeground(1, notification);
        }

        Bundle bundle = intent.getExtras();
        String backgroundProcess;
        if (bundle!=null){
            backgroundProcess = bundle.getString("proses");

            if (backgroundProcess.equals("home")){
                cekTransaksi();
            }else if (backgroundProcess.equals("form_transaksi_back")){
                dataList = (ArrayList<HashMap<String, String>>) bundle.getSerializable("dataList");
                back();
            }
        }


        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    //
    void cekTransaksi(){
        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.post("https://jualanpraktis.net/android/cek_transaksi.php")
                .addBodyParameter("status","1")
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void back(){

        HashMap<String,String> param = new HashMap<>();
        // param.put("customer", user.getId());

        int i = 0;
        for (HashMap<String, String> data : dataList){
            param.put("jumlah["+i+"]", data.get("jumlah"));
            param.put("ket1["+i+"]", data.get("id_variasi"));
            i++;
        }

        AndroidNetworking.post("https://jualanpraktis.net/android/pesan-update.php")
                .addBodyParameter(param)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // progressDialog.dismiss();
                       // finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        // progressDialog.dismiss();

                        back();

                    }
                });

    }




    //buat cek hasilnya
    private void displayNotification(String latitude, String longitude) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Lokasi Terkini");
        // bigText.setBigContentTitle("1");
        bigText.setBigContentTitle(latitude+" "+longitude);
        bigText.setSummaryText("Text in detail");

        //mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_logo_hitam);
        // mBuilder.setContentTitle("2");
        mBuilder.setContentTitle(latitude+longitude);
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "epatrol";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

}
