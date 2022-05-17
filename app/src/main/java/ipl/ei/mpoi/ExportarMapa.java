package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import ipl.ei.mpoi.databinding.FragmentExportarMapaBinding;
import ipl.ei.mpoi.databinding.FragmentMenuBinding;
import ipl.ei.mpoi.databinding.FragmentPaginaInicalBinding;
import ipl.ei.mpoi.objects.PointMap;

public class ExportarMapa extends Fragment {

    private @NonNull FragmentExportarMapaBinding binding;

    public ExportarMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExportarMapaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setMapListAdapter(binding.exportarMapList);
        binding.exportarMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PointMap)parent.getItemAtPosition(position)).toXml(getActivity().getExternalMediaDirs()[0]);
            }
        }

        );
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ExportarMapa.this).navigate(R.id.action_exportarMapa2_to_menu);
            }
        });
    }
}