package com.example.abcautovaluers

import android.util.Log
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseError
import androidx.annotation.NonNull
import java.nio.file.Files.exists
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class Validate(private val value: String, private val editText: TextInputLayout) {

    private val tag = "Validate"

    private val containWhiteSpace = Regex("\\A\\w{4,20}\\z")
    private var emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private val phoneNumberPattern = Regex("[0-9]{9}")
    private val atLeastOneDigit = Regex("(?=.*[0-9])")
    private val atLeastOneLowerCaseLetter = Regex("(?=.*[a-z])")
    private val atLeastOneUpperCaseLetter = Regex("(?=.*[A-Z])")
    private val atLeastOneSpecialCharacter = Regex("(?=.*[@#$%^&+=])")
    private val atLeastFourCharacters = Regex(".{4,}")


    fun stringEmpty(): Boolean {

        val state: Boolean = value.isEmpty()

        return if (state) {

            editText.error = "Field can't be empty"
            true

        } else {

            false

        }

    }

    fun containsLowerCaseLetter(): Boolean {

        Log.d(tag, value.contains(atLeastOneLowerCaseLetter).toString())

        val state = value.contains(atLeastOneLowerCaseLetter)

        return if (state) {

            state
        } else {

            editText.error = "Field should contain atLeast one lower case letter"
            state

        }


    }


    fun containsUpperCaseLetter(): Boolean {

        Log.d(tag, value.contains(atLeastOneUpperCaseLetter).toString())
        val state = value.contains(atLeastOneUpperCaseLetter)

        return if (state) {

            state
        } else {

            editText.error = "Field should contain at least one upper case letter"
            state

        }

    }


    fun containsDigit(): Boolean {

        Log.d(tag, value.contains(atLeastOneDigit).toString())
        val state = value.contains(atLeastOneDigit)

        return if (state) {

            state
        } else {

            editText.error = "Field should contain at least one digit"
            state

        }

    }


    fun doesNotContainsSpecialCharacter(): Boolean {

        Log.d("special characters", value.contains(atLeastOneSpecialCharacter).toString())
        val state = value.contains(atLeastOneSpecialCharacter)

        return if (state) {

            Log.d(tag, "Contains special character")
            editText.error = "Field should not contain a special character"
            false

        } else {

            Log.d(tag, "Does not contain special character")
            true

        }

    }

    fun containsSpecialCharacter(): Boolean {

        Log.d(tag, value.contains(atLeastOneSpecialCharacter).toString())
        val state = value.contains(atLeastOneSpecialCharacter)

        return if (state) {

            state

        } else {

            editText.error = "Field should contain at least one special character"
            state

        }

    }

    fun fourCharacters(): Boolean {

        Log.d("Four characters", value.contains(atLeastFourCharacters).toString())
        val state = value.contains(atLeastFourCharacters)

        return if (state) {

            state

        } else {

            editText.error = "Too short! Field should contain at least four characters"
            state

        }

    }

    fun phoneNumber(): Boolean {

        Log.d(tag, value.contains(phoneNumberPattern).toString())
        val state = value.contains(phoneNumberPattern)

        return if (state) {

            state
        } else {

            editText.error = "Invalid phone number"
            state

        }

    }

    fun validateEmail(): Boolean{

        val state = value.contains(emailPattern)
        Log.d(tag, "email state ${value.contains(phoneNumberPattern).toString()}")


        return if (state){
            state
        } else {
            editText.error = "Invalid email!"
            false
        }

    }

    fun validateNumberOfNames(): Boolean {

        val delimiter = " "

        val parts = value.split(delimiter)

        val total = parts.size

        Log.d(tag, total.toString())

        return when {
            total < 2 -> {

                editText.error = "Please provide at least two names"
                false

            }
            total > 3 -> {

                editText.error = "Please provide three names only"
                false

            }
            else -> true
        }
    }

    fun noWhiteSpaces(): Boolean {

        Log.d("Spaces", value.contains(containWhiteSpace).toString())
        val state = value.contains(containWhiteSpace)

        return if (state) {

            Log.d("Spaces",  "Contains spaces")
            false

        } else {

            Log.d("Spaces",  "Does not contain spaces")
            editText.error = "Field should not contain spaces"
            true

        }
    }

}