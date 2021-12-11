package com.kernel.scanner.cargo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kernel.scanner.R
import com.kernel.scanner.databinding.FragmentCargoBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CargoFragment : Fragment() {

    private var _binding: FragmentCargoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val cargoViewModel:CargoViewModel by viewModels()
    companion object{
        val REQUEST_KEY="GET_CODE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCargoBinding.inflate(inflater, container, false)
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_cargo_to_navigation_scanner)
        }
        cargoViewModel.cargo.observe(viewLifecycleOwner,{cargo->
            Log.d("D_CargoFragment","onCreateView: ${cargo}");
        })
        cargoViewModel.loadCargoById(CargoActivity.getIdFromIntent(requireActivity().intent))
        setFragmentResultListener(REQUEST_KEY,{
            requestKey, bundle ->
            binding.textviewFirst.text=bundle["code"].toString()
        })
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}