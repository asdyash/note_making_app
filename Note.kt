package com.yash.androidproject

import android.os.Parcel
import android.os.Parcelable

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val color: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeInt(color)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }
        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}
