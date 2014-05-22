package ro.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import ro.meditrack.adapters.FarmaciiAdapter;
import ro.meditrack.db.DatabaseHandler;
import ro.meditrack.model.Farmacie;

import java.util.ArrayList;

/**
 * Pharmacies fragment.
 */
public class FarmaciiFragment extends ListFragment {


    private ArrayList<Farmacie> farmacii;
    private FarmaciiAdapter adapter;
    private Farmacie farmacieAleasa;

    private boolean showOnlyCompensat = false;
    private boolean showOnlyNonstop = false;

    private int compensatClickCount = 1;
    private int nonstopClickCount = 1;


    public void setAbTitle() {
        getActivity().getActionBar().setTitle("Farmacii");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_farmacii, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void initializeAdapter() {
        adapter = new FarmaciiAdapter(getActivity().getApplicationContext(), farmacii, showOnlyCompensat, showOnlyNonstop);
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAbTitle();

    }

    public void getPharmaciesFromDb() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        farmacii = (ArrayList) db.getAllFarmacii();

        Log.d("FarmaciiFragment", "Queried pharmacies from DB!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        populateFarmaciiFromDB();
        // ar trebui aici sa fac call la server
/*
        final InputStream inputStream = getResources().openRawResource(R.raw.android);
        startGps();*/


//        GsonClient gson = GsonClient.getSimpleInstance();
//        Log.v("GsonClient", "QUERYING MEDIPLACES IN FARMACIIFRAGMENT!");


        getPharmaciesFromDb();

        initializeAdapter();


        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        farmacieAleasa = farmacii.get(position);

        Fragment fragment = new FarmacieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nume_farmacie", farmacieAleasa.getName());
        bundle.putStringArray("orar_farmacie", farmacieAleasa.getOpenHours());
        bundle.putString("adresa_farmacie", farmacieAleasa.getVicinity());
        bundle.putDouble("lat_farmacie", farmacieAleasa.getLat());
        bundle.putDouble("lng_farmacie", farmacieAleasa.getLng());
        bundle.putInt("compensat_farmacie", farmacieAleasa.getCompensat());
        bundle.putString("ph_no_farmacie", farmacieAleasa.getPhNumber());
        bundle.putString("url_farmacie", farmacieAleasa.getPhNumber());
        bundle.putBoolean("open_now_farmacie", farmacieAleasa.getOpenNow());


        Log.d("COORDS 1", farmacieAleasa.getLat() + " / " + farmacieAleasa.getLng());


        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (item.getItemId()) {
            case R.id.filtru_compensat:
                compensatClickCount++;

                if (compensatClickCount % 2 == 0) {
                    showOnlyCompensat = true;
                    Toast.makeText(getActivity(), "Compensat On", Toast.LENGTH_SHORT).show();
                } else {
                    showOnlyCompensat = false;
                    Toast.makeText(getActivity(), "Compensat Off", Toast.LENGTH_SHORT).show();
                }

                onCreateView(inflater, null, null);
                return true;
            case R.id.filtru_nonstop:
                nonstopClickCount++;

                if (nonstopClickCount % 2 == 0) {
                    showOnlyNonstop = true;
                    Toast.makeText(getActivity(), "Open On", Toast.LENGTH_SHORT).show();
                } else {
                    showOnlyNonstop = false;
                    Toast.makeText(getActivity(), "Open Off", Toast.LENGTH_SHORT).show();
                }

                onCreateView(inflater, null, null);
                return true;
            case R.id.refresh:
                // TODO
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


// ***************************************** DUMP *********************************************************************

/*    public void populateFarmaciiFromDB() {
        Resources resources = getResources();
        // We are using resources, the goal is to use a web server.

        DatabaseHandler db = DatabaseHandler.getInstance(getActivity());

        if (db.isFarmaciiTableEmpty()) {
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_sensiblu1),
                resources.getStringArray(R.array.orar_sensiblu1),
                resources.getString(R.string.adresa_sensiblu1),
                R.drawable.ic_sensiblu,45.4149468,28.0154429, 0));
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_sensiblu2),
                resources.getStringArray(R.array.orar_sensiblu2),
                resources.getString(R.string.adresa_sensiblu2),
                R.drawable.ic_sensiblu, 45.434956,28.0242761, 0));
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_sensiblu3),
                resources.getStringArray(R.array.orar_sensiblu3),
                resources.getString(R.string.adresa_sensiblu3),
                R.drawable.ic_sensiblu, 45.4337246,28.0176464, 0));
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_centrofarm),
                resources.getStringArray(R.array.orar_centrofarm),
                resources.getString(R.string.adresa_centrofarm),
                R.drawable.ic_sensiblu, 45.42842,28.036207, 1));
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_caroldavila),
                resources.getStringArray(R.array.orar_caroldavila),
                resources.getString(R.string.adresa_caroldavila),
                R.drawable.ic_sensiblu, 45.4412903,28.0562351, 1));
        db.addFarmacie(new Farmacie(resources.getString(R.string.farmacie_myosotis1),
                resources.getStringArray(R.array.orar_myosotis1),
                resources.getString(R.string.adresa_myosotis1),
                R.drawable.ic_sensiblu, 45.4537705,28.0255884, 1));
        }


        List<Farmacie> listaFarmacii = db.getAllFarmacii();

        farmacii = new ArrayList<Farmacie>(listaFarmacii);


    }

        public void startGps() {

        gpsTracker = new GpsTracker(getActivity());

        if (gpsTracker.canGetLocation()) {
            Toast.makeText(getActivity(), gpsTracker.getLatitude() + " / " + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        }

    }
    */
