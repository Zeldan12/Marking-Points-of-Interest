package ipl.ei.mpoi;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ipl.ei.mpoi.databinding.FragmentExportarMapaBinding;
import ipl.ei.mpoi.objects.PointMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        ((MainActivity) getActivity()).setActionBarTitle("Exportar Mapa");

        ((MainActivity) getActivity()).setMapListAdapter(binding.exportarMapList);

        binding.exportarMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Onde pretende Exportar?");
                /*final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);*/
                builder.setPositiveButton("Cloud", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postMap(((PointMap)parent.getItemAtPosition(position)));
                    }
                });
                builder.setNegativeButton("Local", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PointMap)parent.getItemAtPosition(position)). toXml(getActivity().getExternalMediaDirs()[0]);
                    }
                });
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
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

    private void postMap(PointMap pointMap){
        String xmlContent = pointMap.toXml();
        if(xmlContent != null){
            MediaType XML = MediaType.parse("application/xml; charset=utf-8");
            RequestBody body = RequestBody.create(xmlContent,XML);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().header("Content-Type","application/xml").url("https://mpoi-server.herokuapp.com/maps").post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText( getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    /*if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    } else {
                        Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_LONG).show();
                    }*/
                    if(response.code() == 200){
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Upload Completo", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else if(response.code() == 409){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Nome já em uso, pretende dar um novo nome ou substituir o mapa?");
                        builder.setPositiveButton("Novo Nome", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Insira o novo nome do mapa");
                                final EditText input = new EditText(getContext());
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);
                                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postMap(pointMap);
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
                        builder.setNeutralButton("Substituir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MediaType XML = MediaType.parse("application/xml; charset=utf-8");
                                RequestBody body = RequestBody.create(xmlContent,XML);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().header("Content-Type","application/xml").url("https://mpoi-server.herokuapp.com/maps").put(body).build();
                                client.newCall(request).enqueue(new Callback() {

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText( getContext(), "Mapa Atualizado", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText( getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                builder.show();
                            }
                        });

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            });
        }
    }
}