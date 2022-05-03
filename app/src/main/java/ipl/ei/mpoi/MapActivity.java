package ipl.ei.mpoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

import ipl.ei.mpoi.objects.PointMap;
import ipl.ei.mpoi.objects.PointOfInterest;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Location loc;
    private PointMap pointMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent i = getIntent();
        pointMap = i.getParcelableExtra("PointMap");
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 10);
            googleMap.animateCamera(cameraUpdate);
            findViewById(R.id.buttonAddPoint).setOnClickListener(this::clickAddPoint);
            findViewById(R.id.buttonConfirm).setOnClickListener(this::clickCreatePoint);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            return;
        }
        Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            findViewById(R.id.buttonAddPoint).setEnabled(true);
            loc = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);
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
                return;
            } else {
                Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location Pemission is necessary!", Toast.LENGTH_SHORT).show();
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
        ((EditText) findViewById(R.id.editTextLatitude)).setText(Double.toString(loc.getLatitude()));
        ((EditText) findViewById(R.id.editTextLongitude)).setText(Double.toString(loc.getLongitude()));
        ((EditText) findViewById(R.id.editTextName)).setText("");
    }

    private void clickCreatePoint(View v){
        findViewById(R.id.addPointView).setVisibility(View.GONE);
        findViewById(R.id.mapEditView).setVisibility(View.VISIBLE);
        Double lat = Double.parseDouble(((EditText) findViewById(R.id.editTextLatitude)).getText().toString());
        Double lng = Double.parseDouble(((EditText) findViewById(R.id.editTextLongitude)).getText().toString());
        LatLng latLng = new LatLng(lat, lng);
        String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        PointOfInterest newPoint = new PointOfInterest(name,latLng,"","","");
        googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
        pointMap.addPoint(newPoint);
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