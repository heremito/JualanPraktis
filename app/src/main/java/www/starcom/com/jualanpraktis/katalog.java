package www.starcom.com.jualanpraktis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import www.starcom.com.jualanpraktis.adapter.KategoriAdapter;
import www.starcom.com.jualanpraktis.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 */
public class katalog extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private RecyclerView listKatalog;
    ArrayList<HashMap<String,String>> kategoriList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public katalog() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static katalog newInstance(int columnCount) {
        katalog fragment = new katalog();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_katalog_list, container, false);

        listKatalog = view.findViewById(R.id.listKatalog);
        listKatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MykatalogRecyclerViewAdapter(DummyContent.ITEMS));
//        }

        getKategori();
        return view;
    }

    private void getKategori(){

//        ll_kategori.setVisibility(View.GONE);
//        lllistproudk.setVisibility(View.GONE);
//        imgDiskon.setVisibility(View.GONE);
//        shimmer_kategori.setVisibility(View.VISIBLE);
//        shimmer_kategori.startShimmerAnimation();
//        shimmer.setVisibility(View.VISIBLE);
//        shimmer.startShimmerAnimation();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/kategori.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            kategoriList.clear();
                            JSONArray array = response.getJSONArray("kategori");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id",jsonObject.getString("id_kategori_produk"));
                                data.put("kategori",jsonObject.getString("kategori"));
                                data.put("gambar", jsonObject.getString("img"));
                                data.put("jumlah", jsonObject.getString("jumlah"));
                                //     data.put("created_at",jsonObject.getString("created_at"));
                                kategoriList.add(data);
                            }
//                            shimmer_kategori.stopShimmerAnimation();
//                            shimmer_kategori.setVisibility(View.GONE);
//                            ll_kategori.setVisibility(View.VISIBLE);


                            KategoriAdapter adapter = new KategoriAdapter(getActivity(),kategoriList);
                            listKatalog.setAdapter(adapter);

//                            getGambarIklan();
//                            getAllProduk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        shimmer.stopShimmerAnimation();
//                        shimmer.setVisibility(View.GONE);
//                        shimmer_kategori.stopShimmerAnimation();
//                        shimmer_kategori.setVisibility(View.GONE);
//                        ll_kategori.setVisibility(View.VISIBLE);
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
                                Log.e(getTag(),anError.getErrorDetail());
                            }else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}