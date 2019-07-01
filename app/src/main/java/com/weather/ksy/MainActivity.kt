package com.weather.ksy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*

class MainActivity : AppCompatActivity() {

    // Firebase
    var auth: FirebaseAuth? = null

    //GoogleLogin
    val GOOGLE_LOGIN_CODE = 9001 // Intent Request ID
    var googleSignInClient: GoogleSignInClient? = null

    var email: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //구글 로그인 클래스를 만듬
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        google_login_button.setOnClickListener { googleSignIn() }
        email_login_button.setOnClickListener {
            email = email_edit.text.toString()
            password = password_edit.text.toString()
            if (email?.length!! > 0 && password?.length!! > 0) {
                createAndLoginEmail(email, password)
            } else {
                Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w("WAN", "Google sign in failed", e)
            }
        }
    }

    private fun createAndLoginEmail(email: String?, password: String?) {
        login_progress_bar.visibility = View.VISIBLE

        Log.d("TAG", email?.length.toString() + ":" + password?.length.toString())

        auth?.createUserWithEmailAndPassword(email!!, password!!)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //                    join
                    login_progress_bar.visibility = View.GONE
                    moveToOpenWeatherActivity(auth?.currentUser)
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                } else if (task.exception?.message.isNullOrEmpty()) {
                    login_progress_bar.visibility = View.GONE
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                } else {
                    //                    login
                    signEmail()
                }
            }
    }

    private fun signEmail() {
        auth?.signInWithEmailAndPassword(email_edit.text?.toString()!!, password_edit.text?.toString()!!)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    login_progress_bar.visibility = View.GONE
                    moveToOpenWeatherActivity(auth?.currentUser)
                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun googleSignIn() {
        login_progress_bar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    login_progress_bar.visibility = View.GONE
                    val user = auth?.currentUser
                    moveToOpenWeatherActivity(user)
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveToOpenWeatherActivity(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, OpenWeatherActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        moveToOpenWeatherActivity(auth?.currentUser)
    }

}
