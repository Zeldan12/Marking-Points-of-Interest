package ipl.ei.mpoi.Fragments.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipl.ei.mpoi.Activities.MainActivity;
import ipl.ei.mpoi.R;
import ipl.ei.mpoi.databinding.FragmentMapasBinding;
import ipl.ei.mpoi.RecyclerViewAdapters.MapRecyclerViewAdapter;
import ipl.ei.mpoi.Objects.PointMap;
import ipl.ei.mpoi.CallBack.SelectCallBack;

public class Mapas extends Fragment implements SelectCallBack {

    private @NonNull FragmentMapasBinding binding;

    public Mapas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //((MainActivity) getActivity()).setActionBarTitle("Editar Mapa");

        ((MainActivity) getActivity()).setMapCardListAdapter(binding.mapList, true, this::selectCallBack);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Mapas.this).navigate(R.id.action_mapas_to_menu);
            }
        });
    }

    @Override
    public void selectCallBack(int position) {
        PointMap map = ((MapRecyclerViewAdapter)binding.mapList.getAdapter()).getItem(position);
        ((MainActivity) getActivity()).changeToMapActivity(map, position);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapList.getAdapter().notifyDataSetChanged();
    }
}