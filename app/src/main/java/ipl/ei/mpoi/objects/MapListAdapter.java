package ipl.ei.mpoi.objects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ipl.ei.mpoi.R;

public class MapListAdapter extends ArrayAdapter<PointMap> {

    private int resourceLayout;
    private Context context;

    public MapListAdapter(Context context, int resource, ArrayList<PointMap> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final TextView text;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            view = inflater.inflate(resourceLayout, parent, false);
        } else {
            view = convertView;
        }

        text = (TextView) view;

        final PointMap item = getItem(position);
        text.setText(item.getName());

        return view;
    }


}



