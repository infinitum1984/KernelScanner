package com.kernel.scanner.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kernel.scanner.R
import com.kernel.scanner.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding?=null
    private val binding:FragmentSettingsBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSettingsBinding.inflate(layoutInflater,container,false)
        binding.switchBrightness.isChecked=SavedSettings.getIsLowerBrightness(requireContext())
        
        binding.switchBrightness.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("D_SettingsFragment","onCreateView: ");
            SavedSettings.saveIsLowerBrightness(requireContext(),isChecked)
        }
        return binding.root
    }

}