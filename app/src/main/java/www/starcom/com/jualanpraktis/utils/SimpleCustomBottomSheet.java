package www.starcom.com.jualanpraktis.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.arthurivanets.bottomsheets.BaseBottomSheet;
import com.arthurivanets.bottomsheets.config.BaseConfig;
import com.arthurivanets.bottomsheets.config.Config;

import www.starcom.com.jualanpraktis.R;

public class SimpleCustomBottomSheet extends BaseBottomSheet {

    public SimpleCustomBottomSheet(@NonNull Activity hostActivity) {
        this(hostActivity, new Config.Builder(hostActivity).build());
    }

    public SimpleCustomBottomSheet(@NonNull Activity hostActivity, @NonNull BaseConfig config) {
        super(hostActivity, config);
    }

    @NonNull
    @Override
    public final View onCreateSheetContentView(@NonNull Context context) {
        return LayoutInflater.from(context).inflate(
                R.layout.view_bottom_sheet_content,
                this,
                false
        );
    }

}
