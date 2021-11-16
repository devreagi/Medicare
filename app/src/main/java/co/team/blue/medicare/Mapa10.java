package co.team.blue.medicare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.team.blue.medicare.databinding.ActivityMapa10Binding;

public class Mapa10 extends FragmentActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private static final String TAG = "Map10";
    private static int FROM_REQUEST_CODE = 1;
    private static int TO_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private ActivityMapa10Binding binding;
    private ArrayList<Marker> tmpRealTimeMarker = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    BottomNavigationView bottomNavigationView;

    TextView textView1, textView2;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapa10Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocalizacion();
        subirLatLongfirebase();

        //Asignar variables botones Onclicklistener
        btn1 = (Button) findViewById(R.id.btnFrom);
        btn2 = (Button) findViewById(R.id.btnTo);

        //linea de codigo para llamar la API key de direcciones.
        Places.initialize(getApplicationContext(), "AIzaSyB37I51erGCgt-A-Bb5zR0byNM0dgfNT6Q");
        //-----------------------------------------------------

        //Set butom FROM
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        btn1.setFocusable(false);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(Mapa10.this);
                startActivityForResult(intent, FROM_REQUEST_CODE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(Mapa10.this);
                startActivityForResult(intent, FROM_REQUEST_CODE);
            }
        });

        bottomNavigationView = findViewById(R.id.footer);
        bottomNavigationView.setSelected(true);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.pedidos) {
                startActivity(new Intent(getApplicationContext(), Pedido.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.carrito) {
                startActivity(new Intent(getApplicationContext(), Carrito.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FROM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //refresco de puntos:
    private void coutownTimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("seconds remaining: ", "" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                Toast.makeText(Mapa10.this, "Puntos actualizados", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);
            }
        }.start();
    }

    // obtiene y muestra la posicion actual en el mapa.
    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void subirLatLongfirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("latitud: ", +location.getLatitude() + "Longitud: " + location.getLongitude());
                            Map<String, Object> latlang = new HashMap<>();
                            latlang.put("latitud", location.getLatitude());
                            latlang.put("longitud", location.getLongitude());
                            mDatabase.child("usuarios").push().setValue(latlang);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mDatabase.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker marker : realTimeMarkers) {
                    marker.remove();
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MapsPojo mp = snapshot.getValue(MapsPojo.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions
                            .title("Aqui estoy")
                            .snippet("Repartidor")
                            .position(new LatLng(latitud, longitud));
                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));
                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarker);
                coutownTimer();
            }

            LatLng colombia = new LatLng(4.340418, -74.369990);
            MarkerOptions markerOption = new MarkerOptions()
                    .position(colombia)
                    .title("Medicare")
                    .snippet("Bodega principal")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.storage));
            Marker marker = googleMap.addMarker(markerOption);

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) Mapa10.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(17)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}
