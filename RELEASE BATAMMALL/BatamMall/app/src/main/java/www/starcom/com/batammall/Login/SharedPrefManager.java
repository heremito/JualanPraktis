package www.starcom.com.jualanpraktis.Login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ADMIN on 13/02/2018.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "batammall";
    private static final String KEY_NAMA = "keynama";
    private static final String KEY_EMAIL = "keyemail"; //username
    private static final String KEY_NOHP = "keynohp";
    private static final String KEY_JK = "keyjk";
    private static final String KEY_ID = "keyid";

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
        editor.putInt(KEY_ID,user.getId());
        editor.putString(KEY_NAMA,user.getNama());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_NOHP,user.getNo_hp());
        editor.putString(KEY_JK,user.getJk());
        editor.apply();
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
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_NOHP, null),
                sharedPreferences.getString(KEY_JK, null)
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
