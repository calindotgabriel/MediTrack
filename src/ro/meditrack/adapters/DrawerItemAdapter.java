package ro.meditrack.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ro.meditrack.R;
import ro.meditrack.model.Item;

import java.util.ArrayList;

/**
 * Adapter used in our drawer to display items.
 */
public class DrawerItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> drawerItems;

    public DrawerItemAdapter(Context context, ArrayList<Item> drawerItems) {
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets the associated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item, null);
        }


        TextView itemText = null;
        if (convertView != null) {
            itemText = (TextView) convertView.findViewById(R.id.item_name);
        }


        if (itemText != null) {
            itemText.setText(drawerItems.get(position)
                    .getName());
        }

        return convertView;
    }
}