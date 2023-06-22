package com.github.user.soilitouraplication.ui.detection


import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Classifier(
    var interpreter: Interpreter? = null,
    var inputSize: Int = 0,
    var labelList: List<String> = emptyList(),
) : SoilClassifier, Parcelable {

    companion object {

        @Throws(IOException::class)
        fun create(
            assetManager: AssetManager,
            modelPath: String,
            labelPath: String,
            inputSize: Int,
        ): Classifier {
            val classifier = Classifier()
            classifier.interpreter = Interpreter(classifier.loadModelFile(assetManager, modelPath))
            classifier.labelList = classifier.loadLabelList(assetManager, labelPath)
            classifier.inputSize = inputSize
            return classifier
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Classifier> = object : Parcelable.Creator<Classifier> {
            override fun createFromParcel(parcel: Parcel): Classifier {
                return Classifier(parcel)
            }

            override fun newArray(size: Int): Array<Classifier?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(parcel: Parcel) : this(
        null, parcel.readInt(), parcel.createStringArrayList() ?: emptyList()
    )

    override fun recognizeImage(bitmap: Bitmap): SoilClassifier.Recognition {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)
        interpreter?.getInputTensor(0)
        val outputTensor = interpreter?.getOutputTensor(0)

        val inputData = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        inputData.order(ByteOrder.nativeOrder())
        inputData.rewind()

        // Pre-process the input image
        val intValues = IntArray(inputSize * inputSize)
        resizedBitmap.getPixels(
            intValues, 0, resizedBitmap.width, 0, 0, resizedBitmap.width, resizedBitmap.height
        )
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = intValues[pixel++]

                // Normalize the pixel values
                val normalizedValue = (value shr 16 and 0xFF) / 255.0f
                inputData.putFloat(normalizedValue)
                val normalizedValue2 = (value shr 8 and 0xFF) / 255.0f
                inputData.putFloat(normalizedValue2)
                val normalizedValue3 = (value and 0xFF) / 255.0f
                inputData.putFloat(normalizedValue3)
            }
        }

        // Run inference
        val outputData = ByteBuffer.allocateDirect(4 * outputTensor?.shape()?.get(1)!!)
        outputData.order(ByteOrder.nativeOrder())
        outputData.rewind()

        interpreter?.run(inputData, outputData)

        // Post-process the output
        outputData.rewind()
        val outputArray = Array(1) { FloatArray(outputTensor.shape()?.get(1)!!) }
        outputData.asFloatBuffer().get(outputArray[0])

        return getSortedResult(outputArray)
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        val labelList = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        while (true) {
            val line = reader.readLine() ?: break
            labelList.add(line)
        }
        reader.close()
        return labelList
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): SoilClassifier.Recognition {
        val maxConfidenceIndex = labelProbArray[0].indices.maxByOrNull { labelProbArray[0][it] }
        val maxConfidence = labelProbArray[0][maxConfidenceIndex!!]

        val recognition = SoilClassifier.Recognition(
            "" + maxConfidenceIndex,
            if (labelList.size > maxConfidenceIndex) labelList[maxConfidenceIndex] else "Unknown",
            maxConfidence
        )

        return recognition.copy(title = recognition.title, confidence = recognition.confidence)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(inputSize)
        parcel.writeStringList(labelList)
    }

    override fun describeContents(): Int {
        return 0
    }
}
