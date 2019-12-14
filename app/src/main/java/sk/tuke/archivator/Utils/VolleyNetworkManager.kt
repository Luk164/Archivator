package sk.tuke.archivator.Utils

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import sk.tuke.archivator.Entities.Item


class VolleyNetworkManager(val context: Context) {

    fun sendItem(item: Item)
    {
        val queue = Volley.newRequestQueue(context)
        val url = "https://parseapi.back4app.com/classes/Item"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, null,
            Response.Listener {
                Log.i("Network info:","Response is: $it")
            },
            Response.ErrorListener {
                Log.e("error in testCall","$it")
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Parse-Application-Id"] = "hlRM63ZG6nfCPDiitGOmFDWZlqgRvPPD2egIDK9y"
                headers["X-Parse-REST-API-Key"] = "yfnlhad5hK9fiBuiZmkna1dxLM6ZD3rdOw95xFgs"
                headers["Content-type"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                val parameters = HashMap<String, String>()
                parameters["Name"] = item.name
                parameters["Description"] = item.desc
                return JSONObject(parameters as Map<*, *>).toString().toByteArray()
            }
        }

        queue.add(jsonObjectRequest)
    }

    fun testCall()
    {
        val queue = Volley.newRequestQueue(context)
        val url = "https://parseapi.back4app.com/classes/Item"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, null,
            Response.Listener {
                Log.i("Network info:","Response is: $it")
            },
            Response.ErrorListener {
                Log.e("error in testCall","$it")
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Parse-Application-Id"] = "hlRM63ZG6nfCPDiitGOmFDWZlqgRvPPD2egIDK9y"
                headers["X-Parse-REST-API-Key"] = "yfnlhad5hK9fiBuiZmkna1dxLM6ZD3rdOw95xFgs"
                headers["Content-type"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                val parameters = HashMap<String, String>()
                parameters["Name"] = "SendTestName2"
                parameters["Description"] = "SendTestDesc2"
                return JSONObject(parameters as Map<*, *>).toString().toByteArray()
            }
        }

        queue.add(jsonObjectRequest)
    }
}