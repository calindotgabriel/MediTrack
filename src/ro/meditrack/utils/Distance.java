package ro.meditrack.utils;

import android.location.Location;

import java.math.BigDecimal;

/**
 * @author motan
 * @date 7/25/14
 */
public class Distance {

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    public static float distFrom(double localLat, double localLng, double placeLat, double placeLng) {

        Location localLocation = new Location("localLocation");
        localLocation.setLatitude(localLat);
        localLocation.setLongitude(localLng);

        Location pharmacyLocation = new Location("pharmacyLocation");
        pharmacyLocation.setLatitude(placeLat);
        pharmacyLocation.setLongitude(placeLng);

        return round(localLocation.distanceTo(pharmacyLocation)/1000 , 3);
    }
}
