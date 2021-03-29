package com.example.grupee

import android.content.Intent
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

class LoginActivity : AppCompatActivity() {
    private var loginButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for changing status bar icon colors   ds
        setContentView(R.layout.activity_loginrefactored)

        /*cirLoginButton.setOnClickListener {
            when{
                TextUtils.isEmpty(editTextEmail.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Wrong email",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(editTextPassword.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                            this@LoginActivity,
                            "Wrong Password",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = editTextEmail.text.toString().trim { it <= ' '}
                    val password: String = editTextPassword.text.toString().trim { it <= ' '}

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

                                    val intent =
                                            Intent(this@LoginActivity, WelcomeUserHomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("username", firebaseUser.displayName)

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
        }*/

    }

    fun goToMainActivity(view: View?) {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
    }

    fun onLoginClick(View: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}