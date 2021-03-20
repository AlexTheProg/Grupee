package com.example.grupee

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.grupee.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private var loginButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for changing status bar icon colors   ds
        setContentView(R.layout.activity_login)
        loginButton = findViewById(R.id.cirLoginButton)
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