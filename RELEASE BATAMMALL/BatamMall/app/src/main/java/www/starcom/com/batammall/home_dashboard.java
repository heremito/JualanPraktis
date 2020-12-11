package www.starcom.com.jualanpraktis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Kategori.ViewPagerAdapter;
import www.starcom.com.jualanpraktis.Kategori.adapterkategori;
import www.starcom.com.jualanpraktis.Kategori.objectkategori;
import www.starcom.com.jualanpraktis.Kategori.urlkategori;

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
    String request_url = "https://batammall.co.id/ANDROID/banner.php";
    ViewPagerAdapter viewPagerAdapter ;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Timer timer ;

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

        CardView cardView = rootView.findViewById(R.id.cari);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchResultsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        requestQueue = Volley.newRequestQueue(getActivity());
        sliderimg = new ArrayList<>();

        viewPager = rootView.findViewById(R.id.pagerslide);
        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);

        sendRequest();

        init();
        loadData();
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
        loadData();
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
        loadData();
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
                        final String UrlImage = "https://batammall.co.id/img/";
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

                    dots[i] = new ImageView(getActivity());
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
    }

    // Menampilkan Data dari Database
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
    }

}
