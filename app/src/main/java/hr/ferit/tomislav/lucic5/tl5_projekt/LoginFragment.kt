package hr.ferit.tomislav.lucic5.tl5_projekt

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val button = view.findViewById<Button>(R.id.registerButton)
        emailInput = view.findViewById(R.id.inputEmail)
        passwordInput = view.findViewById(R.id.inputPassword)
        loginButton = view.findViewById(R.id.loginButton)


        loginButton.setOnClickListener{
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter a valid email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val login = FirebaseAuth.getInstance()
                login.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            val homeFragment = MainFragment()
                            val transaction = fragmentManager?.beginTransaction()
                            transaction?.replace(R.id.mainFrame,homeFragment)
                            transaction?.commitNow()
                        } else {
                            // Login failed, display an error message
                            Toast.makeText(context, "Please enter a valid email and password.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        button.setOnClickListener{
            val homeFragment = RegisterFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.xml.slide_in,R.xml.fade_out, R.xml.fade_in, R.xml.slide_out)
            transaction?.addToBackStack(null)
            transaction?.replace(R.id.mainFrame, homeFragment)?.commit()

        }
        return view
    }

}