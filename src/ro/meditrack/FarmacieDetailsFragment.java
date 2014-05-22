package ro.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import ro.meditrack.detectors.GpsTracker;

import java.math.BigDecimal;

/**
 * Detail fragment, displayed when one Pharmacy from FarmaciiFragment is clicked.
 */
public class FarmacieDetailsFragment extends Fragment {

    TextView nume;
    TextView adresa;
    TextView oraLuni;
    TextView oraMarti;
    TextView oraMiercuri;
    TextView oraJoi;
    TextView oraVineri;
    TextView oraSambata;
    TextView oraDuminica;
    TextView locatie;
    TextView compensat;

    String nume_farmacie;
    String adresa_farmacie;
    String[] orar_farmacie;

    String orarZileLucratoare;
    String orarSambata;
    String orarDuminica;

    double lat;
    double lng;

    int compensatValue;


    public FarmacieDetailsFragment() {
    }

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

    /**
     * Calculate the distance between two points in Google Map
     *
     * @param o1
     * @param o2
     * @return dist
     */
    public static float distFrom(LatLng o1, LatLng o2) {
        double lat1 = o1.latitude;
        double lng1 = o1.longitude;

        double lat2 = o2.latitude;
        double lng2 = o2.longitude;

        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Float(dist * meterConversion).floatValue();
    }

    /**
     * Set the layout of fragment
     *
     * @param inflater           system layout inflater
     * @param container          actual group of views
     * @param savedInstanceState previous information
     * @return created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_farmacie, container, false);
        return v;
    }

    /**
     * Get the information from the bundle received from father fragment
     *
     * @param bundle
     */
    public void getInfoFromFatherFragment(Bundle bundle) {
        nume_farmacie = bundle.getString("nume_farmacie");
        adresa_farmacie = bundle.getString("adresa_farmacie");
        orar_farmacie = bundle.getStringArray("orar_farmacie");
        compensatValue = bundle.getInt("compensat_farmacie");
        //TODO Class holder for keys

        orarZileLucratoare = orar_farmacie[0];
        orarSambata = orar_farmacie[1];
        orarDuminica = orar_farmacie[2];

        lat = bundle.getDouble("lat_farmacie");
        lng = bundle.getDouble("lng_farmacie");
    }

    /**
     * Round to certain number of decimals
     */
    public void populateFarmacieInfo() {
        View v = getView();

        nume = (TextView) v.findViewById(R.id.nume_farmacie);
        adresa = (TextView) v.findViewById(R.id.adresa_farmacie);

        nume.setText(nume_farmacie);
        adresa.setText(adresa_farmacie);

        oraLuni = (TextView) v.findViewById(R.id.oraluni);
        oraMarti = (TextView) v.findViewById(R.id.oramarti);
        oraMiercuri = (TextView) v.findViewById(R.id.oramiercuri);
        oraJoi = (TextView) v.findViewById(R.id.orajoi);
        oraVineri = (TextView) v.findViewById(R.id.oravineri);
        oraSambata = (TextView) v.findViewById(R.id.orasambata);
        oraDuminica = (TextView) v.findViewById(R.id.oraduminica);
        locatie = (TextView) v.findViewById(R.id.locatie);
        compensat = (TextView) v.findViewById(R.id.compensatie);

        oraLuni.setText(orarZileLucratoare);
        oraMarti.setText(orarZileLucratoare);
        oraMiercuri.setText(orarZileLucratoare);
        oraJoi.setText(orarZileLucratoare);
        oraVineri.setText(orarZileLucratoare);
        oraSambata.setText(orarSambata);
        oraDuminica.setText(orarDuminica);


        //GPS WORK
        GpsTracker gps = new GpsTracker(getActivity());
        LatLng myLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        LatLng phaLocation = new LatLng(lat, lng);

        float dist = distFrom(myLocation, phaLocation);

        locatie.setText("Distanta de " + round(dist / 1000, 2) + " km.");

        if (dist > 10.) {
            ImageView butonLocatie = (ImageView) v.findViewById(R.id.locatie_buton);
            butonLocatie.setImageResource(R.drawable.red);
        }

        if (compensatValue == 1)
            compensat.setText("Elibereaza compensat");
        else {
            compensat.setText("Nu elibereaza compensat");
            ImageView butonCompensat = (ImageView) v.findViewById(R.id.compensat_buton);
            butonCompensat.setImageResource(R.drawable.red);
        }

        ImageView goFarmacieToMap = (ImageView) v.findViewById(R.id.go_map_button);

        goFarmacieToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HartaFragment();

                Bundle bundle = new Bundle();
                bundle.putDouble("lat", lat);
                bundle.putDouble("lng", lng);
                bundle.putInt("compensat", compensatValue);

                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
    }

    /**
     * When view is created, get information from bundle
     * and populated the pharmacy with it.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            getInfoFromFatherFragment(bundle);
            populateFarmacieInfo();
        }
    }
}
