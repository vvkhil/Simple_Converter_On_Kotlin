package com.example.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.ActivityMainBinding
import java.lang.Long

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            nv.setNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.length -> {
                        openFrag(LengthFragment.newInstance())
                        dataModel.message.value = ""
                    }
                    R.id.weight -> {
                        openFrag(WeightFragment.newInstance())
                        dataModel.message.value = ""
                    }
                    R.id.time -> {
                        openFrag(TimeFragment.newInstance())
                        dataModel.message.value = ""
                    }

                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }
        }

        binding.proButton?.setOnClickListener {
            dataModel.proMessage.value = "unlock"
        }

        binding.apply {
            zero?.setOnClickListener {
                val prevText: EditText = findViewById(R.id.edFrom)
                if(prevText.text.length == 1 && prevText.text[0].equals('0')) {
                    dataModel.message.value = ""
                    Toast.makeText(
                        applicationContext, "You cannot enter second zero",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                    dataModel.message.value = "0"
            }
            one.setOnClickListener {
                dataModel.message.value = "1"
            }
            two.setOnClickListener {
                dataModel.message.value = "2"
            }
            three.setOnClickListener {
                dataModel.message.value = "3"
            }
            four.setOnClickListener {
                dataModel.message.value = "4"
            }
            five.setOnClickListener {
                dataModel.message.value = "5"
            }
            six.setOnClickListener {
                dataModel.message.value = "6"
            }
            seven.setOnClickListener {
                dataModel.message.value = "7"
            }
            eight.setOnClickListener {
                dataModel.message.value = "8"
            }
            nine.setOnClickListener {
                dataModel.message.value = "9"
            }
            pointer?.setOnClickListener {
                var canAddDecimal = false
                val prevText1:EditText = findViewById(R.id.edFrom)
                for (i in prevText1.text){
                    if(i == '.') {
                        canAddDecimal = true
                        Toast.makeText(applicationContext, "You already have point in number",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                if(prevText1.text.isEmpty()){
                    dataModel.message.value = "0."
                } else if(!canAddDecimal)
                    dataModel.message.value = "."
            }
        }

    }

    private fun openFrag(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeHolder, f)
            .commit()
    }

//    fun numberAction(view: View) {
//        if(view is Button) {
//            if(canAddValue) {
//                dataModel.message.value = ""
//                if(view.text == "." && canAddDecimal == false) {
//                    Toast.makeText(applicationContext, "You already have point in number",
//                        Toast.LENGTH_SHORT).show()
//                }
//                if(view.text == ".") {
//                    if(canAddDecimal) {
//                        dataModel.message.value = view.text.toString()
//                    }
//                    canAddDecimal = false
//                }
//                else {
//                    dataModel.message.value = view.text.toString()
//                }
//                canAddValue = false
//            }
//            else {
//                if(view.text == "." && canAddDecimal == false) {
//                    Toast.makeText(applicationContext, "You already have point in number",
//                        Toast.LENGTH_SHORT).show()
//                }
//                if(view.text == ".") {
//                    if(canAddDecimal) {
//                        dataModel.message.value = view.text.toString()
//                    }
//                    canAddDecimal = false
//                }
//                else {
//                    dataModel.message.value = view.text.toString()
//                }
//            }
//        }
//    }

    fun allClearAction(view: View) {
        dataModel.deleteMessage.value = "all_clear"
    }

    fun backSpaceAction(view: View) {
        dataModel.backMessage.value = "back"
    }
}