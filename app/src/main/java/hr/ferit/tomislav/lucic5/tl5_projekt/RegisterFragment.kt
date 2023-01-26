package hr.ferit.tomislav.lucic5.tl5_projekt


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var repeatPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var database: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        emailInput = view.findViewById(R.id.registerEmail)
        passwordInput = view.findViewById(R.id.registerPassword)
        repeatPasswordInput = view.findViewById(R.id.repeatPassword)
        registerButton = view.findViewById(R.id.registerButton2)
        database = Firebase.database.reference


        registerButton.setOnClickListener {
            registerNewUser()

        }

        return view
    }

    private fun registerNewUser() {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val repeatPass = repeatPasswordInput.text.toString()

        val userData = mapOf(
            "email" to email
        )

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter a valid email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != repeatPass) {
            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child("users/" + auth.currentUser?.uid).setValue(userData)
                    activity?.let {
                            val intent = Intent(it, MainActivity::class.java)
                            it.startActivity(intent)
                        }
                } else {
                    // Registration failed, display an error message
                    Toast.makeText(context, "Failed to register: ${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
