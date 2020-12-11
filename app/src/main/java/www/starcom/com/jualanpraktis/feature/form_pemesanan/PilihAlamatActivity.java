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
import www.starcom.com.jualanpraktis.databinding.ActivityPilihAlamatBinding;

public class PilihAlamatActivity extends AppCompatActivity {

    Activity activity = PilihAlamatActivity.this;
    ActivityPilihAlamatBinding binding;
    String key,titlebar,url;
    int reqCode;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_pilih_alamat);
        AndroidNetworking.initialize(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            key = bundle.getString("key");
            reqCode = bundle.getInt("code");
            if (key.equals("province")){
               titlebar = "Provinsi";
            }else if (key.equals("city")){
                titlebar = "Kota/Kabupaten";
                id = bundle.getString("id");
            }else if (key.equals("subdistrict")){
                titlebar = "Kecamatan";
                id = bundle.getString("id");
            }

        }



        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Pilih "+titlebar);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setHasFixedSize(false);

        getData(key);

    }


    private void getData(final String key){
        if (reqCode==0){
             url ="https://pro.rajaongkir.com/api/"+key;
        }else if (reqCode==1){
             url ="https://pro.rajaongkir.com/api/"+key+"?province="+id;
        }else if (reqCode==2){
            url ="https://pro.rajaongkir.com/api/"+key+"?city="+id;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        String apikey = "9bc530edfde2ffdba603d779832349cf";
        AndroidNetworking.get(url)
                .addHeaders("key",apikey )
                .setTag(PilihAlamatActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");
                            ArrayList<HashMap<String,String>> data = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                if (key.equals("province")){
                                    item.put("id",obj.getString("province_id"));
                                    item.put("nama",obj.getString("province"));
                                }else if (key.equals("city")){
                                    item.put("id",obj.getString("city_id"));
                                    item.put("nama",obj.getString("city_name"));
                                }else if (key.equals("subdistrict")){
                                    item.put("id",obj.getString("subdistrict_id"));
                                    item.put("nama",obj.getString("subdistrict_name"));
                                }
                                data.add(item);
                            }

                            //set adapter

                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            AdapterAlamat adapter = new AdapterAlamat(activity,data,key);
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
