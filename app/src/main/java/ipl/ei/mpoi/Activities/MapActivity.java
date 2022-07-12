package ipl.ei.mpoi.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ipl.ei.mpoi.Objects.PointMap;
import ipl.ei.mpoi.Objects.PointOfInterest;
import ipl.ei.mpoi.R;
import ipl.ei.mpoi.RecyclerViewAdapters.PointRecyclerViewAdapter;
import ipl.ei.mpoi.CallBack.SelectCallBack;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, SelectCallBack, GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private Location loc;
    private PointMap pointMap;
    private ArrayList<Marker> markers;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent i = getIntent();
        pointMap = i.getParcelableExtra("PointMap");
        requestPermissions(new String[] {Manifest.permission.MANAGE_EXTERNAL_STORAGE},3);
        findViewById(R.id.buttonAddPoint).setEnabled(false);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    public void onStart() {
        super.onStart();
        PointRecyclerViewAdapter adapter = new PointRecyclerViewAdapter(this, pointMap.getPoints(),this::selectCallBack);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView view = findViewById(R.id.pointList);
        view.setLayoutManager(linearLayoutManager);
        view.setAdapter(adapter);
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
                pointMap.setName(((EditText)findViewById(R.id.editTextNomeMapa)).getText().toString());
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
        markers = new ArrayList<>();
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
        ArrayList<PointOfInterest> points = pointMap.getPoints();
        CameraUpdate cameraUpdate;
        if(points.size() > 0){
            for (PointOfInterest point : points) {
                Marker newMarker = googleMap.addMarker(new MarkerOptions().position(point.getPosition()).title(point.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon)));

                markers.add(newMarker);

            }
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(pointMap.getPoints().get(0).getPosition(), 18);
            googleMap.animateCamera(cameraUpdate);
        }else{
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10);
        }
        googleMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
        googleMap.animateCamera(cameraUpdate);
        findViewById(R.id.buttonAddPoint).setOnClickListener(this::clickAddPoint);


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
        findViewById(R.id.buttonGravarPonto).setOnClickListener(this::clickCreatePoint);
        findViewById(R.id.buttonCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.addPointView).setVisibility(View.GONE);
                findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
            }
        });
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
        Marker newMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
        markers.add(newMarker);
        pointMap.addPoint(newPoint);
    }

    private void showPontosView(){
        ((TextView)findViewById(R.id.editTextNomeMapa)).setText(pointMap.getName());
        ((RecyclerView)findViewById(R.id.pointList)).getAdapter().notifyDataSetChanged();

        findViewById(R.id.mapEditView).setVisibility(View.GONE);
        findViewById(R.id.editarPontosView).setVisibility(View.VISIBLE);
    }

    public void removePoint(int position){
        pointMap.removePoint(position);
        Marker marker = markers.remove(position);
        marker.remove();
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


    @Override
    public void selectCallBack(int position) {
        findViewById(R.id.buttonCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.addPointView).setVisibility(View.GONE);
                findViewById(R.id.editarPontosView).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.editarPontosView).setVisibility(View.GONE);
        findViewById(R.id.addPointView).setVisibility(View.VISIBLE);
        PointOfInterest poi = pointMap.getPoints().get(position);
        setUpPointWindow(poi);
        findViewById(R.id.buttonGravarPonto).setOnClickListener(view ->{
            findViewById(R.id.addPointView).setVisibility(View.GONE);
            findViewById(R.id.editarPontosView).setVisibility(View.VISIBLE);
            poi.setCategory(((Spinner)findViewById(R.id.spinnerCategoria)).getSelectedItem().toString());
            poi.setDescription (((EditText)findViewById(R.id.descriptionInput)).getText().toString());
            poi.setName(((EditText)findViewById(R.id.nameInput)).getText().toString());
            markers.get(position).setTitle(poi.getName());
            ((RecyclerView)findViewById(R.id.pointList)).getAdapter().notifyDataSetChanged();
        });
    }

    private void setUpPointWindow(PointOfInterest poi){

        ((TextView) findViewById(R.id.textLatitude)).setText(Double.toString(poi.getLatitude()));
        ((TextView) findViewById(R.id.textLongitude)).setText(Double.toString(poi.getLongitude()));
        ((TextView) findViewById(R.id.textAltitude)).setText(Double.toString(poi.getAltitude()));
        ((EditText) findViewById(R.id.nameInput)).setText(poi.getName());
        ((EditText) findViewById(R.id.descriptionInput)).setText(poi.getDescription());
        Resources res = getResources();
        String[] cat = res.getStringArray(R.array.categorias);
        for (int i = 0; i < cat.length; i++) {
            if(cat[i].compareTo(poi.getCategory()) == 0){
                ((Spinner)findViewById(R.id.spinnerCategoria)).setSelection(i);
            }
        }

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        int position = markers.indexOf(marker);

        findViewById(R.id.buttonCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.addPointView).setVisibility(View.GONE);
                findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.mapEditView).setVisibility(View.GONE);
        findViewById(R.id.addPointView).setVisibility(View.VISIBLE);
        PointOfInterest poi = pointMap.getPoints().get(position);
        setUpPointWindow(poi);
        findViewById(R.id.buttonGravarPonto).setOnClickListener(view ->{
            findViewById(R.id.addPointView).setVisibility(View.GONE);
            findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
            poi.setCategory(((Spinner)findViewById(R.id.spinnerCategoria)).getSelectedItem().toString());
            poi.setDescription (((EditText)findViewById(R.id.descriptionInput)).getText().toString());
            poi.setName(((EditText)findViewById(R.id.nameInput)).getText().toString());
            markers.get(position).setTitle(poi.getName());
        });
    }
}