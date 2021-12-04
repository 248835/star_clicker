package com.example.starclicker.gameOver

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starclicker.R
import com.example.starclicker.database.DatabaseDao
import com.example.starclicker.database.Score
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseError
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.util.HashMap

class GameOverViewModel(private val database: DatabaseDao) : ViewModel() {
    val bestScore = database.getBestScore()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun insertScore(score: Score) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.insertScore(score)
            }
        }
    }

    fun showSignInDialog(context: Context, score: Int) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in, null)
        val alertDialog = AlertDialog.Builder(context).setView(view).create()
        val tietEmail: TextInputEditText = view.findViewById(R.id.tietEmail)
        val tietPassword: TextInputEditText = view.findViewById(R.id.tietPassword)
        view.findViewById<View>(R.id.btnSignIn).setOnClickListener { v: View? ->
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString().trim { it <= ' ' }
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (password.length >= 6) {
                        signIn(email, password, context, score)
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
        view.findViewById<View>(R.id.tvSignUp).setOnClickListener { v: View? ->
            showSignUpDialog(context, score)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun signIn(email: String, password: String, context: Context, score: Int) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Signing In...")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.currentUser?.uid!!)
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val remoteBestScore = (snapshot.child("score").value as Long).toInt()
                        if(score>remoteBestScore){
                            ref.child("score").setValue(score)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
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

    fun showSignUpDialog(context: Context, score: Int) {
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
                        signUp(name, email, password, context, score)
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
        view.findViewById<View>(R.id.tvSignIn).setOnClickListener { v: View? ->
            showSignInDialog(context, score)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun signUp(name: String, email: String, password: String, context: Context, score: Int) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Signing Up...")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val hashMap = HashMap<Any, Any>()
                hashMap["name"] = name
                hashMap["score"] = score
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