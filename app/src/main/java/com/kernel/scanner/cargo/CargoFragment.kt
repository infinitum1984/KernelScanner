package com.kernel.scanner.cargo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kernel.scanner.R
import com.kernel.scanner.adapter.SealListAdapter
import com.kernel.scanner.databinding.FragmentCargoBinding
import com.kernel.scanner.model.Cargo
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.OrientationHelper
import com.kernel.scanner.test.TestActivity


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CargoFragment : Fragment() {

    private var _binding: FragmentCargoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _cargoViewModel:CargoViewModel?=null
    private val cargoViewModel get() = _cargoViewModel!!
    companion object{
        val REQUEST_KEY="GET_CODE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCargoBinding.inflate(inflater, container, false)


        _cargoViewModel= ViewModelProvider(this,CargoViewModelFactory(CargoActivity.getIdFromIntent(requireActivity().intent))).get(CargoViewModel::class.java)
        binding.buttonScan.setOnClickListener {
            cargoViewModel.scan(binding.textInputNumber.text.toString())
        }
        cargoViewModel.cargo.observe(this,{cargo->
           bindCargo(cargo)
        })

        cargoViewModel.savedState.observe(this,{saved->
            if (saved){
                binding.buttonScan.text="Зберегти"
                binding.textViewInfo.text="Переконайтеся, що номер введений вірно та натисніть зберегти."

            }else{
                binding.textViewInfo.text="Наведить камеру телефона на пломбу та натисніть сканувати."
                binding.buttonScan.text="Сканувати"

            }
        })
        cargoViewModel.sanState.observe(this,{isScan->
            if (isScan) {
                //startActivity(Intent(requireContext(),TestActivity::class.java))
                findNavController().navigate(R.id.action_navigation_cargo_to_navigation_scanner)
            }else{
            }

        })
        setFragmentResultListener(REQUEST_KEY,{
            requestKey, bundle ->
            binding.textInputNumber.setText(bundle["code"].toString())

        })
        binding.textInputNumber.addTextChangedListener {
            if (it==null) return@addTextChangedListener
            val text=it!!.toString()
            cargoViewModel.processInputNumber(text)
        }
        val sealAdapter=SealListAdapter({

        })
        binding.recyclerSeal.adapter=sealAdapter
        cargoViewModel.seals.observe(this,{
            sealAdapter.setupData(it)
        })
        cargoViewModel.textClear.observe(this,{
            if (it){
                binding.textInputNumber.setText("")
            }
        })
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            OrientationHelper.VERTICAL,
        )
        var itemDecorationVertical = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecorationVertical.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        var itemDecorationHorizontal = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        itemDecorationHorizontal.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        binding.recyclerSeal.addItemDecoration(itemDecorationVertical)
        binding.recyclerSeal.addItemDecoration(itemDecorationHorizontal)
        return binding.root

    }

    private fun bindCargo(cargo: Cargo) {
        binding.textViewCarNum.text=cargo.carNumber
        binding.textViewTrailerNum.text=cargo.trailerNumber
        binding.textViewDriverNum.text=cargo.driverPhone
        binding.textViewDriverName.text=cargo.driverName
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}