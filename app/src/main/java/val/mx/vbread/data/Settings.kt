package `val`.mx.vbread.data

import android.content.Context
import androidx.preference.PreferenceManager

class Settings(val context: Context) {

    val prefrence = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        var instance: Settings? = null;

        fun init(context: Context) {
            if (instance == null)
                instance = Settings(context)
        }
    }


    fun getResolutionScale(): Double {
        val ret = prefrence.getString("resolution_scale", "1")?.toDouble()
        return ret!!
    }

}