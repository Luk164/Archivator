package sk.tuke.archivator

import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import sk.tuke.archivator.ViewModels.AppViewModel
import sk.tuke.archivator.ViewModels.ItemViewModel
import java.lang.Math.toDegrees
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPrefs : SharedPreferences

    private var currentDegree = 0.0f
    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var magnetometer: Sensor



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
        appViewModel = ViewModelProviders.of(this)[AppViewModel::class.java]

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.login, R.id.mainScreen), drawer_layout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        appViewModel.username.observe(this, androidx.lifecycle.Observer {
            nav_view.getHeaderView(0).tv_username.text = it
        })

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD)
    }

    override fun onStart() {
        super.onStart()
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
            if (key == "setting_select_theme") {
                recreate()
            }
        }

        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onStop() {
        super.onStop()

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor === accelerometer) {
            lowPass(event.values, lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event.sensor === magnetometer) {
            lowPass(event.values, lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val degree = (toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360

                currentDegree = -degree

                nav_view.getHeaderView(0).tv_heading
            }
        }
    }

    fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f

        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    }
}
