package sk.tuke.archivator

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import sk.tuke.archivator.Objects.Global
import sk.tuke.archivator.Utils.VolleyNetworkManager
import sk.tuke.archivator.ViewModels.AppViewModel
import sk.tuke.archivator.ViewModels.ItemViewModel
import java.lang.Exception
import java.lang.Math.toDegrees
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPrefs : SharedPreferences

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

        Global.dateFormatter = SimpleDateFormat("dd.MM.yyyy G", Locale.getDefault())
        setContentView(R.layout.activity_main)
        Global.VNM = VolleyNetworkManager(this)
        itemViewModel = ViewModelProviders.of(this)[ItemViewModel::class.java]
        appViewModel = ViewModelProviders.of(this)[AppViewModel::class.java]

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.login, R.id.mainScreen), drawer_layout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cameraManager.cameraIdList[0]

            nav_view.getHeaderView(0).sw_flashlight.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                {
                    try {
                        cameraManager.setTorchMode(cameraId, true)
                    } catch (exception: Exception) {
                    }
                }
                else
                {
                    try {
                        cameraManager.setTorchMode(cameraId, false)

                    } catch (e: Exception) {
                        Toast.makeText(this, "ERROR: Torch failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            nav_view.getHeaderView(0).sw_flashlight.visibility = View.GONE
        }


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

        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, magnetometer)
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor === accelerometer) {
                lowPass(event.values, lastAccelerometer)
                lastAccelerometerSet = true
            } else if (event.sensor === magnetometer) {
                lowPass(event.values, lastMagnetometer)
                lastMagnetometerSet = true
            }
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val degree = (toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360

                if (sharedPrefs.getBoolean("switch_pref_simple_compass", true)) //4 wind rose
                {
                    if (315 < degree || degree <= 45)
                    {
                        nav_view.getHeaderView(0).tv_heading.text = "N"
                    }
                    else if (45 < degree && degree <= 135)
                    {
                        nav_view.getHeaderView(0).tv_heading.text = "E"
                    }
                    else if (135 < degree && degree <= 225)
                    {
                        nav_view.getHeaderView(0).tv_heading.text = "S"
                    }
                    else if (225 < degree && degree <= 315)
                    {
                        nav_view.getHeaderView(0).tv_heading.text = "W"
                    }
                }
                else //16 wind rose or degrees
                {
                    nav_view.getHeaderView(0).tv_heading.text = degree.roundToInt().toString()

//                    if (348.75 < degree && degree <= 11.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "N"
//                    } else if (11.25 < degree && degree <= 33.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "NNE"
//                    } else if (33.75 < degree && degree <= 56.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "NE"
//                    } else if (56.25 < degree && degree <= 78.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "ENE"
//                    } else if (75.75 < degree && degree <= 101.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "E"
//                    } else if (101.25 < degree && degree <= 123.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "ESE"
//                    } else if (123.75 < degree && degree <= 146.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "SE"
//                    } else if (146.25 < degree && degree <= 168.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "SSE"
//                    } else if (168.75 < degree && degree <= 191.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "S"
//                    } else if (191.25 < degree && degree <= 213.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "SSW"
//                    } else if (213.75 < degree && degree <= 236.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "SW"
//                    } else if (236.25 < degree && degree <= 258.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "WSW"
//                    } else if (258.75 < degree && degree <= 281.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "W"
//                    } else if (281.25 < degree && degree <= 303.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "WNW"
//                    } else if (303.75 < degree && degree <= 326.25) {
//                        nav_view.getHeaderView(0).tv_heading.text = "NW"
//                    } else if (326.25 < degree && degree <= 348.75) {
//                        nav_view.getHeaderView(0).tv_heading.text = "NNW"
//                    }
                }
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
