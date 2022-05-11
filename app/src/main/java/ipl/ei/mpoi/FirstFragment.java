package ipl.ei.mpoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import ipl.ei.mpoi.databinding.FragmentFirstBinding;
import ipl.ei.mpoi.objects.PointMap;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToMapActivity();
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    teste();
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void changeToMapActivity(){
        Intent i = new Intent(this.getActivity(), MapActivity.class);
        i.putExtra("PointMap",new PointMap("Mapa1"));
        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void teste() throws ParserConfigurationException, IOException, SAXException {
        PointMap map = new PointMap(this.getActivity().getApplicationContext().getExternalMediaDirs()[0], "PointMap-Mapa1.xml");
        Intent i = new Intent(this.getActivity(), MapActivity.class);
        i.putExtra("PointMap",map);
        startActivity(i);
    }

}