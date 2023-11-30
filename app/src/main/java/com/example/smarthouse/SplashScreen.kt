package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.Tools.SharedPreferansesManager
import com.example.smarthouse.Tools.SupabaseManager
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // время показа заставки в миллисекундах
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val imageManager = ImageManager()

        val dataBaseManager = DataBaseManager()

//         GlobalScope.launch(Dispatchers.Main) { // Используем Dispatcher.Main для выполнения в основном потоке
//             val imageByteArray1 = withContext(Dispatchers.IO) {
//
//                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.thermo_blue1)
//             }
//             val imageByteArray2 = withContext(Dispatchers.IO) {
//                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.thermo_gray1)
//             }
//             val imageByteArray3 = withContext(Dispatchers.IO) {
//                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.thermo_white1)
//             }
//
//             dataBaseManager.addNewIcon(
//                 this@SplashScreen,
//                 imageByteArray3,
//                 imageByteArray1,
//                 imageByteArray2
//             )
//         }

        val sharedPreferansesManager = SharedPreferansesManager()
        val email_ = sharedPreferansesManager.getFromSharedPreferences(this, "email")
        val pass = sharedPreferansesManager.getFromSharedPreferences(this, "pass")
        if (email_ != null && pass != null) {
            GlobalScope.launch(Dispatchers.Main) {
                SupabaseManager.getSupabaseClient().gotrue.loginWith(Email) {
                    email = email_
                    password = pass
                }
                val intent = Intent(this@SplashScreen, MakePinActivity::class.java)
                startActivity(intent)
            }
        } else {
            Handler().postDelayed({
                val intent = Intent(this, AuthorizationActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)
        }

        //  GlobalScope.launch (Dispatchers.Main){ try{ val dataBaseManager=DataBaseManager()
        //      Log.println(Log.ERROR,"aaa",dataBaseManager.getRoomTypeId("Кухня").toString())}
        //  catch (e:Exception){
        //      Log.println(Log.ERROR,"aaa",e.message.toString())
        //  }
        //  }


    }
}