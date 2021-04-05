package com.example.abcautovaluers

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class ValuationDetails(

    var valuationId: String? = null,
    var clientName:String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var valuatedBy : String? = null,
    var instructions: String? = null,

    var insurer: String? = null,
    var policyNumber: String? = null,
    var expiryDate: String? = null,
    var insuranceCertificateNumber: String? = null,

    var vehicleMakeModel: String? = null,
    var registrationNumber: String? = null,
    var bodyType: String? = null,
    var color: String? = null,
    var transmission: String? = null,
    var chassisNumber: String? = null,
    var engineNumber: String? = null,
    var engineType: String? = null,
    var engineCapacity: String? = null,
    var odometerReading: String? = null,
    var fuelType: String? = null,

    var tyres: String? = null,
    var antiTheftDevice: List<String>? = null,
    var bodyWork: String? = null,
    var mechanical: String? = null,
    var electrical: String? = null,
    var extras: String? = null,
    var generalCondition: String? = null,
    var typesOfLights: String? = null,
    var numberOfAirbags: String? = null,
    var gearBok: String? = null,

    var logBookProvided: Boolean = false,
    var idProvided: Boolean = false,
    var pinCertificateProvided: Boolean = false,
    var valuationLetterProvided: Boolean = false,

    var signed: Boolean = false
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(valuationId)
        parcel.writeString(clientName)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(valuatedBy)
        parcel.writeString(instructions)
        parcel.writeString(insurer)
        parcel.writeString(policyNumber)
        parcel.writeString(expiryDate)
        parcel.writeString(insuranceCertificateNumber)
        parcel.writeString(vehicleMakeModel)
        parcel.writeString(registrationNumber)
        parcel.writeString(bodyType)
        parcel.writeString(color)
        parcel.writeString(transmission)
        parcel.writeString(chassisNumber)
        parcel.writeString(engineNumber)
        parcel.writeString(engineType)
        parcel.writeString(engineCapacity)
        parcel.writeString(odometerReading)
        parcel.writeString(fuelType)
        parcel.writeString(tyres)
        parcel.writeStringList(antiTheftDevice)
        parcel.writeString(bodyWork)
        parcel.writeString(mechanical)
        parcel.writeString(electrical)
        parcel.writeString(extras)
        parcel.writeString(generalCondition)
        parcel.writeString(typesOfLights)
        parcel.writeString(numberOfAirbags)
        parcel.writeString(gearBok)
        parcel.writeByte(if (logBookProvided) 1 else 0)
        parcel.writeByte(if (idProvided) 1 else 0)
        parcel.writeByte(if (pinCertificateProvided) 1 else 0)
        parcel.writeByte(if (valuationLetterProvided) 1 else 0)
        parcel.writeByte(if (signed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ValuationDetails> {
        override fun createFromParcel(parcel: Parcel): ValuationDetails {
            return ValuationDetails(parcel)
        }

        override fun newArray(size: Int): Array<ValuationDetails?> {
            return arrayOfNulls(size)
        }
    }
}
