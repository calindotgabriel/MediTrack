package ro.meditrack;

import android.app.*;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import ro.meditrack.adapters.DrawerItemAdapter;
import ro.meditrack.db.DatabaseHandler;
import ro.meditrack.detectors.GpsTracker;
import ro.meditrack.detectors.InternetConnectionDetector;
import ro.meditrack.gson.GsonClient;
import ro.meditrack.model.Farmacie;
import ro.meditrack.model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity with drawer and frame for fragments.
 */
public class MainActivity extends Activity {

    KeyStore keyStore;
    private String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerItemAdapter adapter;
    private ArrayList<Item> drawerItems;
    private GpsTracker gpsTracker;
    private List<Farmacie> pharmacies;
    private ProgressDialog pDialog;
    private GsonClient mClient;
    private DatabaseHandler db = new DatabaseHandler(this);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeLayout();
        setActionBar();
        addItemsToDrawer();
        initializeAdapter();
        initializeDrawerToggle();
        alertNoInternet();
        startGps();

        if (db.isFarmaciiTableEmpty())
            contactGson();
        else
            displayView(0);
    }


    public void initializeLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }


    public void setActionBar() {
        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }


    public void addItemsToDrawer() {
        drawerItems = new ArrayList<Item>();
        Item farmacii = new Item("Farmacii");
        Item medicamente = new Item("Medicamente");
        Item harta = new Item("Harta");
        Item contact = new Item("Contact de urgenta");
        drawerItems.add(farmacii);
        drawerItems.add(medicamente);
        drawerItems.add(harta);
        drawerItems.add(contact);
    }


    public void initializeAdapter() {
        adapter = new DrawerItemAdapter(getApplicationContext(), drawerItems);
        mDrawerList.setAdapter(adapter);
        //drawerItems.clear();
    }


    public void initializeDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    public void contactGson() {
        new LoadGsonPharmaciesTask().execute("Querying");
    }

    class LoadGsonPharmaciesTask extends AsyncTask<String, Integer, List<Farmacie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayView(-1);
        }

        @Override
        protected List<Farmacie> doInBackground(String... params) {

            startGson();
            mClient = GsonClient.getSimpleInstance();

            do {
                pharmacies = mClient.getPharmacies();
            } while (pharmacies == null);

            return pharmacies;
        }

        @Override
        protected void onPostExecute(List<Farmacie> pharmacies) {
            super.onPostExecute(pharmacies);

            for (Farmacie f : pharmacies) {
                f.setIcon(R.drawable.ic_sensiblu);
                db.addFarmacie(f);
            }
            db.close();

            displayView(0);
        }
    }


    public void startGson() {
        final InputStream inputStream = getResources().openRawResource(R.raw.android);

        startListening(inputStream);
        GsonClient.getInstance(keyStore).sendInfo("Aplicatia MediTrack comunica cu tine, server!",
                gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }



    public void displayView(int position) {
        boolean showMap = true;

        Fragment hartaFragment = getFragmentManager().findFragmentById(R.id.map);
        if (hartaFragment != null)
            if (hartaFragment.isVisible()) {
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.remove(hartaFragment);
                ft.commit();*/
                showMap = false;
            }

        Fragment fragment = null;
        switch (position) {
/*            case -2:
                fragment = new NoPharmaciesFragment();*/
            case -1:
                fragment = new LoadingFragment();
                break;
            case 0:
                fragment = new FarmaciiFragment();
                break;
            case 1:
                fragment = new MedicamenteFragment();
                break;
            case 2: {
                if (showMap)
                    fragment = new HartaFragment();
                else {
                    checkAndCloseDrawer(position);
                }
                break;
            }
            case 3:
                fragment = new ContactFragment();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Ai dat click pe " + drawerItems.get(position).getName()
                        + " cu pozitia " + position
                        + " care este inca in lucru !", Toast.LENGTH_SHORT).show();
        }


        if (fragment != null) { //TODO glabalize fragmentmanager
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            checkAndCloseDrawer(position);

        }
    }

    public void alertNoInternet() {
        InternetConnectionDetector net = new InternetConnectionDetector(this);
        if (!net.isConnectingToInternet()) {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startListening(InputStream inputStream) {

        KeyStore localTrustStore;

        try {
            localTrustStore = KeyStore.getInstance("BKS");
            localTrustStore.load(inputStream, "123456".toCharArray());
            this.keyStore = localTrustStore;

        } catch (KeyStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CertificateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void startGps() {
        gpsTracker = new GpsTracker(this);
        if (!gpsTracker.canGetLocation())
            Toast.makeText(this, "No connection detected, please turn GPS on!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.frame_container);

        if (currentFragment instanceof FarmacieDetailsFragment) {
//            super.onBackPressed();

            Fragment fragment = new FarmaciiFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frame_container, fragment).commit();

        } else
            mDrawerLayout.openDrawer(mDrawerList);
    }

    public void checkAndCloseDrawer(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


/*
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                contactGson();
                displayView(-1);
                DatabaseHandler db = new DatabaseHandler(getApplicationContext()); //TODO globalize db Handler
                db.dropDB();
                return true;
            }

            default:
            return false;

        }
    }*/

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }


}
