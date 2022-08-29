package com.example.googlesignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    lateinit var signInClient : GoogleSignInClient
    lateinit var account : GoogleSignInAccount
    lateinit var emailLabel : TextView
    lateinit var signInButton : SignInButton
    lateinit var logoutButton : Button
    val SIGNIN_RCODE : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailLabel = findViewById(R.id.emailLabel)
        signInButton = findViewById(R.id.signin_btn)
        logoutButton = findViewById(R.id.logou_btn)

        signInButton.setOnClickListener{ onSignInButtonClicked() }

        var signInOptions : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, signInOptions)
        account = GoogleSignInAccount.createDefault()
    }

    fun onSignInButtonClicked() {
        var sigInIntent = signInClient.signInIntent
        startActivityForResult(sigInIntent, SIGNIN_RCODE)
    }

    override fun onStart() {
        super.onStart()

        var account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            updateUI()
        }
    }

    fun updateUI(){
        if(account != null || account.email == GoogleSignInAccount.createDefault().email){
            emailLabel.text = account.email
            signInButton.setVisibility(View.INVISIBLE)
        } else {
            emailLabel.text = ""
            signInButton.setVisibility(View.VISIBLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SIGNIN_RCODE) {
            var task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            var account : GoogleSignInAccount = completedTask.result
            Log.i("MainActivity", account.email as String)
        } catch (e: ApiException) {
            Log.w("MainActivity", "sigInResult:failed code="+e.statusCode)
            updateUI()
        }
    }
}