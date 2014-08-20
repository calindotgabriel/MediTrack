package ro.meditrack.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import ro.meditrack.R;
import ro.meditrack.model.Farmacie;
import ro.meditrack.picasso.CircleTransform;
import ro.meditrack.utils.DayProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to model a Pharmacy type entity.
 */
public class FarmaciiAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Farmacie> mFarmacies;
    private boolean showOnlyCompensat;
    private boolean showOnlyNonstop;

    TextView farmacieText;
//    TextView farmacieOrar;
    TextView farmacieAdresa;
    ImageView farmacieIcon;

    Farmacie farmacie;

    /**
     * Class constructor
     * @param context application context
     * @param mFarmacies arraylist filled with pharmacies
     * @param showOnlyCompensat if true, show only pharmacies who give prescriptions.
     * @param showOnlyNonstop if true, show only pharmacies who are non-stop
     */
    public FarmaciiAdapter(Context context, ArrayList<Farmacie> mFarmacies,
                           boolean showOnlyCompensat, boolean showOnlyNonstop) {
        this.context = context;
        this.mFarmacies = mFarmacies;
        this.showOnlyCompensat = showOnlyCompensat;
        this.showOnlyNonstop = showOnlyNonstop;
    }

    @Override
    public int getCount() {
        return mFarmacies.size();
    }

    @Override
    public Object getItem(int position) {
        return mFarmacies.get(position);
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

        farmacie = mFarmacies.get(position);

//        if (!showOnlyCompensat && !showOnlyNonstop) {
//            return showFarmacie(convertView);
//        }
//        else if (showOnlyCompensat && (!showOnlyNonstop)) {
//            if (farmacie.getCompensat() == 1)
//                return showFarmacie(convertView);
//            else
//                return getEmptyView();
//        }
//        else if (!showOnlyCompensat && showOnlyNonstop) {
//            if (farmacie.isNonstop())
//                return showFarmacie(convertView);
//            else
//                return getEmptyView();
//        }
//        else {
//            // both true
//            if (farmacie.getCompensat() == 1 && farmacie.isNonstop())
//                return showFarmacie(convertView);
//            else
//                return getEmptyView();
//
//        }

        return showFarmacie(convertView);
    }
    /**
     * Return a empty view.
     */
    public View getEmptyView() {
       return View.inflate(context, R.layout.empty, null);
    }


    public void updatePharmacyList(List<Farmacie> newList) {
        mFarmacies.clear();
        mFarmacies.addAll(newList);
        this.notifyDataSetChanged();
    }


    /**
     * Shows the current pharmacy
     * @return the view with the pharmacy info in it.
     */
    public View showFarmacie(View convertView) {

        if (convertView != null) {
            farmacieText = (TextView) convertView.findViewById(R.id.item_header);
            farmacieAdresa = (TextView) convertView.findViewById(R.id.item_subheader);
            farmacieIcon = (ImageView) convertView.findViewById(R.id.farmacie_icon);
        }


        if (farmacieText != null) {
            farmacieText.setText(farmacie.getItemDescription());
        }

        if (farmacieAdresa != null) {
            farmacieAdresa.setText(farmacie.getVicinity());
        }


        Picasso.with(context)
//                .load(farmacie.getIcon())
                .load(R.drawable.ic_compensat_da)
                .transform(new CircleTransform())
                .into(farmacieIcon);

        return convertView;

    }



}
