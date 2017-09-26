package com.example.rivios_.sportapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap map;
    private GeoApiContext geoCtx;
    private Geocoder geocoder;

    private static final LatLng cro = new LatLng(44.7380161, 18.1921251);

    ArrayList<Marker> checkpoints = new ArrayList<Marker>();
    ArrayList<Polyline> parts = new ArrayList<Polyline>();
    long routeDistance = 0;
    String routeEncoded = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        geoCtx = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_directions_key))
                .queryRateLimit(3)
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build();
        if (Geocoder.isPresent())
        {
            geocoder = new Geocoder(this, Locale.getDefault());
        }
        else
        {
            geocoder = null;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(convert(cro), 6));
        map.setOnMapClickListener(this);
    }

    private com.google.android.gms.maps.model.LatLng convert(LatLng ll)
    {
        return new com.google.android.gms.maps.model.LatLng(ll.lat, ll.lng);
    }

    @Override
    public void onMapClick(com.google.android.gms.maps.model.LatLng latLng) {
        MarkerOptions opt = new MarkerOptions().position(latLng);
        if (checkpoints.size() == 0) {
            opt.title("Start");
        } else {
            opt.title("Stanica " + checkpoints.size());
        }

        Marker lastCheckpoint;
        try {
            lastCheckpoint = checkpoints.get(checkpoints.size() - 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            lastCheckpoint = null;
        }

        if (lastCheckpoint != null) {
            DirectionsResult results;
            try {
                results = DirectionsApi.newRequest(geoCtx)
                        .mode(TravelMode.WALKING)
                        .origin(new LatLng(lastCheckpoint.getPosition().latitude, lastCheckpoint.getPosition().longitude))
                        .destination(new LatLng(latLng.latitude, latLng.longitude))
                        .await();
            } catch (Exception e) {
                Log.d("PERO", "Nesto ne valja s mapom.");
                Log.d("PERO", e.toString());
                return;
            }

            ArrayList<com.google.android.gms.maps.model.LatLng> decodedPath = new ArrayList<>();
            for (LatLng ll : results.routes[0].overviewPolyline.decodePath()) {
                decodedPath.add(convert(ll));
            }
            routeDistance += results.routes[0].legs[0].distance.inMeters;
            routeEncoded += results.routes[0].overviewPolyline.getEncodedPath();

            Log.d("PERO", "Dist " + Long.toString(routeDistance));

            Polyline newPart = map.addPolyline(new PolylineOptions().addAll(decodedPath));
            parts.add(newPart);
        }

        opt.snippet(getAddress(latLng.latitude, latLng.longitude));
        Marker newCheckpoint = map.addMarker(opt);
        checkpoints.add(newCheckpoint);
    }

    public void undoMarker(View v) {
        if (checkpoints.size() > 0) {
            checkpoints.get(checkpoints.size() - 1).remove();
            checkpoints.remove(checkpoints.size() - 1);
        }

        if (parts.size() > 0) {
            parts.get(parts.size() - 1).remove();
            parts.remove(parts.size() - 1);
        }
    }

    public void saveRoute(View v)
    {
        if (parts.size() == 0)
        {
            Toast.makeText(this, "Nije unešena ruta.", Toast.LENGTH_SHORT).show();
            return;
        }

        String start = checkpoints.get(0).getSnippet();
        String finish = checkpoints.get(checkpoints.size() - 1).getSnippet();

        Log.d("PERO", "Dist " + Long.toString(routeDistance));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.ROUTE_TAG, routeEncoded);
        resultIntent.putExtra(Constants.DIST_TAG, routeDistance);
        resultIntent.putExtra(Constants.START_TAG, start);
        resultIntent.putExtra(Constants.FINISH_TAG, finish);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    public String getAddress(double lat, double lng)
    {
        try {
            List<Address> addr = geocoder.getFromLocation(lat, lng, 1);
            Address a = addr.get(0);

            return a.getThoroughfare() + " " + a.getSubThoroughfare() + ", " + a.getLocality() + ", " + a.getCountryName();
        }
        catch (IOException e)
        {
            Log.d("PERO", "Greška geokodera");
            return null;
        }
    }
}
