package ipl.ei.mpoi.Fragments.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ipl.ei.mpoi.R;
import ipl.ei.mpoi.databinding.FragmentImportMapaBinding;
import ipl.ei.mpoi.RecyclerViewAdapters.ImportRecyclerViewAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImportMapa extends Fragment {


    private @NonNull
    FragmentImportMapaBinding binding;

    public ImportMapa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImportMapaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapas();
        binding.buttonVoltarImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ImportMapa.this).navigate(R.id.action_importMapa_to_menu);
            }
        });

        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMapas();
            }
        });
    }

    private void getMapas(){
        binding.progressBarImport.setVisibility(View.VISIBLE);
        Thread thread = new Thread() {
            @Override
            public void run() {
                ArrayList<String> aux = new ArrayList<>();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://mpoi-server.herokuapp.com/maps").get().build();
                try (Response response = client.newCall(request).execute()) {
                    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                    String string = response.body().string();
                    Document document = documentBuilder.parse((new InputSource(new StringReader(string))));
                    document.getDocumentElement().normalize();
                    Element mapsElement = (Element) document.getElementsByTagName("maps").item(0);
                    NodeList maps = mapsElement.getChildNodes();

                    for (int i = 0; i < maps.getLength(); i++) {
                        Node node = maps.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            aux.add(node.getFirstChild().getTextContent());
                        }
                    }
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText( getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        RecyclerView listView = binding.importMapList;
                        ImportRecyclerViewAdapter adapter = new ImportRecyclerViewAdapter(getContext(),aux);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        listView.setLayoutManager(linearLayoutManager);
                        listView.setAdapter(adapter);
                        binding.progressBarImport.setVisibility(View.GONE);
                    }
                });


            }
        };

        thread.start();

    }
}