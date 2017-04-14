package sis.pewpew.fragments;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap mMap;
    private final int PERMISSION_REQUEST_CODE = 5;
    private LocationManager mLocationManager;
    private LatLng mCurrentLocationLatLng;
    private LatLng mDefaultLocation = new LatLng(55.755826, 37.6173);
    private Location mLastKnownLocation;
    private MainActivity mainActivity = new MainActivity();


    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mCurrentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mainActivity.mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

        if (mainActivity.mLocationPermissionGranted) {
            mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            mLastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    0, mLocationListener);
            if (mCurrentLocationLatLng == null) {
                mCurrentLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mainActivity.mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "RESULT WORKS", Toast.LENGTH_SHORT).show();
                    mMap.addMarker(new MarkerOptions().position(mCurrentLocationLatLng).title("Your location").snippet("Finally"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(mDefaultLocation).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mainActivity.mLocationPermissionGranted = true;
                } else {
                    Toast.makeText(getActivity(),
                            "Невозможно определить местоположение. Пожалуйста, предоставьте приложению доступ  к вашей геопозиции",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.map_fragment_name));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (mainActivity.mLocationPermissionGranted) {
                    mMap.addMarker(new MarkerOptions().position(mCurrentLocationLatLng).title("Вы находитесь здесь"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(mDefaultLocation).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    Toast.makeText(getActivity(),
                            "Невозможно определить местоположение. Пожалуйста, предоставьте приложению доступ  к вашей геопозиции",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
