package com.example.converter

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                    "g" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text = edFrom.text
                            "kg" -> tvTo.text =
                                (edFrom.text.toString().toFloat() / 1000).toString()
                            "ton" -> tvTo.text =
                                (edFrom.text.toString().toFloat() / 1000000).toString()
                        }
                    }
                    "kg" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 1000).toString()
                            "kg" -> tvTo.text = edFrom.text
                            "ton" -> tvTo.text =
                                (edFrom.text.toString().toFloat() / 1000).toString()
                        }
                    }
                    "ton" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 1000000).toString()
                            "kg" -> tvTo.text =
                                (edFrom.text.toString().toFloat() * 1000).toString()
                            "ton" -> tvTo.text = edFrom.text
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