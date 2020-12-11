package www.starcom.com.jualanpraktis.utils.Listener;

import android.util.Log;

import www.starcom.com.jualanpraktis.utils.Enums.ActionEnum;
import www.starcom.com.jualanpraktis.utils.Interface.ValueChangedListener;


/**
 * Created by travijuu on 19/12/16.
 */

public class DefaultValueChangedListener implements ValueChangedListener {

    public void valueChanged(int value, ActionEnum action) {

        String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
        String message = String.format("NumberPicker is %s to %d", actionText, value);
        Log.v(this.getClass().getSimpleName(), message);
    }
}
