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
        dataModel.message.observe(activity as LifecycleOwner) {
            binding.tvFrom.text = it
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

        binding.apply {
            convButton.setOnClickListener {
                when(fromSpin.selectedItem.toString()) {
                    "cm" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text = tvFrom.text
                            "m" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 100).toString()
                            "km" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 100000).toString()
                        }
                    }
                    "m" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 100).toString()
                            "m" -> tvTo.text = tvFrom.text
                            "km" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 1000).toString()
                        }
                    }
                    "km" -> {
                        when(toSpin.selectedItem.toString()) {
                            "cm" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 100000).toString()
                            "m" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 1000).toString()
                            "km" -> tvTo.text = tvFrom.text
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