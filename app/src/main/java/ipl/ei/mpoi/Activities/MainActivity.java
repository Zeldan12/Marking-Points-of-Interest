package ipl.ei.mpoi.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ipl.ei.mpoi.Fragments.Main.ImportMapa;
import ipl.ei.mpoi.R;
import ipl.ei.mpoi.databinding.ActivityMainBinding;
import ipl.ei.mpoi.RecyclerViewAdapters.MapRecyclerViewAdapter;
import ipl.ei.mpoi.Objects.PointMap;
import ipl.ei.mpoi.CallBack.SelectCallBack;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<PointMap> maps;
    private int editMapIndex = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maps = new ArrayList<PointMap>();
        File file = new File(getFilesDir(),"maps.dat");
        if(file.isFile()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);

                maps = (ArrayList<PointMap>) ois.readObject();

                ois.close();
                bis.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);


        AppBarConfiguration appBarConfiguration2 = new AppBarConfiguration.Builder(R.id.menu, R.id.paginaInical,R.id.exportarMapa2, R.id.mapas,R.id.sobre2).build();

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration2);

    }

    public void onStart() {
        super.onStart();
    }


    public void setMapCardListAdapter(RecyclerView view, boolean elimButton, SelectCallBack callBack){
        MapRecyclerViewAdapter adapter = new MapRecyclerViewAdapter(this, maps, elimButton, callBack);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(linearLayoutManager);
        view.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                /*Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);*/
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(){
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void changeToMapActivity(String mapName){
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("PointMap", (Parcelable) new PointMap(mapName));
        editMapIndex = -1;
        activityResultLauncher.launch(i);
    }

    public void changeToMapActivity(PointMap map, int index){
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("PointMap", (Parcelable) map);
        editMapIndex = index;
        activityResultLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent i = result.getData();
                        PointMap pointMap = i.getParcelableExtra("PointMap");
                        if(editMapIndex == -1){
                            maps.add(pointMap);
                        }else{
                            maps.set(editMapIndex,pointMap);
                        }
                        save();
                    }
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void save(){
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
        try {
            File file = new File(getFilesDir(),"maps.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(maps);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importMap(String name){
        View loader = findViewById(R.id.progressBarImport);
        loader.setVisibility(View.VISIBLE);
        Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://mpoi-server.herokuapp.com/maps/" + name).get().build();
            try (Response response = client.newCall(request).execute()) {
                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                Document document = documentBuilder.parse((new InputSource(new StringReader(response.body().string()))));
                PointMap newMap = new PointMap(document);
                maps.add(newMap);
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void run() {
                        loader.setVisibility(View.GONE);
                        Toast.makeText( getApplicationContext(), "Mapa Importado!", Toast.LENGTH_LONG).show();
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main) ;
                        NavController navController = navHostFragment.getNavController();
                        save();
                        navController.navigate(R.id.action_importMapa_to_mapas);
                    }
                });
            } catch (IOException | ParserConfigurationException | SAXException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        loader.setVisibility(View.GONE);
                        Toast.makeText( getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    });
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void removeMap(PointMap map){
        maps.remove(map);
        save();
    }
}