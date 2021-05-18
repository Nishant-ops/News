 package com.example.news_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Setting_Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_2);
    }

    public static class newsPreferenceFragment extends PreferenceFragment implements  Preference.OnPreferenceChangeListener {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            Preference query = findPreference(getString(R.string.setting_query_Search));
            bindPreferenceSummaryToValue(query);

            Preference section=findPreference(getString(R.string.setting_section_search));
            bindPreferenceSummaryToValue(section);


        }

        private void bindPreferenceSummaryToValue(Preference preference) {
          preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");

            onPreferenceChange(preference, preferenceString);
        }

        public boolean onPreferenceChange(Preference preference, Object value) {
            String mag = value.toString();
            preference.setSummary(mag);
            return true;
        }
    }
}