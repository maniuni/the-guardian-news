package com.example.android.the_guardian_news;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Update the preference summary when the settings
            // activity is launched. Given the key of a
            // preference we can use the PreferenceFragment
            // findPreference() method to get the Preference object,
            // then use a helper method to setup the preference.
            Preference sectionPreference =
                    findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(sectionPreference);
        }

        /**
         * When the preference changes update the summary
         * so that the app knows it has been changed.
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // If it's a ListPreference we have to find which value
            // has been chosen by finding its index and obtaining
            // the array of values, then using it's index to extract
            // the option and set it up for the summary.
            if(preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if(prefIndex >= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /** Set the NewsPreferenceFragment instance as a listener on each preference
         * Read the current value of the preference stored in the SharedPreferences
         * on the device. Display that in the preference summary so that the user
         * can see the current value of the preference.
         * */
        private void bindPreferenceSummaryToValue(Preference preference){
            if(preference != null){
                preference.setOnPreferenceChangeListener(this);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                String preferenceString = sharedPreferences.getString(preference.getKey(), "");
                onPreferenceChange(preference, preferenceString);
            }
        }
    }
}
