package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
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
import www.starcom.com.jualanpraktis.databinding.ActivityPilihLayananEkspedisiBinding;

public class PilihLayananEkspedisiActivity extends AppCompatActivity {
    Activity activity = PilihLayananEkspedisiActivity.this;
    ActivityPilihLayananEkspedisiBinding binding;
    HashMap<String,String> param = new HashMap<>();
    ArrayList<HashMap<String,String>> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_pilih_layanan_ekspedisi);
        AndroidNetworking.initialize(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            param = (HashMap<String, String>) bundle.getSerializable("param");
            }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Pilih Ekspedisi");

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setHasFixedSize(false);

        getData(param);

    }

    private void getData(HashMap<String,String> param){

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        AndroidNetworking.post("https://pro.rajaongkir.com/api/cost")
                .addHeaders("key","9bc530edfde2ffdba603d779832349cf")
                .addBodyParameter(param)
                .setTag(PilihLayananEkspedisiActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {

                            //get main result
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            //get list layanan
                            JSONObject object1 = array.getJSONObject(0);
                            JSONArray array1 = object1.getJSONArray("costs");
                            for (int i = 0; i < array1.length(); i++){
                                JSONObject obj = array1.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                item.put("nama",obj.getString("service"));
                                item.put("deskripsi",obj.getString("description"));

                                JSONArray arraycost = obj.getJSONArray("cost");
                                JSONObject objCost = arraycost.getJSONObject(0);

                                item.put("harga", Integer.toString(objCost.getInt("value")));
                              //  NumberFormat nf = new DecimalFormat("#,###");
                              //  item.setDecimalValue("Rp. "+ nf.format(objCost.getInt("value")));
                                item.put("estimasi","Estimasi Pengiriman "+objCost.getString("etd")+" hari");
                                data.add(item);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            AdapterLayanan adapter = new AdapterLayanan(activity,data);
                            binding.recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        binding.progressBar.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);

                        Toast.makeText(activity,"Gagal memuat data",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
