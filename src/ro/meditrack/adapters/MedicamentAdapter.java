package ro.meditrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ro.meditrack.R;
import ro.meditrack.model.Medicament;

import java.util.ArrayList;

/**
 * Created by motan on 3/8/14.
 */
public class MedicamentAdapter extends ArrayAdapter<Medicament> {

    private ArrayList<Medicament> medicamente;
    private ArrayList<Medicament> medicamenteAll;
    private ArrayList<Medicament> suggestions;
    private int viewResourceId;

    public MedicamentAdapter(Context context, int viewResourceId, ArrayList<Medicament> medicamente) {
        super(context, viewResourceId, medicamente);
        this.medicamente = medicamente;
        this.medicamenteAll = (ArrayList<Medicament>) medicamente.clone();
        this.suggestions = new ArrayList<Medicament>();
        this.viewResourceId = viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  layoutInflater.inflate(viewResourceId, null);
        }

        Medicament medicament = medicamente.get(position);

        if (medicament != null) {
            TextView medicamentText = (TextView) convertView.findViewById(R.id.dropdown_item_medicament);

            if (medicamentText != null) {
                medicamentText.setText(medicament.getItemDescription());
            }
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Medicament medicament : medicamenteAll) {
                    if (medicament.getItemDescription().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(medicament);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            else return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Medicament> filteredList = (ArrayList<Medicament>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (Medicament m : filteredList) {
                    add(m);
                }
                notifyDataSetChanged();
            }
        }
    };

}







