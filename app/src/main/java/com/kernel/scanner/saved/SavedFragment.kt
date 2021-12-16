package com.kernel.scanner.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.kernel.scanner.R
import com.kernel.scanner.adapter.CargoListAdapter
import com.kernel.scanner.cargo.CargoActivity
import com.kernel.scanner.databinding.FragmentSavedBinding


class SavedFragment : Fragment() {

    private lateinit var savedViewModel: SavedViewModel

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: CargoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedViewModel =
            ViewModelProvider(this).get(SavedViewModel::class.java)

        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter= CargoListAdapter({ cargo->
            CargoActivity.startActivity(requireContext(),cargo.id)
        })
        binding.recyclerSaved.adapter=adapter

        var itemDecorationHorizontal = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecorationHorizontal.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider)!!)

        binding.recyclerSaved.addItemDecoration(itemDecorationHorizontal)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedViewModel.listSaved.observe(viewLifecycleOwner,{ list->
            adapter.setupData(list)
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}