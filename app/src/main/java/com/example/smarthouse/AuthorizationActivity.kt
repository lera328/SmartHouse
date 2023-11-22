package com.example.smarthouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.smarthouse.DB.TypesOfRoom
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AuthorizationActivity : AppCompatActivity() {
    val supabaseUrl = "https://dqjyaffhgdrfsiabzmyi.supabase.co"
    val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRxanlhZmZoZ2RyZnNpYWJ6bXlpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA1ODA3MTEsImV4cCI6MjAxNjE1NjcxMX0.HlrbHC8AFPB8UxXM6aH3IZ_qdtw_xwE6Tq1ToN6oj0M"
    var client= createSupabaseClient(
        supabaseUrl,
        supabaseKey
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)




        GlobalScope.launch  {
         test_()
        }
    }

    private suspend fun  test(){
        val city = client.postgrest["cities"].select().decodeSingle<TypesOfRoom>()
    }

    private suspend fun test_() {
        try {
            val typeOfRoom = TypesOfRoom(type = "Кухня")
            client.postgrest["typesOfRoom"].insert(
                typeOfRoom,
                returning = Returning.REPRESENTATION
            )
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(
                    this@AuthorizationActivity,
                    "Ошибка: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("aaa", e.message.toString())
            }
        }
    }
    }