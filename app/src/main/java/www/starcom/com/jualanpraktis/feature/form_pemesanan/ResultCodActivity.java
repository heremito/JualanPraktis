package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityResultCodBinding;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;

public class ResultCodActivity extends AppCompatActivity {

    ActivityResultCodBinding binding;
    Activity activity = ResultCodActivity.this;

    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    loginuser user ;
    String id_transkasi,berat,nominal_belanja,ongkir,totalbayar,nohp;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_result_cod);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_result_cod);
        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(activity).getUser();
        progressDialog = new ProgressDialog(activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            id_transkasi = bundle.getString("id_transaksi");
            binding.lblNama.setText(bundle.getString("nama"));
            binding.lblAlamat.setText(bundle.getString("alamat"));
            binding.lblNoTelpon.setText(bundle.getString("no_hp"));
            berat = bundle.getString("berat");
            nominal_belanja = bundle.getString("nominal_belanja");
            ongkir = bundle.getString("ongkos_kirim");
            totalbayar = bundle.getString("total");

            dataList = (ArrayList<HashMap<String, String>>) bundle.getSerializable("dataList");
        }


        binding.lblBerat.setText(berat+" Gram");
        binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(nominal_belanja)));
        binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(ongkir)));
        binding.lblTotalBayar.setText(FormatText.rupiahFormat(Double.parseDouble(totalbayar)));

        //list pesanan saya
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);


        CartAdapter adapter = new CartAdapter(activity,dataList,null);
        binding.recyclerView.setAdapter(adapter);

        binding.btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Database(getBaseContext()).cleanAllChart();
                Shared.getInstance(getApplicationContext()).logout();

                konfirmasiTransaksi();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(activity,"Tidak bisa kembali, harap selesaikan transaksi",Toast.LENGTH_LONG).show();
    }

    private void konfirmasiTransaksi(){


        progressDialog.setTitle("Konfirmasi Transaksi");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDateTime = currentDate +" "+ currentTime;

        HashMap<String,String> param = new HashMap<>();
        param.put("id_customer",user.getId());
        param.put("id_transaksi",id_transkasi);
        param.put("jumlah_bayar",totalbayar);
        param.put("tgl_bayar",currentDateTime);
        param.put("tgl_konfirmasi",currentDateTime);
        param.put("tujuan_pembayaran","cod");
        param.put("user_konfirmasi",user.getNama());
        param.put("status","Pending");
        param.put("status_konfirmasi","1");

        AndroidNetworking.post("https://jualanpraktis.net/android/konfirmasi.php")
                .addBodyParameter(param)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
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
