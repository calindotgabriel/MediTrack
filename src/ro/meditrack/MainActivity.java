package ro.meditrack;

import android.app.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ro.meditrack.adapters.DrawerItemAdapter;
import ro.meditrack.gson.GsonClient;
import ro.meditrack.model.Item;
import ro.meditrack.model.ItemInterface;


import java.io.InputStream;
import java.util.ArrayList;

/**
 * APPLICATION ENTRY POINT
 * This Activity acts as a container for all the fragments.
 */
public class MainActivity extends Activity {


    ActionBarDrawerToggle drawerToggle;

    /**
     * Entry point method.
     * @param savedInstanceState bundle with data saved from users sessions
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<ItemInterface> drawerItems = new ArrayList<>();
        ListView drawerList = (ListView) findViewById(R.id.list);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        InputStream key = getResources().openRawResource(R.raw.android);

        initLayout(drawerItems, drawerList, drawerLayout);
        initGson(key);

        displayFragment(0, drawerLayout, drawerList);

    }

    /**
     * Initialize the main layout.
     *
     * @param drawerItems List of choices available in the drawer.
     * @param drawerList Drawer's ListView, filled with drawerItems.
     * @param drawerLayout Main Drawer container.
     */
    public void initLayout(ArrayList<ItemInterface> drawerItems,
                           ListView drawerList, DrawerLayout drawerLayout) {

        SlideMenuClickListener slideMenuClickListener =
                new SlideMenuClickListener();
        slideMenuClickListener.setDrawerLayout(drawerLayout);
        slideMenuClickListener.setDrawerList(drawerList);
        drawerList.setOnItemClickListener(slideMenuClickListener);

        addChoicesToDrawer(drawerItems);
        DrawerItemAdapter adapter = new DrawerItemAdapter(this, drawerItems);
        drawerList.setAdapter(adapter);

        setActionBar();
        initDrawerToggle(drawerLayout);

    }

    /**
     * Initialize GSON.
     *
     * @param key The Gson Key required for client - server communication.
     */
    public void initGson(InputStream key) { GsonClient.registerKeystore(key); }


    /**
     * Configures the action bar.
     */
    public void setActionBar() {
        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true); // shows the 'hamburger' at the actionbar
        }
    }

    /**
     * Add items as choices for the user.
     *
     * @param drawerItems ArrayList about to be filled with available choices.
     */
    public void addChoicesToDrawer(ArrayList<ItemInterface> drawerItems) {
        drawerItems.add(new Item("Farmacii"));
        drawerItems.add(new Item("Medicamente"));
        drawerItems.add(new Item("Harta"));
        drawerItems.add(new Item("Contact de urgenta"));
    }


    /**
     * Used for the 'toggle' feature - swipe opens the drawer.
     * @param drawerLayout The Drawer Container
     */
    public void initDrawerToggle(DrawerLayout drawerLayout) {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
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
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Displays fragments accordingly to what the user selected.
     *
     * @param position position where the user clicked, meaning the feature he wants to use
     * @param drawerLayout the drawer container
     * @param drawerList the list with options
     */
    public void displayFragment(int position, DrawerLayout drawerLayout, ListView drawerList) {
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
                    checkAndCloseDrawer(position, drawerLayout, drawerList);
                }
                break;
            }
            case 3:
                fragment = new ContactFragment();
                break;
            default:

        }

        goToFragment(fragment);
        checkAndCloseDrawer(position, drawerLayout, drawerList);

    }

    /**
     * Navigates to Fragment.
     * Puts the selected fragment in the main frame.
     * @param fragment - the picked fragment
     */
    public void goToFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
    }


/*
    TODO bugfix
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
            drawerLayout.openDrawer(mDrawerList);
    }*/

    /**
     * Close the drawer.
     *
     * @param position - the position
     * @param drawerLayout - main drawer layout
     * @param drawerList - drawer's list
     */
    public void checkAndCloseDrawer(int position, DrawerLayout drawerLayout,
                                    ListView drawerList) {
/*        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);*/
        drawerLayout.closeDrawer(drawerList);
    }

    /**
     * Called when the action bar is clicked.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        assert(drawerToggle != null);
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     *
     * Used for showing the 'hamburger' at ab.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        assert(drawerToggle != null);
        drawerToggle.syncState();
    }

    /**
     *
     * Used for showing the 'hamburger' at ab.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        assert(drawerToggle != null);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Class used for the 'slide' functionality.
     * TODO move me
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        DrawerLayout drawerLayout;
        ListView drawerList;

        public DrawerLayout getDrawerLayout() {
            return drawerLayout;
        }
        public void setDrawerLayout(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
        }

        public ListView getDrawerList() {
            return drawerList;
        }
        public void setDrawerList(ListView drawerList) {
            this.drawerList = drawerList;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            assert(getDrawerLayout() != null);
            assert(getDrawerList() != null);
            displayFragment(position, getDrawerLayout(), getDrawerList());
        }
    }


}
