package www.starcom.com.jualanpraktis.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {
    public Pref(Context context){
        this.context = context;
    };

    private Context context;

    public String Read(String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
        return pref.getString(key, "");
    }

    public void Write(String key, final String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // Boolean
    public  boolean ReadBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.context);
        return settings.getBoolean(key, defaultValue);
    }

    public void WriteBoolean(String key, boolean value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getLoginMethod() {
        return this.Read("loginMethod");
    }
    public void setLoginMethod(String loginMethod) {
        this.Write("loginMethod", loginMethod);
    }


    public String getLimitBelanja() {
        return this.Read("LimitBelanja");
    }
    public void setLimitBelanja(String LimitBelanja) {
        this.Write("LimitBelanja", LimitBelanja);
    }


    public String getNik() {
        return this.Read("Nik");
    }
    public void setNik(String Nik) {
        this.Write("Nik", Nik);
    }


}
