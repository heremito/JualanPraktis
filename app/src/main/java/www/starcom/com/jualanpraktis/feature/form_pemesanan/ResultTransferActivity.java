package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.databinding.ActivityResultTransferBinding;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.utils.CustomProgressDialog;

public class ResultTransferActivity extends AppCompatActivity {

    Activity activity = ResultTransferActivity.this;
    ActivityResultTransferBinding binding;

    loginuser user ;
    String id_transkasi,berat,nominal_belanja,ongkir,totalbayar,nohp;
    CustomProgressDialog progressDialog;
    String filePath,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(activity,R.layout.activity_result_transfer);

       Bundle bundle = getIntent().getExtras();
       if (bundle!=null){
           id_transkasi = bundle.getString("id_transaksi");
           totalbayar = bundle.getString("total");
           status = bundle.getString("status");
       }

        user = SharedPrefManager.getInstance(this).getUser();
        progressDialog = new CustomProgressDialog(activity);
        if (status.equals("result")){

            binding.txtResult.setVisibility(View.VISIBLE);
            binding.btnSelesai.setVisibility(View.VISIBLE);
            binding.btnKonfirmasi.setVisibility(View.GONE);
            binding.llTransfer.setVisibility(View.GONE);
        }else {

            binding.btnSelesai.setVisibility(View.GONE);
            binding.txtResult.setVisibility(View.GONE);
            binding.btnKonfirmasi.setVisibility(View.VISIBLE);
            binding.llTransfer.setVisibility(View.VISIBLE);
        }

        binding.nominalBayar.setText(FormatText.rupiahFormat(Double.parseDouble(totalbayar)));

       klik();
    }



    void klik(){
        binding.btnSalinNominal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(totalbayar, totalbayar);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity,"Berhasil menyalin nominal",Toast.LENGTH_SHORT).show();

            }
        });
        binding.btnSalinRek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("37001040000385","37001040000385");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity,"Berhasil menyalin rekening",Toast.LENGTH_SHORT).show();
            }
        });
        binding.imgBuktiTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(activity)
                        .crop(280,280)
                        .compress(512)
                        .start();
            }
        });
        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath!=null){
                    konfirmasiTransfer();
                }else {
                    Toast.makeText(activity,"Masukan bukti transfer",Toast.LENGTH_SHORT).show();
                }
                //startActivity(new Intent(activity, KonfirmasiTransferActivity.class));
            }
        });

        binding.btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("tab",2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Uri uri = data.getData();

            binding.imgBuktiTransfer.setImageURI(uri);

            filePath = uri.getPath();
        }
    }

    private void konfirmasiTransfer(){


        progressDialog.progress("Konfirmasi Transfer","Loading...");

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDateTime = currentDate +" "+ currentTime;

        HashMap<String,String> param = new HashMap<>();
        param.put("id_customer",user.getId());
        param.put("id_transaksi",id_transkasi);
        param.put("jumlah_bayar",totalbayar);
        param.put("tgl_bayar",currentDateTime);
        param.put("tgl_konfirmasi",currentDateTime);
        param.put("tujuan_pembayaran","transfer manual");
        param.put("user_konfirmasi",user.getNama());
        param.put("status","Pending");
        param.put("status_konfirmasi","1");

        AndroidNetworking.upload("https://jualanpraktis.net/android/konfirmasi-transfer.php")
                .addMultipartParameter(param)
                .addMultipartFile("foto",new File(filePath))
                .setTag(activity)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("Data Berhasil Di Kirim")){
                            // progressDialog.dismiss();
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                            Toast.makeText(activity,"Berhasil Konfirmasi Transfer",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(activity,"Gagal Konfirmasi Transfer",Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if (status.equals("result")){
            Toast.makeText(activity,"Tidak bisa kembali harap selesaikan pesanan",Toast.LENGTH_SHORT).show();
        }else {super.onBackPressed();}
    }
}