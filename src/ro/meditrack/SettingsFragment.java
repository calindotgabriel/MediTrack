package ro.meditrack;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import ro.meditrack.exception.GsonInstanceNullException;
import ro.meditrack.gson.GsonClient;


/**
 * @author motan
 * @date 8/25/14
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String PREF_IP = "pref_ip";
    public static final String PREF_PORT = "pref_port";

    public  GsonClient gsonInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferances);

        try {
             gsonInstance = GsonClient.getSimpleGsonInstance();

        } catch (GsonInstanceNullException e) {
            e.printStackTrace();
        }

        final EditTextPreference ipPref = (EditTextPreference) findPreference(PREF_IP);
        final EditTextPreference portPref = (EditTextPreference) findPreference(PREF_PORT);

        ipPref.setDefaultValue(gsonInstance.getServerIp());
        portPref.setDefaultValue(gsonInstance.getServerIp());

        ipPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newIp = ipPref.getText();
                ipPref.setDefaultValue(newIp);
                return true;
            }
        });

        portPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newPort = portPref.getText();
                portPref.setDefaultValue(newPort);
                return false;
            }
        });


    }
}
