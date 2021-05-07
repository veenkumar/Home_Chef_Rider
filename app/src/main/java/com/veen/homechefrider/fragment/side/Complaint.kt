package com.veen.homechefrider.fragment.side

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.veen.gaffer.utils.Constants
import com.veen.gaffer.utils.Helper
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentComplaintBinding
import com.veen.homechefrider.model.complain.ComplainReq
import com.veen.homechefrider.model.complain.ComplainRes
import com.veen.homechefrider.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_complaint.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class Complaint : Fragment() {
    private val pickImage = 101
    private var encodedImage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_complaint, container, false)
        val binding = DataBindingUtil.inflate<FragmentComplaintBinding>(inflater, R.layout.fragment_complaint, container, false)
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())
        binding.complaintImage.setOnClickListener {
            if (!Helper.isReadExternalStoragePermissionAllowed(requireActivity()) || (!Helper.isCameraPermission(requireActivity()))) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CAMERA), Constants.STORAGE_PERMISSION_CODE_PHOTO)
            } else {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
        }

        binding.complaintUpdate.setOnClickListener {
            if (TextUtils.isEmpty(binding.complaintSubject.text.toString())) {
                binding.complaintSubject.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.complaintMessage.text.toString())) {
                binding.complaintMessage.setError("Field not empty!")
            } else {
                doComplaint(getsaveToken, getsaveLogin, binding.complaintSubject, binding.complaintMessage)
            }
        }
        return binding.root
    }

    private fun doComplaint(
        getsaveToken: String,
        getsaveLogin: String,
        complaintSubject: EditText,
        complaintMessage: EditText
    ) {
        try {
            RetrofitInstance.instence?.complain(getsaveToken, ComplainReq(
                encodedImage,
                complaintMessage.text.toString(),
                complaintSubject.text.toString(),
                getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ComplainRes> {
                override fun onResponse(call: Call<ComplainRes>, response: Response<ComplainRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "" + response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        complaintMessage.text.clear()
                        complaintSubject.text.clear()
                        complaintImagetext.text = ""
                    }
                }

                override fun onFailure(call: Call<ComplainRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == Constants.STORAGE_PERMISSION_CODE_PHOTO) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            val imageUri = data!!.data
            complaintImagetext.text = "Image Available"
            val bitmap: Bitmap
            try {
                bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(imageUri!!))
//                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false)
                encodedImage = getEncoded64ImageStringFromBitmap(bitmap).toString()
                Log.e("exp", encodedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteFormat = stream.toByteArray()
        val imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP)
        return imgString
    }
}