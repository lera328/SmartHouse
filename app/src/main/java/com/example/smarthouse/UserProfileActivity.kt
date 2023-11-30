package com.example.smarthouse

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.smarthouse.Tools.DataBaseManager
import com.example.smarthouse.Tools.ImageManager
import com.example.smarthouse.Tools.SupabaseManager
import com.example.smarthouse.Tools.UsersManager
import com.example.smarthouse.databinding.ActivityUserProfileBinding
import io.github.jan.supabase.gotrue.gotrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class UserProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserProfileBinding
    private val PICK_IMAGE = 1
    val userManager = UsersManager()
    val imageManager = ImageManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val user = userManager.getUser()
            val userInformation = userManager.getUserInformation()
            if (userInformation.image.length > 1) {
                val retrievedImageFile = File(this@UserProfileActivity.filesDir, "image.jpg")
                val imageManager = ImageManager()
                imageManager.byteArrayToImage(userInformation.image, retrievedImageFile)
                val bitmap = BitmapFactory.decodeFile(retrievedImageFile.absolutePath)
                binding.imageView6.setImageBitmap(bitmap)
            }
            binding.etUserName.setText(user.username)
            binding.etAdress.setText(userInformation.home_address)
            binding.etEmail.setText(user.email)
            binding.progressBar.visibility = View.GONE
        }
    }


    fun onClickToGetImage(view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.progressBar.visibility = View.VISIBLE
                        val selectedImageUri = data?.data
                        binding.imageView6.setImageURI(selectedImageUri)
                        val imageString = imageManager.imageToByteArray(
                            this@UserProfileActivity,
                            selectedImageUri!!
                        )

                        if (imageString != null) {
                            userManager.changeUserImage(imageString)
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun onClickSave(view: View) {
        binding.progressBar.visibility = View.VISIBLE
        val email = binding.etEmail.text.toString()
        val userName = binding.etUserName.text.toString()
        val address = binding.etAdress.text.toString()
        if (email.length > 1 && userName.length > 1 && address.length > 1) {
            GlobalScope.launch(Dispatchers.Main) {
                userManager.changeUserInformation(address, userName, email)
            }
            Toast.makeText(this@UserProfileActivity,"Изменения сохранены!", Toast.LENGTH_SHORT).show()
        }
        binding.progressBar.visibility = View.GONE
    }

    fun onClickExit(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            SupabaseManager.getSupabaseClient().gotrue.logout()
            val intent = Intent(this@UserProfileActivity, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun OnClickEx(view: View) {
        val intent=Intent(this, YourHouseActivity::class.java)
        startActivity(intent)
    }
}