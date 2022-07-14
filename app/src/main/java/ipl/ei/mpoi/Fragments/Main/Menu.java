package ipl.ei.mpoi.Fragments.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ipl.ei.mpoi.Activities.MainActivity;
import ipl.ei.mpoi.R;
import ipl.ei.mpoi.databinding.FragmentMenuBinding;


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

        //((MainActivity) getActivity()).setActionBarTitle("Menu");

        binding.criarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Insira o nome do mapa");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().trim().equals("")){
                            Toast.makeText(getContext(), "Nome não pode estar vazio!", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }else{
                            ((MainActivity)getActivity()).changeToMapActivity(input.getText().toString().trim());
                        }

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

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

        binding.importarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Menu.this).navigate(R.id.action_menu_to_importMapa);
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