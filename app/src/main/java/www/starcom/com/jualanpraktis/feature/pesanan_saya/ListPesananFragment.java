package www.starcom.com.jualanpraktis.feature.pesanan_saya;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.adapter.PesananAdapter;
import www.starcom.com.jualanpraktis.databinding.FragmentPesananSayaBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPesananFragment extends Fragment {

    public ListPesananFragment() {
        // Required empty public constructor
    }


    FragmentPesananSayaBinding binding;
    String url = "https://jualanpraktis.net/android/pesanan.php";
    loginuser user ;

    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    PesananAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding.inflate(inflater,container,false);

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        user = SharedPrefManager.getInstance(getActivity()).getUser();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setHasFixedSize(false);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_customer", user.getId())
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);

                        dataList.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_pesanan",jsonObject.getString("id_transaksi"));
                                data.put("tanggal",jsonObject.getString("tgl_transaksi"));
                                data.put("jumlah",jsonObject.getString("total_bayar"));
                                data.put("status_order",jsonObject.getString("status_order"));
                                data.put("no_resi",jsonObject.getString("no_resi"));
                                data.put("status_konfirmasi",jsonObject.getString("status_konfirmasi"));
                                data.put("status_kirim",jsonObject.getString("status_kirim"));
                                data.put("opsi_bayar",jsonObject.getString("id_opsi_bayar"));
                                data.put("kurir",jsonObject.getString("kurir"));
                                data.put("status_verifikasi",jsonObject.getString("status_verivikasi"));
                                dataList.add(data);
                            }


                            binding.recyclerView.setVisibility(View.VISIBLE);
                            adapter = new PesananAdapter(getActivity(),dataList);
                            binding.recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        dataList.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
