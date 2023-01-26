package hr.ferit.tomislav.lucic5.tl5_projekt

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

enum class ItemClickType{
    REMOVE
}

class TransactionAdapter(private var trList:ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var deletedTransaction: Transaction

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val curTransaction = trList[position]
        holder.amount.text = curTransaction.amount.toString()
        holder.label.text = curTransaction.label

        if(curTransaction.amount.toInt() >= 0){
            holder.amount.text = "+ €%.2f".format(curTransaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }else {
            holder.amount.text = "- €%.2f".format(Math.abs(curTransaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
        holder.label.text = curTransaction.label


    }

    override fun getItemCount(): Int {
        return trList.size
    }

    fun addItem(transaction: Transaction){
        trList.add(0, transaction)
        notifyItemInserted(0)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val label: TextView = itemView.findViewById(R.id.label)
        val amount: TextView = itemView.findViewById(R.id.amount)
        private val deleteBtn = itemView.findViewById<ImageButton>(R.id.deleteBtn)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val curTransaction = trList[position]
                val fragment = UpdateTransactionFragment.newInstance(curTransaction)
                val fragmentManager =
                    (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            deleteBtn.setOnClickListener {
                val position = adapterPosition
                val curTransaction = trList[position]

                val builder = AlertDialog.Builder(itemView.context)
                builder.setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes") { _, _ ->
                        // user confirmed, delete the transaction
                        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                            .child("transactions").child(curTransaction.label)
                        databaseRef.removeValue(null)
                    }
                    .setNegativeButton("No") { _, _ ->
                        // user cancelled, do nothing
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }
    }
    interface ContentListener{
        fun onItemButtonClick(index: String, transaction: Transaction, clickType: ItemClickType)
    }
}

