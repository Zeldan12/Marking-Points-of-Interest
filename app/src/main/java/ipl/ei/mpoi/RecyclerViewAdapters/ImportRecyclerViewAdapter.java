package ipl.ei.mpoi.RecyclerViewAdapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ipl.ei.mpoi.Activities.MainActivity;
import ipl.ei.mpoi.R;

public class ImportRecyclerViewAdapter extends RecyclerView.Adapter<ImportRecyclerViewAdapter.Viewholder>{

    private final Context context;
    private final ArrayList<String> mapList;

    public ImportRecyclerViewAdapter(Context context, ArrayList<String> mapList){
            this.context = context;
            this.mapList = mapList;
            }

    @NonNull
    @Override
    public ImportRecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.import_map_card_view, parent, false);
            return new Viewholder(view);
            }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ImportRecyclerViewAdapter.Viewholder holder, int position) {
        holder.mapName.setText(mapList.get(position));
        holder.itemView.setOnClickListener(view->{
            ((MainActivity)context).importMap(mapList.get(position));
        });
    }

    @Override
    public int getItemCount() {
            return mapList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final TextView mapName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.textViewNomeMapaImport);
        }
    }

}
