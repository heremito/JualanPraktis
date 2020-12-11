package www.starcom.com.jualanpraktis.feature.pembayaran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.databinding.ActivityResultPembayaranBinding;

public class ResultPembayaranActivity extends AppCompatActivity {

    ActivityResultPembayaranBinding binding;
    Activity activity = ResultPembayaranActivity.this;
    String pay_method,trx_id,session_id,total,id_transaksi;
    ProgressDialog progressDialog;
    loginuser user ;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_result_pembayaran);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_result_pembayaran);
        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(activity).getUser();
        progressDialog = new ProgressDialog(ResultPembayaranActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            pay_method = bundle.getString("pay_method");
            trx_id = bundle.getString("trx_id");
            session_id = bundle.getString("session_id");
            total = bundle.getString("total");
            id_transaksi = bundle.getString("id_transaksi");

            String channel = bundle.getString("channel");
            if (pay_method.equals("cstore")){
                binding.llCsStore.setVisibility(View.VISIBLE);
                binding.llVa.setVisibility(View.GONE);
                binding.llQris.setVisibility(View.GONE);
                binding.llPaylater.setVisibility(View.GONE);
                binding.llTransfer.setVisibility(View.GONE);
                String kode_pembayaran = bundle.getString("kode_pembayaran");
                String expired = bundle.getString("expired");

                binding.lblKodePembayaran.setText(kode_pembayaran);
                binding.tanggalKadaluwarsa.setText(expired);
                binding.nominalBayar.setText(FormatText.rupiahFormat(Double.parseDouble(total)));
                if (channel.equals("indomaret")){
                    binding.imgBayar.setImageDrawable(getResources().getDrawable(R.drawable.indomaret));
                    binding.imgBiayaAdmin.setImageDrawable(getResources().getDrawable(R.drawable.indomaret));
                }else if (channel.equals("alfamart")){
                    binding.imgBayar.setImageDrawable(getResources().getDrawable(R.drawable.afamart));
                    binding.imgBiayaAdmin.setImageDrawable(getResources().getDrawable(R.drawable.afamart));
                }

            }else if (pay_method.equals("va")){
                binding.llVa.setVisibility(View.VISIBLE);
                binding.llCsStore.setVisibility(View.GONE);
                binding.llQris.setVisibility(View.GONE);
                binding.llPaylater.setVisibility(View.GONE);
                binding.llTransfer.setVisibility(View.GONE);

                String nomor_va = bundle.getString("nomor_va");
                String nama_va = bundle.getString("nama_va");

                binding.lblNomorVa.setText(nomor_va);
                binding.lblNamaVa.setText(nama_va);
                binding.lblNominalVa.setText(FormatText.rupiahFormat(Double.parseDouble(total)));
                if (channel.equals("mandiri")){
                    binding.imgVa.setImageDrawable(getResources().getDrawable(R.drawable.mandiri));
                }else if (channel.equals("bni")){
                    binding.imgVa.setImageDrawable(getResources().getDrawable(R.drawable.bni));
                }else if (channel.equals("cimbniaga")){
                    binding.imgVa.setImageDrawable(getResources().getDrawable(R.drawable.cimb_niaga));
                }

            }else if (pay_method.equals("transfer")){
                binding.llPaylater.setVisibility(View.GONE);
                binding.llQris.setVisibility(View.GONE);
                binding.llCsStore.setVisibility(View.GONE);
                binding.llVa.setVisibility(View.GONE);
                binding.llTransfer.setVisibility(View.VISIBLE);

                String PaymentNo = bundle.getString("PaymentNo");
                String PaymentName = bundle.getString("PaymentName");
                String SubTotal = bundle.getString("SubTotal");
                String Expired = bundle.getString("Expired");
                String Fee =bundle.getString("Fee");

                binding.lblNomorTransfer.setText(PaymentNo+" - "+PaymentName);
                binding.lblFeeTransferr.setText(FormatText.rupiahFormat(Double.parseDouble(Fee)));
                binding.lblNamaTransfer.setText(PaymentName);
                binding.lblNominalTransfer.setText(FormatText.rupiahFormat(Double.parseDouble(total)));
                binding.tanggalKadaluwarsaTransfer.setText(Expired);


            }else if (pay_method.equals("qris")){
                binding.llQris.setVisibility(View.VISIBLE);
                binding.llCsStore.setVisibility(View.GONE);
                binding.llVa.setVisibility(View.GONE);
                binding.llPaylater.setVisibility(View.GONE);
                binding.llTransfer.setVisibility(View.GONE);

                String QrString = bundle.getString("QrString");
                String PaymentName = bundle.getString("PaymentName");
                String Fee = bundle.getString("Fee");
                String Expired = bundle.getString("Expired");

                //encode to Qr Code
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(QrString, BarcodeFormat.QR_CODE,250,250);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    binding.imgQrcodeQris.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                binding.lblFeeQris.setText("+"+FormatText.rupiahFormat(Double.parseDouble(Fee)));
                binding.lblNamaQris.setText(PaymentName);
                binding.nominalBayarQris.setText(FormatText.rupiahFormat(Double.parseDouble(total)));
                binding.tanggalKadaluwarsaQris.setText(Expired);


            }else if (pay_method.equals("paylater")){
                binding.llPaylater.setVisibility(View.VISIBLE);
                binding.llQris.setVisibility(View.GONE);
                binding.llCsStore.setVisibility(View.GONE);
                binding.llVa.setVisibility(View.GONE);
                binding.llTransfer.setVisibility(View.GONE);

                String Url = bundle.getString("Url");
                String PaymentName = bundle.getString("PaymentName");
                String Fee = bundle.getString("Fee");
                String Expired = bundle.getString("Expired");

                binding.lblUrlPaylater.setText(Url);
                binding.lblFeePaylater.setText("+"+FormatText.rupiahFormat(Double.parseDouble(Fee)));
                binding.lblNamaPaylater.setText(PaymentName);
                binding.nominalBayarPaylater.setText(FormatText.rupiahFormat(Double.parseDouble(total)));
                binding.tanggalKadaluwarsaPaylater.setText(Expired);

            }

        }

        onClick(binding.btnTakeSc,0);
        onClick(binding.btnSelesai,1);
    }

    private void onClick(Button button, final int code){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code==0){
                    takeScreenshot();
                }else if (code==1){

                    new Database(getBaseContext()).cleanAllChart();
                    Shared.getInstance(getApplicationContext()).logout();
                    konfimasiTransaksi();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(activity,"Tidak bisa kembali, harap selesaikan transaksi",Toast.LENGTH_LONG).show();
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        Long tsLong = System.currentTimeMillis()/1000;
        String  timestamp = tsLong.toString();
        String name = "IMG_Batammall_"+timestamp;


        try {
            // image naming and path  to include sd card  appending name you choose for file
            File file = new File(Environment.getExternalStorageDirectory().toString(),"BatamMall");
            if(!file.exists()){
                file.mkdir();
            }
            String mPath = Environment.getExternalStorageDirectory().toString()+"/BatamMall" + "/" + name + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(activity,"Berhasil mengambil screenshoot",Toast.LENGTH_SHORT).show();
         //   openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void konfimasiTransaksi(){
        progressDialog.setTitle("Konfirmasi Transaksi");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDateTime = currentDate +" "+ currentTime;


        HashMap<String,String> param = new HashMap<>();
        param.put("id_customer",user.getId());
        param.put("id_transaksi",id_transaksi);
        param.put("jumlah_bayar",total);
        param.put("tgl_bayar",currentDateTime);
        param.put("tgl_konfirmasi",currentDateTime);
        param.put("tujuan_pembayaran",pay_method);
        param.put("status","Pending");
        param.put("user_konfirmasi",user.getNama());
        param.put("status_konfirmasi","1");
        param.put("trx_id",trx_id);
        param.put("session_id",session_id);

        AndroidNetworking.post("https://jualanpraktis.net/android/konfirmasi.php")
                .addBodyParameter(param)
                .setTag(ResultPembayaranActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Data Berhasil Di Kirim")){
                            progressDialog.dismiss();
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                            Toast.makeText(activity,"Transaksi Selesai",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });

    }





}
