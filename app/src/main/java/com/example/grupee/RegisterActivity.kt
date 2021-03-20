package com.example.grupee

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grupee.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        changeStatusBarColor()

        cirRegisterButton.setOnClickListener{
            when{
                TextUtils.isEmpty(editTextEmail.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                            this@RegisterActivity,
                            "Please Enter Your Email",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPassword.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                            this,
                            "Please Enter Your Password",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextName.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                            this,
                            "Please Enter Your Name",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextMobile.text.toString().trim{it <=' '}) -> {
                    Toast.makeText(
                            this,
                            "Please Enter Your Phone Number",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = editTextEmail.text.toString().trim{it <= ' '}
                    val name : String = editTextName.text.toString().trim { it <= ' '}
                    val phoneNumber: String = editTextMobile.text.toString().trim{ it <= ' '}
                    val password: String = editTextPassword.text.toString().trim { it <= ' '}

                    //Create an instance and register a user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    OnCompleteListener<AuthResult> { task ->
                                        //daca am reusit inregistrarea
                                        if(task.isSuccessful){
                                            //Registered User
                                            val firebaseUser: FirebaseUser = task.result!!.user!!

                                            Toast.makeText(
                                                    this@RegisterActivity,
                                                    "You have been registered successfully",
                                                    Toast.LENGTH_SHORT
                                            ).show()

                                            val intent =
                                                    Intent(this@RegisterActivity, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            intent.putExtra("user_id", firebaseUser.uid)
                                            intent.putExtra("email_id", email)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            //daca nu ne-am inregistrat cu succes
                                            Toast.makeText(
                                                    this@RegisterActivity,
                                                    task.exception!!.message.toString(),
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            )
                }
            }
        }
    }

    private fun changeStatusBarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.statusBarColor = resources.getColor(R.color.register_bk_color)
    }

    fun onLoginClick(view: View?) {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}