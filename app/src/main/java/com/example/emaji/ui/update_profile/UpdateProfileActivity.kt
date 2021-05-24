package com.example.emaji.ui.update_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.emaji.R
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfileActivity : AppCompatActivity() {

    private val updateProfileViewModel : UpdateProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        observe()
        update()
    }

    private fun observe() {
        observeState()
    }

    private fun observeState() = updateProfileViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(profileState: UpdateProfileState?) {
        profileState?.let {
            when(it){
                is UpdateProfileState.Loading -> handleLoading(it.state)
                is UpdateProfileState.ShowToast -> showToast(it.message)
                is UpdateProfileState.Success -> handleSuccess()
                is UpdateProfileState.Validate -> handleValidate(it)
                is UpdateProfileState.Reset -> handleReset()
            }
        }
    }

    private fun update(){
        btn_update.setOnClickListener {
            val name = input_name.text.toString().trim()
            val email = input_email.text.toString().trim()
            if (updateProfileViewModel.validate(name, email)){
                updateProfileViewModel.updateProfile(Constants.getToken(this@UpdateProfileActivity), name, email)
            }
        }
    }

    private fun handleReset() {
        setErrorName(null)
        setErrorEmail(null)
    }

    private fun handleValidate(validate: UpdateProfileState.Validate) {
        validate.name?.let { setErrorName(it) }
        validate.email?.let { setErrorEmail(it) }
    }

    private fun handleSuccess() {
        finish()
    }

    private fun handleLoading(state: Boolean) {
        btn_update.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun setErrorName(err : String?) { error_name.error = err }
    private fun setErrorEmail(err : String?) { error_email.error = err }
}