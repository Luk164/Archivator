package sk.tuke.archivator

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class Settings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }

}