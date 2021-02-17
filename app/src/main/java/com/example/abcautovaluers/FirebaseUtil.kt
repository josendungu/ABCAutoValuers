package com.example.abcautovaluers

import android.util.Log
import com.google.firebase.database.*

class FirebaseUtil {

    companion object{

        private var firebaseDatabase: FirebaseDatabase? = null
        private var databaseReference: DatabaseReference? = null


        fun openFirebaseReference(ref: String): DatabaseReference{

            if (firebaseDatabase == null){

                firebaseDatabase = FirebaseDatabase.getInstance()

            }

            databaseReference = firebaseDatabase!!.reference.child(ref)

            return databaseReference as DatabaseReference

        }

    }



}