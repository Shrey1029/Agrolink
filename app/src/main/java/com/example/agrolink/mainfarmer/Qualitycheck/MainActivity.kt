package com.example.agrolink.mainfarmer.Qualitycheck

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.agrolink.R
import com.example.agrolink.ml.FinalModel
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.DataType

class MainActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var qualityTextView: TextView
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button

    private val cameraRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_qc)

        // Initialize views
        productImage = findViewById(R.id.product_image)
        qualityTextView = findViewById(R.id.quality)
        cameraButton = findViewById(R.id.camera_button)
        galleryButton = findViewById(R.id.gallery_button)

        // Set up button listeners
        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
            }
        }

        galleryButton.setOnClickListener {
            openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, cameraRequestCode)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                productImage.setImageBitmap(bitmap)
                runModelOnImage(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            productImage.setImageBitmap(bitmap)
            runModelOnImage(bitmap)
        }
    }

    private fun runModelOnImage(bitmap: Bitmap) {
        // Resize the bitmap to match the TFLite model's input size
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        // Create TensorImage from the bitmap
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)

        // Load the TFLite model
        val model = FinalModel.newInstance(this)

        // Create the input feature (tensor buffer) matching the model input shape
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Run inference on the input
        val outputs = model.process(inputFeature0)
        val probabilities = outputs.outputFeature0AsTensorBuffer.floatArray

        // Find the index with the highest probability
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = probabilities[maxIndex] * 100

        // Map the index to quality labels (only high and low)
        val quality = when (maxIndex) {
            1 -> "High Quality" // 1 for high quality
            0 -> "Low Quality"  // 0 for low quality
            else -> "Unknown"
        }

        // Display the result
        qualityTextView.text = "Quality: $quality\nConfidence: ${"%.2f".format(confidence)}%"

        // Close the model to release resources
        model.close()
    }

}
