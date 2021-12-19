package com.kernel.scanner.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.kernel.scanner.R
import com.kernel.scanner.databinding.FragmentQueueBinding
import com.kernel.scanner.adapter.CargoListAdapter
import com.kernel.scanner.cargo.CargoActivity


class QueueFragment : Fragment() {

    private lateinit var queueViewModel: QueueViewModel

    private var _binding: FragmentQueueBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter:CargoListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        queueViewModel =
            ViewModelProvider(this).get(QueueViewModel::class.java)

        _binding = FragmentQueueBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter= CargoListAdapter({ cargo->
            CargoActivity.startActivity(requireContext(),cargo.id)
        })
        binding.recyclerQueue.adapter=adapter


        var itemDecorationHorizontal = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecorationHorizontal.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider)!!)

        binding.recyclerQueue.addItemDecoration(itemDecorationHorizontal)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queueViewModel.listQueue.observe(viewLifecycleOwner,{ list->
            adapter.setupData(list)
        })
        binding.buttonTestAdd.setOnClickListener {
            queueViewModel.addTest()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}