package com.kernel.scanner.cargo

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.material.snackbar.Snackbar
import com.kernel.scanner.model.Seal


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CargoFragment : Fragment(), SealListAdapter.SealClickListener {

    companion object{
        val REQUEST_KEY="GET_CODE"
    }

    private var _binding: FragmentCargoBinding? = null

    private val binding get() = _binding!!

    private lateinit var cargoViewModel :CargoViewModel

    lateinit var sealAdapter:SealListAdapter
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                findNavController().navigate(R.id.action_navigation_cargo_to_navigation_scanner)

            } else {
                Snackbar.make(binding.root,getString(R.string.allow_permissions),Snackbar.LENGTH_SHORT).show()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCargoBinding.inflate(inflater, container, false)

        cargoViewModel= ViewModelProvider(this,CargoViewModelFactory(CargoActivity.getIdFromIntent(requireActivity().intent))).get(CargoViewModel::class.java)

        setFragmentResultListener(REQUEST_KEY,{
            requestKey, bundle ->
            binding.textInputNumber.setText(bundle["code"].toString())

        })

        binding.textInputNumber.addTextChangedListener {editable->

            if (editable==null) return@addTextChangedListener

            val text=editable.toString()
            cargoViewModel.processInputNumber(text)
        }

        sealAdapter=SealListAdapter(this)
        binding.recyclerSeal.adapter=sealAdapter


         var itemDecorationVertical = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecorationVertical.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider)!!)

        var itemDecorationHorizontal = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        itemDecorationHorizontal.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.recycler_divider)!!)

        binding.recyclerSeal.addItemDecoration(itemDecorationVertical)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargoViewModel.cargo.observe(this,{cargo->
            bindCargo(cargo)
        })

        binding.buttonScan.setOnClickListener {
            cargoViewModel.scan(binding.textInputNumber.text.toString())
        }

        cargoViewModel.savedState.observe(this,{saved->
           processSaved(saved)
        })
        cargoViewModel.sanState.observe(this,{isScan->
           processIsScan(isScan)

        })
        cargoViewModel.textClear.observe(this,{isClear->
            if (isClear)
                binding.textInputNumber.setText("")

        })
        cargoViewModel.seals.observe(this,{listSeal->
            sealAdapter.setupData(listSeal)
        })


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun processIsScan(isScan:Boolean){
        if (isScan) {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                findNavController().navigate(R.id.action_navigation_cargo_to_navigation_scanner)

            }else{
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }


        }
    }

    private fun processSaved(saved:Boolean){
        if (saved){
            binding.buttonScan.text=getString(R.string.save_text)
            binding.textViewInfo.text=getString(R.string.check_text)

        }else{
            binding.textViewInfo.text=getString(R.string.info_scan_text)
            binding.buttonScan.text=getString(R.string.scan_text)

        }
    }

    private fun bindCargo(cargo: Cargo) {
        binding.textViewCarNum.text=cargo.carNumber
        binding.textViewTrailerNum.text=cargo.trailerNumber
        binding.textViewDriverNum.text=cargo.driverPhone
        binding.textViewDriverName.text=cargo.driverName

        if (cargo.lastEdit>0) {
            val timeTxt = DateUtils.formatDateTime(
                requireContext(), cargo.lastEdit,
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
            )
            binding.textViewLastEdit.text = timeTxt

        }
        activity?.title=cargo.carNumber

    }




    override fun onItemClick(seal: Seal) {
        copySealNumber(seal)
    }

    private fun copySealNumber(seal: Seal) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", seal.number)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root,getString(R.string.num_is_copied),Snackbar.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(seal: Seal) {
        cargoViewModel.deleteSeal(seal)
    }
}