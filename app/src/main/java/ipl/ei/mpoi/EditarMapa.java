package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import ipl.ei.mpoi.databinding.FragmentEditarMapaBinding;
import ipl.ei.mpoi.databinding.FragmentExportarMapaBinding;

public class EditarMapa extends Fragment {

    private @NonNull FragmentEditarMapaBinding binding;

    public EditarMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditarMapaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Editar Mapa");

        ((MainActivity) getActivity()).setMapListAdapter(binding.mapList);
        /*binding.buttonEditConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changeToMapActivity(binding.mapList);
            }
        });*/

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
        });
    }
}