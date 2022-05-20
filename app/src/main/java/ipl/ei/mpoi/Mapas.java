package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.jetbrains.annotations.NotNull;

import ipl.ei.mpoi.databinding.FragmentExportarMapaBinding;
import ipl.ei.mpoi.databinding.FragmentMapasBinding;
import ipl.ei.mpoi.objects.PointMap;

public class Mapas extends Fragment {

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
        ((MainActivity) getActivity()).setMapListAdapter(binding.mapList);
        binding.mapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PointMap)parent.getItemAtPosition(position)).toXml(getActivity().getExternalMediaDirs()[0]);
            }
        }
        );

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Mapas.this).navigate(R.id.action_exportarMapa2_to_menu);
            }
        });
    }

}