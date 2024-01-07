import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseInit : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
