package com.example.shoppingcheck_list

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shoppingcheck_list.databinding.ActivityProductRecognizeBinding
import com.example.shoppingcheck_list.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.lang.Integer.min
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ProductRecognizeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductRecognizeBinding
    private lateinit var button: Button
    private lateinit var resText: TextView
    private lateinit var imageView: ImageView
    private val imageSize = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductRecognizeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        button = binding.photoButton
        resText = binding.resultText
        imageView = binding.preview

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                takePicturePreview.launch(null)
            } else {
                requestedPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

    }
    /**
     * The function which check camera permission and start taking photo function
     */
    private val requestedPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePicturePreview.launch(null)
            }
        }

    /**
     * The function which take photo and start recognition
     */
    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {

                val dimension = min(bitmap.width, bitmap.height)
                var bitmapFin = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)

                bitmapFin = Bitmap.createScaledBitmap(bitmapFin, imageSize, imageSize, false)
                imageView.setImageBitmap(bitmapFin)
                outputGenerator(bitmapFin)
            }
        }

    /**
     * The function which recognise the letter
     * @param bitmap the bitmap of picture
     */
    private fun outputGenerator(bitmap: Bitmap) {
        val model = Model.newInstance(this)


        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 50, 50), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++]
                byteBuffer.putFloat((value and 0xFF) * (1f / 51f))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer


        val confidences: FloatArray = outputFeature0.floatArray
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        Toast.makeText(this, maxPos.toString(), Toast.LENGTH_LONG).show();

        model.close()
    }

}