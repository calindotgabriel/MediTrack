package ro.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import ro.meditrack.db.DbHelper;
import ro.meditrack.detectors.GpsTracker;
import ro.meditrack.model.Farmacie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by motan on 3/11/14.
 */
public class HartaFragment extends Fragment {
    double chosenPharmacytLat;
    double chosenPharmacyLng;
    int compensat;
    private GoogleMap map;
    private double currentLatitude;
    private double currentLongitude;
    private GpsTracker gps;

    private DbHelper dbHelper;

    private ArrayList<Farmacie> farmacii;


    /*
       Setting the title of the action bar
     ***/
    public void setAbTitle() {
        getActivity().getActionBar().setTitle("Harta");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_main, null, false);
    }

    public void beginTransaction(int id) {
        Toast.makeText(getActivity(), farmacii.get(id).getName(), Toast.LENGTH_SHORT).show();
        Log.d("HartaFragment", farmacii.get(id).getName());

        Farmacie farmacieAleasa = farmacii.get(id);

        Fragment fragment = new FarmacieDetailsFragment();


        Bundle bundle = new Bundle();
        bundle.putString("nume_farmacie", farmacieAleasa.getName());
        bundle.putStringArray("orar_farmacie", farmacieAleasa.getOpenHours());
        bundle.putString("adresa_farmacie", farmacieAleasa.getVicinity());
        bundle.putDouble("lat_farmacie", farmacieAleasa.getLat());
        bundle.putDouble("lng_farmacie", farmacieAleasa.getLng());
//        bundle.putInt("compensat_farmacie", farmacieAleasa.getCompensat());
        bundle.putString("ph_no_farmacie", farmacieAleasa.getPhNumber());
        bundle.putString("url_farmacie", farmacieAleasa.getPhNumber());
        bundle.putBoolean("open_now_farmacie", farmacieAleasa.getOpenNow());

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_container, fragment).commit();
    }

    public int parseId(String oldId) {//todo replace
        return Integer.parseInt(oldId.replace("m", ""));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAbTitle();

        map = getGoogleMap();
        map.setMyLocationEnabled(true);
/*        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                beginTransaction(parseId(marker.getId()));
//                Toast.makeText(getActivity(), marker.getId(), Toast.LENGTH_LONG).show();
                return false;
            }
        });*/

        gps = new GpsTracker(getActivity());
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
            gps.setLatitude(currentLatitude);
            gps.setLongitude(currentLongitude);
        }

//        addFarmaciiMarkers();

        Bundle bundle = getArguments();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15));

        if (bundle != null) {
            getInfoFromBundle(bundle);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(chosenPharmacytLat, chosenPharmacyLng), 15));
        } else
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15));

        gps.stopUsingGPS();


        List<Farmacie> farmacies = getHelper().getRuntimeDao().queryForAll();

        for (Farmacie f : farmacies) {

            BitmapDescriptor colorBitmapDescriptor =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

            map.addMarker(new MarkerOptions()
                    .title(f.getName())
                    .snippet(f.getVicinity())
                    .position(new LatLng(f.getLat(), f.getLng()))
                    .icon(colorBitmapDescriptor));

        }

    }

    public void getInfoFromBundle(Bundle bundle) {
        chosenPharmacytLat = bundle.getDouble("lat");
        chosenPharmacyLng = bundle.getDouble("lng");
        compensat = bundle.getInt("compensat");

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(chosenPharmacytLat, chosenPharmacyLng), 15));
    }

    /*
    Adding markers on the farmacies that are in the database
***/
    /*public void addFarmaciiMarkers() {
        DatabaseHandler db = DatabaseHandler.getInstance(getActivity());
        List<Farmacie> listaFarmacii = db.getAllFarmacii();
        farmacii = new ArrayList<Farmacie>(listaFarmacii);


*//*
        map.addMarker(new MarkerOptions()
                .title("You are here!")
//                    .snippet("")
                .position(new LatLng(currentLatitude, currentLongitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
*//*


        for (Farmacie f : farmacii) {
            BitmapDescriptor colorBitmapDescriptor;
            if (f.isNonstop())
                colorBitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            else
                colorBitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

            map.addMarker(new MarkerOptions()
                    .title(f.getName())
//                    .snippet("")
                    .position(new LatLng(f.getLat(), f.getLng()))
                    .icon(colorBitmapDescriptor));
        }

*/
//    }



    public GoogleMap getGoogleMap() {
        if (map == null && getActivity() != null && getActivity().getFragmentManager() != null) {
            MapFragment mf = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            if (mf != null) {
                map = mf.getMap();
            }
        }
        return map;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }

        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }


    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DbHelper.class);
        }
        return dbHelper;
    }




}
