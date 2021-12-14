package com.kernel.scanner.cargo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kernel.scanner.R
import com.kernel.scanner.databinding.FragmentScannerBinding
import com.kernel.scanner.findCodeInString
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

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
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    var code = ""

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
            //.setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
            if (code!="") return@Analyzer
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            // insert your code here.
            val mediaImage = imageProxy.image
            if (mediaImage == null) return@Analyzer


            var image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    // ...
                    if (!isAdded) return@addOnSuccessListener

                    Log.d("D_MainActivity", "bindPreview: ${visionText.text}");
                    val rec_code = findCodeInString(visionText.text)
                    code = rec_code
                    if (rec_code=="") return@addOnSuccessListener


                    requireActivity().runOnUiThread {
                            binding.textViewCode.text = "Розпізнано код: "+rec_code
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

        cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }

}