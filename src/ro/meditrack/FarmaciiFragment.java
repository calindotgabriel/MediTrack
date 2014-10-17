package ro.meditrack;

import android.app.*;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import ro.meditrack.adapters.FarmaciiAdapter;
import ro.meditrack.db.DbHelper;
import ro.meditrack.detectors.GpsTracker;
import ro.meditrack.detectors.InternetConnectionDetector;
import ro.meditrack.gson.GsonClient;
import ro.meditrack.model.Farmacie;
import ro.meditrack.shared.Holder;

import java.util.ArrayList;
import java.util.List;

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

    private Menu optionsMenu;

    private double lat;
    private double lng;


    private GsonClient mClient;

    private DbHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        farmacii = (ArrayList) getHelper().getRuntimeDao().queryForAll();

        adapter = new FarmaciiAdapter(getActivity(), farmacii, showOnlyCompensat, showOnlyNonstop);
        setListAdapter(adapter);

        hasInternet();
        hasGps();


        mClient = GsonClient.getInstance();
        mClient.setContext(getActivity());


        if (farmacii.size() == 0)
            new LoadPharmacies().execute();


        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        farmacieAleasa = farmacii.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.FARMACIE_KEY, farmacieAleasa);


        Fragment farmacieDetailsFragment = new FarmacieDetailsFragment();
        farmacieDetailsFragment.setArguments(bundle);
        goToFragment(farmacieDetailsFragment);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (item.getItemId()) {
/*            case R.id.filtru_compensat:
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
            case R.id.filtru_nonstop: //TODO
                nonstopClickCount++;

                if (nonstopClickCount % 2 == 0) {
                    showOnlyNonstop = true;
                    Toast.makeText(getActivity(), "Open On", Toast.LENGTH_SHORT).show();
                } else {
                    showOnlyNonstop = false;
                    Toast.makeText(getActivity(), "Open Off", Toast.LENGTH_SHORT).show();
                }

                onCreateView(inflater, null, null);
                return true;*/

            case R.id.refresh:

                //DB IN CREATE MODE
                if (mClient != null)
                    mClient.clearPharmacies();
                new LoadPharmacies().execute();
                return true;

            case R.id.settings:
                goToFragment(new SettingsFragment());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    class LoadPharmacies extends AsyncTask<Void, Void, List<Farmacie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mClient.clearPharmacies();

            setRefreshActionButtonState(true);
        }

        @Override
        protected List<Farmacie> doInBackground(Void... params) {

            mClient.getPharmaciesFromSv("Querying...", lat, lng);

            List<Farmacie> pharmacies;

            do {
                pharmacies = mClient.getPharmacies();
            } while (pharmacies == null);

            return pharmacies;
        }

        @Override
        protected void onPostExecute(List<Farmacie> pharmacies) {
            super.onPostExecute(pharmacies);

            RuntimeExceptionDao<Farmacie, Integer> mDao = getHelper().getRuntimeDao();

            getHelper().resetDb();

            for (Farmacie f : pharmacies) {
                f.setIcon(R.drawable.ic_da_compensat);
                mDao.create(f);
            }

            setRefreshActionButtonState(false);

            adapter.updatePharmacyList(pharmacies);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwingRightInAnimationAdapter swingRightInAnimationAdapter
                = new SwingRightInAnimationAdapter(adapter);
        swingRightInAnimationAdapter.setAbsListView(getListView());

        getListView().setAdapter(swingRightInAnimationAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;
        inflater.inflate(R.menu.menu_farmacii, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar ab = getActivity().getActionBar();
        if (ab != null)
            ab.setTitle("Farmacii");
    }



    public boolean hasGps() {
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lng = gpsTracker.getLongitude();
            Toast.makeText(getActivity(), lat + " / " + lng, Toast.LENGTH_SHORT).show();
            Holder.lat = lat;
            Holder.lng = lng;
            return true;
        }
        else {
            Toast.makeText(getActivity(), "No GPS! Cannot query pharmacies!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public boolean hasInternet() {
        InternetConnectionDetector net = new InternetConnectionDetector(getActivity());
        if (net.isConnectingToInternet())
            return true;
        else {
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DbHelper.class);
        }
        return dbHelper;
    }

    public void goToFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_container, fragment).commit();
    }

}




