package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipl.ei.mpoi.databinding.FragmentFirstBinding;
import ipl.ei.mpoi.databinding.FragmentPaginaInicalBinding;

public class PaginaInical extends Fragment {

    private @NonNull FragmentPaginaInicalBinding binding;

    public PaginaInical() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaginaInicalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PaginaInical.this).navigate(R.id.action_paginaInical_to_menu);
            }
        });
    }
}