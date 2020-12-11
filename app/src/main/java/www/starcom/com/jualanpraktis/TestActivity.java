package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.adapter.ProdukAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityTestBinding;
import www.starcom.com.jualanpraktis.model.ListProduk;

public class TestActivity extends AppCompatActivity {

    Activity activity = TestActivity.this;
    ActivityTestBinding binding;
    ProdukAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_test);
        AndroidNetworking.initialize(getApplicationContext());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        //ViewCompat.setNestedScrollingEnabled(binding.recyclerView, false);

        binding.recyclerView.setHasFixedSize(true);
    }

    private void getDataIklan(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post("https://trading.my.id/android/produk_tenan.php")
                .addBodyParameter("id_member ","6")
                .setTag("6")
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ListProduk.ObjectSub.class, new ParsedRequestListener<ListProduk.ObjectSub>() {
                    @Override
                    public void onResponse(ListProduk.ObjectSub response) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);

                        adapter = new ProdukAdapter(getApplicationContext(), response.produksejenislist,"list");
                        binding.recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
