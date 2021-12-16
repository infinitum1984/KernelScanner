package com.kernel.scanner.scanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kernel.scanner.R
import com.kernel.scanner.cargo.CargoFragment
import com.kernel.scanner.databinding.FragmentScannerBinding
import com.kernel.scanner.findCodeInString
import com.kernel.scanner.settings.SavedSettings
import java.util.concurrent.*
import kotlin.experimental.and
import kotlin.math.abs

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ScannerFragment : Fragment() {
    var _binding: FragmentScannerBinding? = null
    val binding get() = _binding!!
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider:ProcessCameraProvider
    private lateinit var cameraSelector:CameraSelector
    private lateinit var camera:Camera
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    var code = ""

    val COMPENSATION_STEP=-7//выведен эксперементальным путём

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.imageButton.setOnClickListener {
           onFlashLight()
        }
        return binding.root

    }


    @SuppressLint("RestrictedApi")
    override fun onDestroyView() {
        cameraProvider.unbindAll()
        cameraProvider.shutdown()
        cameraExecutor.shutdown()
        recognizer.close()

        super.onDestroyView()
        _binding = null


    }

    @SuppressLint("UnsafeOptInUsageError", "RestrictedApi")
    fun bindPreview(cameraProvider: ProcessCameraProvider) {
        var preview: Preview = Preview.Builder()
            .build()
        cameraSelector = CameraSelector.Builder()

            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider())

        val imageAnalysis = ImageAnalysis.Builder()
            // enable the following line if RGBA output is needed.
            // .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
            if (code!="") return@Analyzer
            // insert your code here.
            val mediaImage = imageProxy.image
            if (mediaImage == null) return@Analyzer


            var image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    if (!isAdded) return@addOnSuccessListener

                    Log.d("D_MainActivity", "bindPreview: ${visionText.text}");
                    val rec_code = findCodeInString(visionText.text)
                    code = rec_code
                    if (rec_code==""){
                        return@addOnSuccessListener
                    }
                    requireActivity().runOnUiThread {
                       binding.textViewCode.text = getString(R.string.code_is_recognized)+rec_code
                       setFragmentResult(CargoFragment.REQUEST_KEY, bundleOf("code" to rec_code))
                        cameraExecutor.shutdown()
                        recognizer.close()
                        cameraProvider.unbindAll()
                        cameraProvider.shutdown()
                        findNavController().navigate(R.id.action_navigation_scanner_to_navigation_cargo)

                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...

                    Log.d("D_MainActivity","bindPreview: ${e.message}");
                    // binding.textView3.text=e.message

                }
                .addOnCompleteListener {
                    imageProxy.close()
                    mediaImage.close()
                }
            // after done, release the ImageProxy object


        })

        camera=cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )

        if (SavedSettings.getIsLowerBrightness(requireContext()) &&
            COMPENSATION_STEP>=camera.cameraInfo.exposureState.exposureCompensationRange.lower
            && camera.cameraInfo.exposureState.isExposureCompensationSupported){

            camera.cameraControl.setExposureCompensationIndex(COMPENSATION_STEP)//увеличиваем контрастность
            Log.d("D_ScannerFragment","bindPreview: lower brihg");

        }

        camera.cameraInfo.torchState.observe(this,{state->
            if (_binding==null) return@observe
            if (!isAdded) return@observe

            if (state==TorchState.ON){

                binding.imageButton.setImageResource(R.drawable.ic_baseline_flashlight_off)

            }else{
                binding.imageButton.setImageResource(R.drawable.ic_baseline_flashlight_on)

            }

        })

    }
    fun analysingBrightness(image: ImageProxy){
        val bytes = ByteArray(image.getPlanes().get(0).getBuffer().remaining())
        image.getPlanes().get(0).getBuffer().get(bytes)
        var total = 0
        for (value in bytes) {
            total += value and 0xFF.toByte()
        }
        if (bytes.size != 0) {
            val luminance = abs(total / bytes.size)
            Log.d("D_TestActivity","analysingBrightness: ${luminance}");
            // luminance is the value you need.
            if (luminance<=50){
                onFlashLight()
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun onFlashLight() {

        if (camera.cameraInfo.hasFlashUnit()){
            if (camera.cameraInfo.torchState.value==TorchState.OFF) {

                camera.cameraControl.enableTorch(true)
            }else{

                camera.cameraControl.enableTorch(false)

            }

        }
    }


}