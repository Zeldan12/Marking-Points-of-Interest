package ipl.ei.mpoi.Fragments.Map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipl.ei.mpoi.databinding.FragmentEditarMapaBinding;
import ipl.ei.mpoi.Objects.PointMap;

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

    }


}