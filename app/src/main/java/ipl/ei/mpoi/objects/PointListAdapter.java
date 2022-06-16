package ipl.ei.mpoi.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PointListAdapter extends ArrayAdapter<PointOfInterest> {
    private int resourceLayout;
    private Context context;

    public PointListAdapter(Context context, int resource, ArrayList<PointOfInterest> items) {
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

        final PointOfInterest item = getItem(position);
        text.setText(item.getName());

        return view;
    }
}
