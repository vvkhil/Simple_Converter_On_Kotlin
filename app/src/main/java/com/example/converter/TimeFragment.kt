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
        val activity = activity
        dataModel.addDecimalMessage.value = true

        dataModel.message.observe(activity as LifecycleOwner) {
            if(binding.edFrom.length() <= 12) {
                binding.edFrom.getText()?.insert(binding.edFrom.getSelectionStart(), it)
            }
            else {
                Toast.makeText(activity, "You can only enter 12 characters",
                    Toast.LENGTH_SHORT).show()
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
            dataModel.copyMessage.value = binding.edFrom.text.toString()
        }

        binding.pasteButton.setOnClickListener {
            dataModel.copyMessage.observe(activity as LifecycleOwner) {
                if(it.toFloatOrNull() != null) {
                    binding.edFrom.setText(it)
                }
                else {
                    Toast.makeText(activity, "Incorrect input",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.swapButton.setOnClickListener {
            binding.apply {
                val fromId = fromSpin.selectedItemPosition
                val toId = toSpin.selectedItemPosition
                fromSpin.setSelection(toId)
                toSpin.setSelection(fromId)

                val buffer = edFrom.text
                edFrom.setText(tvTo.text)
                tvTo.text = buffer
            }
        }

        binding.apply {
            convButton.setOnClickListener {
                when(fromSpin.selectedItem.toString()) {
                    "sec" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text = edFrom.text
                            "min" -> tvTo.text = convertBigDecimal(60, edFrom)
                            "h" -> tvTo.text = convertBigDecimal(3600, edFrom)
                        }
                    }
                    "min" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 60).toString()
                            "min" -> tvTo.text = edFrom.text
                            "h" -> tvTo.text = convertBigDecimal(60, edFrom)
                        }
                    }
                    "h" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 3600).toString()
                            "min" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 60).toString()
                            "h" -> tvTo.text = edFrom.text
                        }
                    }
                }
            }
        }
    }

    fun convertBigDecimal(value: Int, editT: EditText):String {
        var res = editT.text.toString().toBigDecimal()
            .divide(BigDecimal(value), 40, RoundingMode.HALF_UP)
            .stripTrailingZeros().toString()

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