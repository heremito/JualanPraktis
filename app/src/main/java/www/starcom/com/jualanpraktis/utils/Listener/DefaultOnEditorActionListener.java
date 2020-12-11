package www.starcom.com.jualanpraktis.utils.Listener;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import www.starcom.com.jualanpraktis.utils.Enums.ActionEnum;
import www.starcom.com.jualanpraktis.utils.NumberPicker;


/**
 * Created by travijuu on 13/04/17.
 */

public class DefaultOnEditorActionListener implements TextView.OnEditorActionListener {

    NumberPicker layout;

    public DefaultOnEditorActionListener(NumberPicker layout) {
        this.layout = layout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            try {
                int value = Integer.parseInt(v.getText().toString());

                layout.setValue(value);

                if (layout.getValue() == value) {
                    layout.getValueChangedListener().valueChanged(value, ActionEnum.MANUAL);
                    return false;
                }
            } catch (NumberFormatException e) {
                layout.refresh();
            }
        }
        return true;
    }
}
