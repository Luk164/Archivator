package sk.tuke.archivator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import sk.tuke.archivator.RoomComponents.AppDatabase
import java.text.SimpleDateFormat
import java.util.*


class VM : ViewModel()
{

}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Global.dateFormatter = SimpleDateFormat("yyyy.MM.dd G", Locale.getDefault())
        Global.db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "Archivator").fallbackToDestructiveMigration().build() //check

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.login, R.id.mainScreen), drawer_layout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

    }
}
