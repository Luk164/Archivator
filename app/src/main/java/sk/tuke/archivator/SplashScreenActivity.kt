package sk.tuke.archivator

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPrefs : SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPrefs.getString("setting_select_theme", "Light")) {
            "Light" -> setTheme(R.style.lightTheme)
            "Dark" -> setTheme(R.style.darkTheme)
            "(AMOLED) Dark" -> setTheme(R.style.darkThemeAmoled)
            "Exp1" -> setTheme(R.style.exp1)
            else -> setTheme(R.style.lightTheme)
        }

        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            //finish this activity
            finish()
        },1000)

    }
}