package my.lokalan.posq

import android.app.Application
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.kotzilla.sdk.analytics.koin.analytics
import my.lokalan.posq.di.initializeKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class PosqApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PosqApplication)
            analytics {
                setApiKey(BuildKonfig.KOTZILLA_KEY)
            }
        }
        Firebase.initialize(this)
        Napier.base(DebugAntilog())
    }
}