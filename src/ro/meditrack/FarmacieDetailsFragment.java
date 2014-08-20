package ro.meditrack;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import ro.meditrack.exception.GsonInstanceNullException;
import ro.meditrack.gson.GsonClient;
import ro.meditrack.model.Farmacie;
import ro.meditrack.picasso.CircleTransform;
import ro.meditrack.shared.Holder;
import ro.meditrack.utils.Distance;

/**
 * Detail fragment, displayed when one Pharmacy from FarmaciiFragment is clicked.
 */
public class FarmacieDetailsFragment extends Fragment {


    public FarmacieDetailsFragment() {
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



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        final Farmacie f = (Farmacie) bundle.getSerializable(Keys.FARMACIE_KEY);

        View v = getView();

        try {
            TextView name = (TextView) v.findViewById(R.id.item_detail_header);
            name.setText(f.getName());

            TextView adress = (TextView) v.findViewById(R.id.item_detail_subheader);
            adress.setText(f.getVicinity());

            ImageView farmacieDetailIcon = (ImageView) v.findViewById(R.id.farmacie_detail_icon);
            Picasso.with(getActivity())
                    .load(R.drawable.ic_compensat_da)
                    .transform(new CircleTransform())
                    .into(farmacieDetailIcon);

            TextView luniVineri = (TextView) v.findViewById(R.id.item_program_subheader_lv);
            luniVineri.setText("L-V: " + f.getOpenHours()[0]);
            TextView sambata = (TextView) v.findViewById(R.id.item_program_subheader_s);
            sambata.setText("S: " + f.getOpenHours()[1]);
            TextView duminica = (TextView) v.findViewById(R.id.item_program_subheader_d);
            duminica.setText("D: " + f.getOpenHours()[2]);

            final TextView compensatDaLabel = (TextView) v.findViewById(R.id.label_da_compensat);
            compensatDaLabel.setText(f.getCompensatDa() + " persoane spun ca da");
            final TextView comepnsatNuLabel = (TextView) v.findViewById(R.id.label_nu_compensat);
            comepnsatNuLabel.setText(f.getCompensatNu() + " persoane spun ca nu");

            TextView labelGoMap = (TextView) v.findViewById(R.id.label_go_map);
            labelGoMap.setText("distanta de " + Distance.distFrom(Holder.lat, Holder.lng,
                                                                  f.getLat(), f.getLng()) + " km.");

            ImageView compensatDa = (ImageView) v.findViewById(R.id.da_compensat);

            compensatDa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        GsonClient gsonClient = GsonClient.getSimpleInstance();
                        gsonClient.contactCompensatField(f.getPlacesId(), true);

                        f.setCompensatDa(f.getCompensatDa() + 1);
                        compensatDaLabel.setText(f.getCompensatDa() + " persoane spun ca da");

                    } catch (GsonInstanceNullException e) {
                        e.printStackTrace();
                    }
                }
            });

            ImageView compensatNu = (ImageView) v.findViewById(R.id.nu_compensat);

            compensatNu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        GsonClient gsonClient = GsonClient.getSimpleInstance();
                        gsonClient.contactCompensatField(f.getPlacesId(), false);

                        f.setCompensatNu(f.getCompensatNu() + 1);
                        comepnsatNuLabel.setText(f.getCompensatNu() + " persoane spun ca nu");

                    } catch (GsonInstanceNullException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

    }



}
