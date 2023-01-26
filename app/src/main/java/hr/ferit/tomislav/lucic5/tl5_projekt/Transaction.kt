package hr.ferit.tomislav.lucic5.tl5_projekt

import android.os.Parcel
import android.os.Parcelable

data class Transaction (
    var id: String,
    var label: String,
    var amount: Double,
    var description: String
) :Parcelable{
    constructor(): this("","", 0.00, "")
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(label)
        parcel.writeDouble(amount)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}