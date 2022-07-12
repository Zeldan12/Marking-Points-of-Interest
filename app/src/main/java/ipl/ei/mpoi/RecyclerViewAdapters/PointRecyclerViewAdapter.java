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

import ipl.ei.mpoi.Objects.PointOfInterest;
import ipl.ei.mpoi.CallBack.SelectCallBack;
import ipl.ei.mpoi.Activities.MapActivity;
import ipl.ei.mpoi.R;

public class PointRecyclerViewAdapter extends RecyclerView.Adapter<PointRecyclerViewAdapter.Viewholder>{

    private Context context;
    private ArrayList<PointOfInterest> pointList;
    private SelectCallBack callBack;

    public PointRecyclerViewAdapter(Context context, ArrayList<PointOfInterest> pointList, SelectCallBack callBack){
        this.context = context;
        this.pointList = pointList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public PointRecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_card_view, parent, false);
        return new PointRecyclerViewAdapter.Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull PointRecyclerViewAdapter.Viewholder holder, int position) {
        PointOfInterest point = pointList.get(position);
        holder.pointName.setText(point.getName());
        holder.selectButton.setOnClickListener(view ->{
            callBack.selectCallBack(position);
        });
        holder.elimButton.setVisibility(View.VISIBLE);
        holder.elimButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("O ponto será removido para sempre! Tem a certeza?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MapActivity)context).removePoint(holder.getAdapterPosition());
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

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public PointOfInterest getItem(int position) {
        return pointList.get(position);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private Button elimButton, selectButton;
        private TextView pointName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pointName = itemView.findViewById(R.id.textViewPointNameCard);
            elimButton = itemView.findViewById(R.id.buttonEliminarPointCard);
            selectButton = itemView.findViewById(R.id.buttonSelectPointCard);
        }
    }
}
