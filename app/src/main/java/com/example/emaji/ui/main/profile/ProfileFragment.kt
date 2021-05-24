package com.example.emaji.ui.main.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.emaji.R
import com.example.emaji.models.User
import com.example.emaji.ui.login.LoginActivity
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val profileViewModel : ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        goToActivity()
        logout()
    }

    private fun logout() {
        requireView().btn_logout.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setMessage(getString(R.string.message_logout))
                setPositiveButton("ya"){d, _ ->
                    d.dismiss()
                    Constants.clearToken(requireActivity())
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                setNegativeButton("tidak"){d, _ -> d.dismiss()  }
            }.show()
        }
    }

    private fun goToActivity() {
        requireView().btn_update_profile.setOnClickListener {

        }
    }

    private fun observe() {
        observeState()
        observeCurrentUser()
    }

    private fun observeState() = profileViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCurrentUser() = profileViewModel.listenToCurrentUser().observe(viewLifecycleOwner, Observer { handleCurrentUser(it) })
    private fun fetchCurrentUser() = profileViewModel.fetchCurrentUser(Constants.getToken(requireActivity()))

    private fun handleCurrentUser(user: User?) {
        user?.let {
            requireView().profile_name.text = it.name
            requireView().profile_email.text = it.email
            requireView().profile_role.text = it.role
        }
    }

    private fun handleUiState(state: ProfileState?) {
        state?.let {
            when(it){
                is ProfileState.Loading -> handleLoading(it.state)
                is ProfileState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchCurrentUser()
    }

}