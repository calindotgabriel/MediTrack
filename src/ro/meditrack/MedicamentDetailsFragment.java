package ro.meditrack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.w3c.dom.Text;

/**
 * Created by motan on 2/26/14.
 */
public class MedicamentDetailsFragment extends Fragment {

    public MedicamentDetailsFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medicament, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String nume_medicament = bundle.getString("nume_medicament");
        String descriere_medicament = bundle.getString("descriere_medicament");


        TextView medicamentName = (TextView) getView().findViewById(R.id.nume_medicament);
        TextView medicamentDescriere = (TextView) getView().findViewById(R.id.descriere_medicament);

        medicamentName.setText(nume_medicament);
        if (descriere_medicament != null)
            medicamentDescriere.setText(descriere_medicament);
        else
            medicamentDescriere.setText("Descriere in lucru");

    }
}
