package ro.meditrack;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment used to display the callable doctors and pharmacists.
 */
public class ContactFragment extends Fragment {
    private Button BUTON_112;
    private Button BUTON_MEDIC;
    private Button BUTON_FARMACIST;

    /**
     * Sets the action bar title.
     */
    public void setAbTitle() {
        getActivity().getActionBar().setTitle("Contact de urgenta");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAbTitle();

        View v = getView();

        BUTON_112 = (Button) v.findViewById(R.id.button1);
        BUTON_MEDIC = (Button) v.findViewById(R.id.button2);
        BUTON_FARMACIST = (Button) v.findViewById(R.id.button3);

        // If one button is clicked, call the associated number.

        BUTON_112.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0766471761"));
                startActivity(callIntent);

            }

        });

        BUTON_MEDIC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0741401091"));
                startActivity(callIntent);

            }

        });

        BUTON_FARMACIST.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0747640445"));
                startActivity(callIntent);

            }

        });
    }

}
