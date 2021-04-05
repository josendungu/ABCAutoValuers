package com.example.abcautovaluers

import android.os.Parcel
import android.os.Parcelable


data class ScheduleDetails(
    var surname: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var id: String? = null,
    var scheduleId: String? = null,
    var plateNumber: String?= null,
    var county: String? = null,
    var town: String? = null,
    var locationDetails: String? = null,
    var instructions: String? = null,
    var day: String? = null,
    var time: String? = null,
    var assignedTo: String? = null,
    var valuated: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(surname)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(id)
        parcel.writeString(scheduleId)
        parcel.writeString(plateNumber)
        parcel.writeString(county)
        parcel.writeString(town)
        parcel.writeString(locationDetails)
        parcel.writeString(instructions)
        parcel.writeString(day)
        parcel.writeString(time)
        parcel.writeString(assignedTo)
        parcel.writeByte(if (valuated) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleDetails> {
        override fun createFromParcel(parcel: Parcel): ScheduleDetails {
            return ScheduleDetails(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleDetails?> {
            return arrayOfNulls(size)
        }
    }
}