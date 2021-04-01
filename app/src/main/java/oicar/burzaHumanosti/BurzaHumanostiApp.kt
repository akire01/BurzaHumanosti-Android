package oicar.burzaHumanosti

import android.app.Application
import android.content.Context


class BurzaHumanostiApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: BurzaHumanostiApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
