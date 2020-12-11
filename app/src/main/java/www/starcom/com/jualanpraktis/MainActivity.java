package www.starcom.com.jualanpraktis;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import www.starcom.com.jualanpraktis.Keranjang.keranjangAdapter;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.feature.akun.AkunCoorperateFragment;
import www.starcom.com.jualanpraktis.feature.akun.AkunFragment;
import www.starcom.com.jualanpraktis.feature.belanja_bulanan.ListBelanjaBulananFragment;
import www.starcom.com.jualanpraktis.feature.pembayaran.ListPembayaranFragment;
import www.starcom.com.jualanpraktis.feature.pesanan_saya.ListPesananFragment;
import www.starcom.com.jualanpraktis.service.ServiceTask;
import www.starcom.com.jualanpraktis.service.spyWorker;

public class MainActivity extends AppCompatActivity {
    public static  MainActivity instance;
    private AkunFragment AkunFragment;
    private AkunCoorperateFragment akunCoorperateFragment;
    private home_dashboard Home_dashboard;
    private keranjang Keranjang ;
    private ListPembayaranFragment pembayaranFragment;
    private ListPesananFragment listPesananFragment;
    private ListBelanjaBulananFragment listBelanjaBulananFragment;
    private katalog Katalog;

    private TabLayout tabLayout;

    List<order> list = new ArrayList<>();
    keranjangAdapter adapter ;
    int total = 0 ;
    int badge = 0 ;
    loginuser user ;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {

                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.CAMERA};
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        pref = new Pref(getApplicationContext());


        startService(new Intent(MainActivity.this, ServiceTask.class)
                .putExtra("proses","home")
        );
      //  startWorkManager();
        checkUpdate();
        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * active workmanager **/
    private Constraints networkConstraints() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)
                .build();
        return constraints;
    }
    private void startWorkManager() {
        Data source = new Data.Builder()
                .putString("workType", "OneTime")
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest
                .Builder(spyWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(networkConstraints())
                .setInputData(source)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("uploadSpy", ExistingPeriodicWorkPolicy.KEEP,request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    void handlerUpdate(){
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //your code
                handler.postDelayed(this,10000);
             //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             //       startForegroundService(new Intent(MainActivity.this, ServiceTask.class));
             //   }else{
                    startService(new Intent(MainActivity.this, ServiceTask.class));
               // }
            }
        },10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // list = new Database(getApplicationContext()).getPesan();
      //  badge = list.size();


        if (SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()) {
            user = SharedPrefManager.getInstance(MainActivity.this).getUser();
            getCountCart();
          //  getCountBBL();

        }
      //

    }

    public static MainActivity getInstance() {
        return instance;
    }
    private void getAllWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
    }
    private void setupTabLayout() {
//        tabLayout.removeAllTabs();
        AkunFragment = new AkunFragment();
        akunCoorperateFragment = new AkunCoorperateFragment();
        Keranjang = new keranjang();
        Home_dashboard = new home_dashboard();
        pembayaranFragment = new ListPembayaranFragment();
        listPesananFragment = new ListPesananFragment();
        listBelanjaBulananFragment = new ListBelanjaBulananFragment();
        Katalog = new katalog();
       // tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Home").setIcon(R.drawable.untuk_anda_icon));
        tabLayout.addTab(tabLayout.newTab().setText("Katalog").setIcon(R.drawable.katalog_icon__red_));
        tabLayout.addTab(tabLayout.newTab().setText("Pesanan").setIcon(R.drawable.pesanan_icon__red_));
        tabLayout.addTab(tabLayout.newTab().setText("Keranjang").setIcon(R.drawable.icon_cart));
//        int tab = getIntent().getIntExtra("tab",0);
//        if (tab==2){
//            tabLayout.addTab(tabLayout.newTab().setText("Pesanan").setIcon(R.drawable.ic_bag),true);
//        }else {
//            tabLayout.addTab(tabLayout.newTab().setText("Pesanan").setIcon(R.drawable.ic_bag));
//        }
        //tabLayout.addTab(tabLayout.newTab().setText("BBL").setIcon(R.drawable.ic_belanja_bulanan));
        tabLayout.addTab(tabLayout.newTab().setText("Akun").setIcon(R.drawable.icon_profile));
       // tab.showBadge().setNumber(badge);

      //  tabLayout.addTab(tabLayout.newTab().setText("Pembayaran").setIcon(R.drawable.ic_rupiah_money));
        //setCurrentTabFragment(getIntent().getIntExtra("tab",0));

    }
    private void bindWidgetsWithAnEvent()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(Home_dashboard);
                break;
            case 1 :
                replaceFragment(Katalog);
                break;
            case 2 :
                replaceFragment(listPesananFragment);
                break;
            case 3 :
                replaceFragment(Keranjang);
                break;
            case 4 :
                replaceFragment(AkunFragment);
                break;
//            case 4 :
//                if (pref.getLoginMethod().equals("coorperate")){
//                    replaceFragment(akunCoorperateFragment);
//                }else {
//                    replaceFragment(AkunFragment);
//                }
//                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    void getCountCart(){
        HashMap<String,String> param = new HashMap<>();
        param.put("customer", user.getId());

        AndroidNetworking.post("https://jualanpraktis.net/android/cart.php")
                .addBodyParameter(param)
                .setTag(MainActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  progressDialog.dismiss();
                        try {
                            ArrayList<String> listdata = new ArrayList<String>();
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0;i<array.length();i++){
                                listdata.add(array.getString(i));
                            }

                            int size = listdata.size();
                            tabLayout.getTabAt(3).showBadge().setNumber(size);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

//    void getCountBBL(){
//        HashMap<String,String> param = new HashMap<>();
//        param.put("customer", user.getId());
//
//        AndroidNetworking.post("https://trading.my.id/android/bbl.php")
//                .addBodyParameter(param)
//                .setTag(MainActivity.this)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //  progressDialog.dismiss();
//                        try {
//                            ArrayList<String> listdata = new ArrayList<String>();
//                            JSONArray array = response.getJSONArray("data");
//
//                            for (int i=0;i<array.length();i++){
//                                listdata.add(array.getString(i));
//                            }
//
//                            int size = listdata.size();
//                            tabLayout.getTabAt(4).showBadge().setNumber(size);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
//    }


    private void checkUpdate(){
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(MainActivity.this)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {

                        if (isUpdateAvailable){
                            new AppUpdater(MainActivity.this)
                                    .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                                    .setDisplay(Display.DIALOG)
                                    .showAppUpdated(true)
                                    .setCancelable(false)
                                    .setButtonDoNotShowAgain("")
                                    .setButtonDismiss("")
                                    .start();
                        }
                        /*
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", update.getLatestVersionCode());
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", update.getUrlToDownload());
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable)); */
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        //Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }
}
