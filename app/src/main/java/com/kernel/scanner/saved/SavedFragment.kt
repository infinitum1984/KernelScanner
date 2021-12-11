package com.kernel.scanner.saved

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kernel.scanner.adapter.CargoListAdapter
import com.kernel.scanner.cargo.CargoActivity
import com.kernel.scanner.databinding.FragmentSavedBinding


class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel
    private var _binding: FragmentSavedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedViewModel =
            ViewModelProvider(this).get(SavedViewModel::class.java)

        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter= CargoListAdapter({ cargo->
            CargoActivity.startActivity(requireContext(),cargo.id)
        })
        binding.recyclerSaved.adapter=adapter
        savedViewModel.listSaved.observe(viewLifecycleOwner,{ list->
            adapter.setupData(list)
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}