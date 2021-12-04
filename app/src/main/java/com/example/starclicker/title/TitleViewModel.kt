package com.example.starclicker.title

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.starclicker.R
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.HashMap

class TitleViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun showUserMenuDialog(context: Context){
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_user_menu, null)
        val alertDialog = AlertDialog.Builder(context).setView(view).create()
        val btnSignIn = view.findViewById<View>(R.id.btnSignIn)
        val btnSignUp = view.findViewById<View>(R.id.btnSignUp)
        val btnSignOut = view.findViewById<View>(R.id.btnSignOut)
        if(FirebaseAuth.getInstance().currentUser == null){
            btnSignIn.visibility = View.VISIBLE
            btnSignUp.visibility = View.VISIBLE
            btnSignOut.visibility = View.GONE
            view.findViewById<View>(R.id.btnSignIn).setOnClickListener { v: View? ->
                alertDialog.dismiss()
                showSignInDialog(context)
            }
            view.findViewById<View>(R.id.btnSignUp).setOnClickListener { v: View? ->
                alertDialog.dismiss()
                showSignUpDialog(context)
            }
        } else {
            btnSignIn.visibility = View.GONE
            btnSignUp.visibility = View.GONE
            btnSignOut.visibility = View.VISIBLE
            view.findViewById<View>(R.id.btnSignOut).setOnClickListener { v: View? ->
                FirebaseAuth.getInstance().signOut()

                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private fun showSignInDialog(context: Context) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in, null)
        val alertDialog = AlertDialog.Builder(context).setView(view).create()
        val tietEmail: TextInputEditText = view.findViewById(R.id.tietEmail)
        val tietPassword: TextInputEditText = view.findViewById(R.id.tietPassword)
        view.findViewById<View>(R.id.btnSignIn).setOnClickListener { v: View? ->
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString().trim { it <= ' ' }
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length >= 6) {
                    signIn(email, password, context)
                    alertDialog.dismiss()
                } else {
                    tietPassword.isFocusable = true
                    tietPassword.error = "Too short password"
                }
            } else {
                tietEmail.isFocusable = true
                tietEmail.error = "Wrong email"
            }
        }
        view.findViewById<View>(R.id.tvSignUp).visibility = View.GONE
        alertDialog.show()
    }

    private fun signIn(email: String, password: String, context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Signing In...")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                progressDialog.dismiss()
            } else {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }.addOnFailureListener { e: Exception ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }

    private fun showSignUpDialog(context: Context) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_sign_up, null)
        val alertDialog = AlertDialog.Builder(context).setView(view).create()
        val tietName: TextInputEditText = view.findViewById(R.id.tietName)
        val tietEmail: TextInputEditText = view.findViewById(R.id.tietEmail)
        val tietPassword: TextInputEditText = view.findViewById(R.id.tietPassword)
        view.findViewById<View>(R.id.btnSignUp).setOnClickListener { v: View? ->
            val name = tietName.text.toString()
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString().trim { it <= ' ' }
            if (name.length > 0) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (password.length >= 6) {
                        signUp(name, email, password, context)
                        alertDialog.dismiss()
                    } else {
                        tietPassword.isFocusable = true
                        tietPassword.error = "Too short password"
                    }
                } else {
                    tietEmail.isFocusable = true
                    tietEmail.error = "Wrong email"
                }
            } else {
                tietName.isFocusable = true
                tietName.error = "No name"
            }
        }
        view.findViewById<View>(R.id.tvSignUp).visibility = View.GONE
        alertDialog.show()
    }

    private fun signUp(name: String, email: String, password: String, context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Signing Up...")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val hashMap = HashMap<Any, Any>()
                hashMap["name"] = name
                hashMap["score"] = 0
                val uid = FirebaseAuth.getInstance().currentUser?.uid!!
                val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                ref.setValue(hashMap).addOnCompleteListener {
                    if (task.isSuccessful){
                        progressDialog.dismiss()
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }.addOnFailureListener { e: Exception ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }
}