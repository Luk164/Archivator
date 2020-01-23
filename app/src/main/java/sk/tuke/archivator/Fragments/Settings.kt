package sk.tuke.archivator.Fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sk.tuke.archivator.R

class Settings : PreferenceFragmentCompat()
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }
}