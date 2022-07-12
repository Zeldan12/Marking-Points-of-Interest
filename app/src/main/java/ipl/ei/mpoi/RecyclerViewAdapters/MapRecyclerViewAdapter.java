package ipl.ei.mpoi.RecyclerViewAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ipl.ei.mpoi.Objects.PointMap;
import ipl.ei.mpoi.CallBack.SelectCallBack;
import ipl.ei.mpoi.Activities.MainActivity;
import ipl.ei.mpoi.R;

public class MapRecyclerViewAdapter extends RecyclerView.Adapter<MapRecyclerViewAdapter.Viewholder>{

    private Context context;
    private ArrayList<PointMap> mapList;
    private boolean elimButton;
    private SelectCallBack callBack;

    public MapRecyclerViewAdapter(Context context, ArrayList<PointMap> mapList, boolean elimButton, SelectCallBack callBack){
        this.context = context;
        this.mapList = mapList;
        this.elimButton = elimButton;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MapRecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_card_view, parent, false);
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MapRecyclerViewAdapter.Viewholder holder, int position) {
        PointMap map = mapList.get(position);
        holder.mapName.setText(map.getName());
        holder.mapPointCount.setText("Pontos: " + map.getPoints().size());
        holder.selectButton.setOnClickListener(view ->{
            callBack.selectCallBack(position);
        });
        if(elimButton){
            holder.elimButton.setVisibility(View.VISIBLE);
            holder.elimButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("O mapa será eliminado para sempre! Tem a certeza?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)context).removeMap(map);
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            });
        }
    }


    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public PointMap getItem(int position) {
        return mapList.get(position);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private Button elimButton, selectButton;
        private TextView mapName, mapPointCount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.textViewNomeMapaCard);
            mapPointCount = itemView.findViewById(R.id.textViewNumeroPontos);
            elimButton = itemView.findViewById(R.id.buttonEliminarMapCard);
            selectButton = itemView.findViewById(R.id.buttonSelectMapCard);
        }
    }
}
