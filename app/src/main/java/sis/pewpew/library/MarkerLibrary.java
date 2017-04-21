package sis.pewpew.library;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MarkerLibrary {
    private double mLatitude[] = {105.45526, 105.57364, 105.53505, 105.45523, 105.51962, 105.77320};
    private double mLongitude[] = {9.99222, 9.88347, 9.84184, 9.77197, 9.55501, 9.67768, 9};
    List<LatLng> points = new ArrayList<>();

    {
        for (int i = 0; i < mLatitude.length - 1; i++) {
            points.add(new LatLng(mLatitude[i], mLongitude[i]));
        }
    }
}
