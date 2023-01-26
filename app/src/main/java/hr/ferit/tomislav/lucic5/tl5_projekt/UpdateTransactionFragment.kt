package hr.ferit.tomislav.lucic5.tl5_projekt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase


class UpdateTransactionFragment : Fragment() {
    private lateinit var transaction: Transaction
    private lateinit var titleToEdit : TextView
    private lateinit var amountToEdit : TextView
    private lateinit var descriptionToEdit : TextView
    private lateinit var updateCloseButton : ImageButton
    private lateinit var updateTransaction : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transaction = it.getParcelable(ARG_TRANSACTION)!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)
        titleToEdit = view.findViewById(R.id.titleEdit)
        amountToEdit = view.findViewById(R.id.amountEdit)
        descriptionToEdit = view.findViewById(R.id.descriptionEdit)
        titleToEdit.text = transaction.label
        amountToEdit.text = transaction.amount.toString()
        descriptionToEdit.text = transaction.description
        updateCloseButton = view.findViewById(R.id.closeBtnUpdate)
        updateTransaction = view.findViewById(R.id.updateTransactionBtn)
        val titleLayout = view.findViewById<TextInputLayout>(R.id.titleLayout)
        val amountLayout = view.findViewById<TextInputLayout>(R.id.amountLayout)
        val database = FirebaseDatabase.getInstance()

        updateTransaction.setOnClickListener {
                val updatedTitle = titleToEdit.text.toString()
                val updatedAmount = amountToEdit.text.toString().toDouble()
                val updatedDescription = descriptionToEdit.text.toString()
                val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                    .child("transactions").child(transaction.label)
                databaseRef.removeValue(null)
                transaction.label = updatedTitle
                transaction.amount = updatedAmount
                transaction.description = updatedDescription

                val databaseRefUpdated =
                    FirebaseDatabase.getInstance().getReference("users").child(userId)
                        .child("transactions").child(transaction.label)
                val updateData = HashMap<String, Any>()
                updateData["label"] = transaction.label
                updateData["amount"] = transaction.amount
                updateData["description"] = transaction.description


                databaseRefUpdated.setValue(updateData)

                var homeFragment = MainFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.mainFrame, homeFragment)
                transaction?.commit()
            }

        updateCloseButton.setOnClickListener{
            var homeFragment = MainFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.mainFrame,homeFragment)
            transaction?.commit()

        }
        return view
    }

    companion object {
        private const val ARG_TRANSACTION = "transaction"

        fun newInstance(transaction: Transaction): UpdateTransactionFragment {
            val fragment = UpdateTransactionFragment()
            val args = Bundle()
            args.putParcelable(ARG_TRANSACTION, transaction)
            fragment.arguments = args
            return fragment
        }
    }

}