package hr.ferit.tomislav.lucic5.tl5_projekt

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class AddTransactionFragment : Fragment() {
    private lateinit var database : FirebaseDatabase
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_transaction2, container, false)
        val addBtn = view.findViewById<Button>(R.id.updateTransactionBtn)
        val titleInput = view.findViewById<TextInputEditText>(R.id.titleEdit)
        val amountInput = view.findViewById<TextInputEditText>(R.id.amountEdit)
        val descriptionInput = view.findViewById<TextInputEditText>(R.id.descriptionEdit)
        val titleLayout = view.findViewById<TextInputLayout>(R.id.titleLayout)
        val amountLayout = view.findViewById<TextInputLayout>(R.id.amountLayout)
        val closeBtn = view.findViewById<ImageButton>(R.id.closeBtnUpdate)
        var recyclerAdapter : TransactionAdapter
        var database = Firebase.database.reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid




        titleInput.addTextChangedListener {
            if(it!!.count() > 0)
                titleLayout.error = null
        }

        amountInput.addTextChangedListener {
            if(it!!.count() > 0)
                amountLayout.error = null
        }
        addBtn.setOnClickListener{
            try{
            val id = UUID.randomUUID().toString()
                var transactionToAdd = Transaction(
                    id = id,
                    label = titleInput.text.toString(),
                    description = descriptionInput.text.toString(),
                    amount = amountInput.text.toString().toDouble()
                )
                val list = ArrayList<Transaction>()
                recyclerAdapter = TransactionAdapter(list)
                    database.child("users").child(userId).child("transactions")
                        .child(transactionToAdd.label).setValue(transactionToAdd)
                        .addOnSuccessListener {
                            recyclerAdapter.addItem(transactionToAdd)
                            var homeFragment = MainFragment()
                            val transaction = fragmentManager?.beginTransaction()
                            transaction?.setCustomAnimations(R.xml.slide_in,R.xml.fade_out)
                            transaction?.replace(R.id.mainFrame, homeFragment)
                            transaction?.commit()
                        }
            }catch(e: java.lang.NumberFormatException) {
                amountLayout.error = "Please enter a valid amount"
                titleLayout.error = "Please enter a valid title"
            }
            }

        closeBtn.setOnClickListener{

            var homeFragment = MainFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.xml.slide_in,R.xml.fade_out)
            transaction?.replace(R.id.mainFrame,homeFragment)
            transaction?.commit()
        }

        return view
    }

}