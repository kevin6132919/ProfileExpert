package edu.tongji.sse.profileexpert.main;

import edu.tongji.sse.profileexpert.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CreateProfileActivity extends PreferenceActivity
{
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.create_profile_preferences);
		setContentView(R.layout.create_profile_display);
    }
}
