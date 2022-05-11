package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipl.ei.mpoi.databinding.FragmentMenuBinding;
import ipl.ei.mpoi.databinding.FragmentSobreBinding;

public class Sobre extends Fragment {

    private @NonNull FragmentSobreBinding binding;

    public Sobre() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSobreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Sobre.this).navigate(R.id.action_sobre2_to_menu);
            }
        });
    }
}