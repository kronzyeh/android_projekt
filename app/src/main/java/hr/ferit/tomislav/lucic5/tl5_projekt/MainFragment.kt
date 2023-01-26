package hr.ferit.tomislav.lucic5.tl5_projekt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var transactions : ArrayList<Transaction>
lateinit var transaction : Transaction
private lateinit var deletedTransaction : Transaction
private lateinit var oldTransactions: ArrayList<Transaction>
private lateinit var recyclerView : RecyclerView
private lateinit var adapter: TransactionAdapter
private lateinit var loadingData : TextView
private lateinit var dbRef : DatabaseReference

val userId = FirebaseAuth.getInstance().currentUser!!.uid
private lateinit var db : FirebaseFirestore




class MainFragment : Fragment() {
    var transactions = arrayListOf<Transaction>()
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transaction = it.getParcelable(MainFragment.ARG_TRANSACTION)!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        updateDashboard()
        return view
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newTransaction = view.findViewById<FloatingActionButton>(R.id.addBtn)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        loadingData = view.findViewById(R.id.tvLoadingData)
        db = FirebaseFirestore.getInstance()

        transactions = arrayListOf<Transaction>()
        adapter = TransactionAdapter(transactions)
        getTransactionData()


        logoutButton?.setOnClickListener{
            activity?.let {
                mAuth.signOut()
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
                activity?.finish()
                Toast.makeText(activity,"Logout successfull",Toast.LENGTH_SHORT).show()
            }
        }
        newTransaction?.setOnClickListener {
            val homeFragment = AddTransactionFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.setCustomAnimations(R.xml.slide_in,R.xml.fade_out)
            transaction?.replace(R.id.mainFrame,homeFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        }


    private fun updateDashboard(){
        var balance = view?.findViewById<TextView>(R.id.balance)
        var expense = view?.findViewById<TextView>(R.id.expense)
        var budget = view?.findViewById<TextView>(R.id.budget)

        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount  = transactions.filter { it.amount >0 }.map{it.amount}.sum()
        val expenseAmount  = totalAmount - budgetAmount

        balance?.text = "€ %.2f".format(totalAmount)
        budget?.text = "€ %.2f".format(budgetAmount)
        expense?.text = "€ %.2f".format(expenseAmount)


    }
    private fun getTransactionData(){
        recyclerView.visibility= View.GONE
        if(transactions.isEmpty()){
                loadingData.visibility = View.GONE
            }
        else{
            loadingData.visibility = View.VISIBLE
        }

        dbRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("transactions")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactions.clear()
                if (snapshot.exists()) {
                    for (tranSnap in snapshot.children) {
                        val tranData =
                            tranSnap.getValue(Transaction::class.java)
                        transactions.add(tranData!!)
                    }
                    val mAdapter = TransactionAdapter(transactions)
                    recyclerView.adapter = mAdapter

                    recyclerView.visibility = View.VISIBLE
                    loadingData.visibility = View.GONE


                }
                updateDashboard()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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