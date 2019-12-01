package sk.tuke.archivator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import sk.tuke.archivator.ViewModels.ItemViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPrefs.getString("setting_select_theme", "Light")) {
            "Light" -> setTheme(R.style.lightTheme)
            "Dark" -> setTheme(R.style.darkTheme)
            "(AMOLED) Dark" -> setTheme(R.style.darkThemeAmoled)
            "Exp1" -> setTheme(R.style.exp1)
            else -> setTheme(R.style.lightTheme)
        }

        Global.dateFormatter = SimpleDateFormat("yyyy.MM.dd G", Locale.getDefault())
        setContentView(R.layout.activity_main)
        itemViewModel = ViewModelProviders.of(this)[ItemViewModel::class.java]

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.login, R.id.mainScreen), drawer_layout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
            if (key == "setting_select_theme") {
                recreate()
            }
        }

        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onStop() {
        super.onStop()

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener)
    }
}
