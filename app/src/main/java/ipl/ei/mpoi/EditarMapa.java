package ipl.ei.mpoi;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import ipl.ei.mpoi.databinding.FragmentEditarMapaBinding;
import ipl.ei.mpoi.databinding.FragmentExportarMapaBinding;
import ipl.ei.mpoi.objects.MapListAdapter;
import ipl.ei.mpoi.objects.PointListAdapter;
import ipl.ei.mpoi.objects.PointMap;
import ipl.ei.mpoi.objects.PointOfInterest;

public class EditarMapa extends Fragment {

    private @NonNull FragmentEditarMapaBinding binding;
    private PointMap pointMap;

    public EditarMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditarMapaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*super.onViewCreated(view, savedInstanceState);
        pointMap = (PointMap)getArguments().getSerializable("pointMap");
        ((MainActivity) getActivity()).setActionBarTitle("Editar Mapa");
        binding.textViewNome.setText(pointMap.getName());
        //System.out.println(pointMap.getPoints().getFirst().getName());
        ArrayAdapter adapter = new PointListAdapter(getContext(), android.R.layout.simple_list_item_1, new ArrayList<PointOfInterest>(pointMap.getPoints()));
        binding.pointList.setAdapter(adapter);

        binding.buttonEditConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changeToMapActivity(binding.mapList);
            }
        });

        binding.buttonEditConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditarMapa.this).navigate(R.id.action_editarMapa2_to_mapas);
            }
        });

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditarMapa.this).navigate(R.id.action_editarMapa2_to_mapas);
            }
        });*/
    }
}