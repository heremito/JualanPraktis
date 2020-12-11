package www.starcom.com.jualanpraktis.feature.pembayaran;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.databinding.FragmentListPembayaranBinding;

public class ListPembayaranFragment extends Fragment {

    public ListPembayaranFragment() {
        // Required empty public constructor
    }

    FragmentListPembayaranBinding binding;
    String baseUrl = "https://my.ipaymu.com/api/";
    String key = "6vin5Or.ljjipPFMM3UTDGzYYHdLf0";
    PembayaranAdapter adapter;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater,container,false);

        AndroidNetworking.initialize(getActivity().getApplicationContext());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setHasFixedSize(false);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  dataList.clear();
        HashMap<String,String> data = new HashMap<>();
        data.put("id","1");
        data.put("tanggal","2020-04-25");
        data.put("jumlah","78585");
        data.put("tipe","Bayar Indomaret");
        data.put("status","Pending");
        data.put("kode_status","0");
        dataList.add(data); */


        dataPembayaran();
    }

    private void dataPembayaran(){
        String url = baseUrl + "transaksi";
        url+= "?key="+key;
        url+= "&format=json";

        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dataList.clear();
                        try {
                            JSONArray array = response.getJSONArray("transaksi");

                            for (int i = 0; i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id",Integer.toString(object.getInt("id")));
                                data.put("tanggal",object.getString("tanggal"));
                                data.put("jumlah",object.getString("jumlah"));
                                data.put("tipe",object.getString("tipe"));
                                data.put("status",object.getString("status"));
                                data.put("kode_status",Integer.toString(object.getInt("kode_status")));
                                dataList.add(data);
                            }

                            adapter = new PembayaranAdapter(getActivity(),dataList);
                            binding.recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}
