package com.example.converter

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    "g" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text = tvFrom.text
                            "kg" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 1000).toString()
                            "ton" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 1000000).toString()
                        }
                    }
                    "kg" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 1000).toString()
                            "kg" -> tvTo.text = tvFrom.text
                            "ton" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 1000).toString()
                        }
                    }
                    "ton" -> {
                        when(toSpin.selectedItem.toString()) {
                            "g" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 1000000).toString()
                            "kg" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 1000).toString()
                            "ton" -> tvTo.text = tvFrom.text
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