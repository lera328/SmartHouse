package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
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

         GlobalScope.launch(Dispatchers.Main) { // Используем Dispatcher.Main для выполнения в основном потоке
             val imageByteArray1 = withContext(Dispatchers.IO) {

                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.condi_large)
             }
             val imageByteArray2 = withContext(Dispatchers.IO) {
                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.condi_middle)
             }
             val imageByteArray3 = withContext(Dispatchers.IO) {
                 imageManager.imageToByteArray(this@SplashScreen, R.drawable.condi_small)
             }

             dataBaseManager.addNewDeviceType(
                 this@SplashScreen,
                 "Кондиционер",
                 imageByteArray1,
                 imageByteArray2,
                 imageByteArray3
             )
         }

      //  GlobalScope.launch (Dispatchers.Main){ try{ val dataBaseManager=DataBaseManager()
      //      Log.println(Log.ERROR,"aaa",dataBaseManager.getRoomTypeId("Кухня").toString())}
      //  catch (e:Exception){
      //      Log.println(Log.ERROR,"aaa",e.message.toString())
      //  }
      //  }

        Handler().postDelayed({
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}