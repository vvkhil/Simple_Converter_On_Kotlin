package com.example.converter

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentLengthBinding

class LengthFragment : Fragment() {
    lateinit var binding: FragmentLengthBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLengthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.edFrom?.showSoftInputOnFocus = false

        dataModel.message.observe(activity as LifecycleOwner) {
            binding.edFrom?.setText(it)
        }

        dataModel.deleteMessage.observe(activity as LifecycleOwner) {
            binding.tvTo.text = it
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

        binding.copyButton.setOnClickListener {
            dataModel.copyMessage.value = binding.edFrom?.text.toString()
        }

        binding.pasteButton.setOnClickListener {
            dataModel.copyMessage.observe(activity as LifecycleOwner) {
                binding.edFrom?.setText(it)
            }
        }

        binding.swapButton.setOnClickListener {
            binding.apply {
                val fromId = fromSpin.selectedItemPosition
                val toId = toSpin.selectedItemPosition
                fromSpin.setSelection(toId)
                toSpin.setSelection(fromId)

                val buffer = edFrom?.text
                edFrom?.setText(tvTo.text)
                tvTo.text = buffer
            }
        }

        binding.apply {
            convButton.setOnClickListener {
                when(fromSpin.selectedItem.toString()) {
                    "cm" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text = edFrom?.text
                            "m" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() / 100).toString()
                            "km" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() / 100000).toString()
                        }
                    }
                    "m" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() * 100).toString()
                            "m" -> tvTo.text = edFrom?.text
                            "km" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() / 1000).toString()
                        }
                    }
                    "km" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() * 100000).toString()
                            "m" -> tvTo.text =
                                (edFrom?.text.toString().toFloat() * 1000).toString()
                            "km" -> tvTo.text = edFrom?.text
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LengthFragment()
    }
}