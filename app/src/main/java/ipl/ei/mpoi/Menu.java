package ipl.ei.mpoi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipl.ei.mpoi.databinding.FragmentMenuBinding;
import ipl.ei.mpoi.databinding.FragmentPaginaInicalBinding;


public class Menu extends Fragment {

    private @NonNull FragmentMenuBinding binding;

    public Menu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.action_menu_to_mapas);
            }
        });

        binding.exportarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.action_menu_to_exportarMapa2);
            }
        });

        binding.sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.action_menu_to_sobre2);
            }
        });

        binding.voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.action_menu_to_paginaInical);
            }
        });
    }
}