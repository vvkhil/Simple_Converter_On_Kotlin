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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentTimeBinding
import java.math.BigDecimal
import java.math.RoundingMode

class TimeFragment : Fragment() {
    lateinit var binding: FragmentTimeBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeBinding.inflate(layoutInflater)
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
                    "sec" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> {
                                tvTo.text = edFrom.text
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "min" -> {
                                tvTo.text = convertBigDecimal(60, edFrom)
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "h" -> {
                                tvTo.text = convertBigDecimal(3600, edFrom)
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    "min" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> {
                                tvTo.text =
                                    (edFrom.text.toString().toBigDecimal() * 60.toBigDecimal()).toPlainString()
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "min" -> {
                                tvTo.text = edFrom.text
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "h" -> {
                                tvTo.text = convertBigDecimal(60, edFrom)
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    "h" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> {
                                tvTo.text =
                                    (edFrom.text.toString().toBigDecimal() * 3600.toBigDecimal()).toPlainString()
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "min" -> {
                                tvTo.text =
                                    (edFrom.text.toString().toBigDecimal() * 60.toBigDecimal()).toPlainString()
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            "h" -> {
                                tvTo.text = edFrom.text
                                if(tvTo.text=="0.0(0)") {
                                    Toast.makeText(activity,"You entered too small value!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun convertBigDecimal(value: Int, editT: EditText):String {
        var res = editT.text.toString().toBigDecimal()
            .divide(BigDecimal(value), 40, RoundingMode.HALF_UP)
            .stripTrailingZeros().toPlainString()

        var round_up_flag = 0
        var dot_flag = 0
        var prev_val = 'a'
        var num_round = 0
        var dot_position = 0
        var index1 = 0
        var let_exist = 0
        for(cur_val in res) {
            if(cur_val == '.' && dot_flag != 1) {
                dot_flag = 1
                dot_position = index1
            }
            if(dot_flag == 1) {
                if(cur_val == prev_val) {
                    num_round +=1
                    prev_val = cur_val
                } else {
                    prev_val = cur_val
                    num_round = 0
                }
            }
            if(num_round == 5) {
                round_up_flag = 1
                break
            }
            index1 += 1
        }

        for (cur_val in res) {
            if(cur_val == 'E') {
                let_exist = 1
            }
        }

        if(round_up_flag == 1 && let_exist == 0) {
            var start_position = index1 - 5
            var new_result = ""
            var index_2 = 0
            for (cur_val in res) {
                new_result += cur_val
                if(index_2 == start_position) {
                    break
                }
                index_2 +=1
            }
            new_result += "($prev_val)"
            return new_result
        } else {
            return res
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimeFragment()
    }
}