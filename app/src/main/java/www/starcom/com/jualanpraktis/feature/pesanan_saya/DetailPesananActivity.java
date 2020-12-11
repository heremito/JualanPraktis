package www.starcom.com.jualanpraktis.feature.pesanan_saya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityDetailPesananBinding;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.resi_pengiriman.DetailResiPengirimanActivity;

public class DetailPesananActivity extends AppCompatActivity {

    ActivityDetailPesananBinding binding;
    Activity activity = DetailPesananActivity.this;
    String id_pesanan;
    ArrayList<HashMap<String,String>> cartList = new ArrayList<>();
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_detail_pesanan);
        AndroidNetworking.initialize(getApplicationContext());
        pref = new Pref(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            id_pesanan = bundle.getString("id_pesanan");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(id_pesanan);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getData(){
        binding.layout.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        AndroidNetworking.post("https://jualanpraktis.net/android/detail_pesanan.php")
                .addBodyParameter("id_transaksi",id_pesanan)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cartList.clear();
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.layout.setVisibility(View.VISIBLE);
                        try {
                            JSONArray listproduk = response.getJSONArray("data");
                            JSONArray transaksi = response.getJSONArray("data2");
                            for (int i = 0; i<listproduk.length(); i++){
                                JSONObject object = listproduk.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                item.put("nomor",object.getString("nomor"));
                                item.put("nama",object.getString("nama_produk"));
                                item.put("variasi",object.getString("ket2"));
                                item.put("gambar",object.getString("image_o"));
                                item.put("harga",object.getString("harga_item"));
                                item.put("jumlah",object.getString("jumlah"));
                                item.put("berat",object.getString("berat"));
                                cartList.add(item);
                            }
                            CartAdapter adapter = new CartAdapter(activity,cartList, null);
                            binding.recyclerView.setAdapter(adapter);

                            final JSONObject detail = transaksi.getJSONObject(0);
                            String alamat_lengkap = detail.getString("alamat")+" Kecamatan "+ detail.getString("subdistrict_name")
                                    +", Kota/Kabupaten "+detail.getString("city_name")+", Provinsi "+detail.getString("province")
                                    +", " +detail.getString("namaNegara");
                            binding.lblNama.setText(detail.getString("nama_penerima"));
                            binding.lblAlamat.setText(alamat_lengkap);

                            if (!pref.getLoginMethod().equals("coorperate")){
                                if (detail.getString("id_opsi_bayar").equals("transfer")){
                                    if (detail.getString("status_verivikasi").equals("null")){
                                        binding.lblNomorResi.setText("Pending");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    }else {
                                        if (detail.getString("no_resi").equals("")||detail.getString("no_resi").equals("null")){
                                            binding.lblNomorResi.setText("Diproses");
                                            binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                                        } else {

                                            binding.lblNomorResi.setText(detail.getString("no_resi"));
                                            binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                            binding.btnLacak.setVisibility(View.VISIBLE);

                                            final String no_resi = detail.getString("no_resi");
                                            final String kurir = detail.getString("kurir");
                                            binding.btnLacak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(activity, DetailResiPengirimanActivity.class);
                                                    intent.putExtra("no_resi",no_resi);
                                                    intent.putExtra("kode_kurir",kurir);
                                                    activity.startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }else {
                                    if (detail.getString("status_kirim").equals("")||detail.getString("status_kirim").equals("null")){
                                        binding.lblNomorResi.setText("Pending");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    }else if (detail.getString("status_kirim").equals("0")){

                                        binding.lblNomorResi.setText("Dalam Pengantaran");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                                    }else if (detail.getString("status_kirim").equals("1")){

                                        binding.lblNomorResi.setText("Sudah diterima");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                    }

                                }
                            }else {
                                if (detail.getString("id_opsi_bayar").equals("transfer")){
                                    if (detail.getString("status_verivikasi").equals("null")){
                                        binding.lblNomorResi.setText("Pending");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    }else {
                                        if (detail.getString("no_resi").equals("")||detail.getString("no_resi").equals("null")){
                                            binding.lblNomorResi.setText("Diproses");
                                            binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                                        } else {

                                            binding.lblNomorResi.setText(detail.getString("no_resi"));
                                            binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                            binding.btnLacak.setVisibility(View.VISIBLE);

                                            final String no_resi = detail.getString("no_resi");
                                            final String kurir = detail.getString("kurir");
                                            binding.btnLacak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(activity, DetailResiPengirimanActivity.class);
                                                    intent.putExtra("no_resi",no_resi);
                                                    intent.putExtra("kode_kurir",kurir);
                                                    activity.startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }else {
                                    if (detail.getString("status_kirim").equals("")||detail.getString("status_kirim").equals("null")){
                                        binding.lblNomorResi.setText("Pending");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.darker_gray));
                                    }else if (detail.getString("status_kirim").equals("0")){

                                        binding.lblNomorResi.setText("Dalam Pengantaran");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                                    }else if (detail.getString("status_kirim").equals("1")){

                                        binding.lblNomorResi.setText("Sudah diterima");
                                        binding.lblNomorResi.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                    }
                                }

                            }


                            binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(detail.getString("total_belanja"))));
                            binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(detail.getString("ongkos_kirim"))));
                            binding.lblBerat.setText(detail.getString("total_berat") +" gram");
                            binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(detail.getString("total_bayar"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(activity, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(activity, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(activity, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
