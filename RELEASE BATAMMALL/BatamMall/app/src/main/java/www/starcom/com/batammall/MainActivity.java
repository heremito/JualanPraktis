package www.starcom.com.jualanpraktis;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import www.starcom.com.jualanpraktis.SubKategori.order;

public class MainActivity extends AppCompatActivity {
    public static  MainActivity instance;
    private akun Akun ;
    private home_dashboard Home_dashboard;
    private keranjang Keranjang ;

    private TabLayout tabLayout;

    List<order> list = new ArrayList<>();
    int total = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
    }

    public static MainActivity getInstance() {
        return instance;
    }
    private void getAllWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
    }
    private void setupTabLayout() {
        Akun = new akun();
        Keranjang = new keranjang();
        Home_dashboard = new home_dashboard();
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("HOME").setIcon(R.drawable.ic_home_black),true);
        tabLayout.addTab(tabLayout.newTab().setText("KERANJANG").setIcon(R.drawable.ic_keranjang_black));
        tabLayout.addTab(tabLayout.newTab().setText("AKUN").setIcon(R.drawable.ic_akun_black));

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
                replaceFragment(Keranjang);
                break;
            case 2 :
                replaceFragment(Akun);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
