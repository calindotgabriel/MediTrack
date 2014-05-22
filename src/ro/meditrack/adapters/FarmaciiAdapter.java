package ro.meditrack.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ro.meditrack.R;
import ro.meditrack.model.Farmacie;
import ro.meditrack.utils.DayProvider;

import java.util.ArrayList;

/**
 * Adapter used to model a Pharmacy type entity.
 */
public class FarmaciiAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Farmacie> drawerFarmacies;
    private boolean showOnlyCompensat;
    private boolean showOnlyNonstop;

    TextView farmacieText;
    TextView farmacieOrar;
    TextView farmacieAdresa;
    ImageView farmacieIcon;

    Farmacie farmacie;

    /**
     * Class constructor
     * @param context application context
     * @param drawerFarmacies arraylist filled with pharmacies
     * @param showOnlyCompensat if true, show only pharmacies who give prescriptions.
     * @param showOnlyNonstop if true, show only pharmacies who are non-stop
     */
    public FarmaciiAdapter(Context context, ArrayList<Farmacie> drawerFarmacies,
                           boolean showOnlyCompensat, boolean showOnlyNonstop) {
        this.context = context;
        this.drawerFarmacies = drawerFarmacies;
        this.showOnlyCompensat = showOnlyCompensat;
        this.showOnlyNonstop = showOnlyNonstop;
    }

    @Override
    public int getCount() {
        return drawerFarmacies.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerFarmacies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * Gets the associated view.
     * Logic is implemented by looking at our restrictions of displaying the pharmacy list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_farmacie, null);
        }

        farmacie = drawerFarmacies.get(position);

        if (!showOnlyCompensat && !showOnlyNonstop) {
            return showFarmacie(convertView);
        }
        else if (showOnlyCompensat && (!showOnlyNonstop)) {
            if (farmacie.getCompensat() == 1)
                return showFarmacie(convertView);
            else
                return getEmptyView();
        }
        else if (!showOnlyCompensat && showOnlyNonstop) {
            if (farmacie.isNonstop())
                return showFarmacie(convertView);
            else
                return getEmptyView();
        }
        else {
            // both true
            if (farmacie.getCompensat() == 1 && farmacie.isNonstop())
                return showFarmacie(convertView);
            else
                return getEmptyView();
        }
    }
    /**
     * Return a empty view.
     */
    public View getEmptyView() {
       return View.inflate(context, R.layout.empty, null);
    }


    /**
     * Shows the current pharmacy
     * @return the view with the pharmacy info in it.
     */
    public View showFarmacie(View convertView) {

        if (convertView != null) {
            farmacieText = (TextView) convertView.findViewById(R.id.item_name);
            farmacieOrar = (TextView) convertView.findViewById(R.id.item_schedule);
            farmacieAdresa = (TextView) convertView.findViewById(R.id.farmacie_adress);
            farmacieIcon = (ImageView) convertView.findViewById(R.id.farmacie_icon);
        }


        if (farmacieText != null) {
            farmacieText.setText(farmacie
                    .getName());
        }

        if (farmacieAdresa != null) {
            farmacieAdresa.setText(farmacie
                    .getVicinity());
        }

        if (farmacieOrar != null) {
            String orarZileLucratoare = farmacie.getOpenHours()[0];

            String orarSambata = "null";
            String orarDuminica = "null";

            if (farmacie.getOpenHours().length > 1 ) {
                if (farmacie.getOpenHours()[1] != null)
                    orarSambata = farmacie.getOpenHours()[1];


                if (farmacie.getOpenHours()[2] != null)
                    orarDuminica = farmacie.getOpenHours()[2];
            }


            if (DayProvider.getConfigForDays() == 1)
                farmacieOrar.setText(orarZileLucratoare);
            else if (DayProvider.getConfigForDays() == 2)
                farmacieOrar.setText(orarSambata);
            else
                farmacieOrar.setText(orarDuminica);
        }

        if (farmacieIcon != null) {
            int icon = farmacie
                    .getIcon();

            if (icon != -1)
                farmacieIcon.setImageResource(icon);
            else
                farmacieIcon.setVisibility(View.GONE);
        }

        return convertView;

    }



}
