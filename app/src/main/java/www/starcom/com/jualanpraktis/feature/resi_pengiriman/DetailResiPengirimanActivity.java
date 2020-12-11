package www.starcom.com.jualanpraktis.feature.resi_pengiriman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
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

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.LacakAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityDetailResiPengirimanBinding;

public class DetailResiPengirimanActivity extends AppCompatActivity {

    ActivityDetailResiPengirimanBinding binding;
    Activity activity = DetailResiPengirimanActivity.this;
    String no_resi,kode_kurir;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();

    String noResi,status,kurir,layanan,tanggalKirim,berat,pengirim,asal,penerima,alamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_detail_resi_pengiriman);
        AndroidNetworking.initialize(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            no_resi = bundle.getString("no_resi");
            kode_kurir = bundle.getString("kode_kurir");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(no_resi);


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
        binding.nested.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        AndroidNetworking.post("https://pro.rajaongkir.com/api/waybill")
                .addHeaders("key","9bc530edfde2ffdba603d779832349cf")
                .addBodyParameter("waybill",no_resi)
                .addBodyParameter("courier",kode_kurir)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rajaongkir = response.getJSONObject("rajaongkir");

                            JSONObject query = rajaongkir.getJSONObject("query");
                            JSONObject result = rajaongkir.getJSONObject("result");
                            JSONObject summary = result.getJSONObject("summary");
                            JSONObject details = result.getJSONObject("details");
                            JSONObject delivery_status = result.getJSONObject("delivery_status");

                            noResi = query.getString("waybill");
                            status = delivery_status.getString("status");
                            kurir = summary.getString("courier_name");
                            layanan = summary.getString("service_code");
                            tanggalKirim = details.getString("waybill_date")+" "+ details.getString("waybill_time");
                            berat = details.getString("weight") + " KG";
                            pengirim = details.getString("shippper_name");
                            asal = details.getString("shipper_address1");
                            penerima = details.getString("receiver_name");
                            alamat = details.getString("receiver_address1") +" "+details.getString("receiver_address2") +" "+details.getString("receiver_address3") +"\n"
                                    +details.getString("receiver_city");

                            dataList.clear();
                            JSONArray manifest = result.getJSONArray("manifest");
                            for (int i = 0; i<manifest.length();i++){
                                JSONObject listObject = manifest.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                item.put("status",listObject.getString("manifest_description"));
                                item.put("tanggal",listObject.getString("manifest_date"));
                                item.put("jam",listObject.getString("manifest_time"));
                                dataList.add(item);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.nested.setVisibility(View.VISIBLE);
                        //DATA DETAIL
                        binding.lblNomorResi.setText(noResi);
                        binding.lblStatus.setText(status);
                        binding.lblKurir.setText(kurir);
                        binding.lblLayanan.setText(layanan);
                        binding.lblTanggalKirim.setText(tanggalKirim);
                        binding.lblBerat.setText(berat);
                        binding.lblPengirim.setText(pengirim);
                        binding.lblAsal.setText(asal);
                        binding.lblNamaPenerima.setText(penerima);
                        binding.lblAlamat.setText(alamat);

                        //LIST LACAK
                        LacakAdapter adapter = new LacakAdapter(activity,dataList);
                        binding.recyclerView.setAdapter(adapter);
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
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(activity, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
