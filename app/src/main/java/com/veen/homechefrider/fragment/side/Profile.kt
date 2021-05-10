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
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import com.veen.gaffer.utils.Constants
import com.veen.gaffer.utils.Helper
import com.veen.homechefrider.R
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentProfileBinding
import com.veen.homechefrider.model.profile.image.ImageReq
import com.veen.homechefrider.model.profile.image.ImageRes
import com.veen.homechefrider.model.profile.update.UpdateProfileReq
import com.veen.homechefrider.model.profile.update.UpdateProfileRes
import com.veen.homechefrider.model.profile.view.ViewProfileReq
import com.veen.homechefrider.model.profile.view.ViewProfileRes
import com.veen.homechefrider.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val pickImage = 100
    private var encodedImage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        val getsaveToken = AppUtils.getsaveToken(requireContext())
        val getsaveLogin = AppUtils.getsaveLogin(requireContext())

        binding.rlPhotoParent.setOnClickListener {
            if (!Helper.isReadExternalStoragePermissionAllowed(requireActivity()) || (!Helper.isCameraPermission(requireActivity()))) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CAMERA), Constants.STORAGE_PERMISSION_CODE_PHOTO)
            } else {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (TextUtils.isEmpty(binding.editName.text.toString())) {
                binding.editName.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editMobile.text.toString())) {
                binding.editMobile.setError("Field not empty!")
            } else if (TextUtils.isEmpty(binding.editAddress.text.toString())) {
                binding.editAddress.setError("Field not empty")
            } else {
                doUpdate(getsaveToken, getsaveLogin)
            }
        }

        try {
            RetrofitInstance.instence?.viewprofile(getsaveToken, ViewProfileReq(
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ViewProfileRes> {
                override fun onResponse(call: Call<ViewProfileRes>, response: Response<ViewProfileRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        Picasso.get().load(response.body()!!.data.profile_pic).into(binding.viewProfile)
                        binding.editName.setText(response.body()!!.data.name, TextView.BufferType.EDITABLE)
                        binding.Email.text = response.body()!!.data.email
                        binding.editMobile.setText(response.body()!!.data.phone, TextView.BufferType.EDITABLE)
                        binding.editAddress.setText(response.body()!!.data.address, TextView.BufferType.EDITABLE)
                    }
                }

                override fun onFailure(call: Call<ViewProfileRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }

    private fun doUpdate(token: String, login: String) {
        try {
            RetrofitInstance.instence?.updateprofile(token, UpdateProfileReq(
                binding.editAddress.text.toString(),
                binding.editName.text.toString(),
                binding.editMobile.text.toString(),
                    login.toInt()
            ))!!.enqueue(object : Callback<UpdateProfileRes> {
                override fun onResponse(call: Call<UpdateProfileRes>, response: Response<UpdateProfileRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateProfileRes>, t: Throwable) {
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
            viewProfile!!.setImageURI(imageUri)
            val bitmap: Bitmap
            try {
                bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(imageUri!!))
//                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false)
                encodedImage = getEncoded64ImageStringFromBitmap(bitmap).toString()
                Log.e("exp", encodedImage)
                updateImage(encodedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateImage(image: String) {
        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())
            RetrofitInstance.instence?.imageupdate(getsaveToken, ImageReq(
                    image,
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<ImageRes> {
                override fun onResponse(call: Call<ImageRes>, response: Response<ImageRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "" + response.body()!!.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ImageRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
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