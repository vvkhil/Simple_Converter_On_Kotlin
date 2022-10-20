package com.example.converter

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentWeightBinding

class WeightFragment : Fragment() {
    lateinit var binding: FragmentWeightBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.edFrom.showSoftInputOnFocus = false
        binding.edFrom.setText("")
        val activity = activity

        dataModel.message.observe(activity as LifecycleOwner) {
            if(it=="") {
                binding.edFrom.setText("")
            }
            else {
                if(binding.edFrom.text.length == 16 && binding.edFrom.text[15].equals('.')) {
                    if(binding.edFrom.length() < 17) {
                        binding.edFrom.getText()?.insert(binding.edFrom.getSelectionStart(), it)
                    }
                    else {
                        Toast.makeText(activity, "You can only enter 17 characters",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    if(binding.edFrom.length() < 16) {
                        binding.edFrom.getText()?.insert(binding.edFrom.getSelectionStart(), it)
                    }
                    else {
                        Toast.makeText(activity, "You can only enter 16 characters",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        dataModel.deleteMessage.observe(activity as LifecycleOwner) {
            if(it == "all_clear") {
                binding.apply {
                    edFrom.setText("")
                    tvTo.text = ""
                }
            }
        }


        dataModel.proMessage.observe(activity as LifecycleOwner) {
            if(it == "unlock") {
                binding.apply {
                    swapButton.visibility = View.VISIBLE
                    copyButton.visibility = View.VISIBLE
                    pasteButton.visibility = View.VISIBLE
                }
            }
        }

        dataModel.backMessage.observe(activity as LifecycleOwner) {
            if(it == "back") {
                binding.apply {
                    val length = edFrom.length()
                    if(length > 0 && edFrom.getSelectionStart() != 0) {
                        var position:Int = edFrom.getSelectionStart()-1
                        var startText =
                            edFrom.text.subSequence(0, edFrom.getSelectionStart() - 1) as Editable?
                        var endText =
                            edFrom.text.subSequence(edFrom.getSelectionStart(), length) as Editable?
                        edFrom.setText("")
                        edFrom.setText(startText)
                        edFrom.append(endText)
                        edFrom.setSelection(position)
                    }
                }
            }
        }


        binding.copyButton.setOnClickListener {
            val prevText: EditText = binding.edFrom
            val str: String = prevText.text.toString()
            if(str.isNotEmpty()){
                dataModel.copyMessage.value = prevText.text.toString()
            }
            else
            {
                Toast.makeText(activity,"Please Enter some text", Toast.LENGTH_SHORT).show()
            }
        }


        binding.pasteButton.setOnClickListener {
            dataModel.copyMessage.observe(activity as LifecycleOwner) {
                if(it.toFloatOrNull() != null) {
                    binding.edFrom.setText(it)
                }
                else {
                    Toast.makeText(activity, "Incorrect paste",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.swapButton.setOnClickListener {
            binding.apply {
                if(tvTo.length() > 16) {
                    Toast.makeText(activity, "You cannot do this because length > 16",
                        Toast.LENGTH_SHORT).show()
                }
                else {
                    if(tvTo.getText().toString().toFloatOrNull() != null) {
                        val fromId = fromSpin.selectedItemPosition
                        val toId = toSpin.selectedItemPosition
                        fromSpin.setSelection(toId)
                        toSpin.setSelection(fromId)

                        val buffer = edFrom.text
                        edFrom.setText(tvTo.text)
                        tvTo.text = buffer
                    }
                    else {
                        Toast.makeText(activity, "You cannot do this because value in ConvertTo is incorrect",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.apply {
            convButton.setOnClickListener {
                when(fromSpin.selectedItem.toString()) {
                    "USD" -> {
                        when(toSpin.selectedItem.toString()) {
                            "USD" -> tvTo.text = edFrom.text
                            "BYN" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (2.53).toBigDecimal()).toPlainString()
                            "RUB" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (62.35).toBigDecimal()).toPlainString()
                        }
                    }
                    "BYN" -> {
                        when(toSpin.selectedItem.toString()) {
                            "USD" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (0.3952).toBigDecimal()).toPlainString()
                            "BYN" -> tvTo.text = edFrom.text
                            "RUB" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (24.60).toBigDecimal()).toPlainString()
                        }
                    }
                    "RUB" -> {
                        when(toSpin.selectedItem.toString()) {
                            "USD" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (0.016).toBigDecimal()).toPlainString()
                            "BYN" -> tvTo.text =
                                (edFrom.text.toString().toBigDecimal() * (0.041).toBigDecimal()).toPlainString()
                            "RUB" -> tvTo.text = edFrom.text
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeightFragment()
    }
}