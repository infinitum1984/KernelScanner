package com.kernel.scanner.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kernel.scanner.R
import com.kernel.scanner.databinding.FragmentAboutBinding
import com.kernel.scanner.databinding.FragmentScannerBinding
import com.kernel.scanner.openProjectLink


class AboutFragment : Fragment() {
    var _binding: FragmentAboutBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding= FragmentAboutBinding.inflate(layoutInflater, container, false)
        binding.linkText.setOnClickListener {
            openProjectLink(requireActivity())
        }
        return binding.root
    }

}