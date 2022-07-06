package ipl.ei.mpoi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import ipl.ei.mpoi.objects.PointListAdapter;
import ipl.ei.mpoi.objects.PointMap;
import ipl.ei.mpoi.objects.PointOfInterest;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Location loc;
    private PointMap pointMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent i = getIntent();
        pointMap = i.getParcelableExtra("PointMap");
        /*findViewById(R.id.buttonPrevious).setOnClickListener(v -> {
            try {
                test(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
        requestPermissions(new String[] {Manifest.permission.MANAGE_EXTERNAL_STORAGE},3);
        findViewById(R.id.buttonAddPoint).setEnabled(false);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    public void onStart() {
        super.onStart();
        findViewById(R.id.buttonGravar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToMainActivity(pointMap);
            }
        });

        findViewById(R.id.buttonPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToMainActivity();
            }
        });
        findViewById(R.id.buttonPontos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPontosView();

            }
        });
        findViewById(R.id.buttonEditCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.editarPontosView).setVisibility(View.GONE);
                findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.buttonEditConfirmar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.editarPontosView).setVisibility(View.GONE);
                findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeToMainActivity(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void changeToMainActivity(PointMap map){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("PointMap", (Parcelable) map);
        setResult(RESULT_OK, i);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setUpMap();
            return;
        }
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},2);

        //Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
    }

    private void setUpMap(){
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LinkedList<PointOfInterest> points = pointMap.getPoints();
        CameraUpdate cameraUpdate;
        if(points.size() > 0){
            for (PointOfInterest point : points) {
                googleMap.addMarker(new MarkerOptions().position(point.getPosition()).title(point.getName()));
            }
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(pointMap.getPoints().getFirst().getPosition(), 18);
            googleMap.animateCamera(cameraUpdate);
        }else{
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10);
        }
        googleMap.animateCamera(cameraUpdate);
        findViewById(R.id.buttonAddPoint).setOnClickListener(this::clickAddPoint);
        findViewById(R.id.buttonGravarPonto).setOnClickListener(this::clickCreatePoint);
        findViewById(R.id.buttonCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.addPointView).setVisibility(View.GONE);
                findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            findViewById(R.id.buttonAddPoint).setEnabled(true);
            loc = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            findViewById(R.id.buttonAddPoint).setEnabled(true);
            /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 20);
            googleMap.animateCamera(cameraUpdate);*/
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                setUpMap();
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
                setUpMap();
            } else {
                /*Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);*/
            }
        }
    }

    private void clickAddPoint(View v) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        findViewById(R.id.mapEditView).setVisibility(View.GONE);
        findViewById(R.id.addPointView).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textLatitude)).setText(Double.toString(loc.getLatitude()));
        ((TextView) findViewById(R.id.textLongitude)).setText(Double.toString(loc.getLongitude()));
        ((TextView) findViewById(R.id.textAltitude)).setText(Double.toString(loc.getAltitude()));
        ((EditText) findViewById(R.id.nameInput)).setText("");
        ((EditText) findViewById(R.id.descriptionInput)).setText("");
    }

    private void clickCreatePoint(View v){
        findViewById(R.id.addPointView).setVisibility(View.GONE);
        findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
        Double lat = Double.parseDouble(((TextView) findViewById(R.id.textLatitude)).getText().toString());
        Double lng = Double.parseDouble(((TextView) findViewById(R.id.textLongitude)).getText().toString());
        Double alt = Double.parseDouble(((TextView) findViewById(R.id.textAltitude)).getText().toString());
        LatLng latLng = new LatLng(lat, lng);
        String categoria = ((Spinner)findViewById(R.id.spinnerCategoria)).getSelectedItem().toString();
        String description = ((EditText)findViewById(R.id.descriptionInput)).getText().toString();
        String name = ((EditText)findViewById(R.id.nameInput)).getText().toString();

        PointOfInterest newPoint = new PointOfInterest(name,latLng.latitude,latLng.longitude,alt, categoria, description);
        googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
        pointMap.addPoint(newPoint);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void test(View v) throws IOException {
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},3);

        pointMap.toXml(getApplicationContext().getExternalMediaDirs()[0]);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }

    private void showPontosView(){
        ((TextView)findViewById(R.id.textViewNome)).setText(pointMap.getName());
        ArrayAdapter adapter = new PointListAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<PointOfInterest>(pointMap.getPoints()));
        ((ListView)findViewById(R.id.pointList)).setAdapter(adapter);
        findViewById(R.id.mapEditView).setVisibility(View.GONE);
        findViewById(R.id.editarPontosView).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}