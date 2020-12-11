package www.starcom.com.jualanpraktis.Spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 15/02/2018.
 */

public class SAdapter3 extends BaseAdapter {

    private List<SObject3.Object3.Results> listData ;
    private LayoutInflater inflater;
    public Context context ;

    public SAdapter3 (Context context, List<SObject3.Object3.Results>  listData){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder spinHolder;
        if (view == null){
            spinHolder = new ViewHolder();
            view = inflater.inflate(R.layout.spinner_list_3,viewGroup,false);
            spinHolder.spinner =  (TextView) view.findViewById(R.id.spinner_list_3);
            view.setTag(spinHolder);
        }else {
            spinHolder = (ViewHolder) view.getTag();
        }
        spinHolder.spinner.setText(listData.get(position).nama_expedisi);
        return view;
    }

    class ViewHolder {
        TextView spinner ;
    }
}
