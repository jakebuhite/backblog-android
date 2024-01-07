package com.tabka.backblog.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.tabka.backblog.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Two buttons: login button and redirect to sign up fragment
        val loginButton: Button = view.findViewById(R.id.login_button)
        val signupButton: Button = view.findViewById(R.id.btn_no_account)

        val firebaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val username: String = view.findViewById<TextInputLayout>(R.id.username).editText!!.editableText.toString().trim()
            val password: String = view.findViewById<TextInputLayout>(R.id.password).editText!!.editableText.toString().trim()

            val statusText: TextView = view.findViewById(R.id.status_message)
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // All fields are complete, now can begin signup process
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
                    // Take them to login fragment
                    statusText.text = getString(R.string.successful_log_in)
                    statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.success))
                    findNavController().navigate(R.id.action_login_fragment_to_friends_fragment)
                }.addOnFailureListener {
                    // Add message saying sign in failed
                    val errorCode = (it as FirebaseAuthException).errorCode
                    statusText.text = getErrorMessage(errorCode)
                    statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                }
            } else {
                // Add message saying that user is missing information
                statusText.text = getString(R.string.fill_all_fields)
                statusText.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
            }
        }

        signupButton.setOnClickListener {
            // Navigate to signup page
            findNavController().navigate(R.id.action_login_fragment_to_signup_fragment)
        }
    }

    private fun getErrorMessage(errorCode: String): String {
        Log.d("Error handling", errorCode)
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> "Invalid email."
            "ERROR_INVALID_PASSWORD" -> "Invalid password."
            "ERROR_INVALID_CREDENTIAL" -> "Incorrect email or password."
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> "An account already exists with the same email address."
            "ERROR_EMAIL_EXISTS" -> "The email address is already in use by another account."
            "ERROR_USER_DISABLED" -> "The user account has been disabled by an administrator."
            "ERROR_USER_DELETED" -> "User not found."
            else -> "There was an error performing authentication."
        }
    }
}