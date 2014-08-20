package ro.meditrack;

import android.app.*;
import android.content.Context;
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
import ro.meditrack.detectors.GpsTracker;
import ro.meditrack.detectors.InternetConnectionDetector;
import ro.meditrack.exception.GsonInstanceNullException;
import ro.meditrack.gson.GsonClient;
import ro.meditrack.gson.GsonHandler;
import ro.meditrack.model.Farmacie;
import ro.meditrack.model.Item;
import ro.meditrack.model.ItemInterface;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity with drawer and frame for fragments.
 */
public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<ItemInterface> drawerItems;
    private Context mCtx;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCtx = this;

        initializeLayout();
        setActionBar();
        addItemsToDrawer();
        initializeAdapter();
        initializeDrawerToggle();

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
        drawerItems = new ArrayList<ItemInterface>();
        drawerItems.add(new Item("Farmacii"));
        drawerItems.add(new Item("Medicamente"));
        drawerItems.add(new Item("Harta"));
        drawerItems.add(new Item("Contact de urgenta"));
    }


    public void initializeAdapter() {
        DrawerItemAdapter adapter = new DrawerItemAdapter(mCtx, drawerItems);
        mDrawerList.setAdapter(adapter);
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


    public void displayView(int position) {
        boolean showMap = true;

        if (position == 2) {
            Fragment hartaFragment = getFragmentManager().findFragmentById(R.id.map);
            if (hartaFragment != null)
                if (hartaFragment.isVisible()) {
                    showMap = false;
                }
        }

        Fragment fragment = null;
        switch (position) {
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
                Toast.makeText(mCtx, "Ai dat click pe " + drawerItems.get(position).getItemDescription()
                        + " cu pozitia " + position
                        + " care este inca in lucru !", Toast.LENGTH_SHORT).show();
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            checkAndCloseDrawer(position);
        }
    }



    @Override
    public void onBackPressed() {

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.frame_container);

        if (currentFragment instanceof FarmacieDetailsFragment) {

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




    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }


}
