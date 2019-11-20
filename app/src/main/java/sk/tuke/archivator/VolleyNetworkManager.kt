package sk.tuke.archivator

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley



class VolleyNetworkManager(val context: Context) {

    fun testCall()
    {
        val queue = Volley.newRequestQueue(context)
        val url = "https://api.openweathermap.org/data/2.5/weather?units=metric&q=Michalovce&appid=08f5d8fd385c443eeff6608c643e0bc5"
//        val url = "https://api.openweathermap.org/data/2.5/weather"
//        val url = "https://www.google.com"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener {
                Log.i("Network info:","Response is: $it")
            },
            Response.ErrorListener {
                Log.e("error in testCall","$it")
            }
        )
        {
//            override fun getParams(): MutableMap<String, String>? {
//                val params = HashMap<String, String>()
//                params.put("q", "Michalovce")
//                params.put("appid", "08f5d8fd385c443eeff6608c643e0bc5")
//                return params
//            }
        }

        queue.add(jsonObjectRequest)

    }
}