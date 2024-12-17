package com.example.EventHub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat

class Cardpayment : AppCompatActivity() {

    private lateinit var cardNumberDisplay: TextView
    private lateinit var cardNumberInput: EditText
    private lateinit var cardNumberError: TextView
    private lateinit var amountInput: EditText
    private lateinit var cvvInput: EditText
    private lateinit var cvvError: TextView
    private lateinit var dateInput: EditText
    private lateinit var dateError: TextView
    private lateinit var postalCodeInput: EditText
    private lateinit var postalCodeError: TextView
    private lateinit var payButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardpayment)

        cardNumberDisplay = findViewById(R.id.cardNumberDisplay)
        cardNumberInput = findViewById(R.id.cardNumberInput)
        cardNumberError = findViewById(R.id.cardNumberError)
        amountInput = findViewById(R.id.amountInput)
        cvvInput = findViewById(R.id.cvvInput)
        cvvError = findViewById(R.id.cvvError)
        dateInput = findViewById(R.id.dateInput)
        dateError = findViewById(R.id.dateError)
        postalCodeInput = findViewById(R.id.postalCodeInput)
        postalCodeError = findViewById(R.id.postalCodeError)
        payButton = findViewById(R.id.payButton)

        setupCardNumberInput()
        setupCvvInput()
        setupDateInput()
        setupPostalCodeInput()

        payButton.setOnClickListener {
            if (validateAllInputs()) {
                startActivity(Intent(this, LoadingActivity::class.java))
            }
        }
    }

    private fun setupCardNumberInput() {
        cardNumberInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().replace("\\s".toRegex(), "")
                if (input.length > 16) {
                    s?.delete(16, s.length)
                }
                val formatted = formatCardNumber(input)
                cardNumberInput.removeTextChangedListener(this)
                cardNumberInput.setText(formatted)
                cardNumberInput.setSelection(formatted.length)
                cardNumberInput.addTextChangedListener(this)
                cardNumberDisplay.text = if (formatted.isEmpty()) "**** **** **** ****" else formatted
                validateCardNumber(input)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCvvInput() {
        cvvInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 4) {
                    s?.delete(4, s.length)
                }
                validateCvv(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupDateInput() {
        dateInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 5) {
                    s?.delete(5, s.length)
                }
                if (s?.length == 2 && !s.toString().contains("/")) {
                    s.append("/")
                }
                validateDate(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupPostalCodeInput() {
        postalCodeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 10) {
                    s?.delete(10, s.length)
                }
                validatePostalCode(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun formatCardNumber(number: String): String {
        return number.chunked(4).joinToString(" ")
    }

    private fun validateCardNumber(number: String): Boolean {
        val isValid = number.length == 16 && number.all { it.isDigit() }
        highlightInput(cardNumberInput, cardNumberError, isValid, "Invalid card number")
        return isValid
    }

    private fun validateCvv(cvv: String): Boolean {
        val isValid = cvv.length in 3..4 && cvv.all { it.isDigit() }
        highlightInput(cvvInput, cvvError, isValid, "Invalid CVV")
        return isValid
    }

    private fun validateDate(date: String): Boolean {
        val isValid = date.matches(Regex("^(0[1-9]|1[0-2])/[0-9]{2}$"))
        highlightInput(dateInput, dateError, isValid, "Invalid date")
        return isValid
    }

    private fun validatePostalCode(postalCode: String): Boolean {
        val isValid = postalCode.length >= 5 && postalCode.all { it.isDigit() || it.isLetter() }
        highlightInput(postalCodeInput, postalCodeError, isValid, "Invalid postal code")
        return isValid
    }

    private fun highlightInput(input: EditText, errorView: TextView, isValid: Boolean, errorMessage: String) {
        val color = if (isValid) android.R.color.black else android.R.color.holo_red_light
        input.setTextColor(ContextCompat.getColor(this, color))
        errorView.visibility = if (isValid) View.GONE else View.VISIBLE
        errorView.text = if (isValid) "" else errorMessage
    }

    private fun validateAllInputs(): Boolean {
        val isCardValid = validateCardNumber(cardNumberInput.text.toString().replace("\\s".toRegex(), ""))
        val isCvvValid = validateCvv(cvvInput.text.toString())
        val isDateValid = validateDate(dateInput.text.toString())
        val isPostalCodeValid = validatePostalCode(postalCodeInput.text.toString())
        val isAmountValid = amountInput.text.toString().isNotEmpty()

        return isCardValid && isCvvValid && isDateValid && isPostalCodeValid && isAmountValid
    }
}