package com.example.nhlmobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nhlmobileapp.databinding.ActivityRegisterBinding
import com.example.nhlmobileapp.helpers.CryptoManager
import com.example.nhlmobileapp.helpers.SimpleResponse
import com.example.nhlmobileapp.responses.LoginResponse
import com.example.nhlmobileapp.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.registerBtn->{
                handleOnRegisterClick()
            }
        }
    }

    private fun handleOnRegisterClick() {
        binding.errorLbl.visibility = View.INVISIBLE
        binding.errorLbl.text = ""
        val username = binding.usernameField.text.toString()
        val password1 = binding.password1Field.text.toString()
        val password2 = binding.password2Field.text.toString()

        viewModel.executeRegister(username, password1, password2)
        viewModel.registerLiveData.observe(this) { response ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: SimpleResponse<LoginResponse>) {
        if (response.statusCode == 200) {
            CryptoManager("authToken").encrypt(response.body.token, filesDir)
            CryptoManager("owmKey").encrypt(response.body.owmKey, filesDir)

            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            binding.errorLbl.visibility = View.VISIBLE
            binding.errorLbl.text = response.message
        }
    }
}