package www.starcom.com.jualanpraktis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Kategori.ViewPagerAdapter;
import www.starcom.com.jualanpraktis.Kategori.adapterkategori;
import www.starcom.com.jualanpraktis.Kategori.objectkategori;
import www.starcom.com.jualanpraktis.SubKategori.adaptersub;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;
import www.starcom.com.jualanpraktis.adapter.KategoriAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukAdapter;
import www.starcom.com.jualanpraktis.feature.produk.ListProdukActivity;
import www.starcom.com.jualanpraktis.feature.produk.ListProdukDiskonActivity;
import www.starcom.com.jualanpraktis.model.ListProduk;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class home_dashboard extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private objectkategori.ObjectKategori objectKategori;
    private adapterkategori adapterkategori ;
    private RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5,
    recyclerView6,recyclerView7,recyclerView8,recyclerView9,recyclerView10
    ,recyclerView11,recyclerView12,recyclerView13,recyclerView14,recyclerView15;

    View rootView ;

    LinearLayout sliderDotspanel;
    private int dotscount ;
    private ImageView[] dots ;
    ViewPager viewPager;
    RequestQueue requestQueue ;
    List<SliderUtils> sliderimg ;
    String request_url = "https://jualanpraktis.net/android/banner.php";
    ViewPagerAdapter viewPagerAdapter ;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Timer timer ;

    //produk
    private RecyclerView recyclerView,rv_produk1,rv_produk2,rv_produk3,rv_produk4 ;
    private objectsub.ObjectSub objectSub;
    private www.starcom.com.jualanpraktis.SubKategori.adaptersub adaptersub ;
    private www.starcom.com.jualanpraktis.adapter.ProdukAdapter produk1,produk2,produk3,produk4,allproduk ;
    GridLayoutManager gridLayoutManager;
    private ShimmerFrameLayout shimmer,shimmerAllProduk,shimmer_kategori;

    //iklan
    ImageView imgIklan1,imgIklan2,imgIklan3,imgIklan4;
    LinearLayout lllistproudk,ll_all_produk,ll_kategori;
    Button btn_see_all;

    //kategori
    private RecyclerView rv_kategori;
    ArrayList<HashMap<String,String>> kategoriList = new ArrayList<>();

    //diskon
    private ImageView imgDiskon;
    //contact
    ImageView img_contact;
    Context context;

    //Cart
    private ImageView imgKeranjang;
    public home_dashboard() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard,container,false);
        context=getActivity().getApplicationContext();
        AndroidNetworking.initialize(context);


        CardView cardView = rootView.findViewById(R.id.cari);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchResultsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

        requestQueue = Volley.newRequestQueue(getActivity());
        sliderimg = new ArrayList<>();

        viewPager = rootView.findViewById(R.id.pagerslide);
        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);

        sendRequest();

        lllistproudk = rootView.findViewById(R.id.lllistproudk);
        ll_all_produk = rootView.findViewById(R.id.ll_all_produk);
        ll_kategori = rootView.findViewById(R.id.ll_kategori);
        //image iklan
        imgIklan1 = rootView.findViewById(R.id.imgIklan1);
        imgIklan2 = rootView.findViewById(R.id.imgIklan2);
        imgIklan3 = rootView.findViewById(R.id.imgIklan3);
        imgIklan4 = rootView.findViewById(R.id.imgIklan4);
        //button
        btn_see_all = rootView.findViewById(R.id.btn_see_all);
        //list produk
        recyclerView = rootView.findViewById(R.id.rv_produk);
        rv_produk1 = rootView.findViewById(R.id.rv_produk1);
        rv_produk2 = rootView.findViewById(R.id.rv_produk2);
        rv_produk3 = rootView.findViewById(R.id.rv_produk3);
        rv_produk4 = rootView.findViewById(R.id.rv_produk4);
        rv_kategori = rootView.findViewById(R.id.rv_kategori);
        recyclerView.setHasFixedSize(true);
        rv_produk1.setHasFixedSize(true);
        rv_produk2.setHasFixedSize(true);
        rv_produk3.setHasFixedSize(true);
        rv_produk4.setHasFixedSize(true);
        rv_kategori.setHasFixedSize(true);

       // gridLayoutManager = new GridLayoutManager(getContext(),3);
        int gridNumber = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk1.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk2.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk3.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk4.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_kategori.setLayoutManager(  new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        shimmer = rootView.findViewById(R.id.shimmer);
        shimmerAllProduk = rootView.findViewById(R.id.shimmerAllProduk);
        shimmer_kategori = rootView.findViewById(R.id.shimmer_kategori);
        lllistproudk.setVisibility(View.GONE);

        imgDiskon = rootView.findViewById(R.id.imgDiskon);
        imgDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListProdukDiskonActivity.class));
            }
        });

        imgKeranjang = rootView.findViewById(R.id.imgKeranjang);
        imgKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), katalog.class);
//                startActivity(intent);
//                startActivity(new Intent(getActivity(), katalog.class));
            }
        });

        img_contact = rootView.findViewById(R.id.img_contact);
        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("http://api.whatsapp.com/send?phone=62895623458199&text= ");
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.setData(uri);
//                startActivity(sendIntent);
              /*  AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                View layoutView = getLayoutInflater().inflate(R.layout.dialog_info, null);
                final TextView txt_number = layoutView.findViewById(R.id.txt_number);
                final ImageView button = layoutView.findViewById(R.id.close);
                txt_number.setPaintFlags(txt_number.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

                dialogBuilder.setView(layoutView);
                final AlertDialog  alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                txt_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("http://api.whatsapp.com/send?phone=62895623458199&text= ");
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(uri);
                        startActivity(sendIntent);
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }); **/
            }
        });

     //   init();
       // loadData();

       // getAllProduk();
        btn_see_all.setVisibility(View.GONE);
       // getProdykPerBaris();
        getKategori();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer !=null){
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // loadData();
      //  btn_see_all.setVisibility(View.GONE);
      //  getProdykPerBaris();
        sendRequest();

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),4000,4000);
        /*
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                                for (int i = 0; i < dotscount; i++) {
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.notactive_dots));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dots));

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });


                }
            });
        }
        */
    }

    @Override
    public void onRefresh() {
       // loadData();
        //getAllProduk();
     //   getProdykPerBaris();
        getKategori();
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.setCurrentItem(1);
                        } else if (viewPager.getCurrentItem() == 1) {
                            viewPager.setCurrentItem(2);
                        } else if (viewPager.getCurrentItem() == 2) {
                            viewPager.setCurrentItem(3);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    public void sendRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        final String UrlImage = "https://jualanpraktis.net/img/";
                        final String Image = jsonObject.getString("image");
                        final Uri uri = Uri.parse(UrlImage + Image);
                        sliderUtils.setSliderImageUrl(uri.toString());
                        Log.d(TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderimg.add(sliderUtils);
                }

                viewPagerAdapter = new ViewPagerAdapter(sliderimg, getActivity());
                viewPager.setAdapter(viewPagerAdapter);
                /*
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(get Activity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.notactive_dots));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dots));
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
/**
    public void loadData(){
        GetData(urlkategori.URL_kategori);
        GetData2(urlkategori.URL_kategori2);
        GetData3(urlkategori.URL_kategori3);
        GetData4(urlkategori.URL_kategori4);
        GetData5(urlkategori.URL_kategori5);
        GetData6(urlkategori.URL_kategori6);
        GetData7(urlkategori.URL_kategori7);
        GetData8(urlkategori.URL_kategori8);
        GetData9(urlkategori.URL_kategori9);
        GetData10(urlkategori.URL_kategori10);
        GetData11(urlkategori.URL_kategori11);
        GetData12(urlkategori.URL_kategori12);
        GetData13(urlkategori.URL_kategori13);
        GetData14(urlkategori.URL_kategori14);
        GetData15(urlkategori.URL_kategori15);
    }

    private void init(){

        /**
        //linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView1 = rootView.findViewById(R.id.rv_item1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView2 = rootView.findViewById(R.id.rv_item2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView3 = rootView.findViewById(R.id.rv_item3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView4 = rootView.findViewById(R.id.rv_item4);
        recyclerView4.setHasFixedSize(true);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView5 = rootView.findViewById(R.id.rv_item5);
        recyclerView5.setHasFixedSize(true);
        recyclerView5.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView6 = rootView.findViewById(R.id.rv_item6);
        recyclerView6.setHasFixedSize(true);
        recyclerView6.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView7 = rootView.findViewById(R.id.rv_item7);
        recyclerView7.setHasFixedSize(true);
        recyclerView7.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView8 = rootView.findViewById(R.id.rv_item8);
        recyclerView8.setHasFixedSize(true);
        recyclerView8.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView9 = rootView.findViewById(R.id.rv_item9);
        recyclerView9.setHasFixedSize(true);
        recyclerView9.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView10 = rootView.findViewById(R.id.rv_item10);
        recyclerView10.setHasFixedSize(true);
        recyclerView10.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        recyclerView11 = rootView.findViewById(R.id.rv_item11);
        recyclerView11.setHasFixedSize(true);
        recyclerView11.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView12 = rootView.findViewById(R.id.rv_item12);
        recyclerView12.setHasFixedSize(true);
        recyclerView12.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView13 = rootView.findViewById(R.id.rv_item13);
        recyclerView13.setHasFixedSize(true);
        recyclerView13.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView14 = rootView.findViewById(R.id.rv_item14);
        recyclerView14.setHasFixedSize(true);
        recyclerView14.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView15 = rootView.findViewById(R.id.rv_item15);
        recyclerView15.setHasFixedSize(true);
        recyclerView15.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
    } **/

    //Menampilkan kategori
    private void getKategori(){

        ll_kategori.setVisibility(View.GONE);
        lllistproudk.setVisibility(View.GONE);
        imgDiskon.setVisibility(View.GONE);
        shimmer_kategori.setVisibility(View.VISIBLE);
        shimmer_kategori.startShimmerAnimation();
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();

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
                            shimmer_kategori.stopShimmerAnimation();
                            shimmer_kategori.setVisibility(View.GONE);
                            ll_kategori.setVisibility(View.VISIBLE);


                            KategoriAdapter adapter = new KategoriAdapter(getActivity(),kategoriList);
                            rv_kategori.setAdapter(adapter);

                                getGambarIklan();
                                getAllProduk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        shimmer_kategori.stopShimmerAnimation();
                        shimmer_kategori.setVisibility(View.GONE);
                        ll_kategori.setVisibility(View.VISIBLE);
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

    //Menampilkan gambar iklan
    private void getGambarIklan(){
        AndroidNetworking.get("https://jualanpraktis.net/android/iklan.php").setTag(getActivity())
                .setPriority(Priority.MEDIUM).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");
                            JSONObject obj = array.getJSONObject(0);

                            String urlImage = "https://jualanpraktis.net/iklan/"+obj.getString("img");

                            Glide.with(context).load(urlImage).into(imgDiskon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    // Menampilkan Data Produk
    private void getProdykPerBaris(){


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/produk2.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ListProduk.ObjectSub.class, new ParsedRequestListener<ListProduk.ObjectSub>() {
                    @Override
                    public void onResponse(final ListProduk.ObjectSub response) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        ll_all_produk.setVisibility(View.VISIBLE);
                        btn_see_all.setVisibility(View.VISIBLE);
                        lllistproudk.setVisibility(View.VISIBLE);

                        String urlImage = "https://jualanpraktis.net/iklan/";
                        //Picasso.get().load(urlImage+response.iklan1.get(0).img).into(imgIklan1);
                        //Picasso.get().load(urlImage+response.iklan2.get(0).img).into(imgIklan2);
                        //Picasso.get().load(urlImage+response.iklan3.get(0).img).into(imgIklan3);
                        //Picasso.get().load(urlImage+response.iklan4.get(0).img).into(imgIklan4);

                        klikIklan(imgIklan1,response.iklan1.get(0).id_member);
                        klikIklan(imgIklan2,response.iklan2.get(0).id_member);
                        klikIklan(imgIklan3,response.iklan3.get(0).id_member);
                        klikIklan(imgIklan4,response.iklan4.get(0).id_member);

                        final String statusList = "home";
                        produk1 = new ProdukAdapter(getActivity(), response.produk1,statusList);
                        produk2 = new ProdukAdapter(getActivity(), response.produk2,statusList);
                        produk3 = new ProdukAdapter(getActivity(), response.produk3,statusList);
                        produk4 = new ProdukAdapter(getActivity(), response.produk4,statusList);
                        rv_produk1.setAdapter(produk1);
                        rv_produk2.setAdapter(produk2);
                        rv_produk3.setAdapter(produk3);
                        rv_produk4.setAdapter(produk4);

                        btn_see_all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              //  allproduk = new ProdukAdapter(getContext(),response.allproduct,statusList);
                              //  recyclerView.setAdapter(allproduk);
                               // btn_see_all.setVisibility(View.GONE);
                               // recyclerView.setVisibility(View.VISIBLE);
                               // getAllProduk();

                                List<ListProduk.ObjectSub.Results> semuaproduk = response.allproduct;
                                Intent intent = new Intent(getActivity(),ListProdukActivity.class);
                                intent.putExtra("status","allproduct");
                                intent.putExtra("id","0");
                                intent.putExtra("kategori","Semua Produk");
                              //  intent.putExtra("dataList", (Serializable) semuaproduk);
                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        lllistproudk.setVisibility(View.VISIBLE);
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

                       // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllProduk(){

        recyclerView.setVisibility(View.GONE);
       // shimmerAllProduk.setVisibility(View.VISIBLE);
       // shimmerAllProduk.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                       // swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        imgDiskon.setVisibility(View.GONE);
                        adaptersub = new adaptersub(getActivity(), response.sub1_kategori1);
                        recyclerView.setAdapter(adaptersub);


                    }

                    @Override
                    public void onError(ANError anError) {
                      //  swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void klikIklan(ImageView imageView, final String id_member){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListProdukActivity.class);
                intent.putExtra("status","produkIklan");
                intent.putExtra("id",id_member);
                intent.putExtra("kategori","Produk Iklan");
                startActivity(intent);
            }
        });
    }

    /**
    private void produkvolley(){
        recyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();

        String url = "https://batammall.co.id/ANDROID/produk.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    shimmer.stopShimmerAnimation();
                    shimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectSub = mGson.fromJson(response, objectsub.ObjectSub.class);
                    adaptersub = new adaptersub(getContext(), objectSub.sub1_kategori1);
                    recyclerView.setAdapter(adaptersub);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data Kategori dari Database
    public void GetData(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView1.setAdapter(adapterkategori);
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d(TAG,error.toString());
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData2(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView2.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData3(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView3.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData4(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView4.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData5(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView5.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData6(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView6.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData7(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView7.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData8(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView8.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData9(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView9.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData10(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView10.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData11(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView11.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData12(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView12.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData13(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView13.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData14(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView14.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData15(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView15.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    } **/

}
