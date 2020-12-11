package www.starcom.com.jualanpraktis.IDTansaksi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ADMIN on 21/02/2018.
 */

public class Shared {
    private static final String SHARED_PREF_NAME2 = "id";
    private static final String KEY_IDT = "keyid_transaksi";

    private static Shared mInstance;
    private static Context mCtx ;

    public Shared(Context context) {
        mCtx = context ;
    }

    public static synchronized Shared getInstance(Context context){
        if (mInstance == null) {
            mInstance = new Shared(context);
        }
        return mInstance;
    }

    public void idT (idtransaksi id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDT,id.getId_transaksi());
        editor.apply();
    }

    //Mengecek sudah login atau belum
    public boolean isIdIn() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IDT, null) != null;

    }

    //akan Memberikan login ke user
    public idtransaksi getIdT() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return new idtransaksi(
                sharedPreferences.getString(KEY_IDT, null)
        );
    }

    //logout user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}


