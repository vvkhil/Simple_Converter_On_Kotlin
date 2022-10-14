package com.example.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.ActivityMainBinding
import java.lang.Long

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var canAddDecimal = true
    private var canAddValue = true
    private val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        dataModel.addDecimalMessage.observe(this) {
            canAddDecimal = it
        }

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

    }

    private fun openFrag(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeHolder, f)
            .commit()
    }

    fun numberAction(view: View) {
        if(view is Button) {
            if(canAddValue) {
                dataModel.message.value = ""
                if(view.text == "." && canAddDecimal == false) {
                    Toast.makeText(applicationContext, "You already have point in number",
                        Toast.LENGTH_SHORT).show()
                }
                if(view.text == ".") {
                    if(canAddDecimal) {
                        dataModel.message.value = view.text.toString()
                    }
                    canAddDecimal = false
                }
                else {
                    dataModel.message.value = view.text.toString()
                }
                canAddValue = false
            }
            else {
                if(view.text == "." && canAddDecimal == false) {
                    Toast.makeText(applicationContext, "You already have point in number",
                        Toast.LENGTH_SHORT).show()
                }
                if(view.text == ".") {
                    if(canAddDecimal) {
                        dataModel.message.value = view.text.toString()
                    }
                    canAddDecimal = false
                }
                else {
                    dataModel.message.value = view.text.toString()
                }
            }
        }
    }

    fun allClearAction(view: View) {
        dataModel.deleteMessage.value = "all_clear"
        canAddDecimal = true
    }

    fun backSpaceAction(view: View) {
        dataModel.backMessage.value = "back"
    }
}