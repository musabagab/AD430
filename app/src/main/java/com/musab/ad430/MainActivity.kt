package com.musab.ad430

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipCodeEditText:EditText = findViewById(R.id.zipcode_edittext)

        val submitButton:Button = findViewById(R.id.submit_btn)

        submitButton.setOnClickListener {
            val zipcode = zipCodeEditText.text.toString()

            if (zipcode.length!=5){
                Toast.makeText(this,R.string.validzipcode,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,zipcode,Toast.LENGTH_LONG).show()
            }

        }

    }



}
