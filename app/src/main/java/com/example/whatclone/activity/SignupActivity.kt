package com.example.whatclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.example.whatclone.R
import com.example.whatclone.util.DATA_USERS
import com.example.whatclone.util.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.til_email
import kotlinx.android.synthetic.main.activity_signup.til_password

class SignupActivity : AppCompatActivity() {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setTextChangedListener(reg_edt_email, til_email)
        setTextChangedListener(reg_edt_password, til_password)
        setTextChangedListener(reg_edt_name, til_name)
        setTextChangedListener(reg_edt_phone, til_phone)
        reg_progress_layout.setOnTouchListener{v, event -> true }

        btn_signup.setOnClickListener {
            onSignup()
        }

        txt_login.setOnClickListener {
            onLogin()
        }
    }

    private fun setTextChangedListener(edt: EditText, til: TextInputLayout) {
        edt.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled = false
            }
        })
    }

    private fun onLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onSignup() {
        var proceed = true
        if (reg_edt_name.text.isNullOrEmpty()){
            til_name.error = "Required Name"
            til_name.isErrorEnabled = true
            proceed = false
        }

        if (reg_edt_phone.text.isNullOrEmpty()){
            til_phone.error = "Required Phone"
            til_phone.isErrorEnabled = true
            proceed = false
        }

        if (reg_edt_email.text.isNullOrEmpty()){
            til_email.error = "Required Email"
            til_email.isErrorEnabled = true
            proceed = false
        }

        if (reg_edt_password.text.isNullOrEmpty()){
            til_password.error = "Required Password"
            til_password.isErrorEnabled = true
            proceed = false
        }

        if (proceed){
            reg_progress_layout.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                reg_edt_email.text.toString(),
                reg_edt_password.text.toString()
            ).addOnCompleteListener { task ->
                if (!task.isSuccessful){
                    reg_progress_layout.visibility = View.GONE
                    Toast.makeText(this, "SignUp Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()

                }else if (firebaseAuth.uid != null){
                    val email = reg_edt_email.text.toString()
                    val phone = reg_edt_phone.text.toString()
                    val name = reg_edt_name.text.toString()
                    val user = User(email, phone, name, "", "Hello i'm new", "", "")
                    firebaseDb.collection(DATA_USERS)
                        .document(firebaseAuth.uid!!).set(user)
                }
                reg_progress_layout.visibility = View.GONE
            }

                .addOnFailureListener {
                    reg_progress_layout.visibility = View.GONE
                    it.printStackTrace()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }
}