package sk.tuke.archivator.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val username:MutableLiveData<String> = MutableLiveData("Guest")
}