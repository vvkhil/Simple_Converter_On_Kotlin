package com.example.converter

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentTimeBinding

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
                    "sec" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text = tvFrom.text
                            "min" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 60).toString()
                            "h" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 3600).toString()
                        }
                    }
                    "min" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 60).toString()
                            "min" -> tvTo.text = tvFrom.text
                            "h" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() / 60).toString()
                        }
                    }
                    "h" -> {
                        when(toSpin.selectedItem.toString()) {
                            "sec" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 3600).toString()
                            "min" -> tvTo.text =
                                (tvFrom.text.toString().toFloat() * 60).toString()
                            "h" -> tvTo.text = tvFrom.text
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimeFragment()
    }
}