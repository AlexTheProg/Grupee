package com.example.grupee

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grupee.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.cirLoginButton
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_loginrefactored.*

class LoginActivity : AppCompatActivity() {


    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for changing status bar icon colors   ds
        setContentView(R.layout.activity_loginrefactored)

        sharedPref = this.getSharedPreferences("LOGIN", MODE_PRIVATE)

        if(sharedPref.getBoolean("logged", false)){
            goToMainActivity(cirLoginButton)
        }

        cirLoginButton.setOnClickListener {
            when{
                TextUtils.isEmpty(editTextEmailLogin.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Wrong email",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPasswordLogin.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Wrong Password",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = editTextEmailLogin.text.toString().trim { it <= ' '}
                    val password: String = editTextPasswordLogin.text.toString().trim { it <= ' '}

                    //Log-In using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                            this,
                                            "You are logged in successfully",
                                            Toast.LENGTH_SHORT
                                    ).show()

                                    sharedPref.edit().putBoolean("logged", true).apply()
                                    val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


                                }else{
                                    Toast.makeText(
                                            this,
                                            "Login has failed. Check your credentials",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                }

            }
        }

    }


    fun goToMainActivity(view: View?) {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
    }

    fun onLoginClick(View: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
    }

}