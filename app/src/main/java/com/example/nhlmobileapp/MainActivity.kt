package com.example.nhlmobileapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.nhlmobileapp.databinding.ActivityMainBinding
import com.example.nhlmobileapp.helpers.CryptoManager
import com.example.nhlmobileapp.helpers.RSAEncryption
import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.responses.LoginResponse
import com.example.nhlmobileapp.viewmodels.LoginViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    public companion object {
        lateinit var fileDirectory: File
    }

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginBtn.setOnClickListener(this)
        binding.registerBtn.setOnClickListener(this)

        fileDirectory = filesDir

        validateToken()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.loginBtn->{
                handleOnLoginClick()
            }
            R.id.registerBtn->{
                handleOnRegisterClick()
            }
        }
    }

    private fun validateToken() {
        viewModel.executeValidateToken()
        viewModel.validateTokenLiveData.observe(this) { response ->
            if (response.isSuccessful) {
                if (response.body.valid) {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
            }
        }
    }

    private fun handleOnLoginClick() {
        binding.errorField.visibility = View.INVISIBLE
        binding.errorField.text = ""
        val username = binding.usernameField.text.toString()
        val password = binding.passwordField.text.toString()

        viewModel.executeLogin(username, password)
        viewModel.loginLiveData.observe(this) { response ->
            handleResponse(response)
        }
    }

    private fun handleOnRegisterClick() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun handleResponse(response: SimpleResponse<LoginResponse>) {
        if (response.statusCode == 200) {
            CryptoManager("authToken").encrypt(response.body.token, filesDir)
            CryptoManager("owmKey").encrypt(response.body.owmKey, filesDir)

            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            binding.errorField.visibility = View.VISIBLE
            binding.errorField.text = response.message
        }
    }
}