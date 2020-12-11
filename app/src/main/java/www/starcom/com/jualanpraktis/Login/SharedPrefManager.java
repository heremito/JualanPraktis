package www.starcom.com.jualanpraktis.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

/**
 * Created by ADMIN on 13/02/2018.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "jalanpraktis";
    private static final String KEY_NAMA = "keynama";
    private static final String KEY_KODE = "keykode"; //foto profil user
    private static final String KEY_EMAIL = "keyemail"; //email
    private static final String KEY_NOHP = "keynohp";
    //private static final String KEY_JK = "keyjk";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ALAMAT = "keyalamat";
    private static final String KEY_NAMATOKO = "keynamatoko";
    private static final String KEY_NOKTP = "keynoktp";
    private static final String KEY_NONPWP = "keynopwp";

    private static final String KEY_ATASNAMA = "keyatasnama";
    private static final String KEY_NAMABANK = "keynamabank";
    private static final String KEY_NOREK = "keynorek";
    private static final String KEY_FOTO = "keyfoto";
//    private static final String KEY_IDPROVINSI = "keyidprovinsi";
//    private static final String KEY_IDKOTA = "keyidkota";
//    private static final String KEY_IDKECAMATAN = "keyidkecamatan";
    private static final String KEY_PROVINSI = "keynamaprovinsi";
    private static final String KEY_KOTA = "keynamakota";
    private static final String KEY_KECAMATAN = "keynamakecamatan";

    private static SharedPrefManager mInstance;
    private static Context mCtx ;

    public SharedPrefManager(Context context) {
        mCtx = context ;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin (loginuser user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID,user.getId());
        editor.putString(KEY_KODE,user.getKode());
        editor.putString(KEY_NAMA,user.getNama());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_NOHP,user.getNo_hp());
        editor.putString(KEY_NAMATOKO,user.getNama_toko());
        //editor.putString(KEY_JK,user.getJk());
        editor.putString(KEY_ALAMAT,user.getAlamat());
        editor.putString(KEY_KECAMATAN,user.getKecamatan());
        editor.putString(KEY_KOTA,user.getKota());
        editor.putString(KEY_PROVINSI,user.getProvinsi());
        editor.putString(KEY_NOKTP,user.getNo_ktp());
        editor.putString(KEY_NONPWP,user.getNo_npwp());
        editor.putString(KEY_ATASNAMA,user.getAtas_nama());
        editor.putString(KEY_NOREK,user.getNo_rek());
        editor.putString(KEY_NAMABANK,user.getNama_bank());
        editor.putString(KEY_FOTO,user.getFoto());

//        editor.putString(KEY_IDPROVINSI,user.getIdProvinsi());
//        editor.putString(KEY_IDKOTA,user.getIdKota());
//        editor.putString(KEY_IDKECAMATAN,user.getIdKecamatan());

        editor.apply();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    //Mengecek sudah login atau belum
    public boolean isLoggedIn() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;

    }

    //akan Memberikan login ke user
    public loginuser getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new loginuser(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_KODE, "-"),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_NAMATOKO,"-"),
                sharedPreferences.getString(KEY_PROVINSI, "-"),
                sharedPreferences.getString(KEY_KOTA, "-"),
                sharedPreferences.getString(KEY_KECAMATAN, "-"),
                sharedPreferences.getString(KEY_ALAMAT, "-"),
                sharedPreferences.getString(KEY_NOKTP,"-"),
                sharedPreferences.getString(KEY_NONPWP,"-"),
                sharedPreferences.getString(KEY_NOHP, "-"),
                sharedPreferences.getString(KEY_EMAIL, null),

                sharedPreferences.getString(KEY_ATASNAMA,"-"),
                sharedPreferences.getString(KEY_NOREK,"-"),
                sharedPreferences.getString(KEY_NAMABANK,"-"),
                sharedPreferences.getString(KEY_FOTO,"-")
//                sharedPreferences.getString(KEY_IDPROVINSI, "-"),
//                sharedPreferences.getString(KEY_IDKOTA, "-"),
//                sharedPreferences.getString(KEY_IDKECAMATAN, "-"),

        );
    }

    //logout user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
