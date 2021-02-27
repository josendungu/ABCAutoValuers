package com.example.abcautovaluers

import android.os.Parcel
import android.os.Parcelable

data class User(val username: String?, val password: String?, val email: String?, val admin: Boolean?, val folderId: String? ) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    ) {
    }

    constructor(): this("","","",null, "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeValue(admin)
        parcel.writeString(folderId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}


