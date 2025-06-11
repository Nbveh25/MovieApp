package com.example.homework.presentaion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var viewBinding: FragmentMainBinding? = null

    private var sectorCount = 3


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)
        
        setupClickListeners()
        updateSectorCount()
    }
    
    private fun setupClickListeners() {

        with(viewBinding) {
            this?.decreaseButton?.setOnClickListener {
                if (sectorCount > 3) {
                    sectorCount--
                    updateSectorCount()
                }
            }

            this?.increaseButton?.setOnClickListener {
                if (sectorCount < 6) {
                    sectorCount++
                    updateSectorCount()
                }
            }

        }
    }
    
    private fun updateSectorCount() {
        with(viewBinding) {
            this?.sectorCountTextView?.text = sectorCount.toString()
            this?.circularSectorView?.setSectorCount(sectorCount)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
} 