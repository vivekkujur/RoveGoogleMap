package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MainActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static ImageView imageView;
    private boolean mLocationPermissionGranted = false;
    private SlidingUpPanelLayout mLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();
        init();// current location button


        //progressbar...............................................................................
        progressBar=findViewById(R.id.progressBar);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.parseColor("#00816a"), PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);


        //DragView Layout decleration ..............................................................
        mLayout = findViewById(R.id.sliding_layout);


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: slideview ");
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting current devices location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLocation.getLatitude(),
                                            currentLocation.getLongitude()), DEFAULT_ZOOM));


                        } else {
                            Log.d(TAG, "onComplete: current location is null");

                        }
                    }
                });

            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exceptions" + e.getMessage());
        }
    }


    private void initMap() {
        // set google map Fragment .................................................................
        Log.d(TAG, "initMap: layout fragment ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

    }


    private void init() {
        Log.d(TAG, "init: onclick for current location");
        imageView = findViewById(R.id.img_gps);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });


    }


    private  void drawPolygon () {
        Log.d(TAG, "drawPolygon:  polygon created ");
    ArrayList<LatLng> inner = new ArrayList<>();
    inner.add(new LatLng(   13.974465, 76.799062));
        inner.add(new LatLng(   12.983497, 76.523388));
        inner.add(new LatLng(   12.622480, 76.781072));
        inner.add(new LatLng(   12.165753, 77.292540));
        inner.add(new LatLng(   12.150262, 77.936679));
        inner.add(new LatLng(   12.592105, 78.249826));
        inner.add(new LatLng(   13.097577, 78.354699));
        inner.add(new LatLng(   13.580837, 78.261784));
        inner.add(new LatLng(   13.919050, 77.941097));
        inner.add(new LatLng(   13.839749, 77.334965));
        inner.add(new LatLng(   13.974465, 76.799062));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(   26.761030, 68.640833)
                        ,new LatLng(   1.915376, 64.155362)
                        ,new LatLng(   2.350266, 98.080751)
                        ,new LatLng(   34.828295, 90.669646)
                        ,new LatLng(   26.761030, 68.640833))
                .addHole(inner)
                .fillColor(0x4dbbbbbb)
                .strokeWidth(2)
                .strokeColor(Color.LTGRAY));


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        //  Toast.makeText(this,"map is ready", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        drawPolygon();
        

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }


    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: request for permissions");
        String[] permission={FINE_LOCATION,COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                mLocationPermissionGranted=true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){

            case LOCATION_PERMISSION_REQUEST_CODE:{

                if (grantResults.length > 0){
                    for (int i=0; i< grantResults.length;i++){

                        if( grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }

                    mLocationPermissionGranted=true;
                    //initialize our map
                    initMap();
                }
            }

        }
    }



}
