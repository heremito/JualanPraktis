package www.starcom.com.jualanpraktis.feature.pembayaran;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.databinding.ActivityPembayaranBinding;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.ResultTransferActivity;

public class PembayaranProccess {
   private ActivityPembayaranBinding binding;
    private Activity activity;
    private ProgressDialog progressDialog;
    public ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    String baseUrl = "https://my.ipaymu.com/api/";
    String baseUrlToken = "https://my.ipaymu.com/";
    String key = "6vin5Or.ljjipPFMM3UTDGzYYHdLf0";

    String notifyUrl = "https://jualanpraktis.net/notify.php";
    loginuser user ;
    String id_transaksi,total,timestamp,no_hp;

    String trx_id,session_id;
    OkHttpClient okHttpClient;

    public PembayaranProccess(ActivityPembayaranBinding binding, Activity activity){
        this.binding  = binding;
        this.activity = activity;
        AndroidNetworking.initialize(activity);
        user = SharedPrefManager.getInstance(activity).getUser();
        progressDialog = new ProgressDialog(activity);
        Long tsLong = System.currentTimeMillis()/1000;
         timestamp = tsLong.toString();




    }

    public void dialog(int code){
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_payment, null);
        final LinearLayout ll_va = layoutView.findViewById(R.id.ll_va);
        final LinearLayout ll_cs_store = layoutView.findViewById(R.id.ll_cs_store);

        final ConstraintLayout pay_mandiri = layoutView.findViewById(R.id.pay_mandiri);
        final ConstraintLayout pay_bni = layoutView.findViewById(R.id.pay_bni);
        final ConstraintLayout pay_cimbniaga = layoutView.findViewById(R.id.pay_cimbniaga);
        final ConstraintLayout pay_bca = layoutView.findViewById(R.id.pay_bca);
        final ConstraintLayout pay_alfamart = layoutView.findViewById(R.id.pay_alfamart);
        final ConstraintLayout pay_indomaret = layoutView.findViewById(R.id.pay_indomaret);

        if (code==0){
            ll_va.setVisibility(View.VISIBLE);
            ll_cs_store.setVisibility(View.GONE);
        }else if (code==1){
            ll_va.setVisibility(View.GONE);
            ll_cs_store.setVisibility(View.VISIBLE);
        }

        onClick(pay_mandiri,0);
        onClick(pay_bni,1);
        onClick(pay_cimbniaga,2);
        onClick(pay_bca,3);
        onClick(pay_alfamart,4);
        onClick(pay_indomaret,5);

        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void onClick(ConstraintLayout constraintLayout, final int code){
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 = mandiri; 1 = pay_bni; 2 = pay_cimbniaga; 3 = pay_bca;
                // 4 = alfamart; 5 = indomart;
              //  activity.startActivity(new Intent(activity, ResultPembayaranActivity.class));
                if (code==0){
                    alertDialog("Virtual Account Mandiri",code);
                }else if (code==1){
                    alertDialog("Virtual Account BNI",code);
                }else if (code==2){
                    alertDialog("Virtual Account CIMB Niaga",code);
                }else if (code==3){
                    alertDialog("Transfer BCA",code);
                }else if (code==4){
                    alertDialog("Alfamart",code);
                }else if (code==5){
                    alertDialog("Indomart",code);
                }


            }
        });
    }





    /**Proses pembayaran melalui Virtual Account**/
    private void processVA(final String endUrl,final String channel,final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("price",total);
        param.put("uniqid",id_transaksi);
        param.put("notifyUrl",notifyUrl);
        param.put("customer_name",user.getNama());
        param.put("customer_phone",no_hp);
        param.put("customer_email",user.getEmail());
        param.put("referenceId",id_transaksi);
        param.put("expired","2");

        AndroidNetworking.post(baseUrl+endUrl)
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String va = "";
                        String displayName = "";
                        try {
                             va = response.getString("va");
                             displayName = response.getString("displayName");
                             trx_id = response.getString("id");
                             session_id = "";


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Intent intent = new Intent(activity,ResultPembayaranActivity.class);

                        intent.putExtra("trx_id",trx_id);
                        intent.putExtra("session_id",session_id);
                        intent.putExtra("id_transaksi",id_transaksi);

                        intent.putExtra("nomor_va",va);
                        intent.putExtra("total",total);
                        intent.putExtra("nama_va",displayName);
                        intent.putExtra("pay_method",pay_method);
                        intent.putExtra("channel",channel);

                        activity.startActivity(intent);
                        activity.finish();

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });

    }

    /**Proses pembayaran melalui Transfer**/
    private void proccessTransfer(final String endUrl,final String channel,final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("amount",total);
        param.put("uniqid",timestamp);
        param.put("notifyUrl",notifyUrl);
        param.put("name",user.getNama());
        param.put("phone",no_hp);
        param.put("email",user.getEmail());
        param.put("referenceId",id_transaksi);

        AndroidNetworking.post(baseUrl+endUrl)
                .addBodyParameter(param)
                .setTag("")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String PaymentNo = "";
                        String PaymentName = "";
                        String Total = "";
                        String Expired = "";
                        String Fee = "";
                        String SubTotal = "";
                        try {
                            JSONObject data = response.getJSONObject("Data");
                             PaymentNo = data.getString("PaymentNo");
                             PaymentName = data.getString("PaymentName");
                             SubTotal = data.getString("SubTotal");
                             Total = data.getString("Total");
                             Expired = data.getString("Expired");
                             Fee =data.getString("Fee");
                            trx_id = data.getString("TransactionId");
                            session_id = "";


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Intent intent = new Intent(activity,ResultPembayaranActivity.class);

                        intent.putExtra("pay_method",pay_method);
                        intent.putExtra("channel",channel);
                        intent.putExtra("id_transaksi",id_transaksi);
                        intent.putExtra("trx_id",trx_id);
                        intent.putExtra("session_id",session_id);

                        intent.putExtra("PaymentNo",PaymentNo);
                        intent.putExtra("PaymentName",PaymentName);
                        intent.putExtra("total",Total);
                        intent.putExtra("Expired",Expired);
                        intent.putExtra("Fee",Fee);

                        activity.startActivity(intent);
                        activity.finish();

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });

    }

    /**Proses pembayaran melalui CS Store**/
    private void getSesionID(final String channel,final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("pay_method","cstore");
        param.put("ureturn","https://www.ipaymu.com");
        param.put("unotify",notifyUrl);
        param.put("ucancel","https://www.ipaymu.com/cancel");
        int i = 0;
        for (HashMap<String,String> item : dataList){
            param.put("product["+i+"]",item.get("nama"));
            param.put("quantity["+i+"]",item.get("jumlah"));
            param.put("price["+i+"]",item.get("harga"));

            i++;
        }

        String url = "https://my.ipaymu.com/payment?format=json";
        AndroidNetworking.post(url)
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String sesionId = "";
                        try {
                             sesionId = response.getString("sessionID");
                            pembayaranStore(sesionId,channel,pay_method);
                           // String url = response.getString("url");

                        } catch (JSONException e) {
                           // e.printStackTrace();
                            Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });

    }
    private void pembayaranStore(final String sesionID, final String chanel, final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("sessionID",sesionID);
        param.put("channel",chanel);
        param.put("name",user.getNama());
        param.put("email",user.getEmail());
        param.put("phone",no_hp);
        param.put("active","2");
        AndroidNetworking.post("https://my.ipaymu.com/api/payment/cstore")
                .addBodyParameter(param)
                .setTag("")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            String kode_pembayaran = response.getString("kode_pembayaran");
                            String expired = response.getString("expired");
                            trx_id = response.getString("trx_id");
                            session_id = response.getString("sessionID");

                            progressDialog.dismiss();
                            Intent intent = new Intent(activity,ResultPembayaranActivity.class);
                            intent.putExtra("pay_method",pay_method);
                            intent.putExtra("trx_id",trx_id);
                            intent.putExtra("session_id",session_id);
                            intent.putExtra("id_transaksi",id_transaksi);

                            intent.putExtra("kode_pembayaran",kode_pembayaran);
                            intent.putExtra("expired",expired);
                            intent.putExtra("channel",chanel);
                            intent.putExtra("total",total);

                            activity.startActivity(intent);
                            activity.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });


    }

    /**Proses pembayaran melalui Qris**/
    private void pembayaranQris(final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("name",user.getNama());
        param.put("phone",no_hp);
        param.put("email",user.getEmail());
        param.put("amount",total);
        param.put("referenceId",id_transaksi);
        param.put("notify_url",notifyUrl);
        param.put("city","Batam");
        param.put("zipCode","29453");

        AndroidNetworking.post(baseUrl+"payment/qris")
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String QrString = "";
                        String PaymentName = "";
                        String Fee = "";
                        String Via = "";
                        String Channel = "";
                        String Expired = "";
                        try {
                            JSONObject object = response.getJSONObject("Data");
                            QrString = object.getString("QrString");
                            PaymentName = object.getString("PaymentName");
                            Fee = object.getString("Fee");
                            Via = object.getString("Via");
                            Channel = object.getString("Channel");
                            Expired = object.getString("Expired");
                            trx_id = object.getString("TransactionId");
                            session_id = object.getString("SessionId");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Intent intent = new Intent(activity,ResultPembayaranActivity.class);

                        intent.putExtra("total",total);
                        intent.putExtra("pay_method",pay_method);
                        intent.putExtra("trx_id",trx_id);
                        intent.putExtra("session_id",session_id);
                        intent.putExtra("id_transaksi",id_transaksi);

                        intent.putExtra("QrString",QrString);
                        intent.putExtra("PaymentName",PaymentName);
                        intent.putExtra("Fee",Fee);
                        intent.putExtra("Expired",Expired);

                        activity.startActivity(intent);
                        activity.finish();

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });
    }

    /**Proses pembayaran akulaku**/
    private void pembayaranPaylater(final String pay_method){
        progressDialog.setTitle( "Memproses Pembayaran");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("name",user.getNama());
        param.put("phone",no_hp);
        param.put("email",user.getEmail());
        param.put("amount",total);
        param.put("referenceId",id_transaksi);
        param.put("notifyUrl",notifyUrl);

        AndroidNetworking.post(baseUrl + "payment/akulaku")
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        String Fee = "";
                        String Via = "";
                        String Channel = "";
                        String Expired = "";
                        String PaymentName = "";
                        String Url = "";
                        try {
                            JSONObject object = response.getJSONObject("Data");
                            Fee = object.getString("Fee");
                            Via = object.getString("Via");
                            Channel = object.getString("Channel");
                            Expired = object.getString("Expired");
                            PaymentName = object.getString("PaymentName");
                            Url = object.getString("Url");
                            trx_id = object.getString("TransactionId");
                            session_id = object.getString("SessionId");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Intent intent = new Intent(activity,ResultPembayaranActivity.class);

                        intent.putExtra("total",total);
                        intent.putExtra("pay_method",pay_method);
                        intent.putExtra("trx_id",trx_id);
                        intent.putExtra("session_id",session_id);
                        intent.putExtra("id_transaksi",id_transaksi);

                        intent.putExtra("Url",Url);
                        intent.putExtra("PaymentName",PaymentName);
                        intent.putExtra("Fee",Fee);
                        intent.putExtra("Expired",Expired);



                        activity.startActivity(intent);
                        activity.finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void ipaymuV2(final String channel,final String pay_method){
        HashMap<String,String> param = new HashMap<>();
        param.put("key",key);
        param.put("name",user.getNama());
        param.put("phone",user.getNo_hp());
        param.put("email",user.getEmail());
        param.put("amount",total);
        param.put("referenceId",id_transaksi);
        param.put("notifyUrl","https://www.ipaymu.com");
        param.put("city","Batam");
        param.put("zipCode","29453");



        AndroidNetworking.post(baseUrl+"payment/qris")
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String QrString = "";
                        String PaymentName = "";
                        String Fee = "";
                        String Via = "";
                        String Channel = "";
                        String Expired = "";
                        try {
                            JSONObject object = response.getJSONObject("Data");
                            QrString = object.getString("QrString");
                            PaymentName = object.getString("PaymentName");
                            Fee = object.getString("Fee");
                            Via = object.getString("Via");
                            Channel = object.getString("Channel");
                            Expired = object.getString("Expired");
                            trx_id = object.getString("TransactionId");
                            session_id = object.getString("SessionId");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Intent intent = new Intent(activity,ResultPembayaranActivity.class);

                        intent.putExtra("total",total);
                        intent.putExtra("pay_method",pay_method);

                        intent.putExtra("QrString",QrString);
                        intent.putExtra("PaymentName",PaymentName);
                        intent.putExtra("Fee",Fee);
                        intent.putExtra("Expired",Expired);

                        activity.startActivity(intent);
                        activity.finish();

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });
    }

    public void alertDialog(String messageKey,final int code){
        new AlertDialog.Builder(activity)
                .setTitle("Konfirmasi Pembayaran")
                .setMessage("Anda yakin ingin melakukan pembayaran melalui " + messageKey+"?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        // 0 = mandiri; 1 = pay_bni; 2 = pay_cimbniaga; 3 = pay_bca;
                        // 4 = alfamart; 5 = indomart;
                        // 6 = qris; 7 = payLater;
                        if (code==0){
                            processVA("getmandiriva","mandiri","va");
                        }else if (code==1){
                            processVA("getbniva","bni","va");
                        }else if (code==2){
                            processVA("getva","cimbniaga","va");
                        }else if (code==3){
                            proccessTransfer("bcatransfer","bca","transfer");
                        }else if (code==4){
                            getSesionID("alfamart","cstore");
                        }else if (code==5){
                            getSesionID("indomaret","cstore");
                        }else if (code==6){
                            pembayaranQris("qris");
                        } else if (code==7){
                            pembayaranPaylater("paylater");
                        }else if (code==8){
                            activity.startActivity(new Intent(activity, ResultTransferActivity.class).putExtra("id_transaksi",id_transaksi)
                                    .putExtra("total",total));
                            activity.finish();
                        }

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_menu_info_details)
                .show();
    }
}
