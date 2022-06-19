package com.lutalic.luboard.presentation.main.auth.signup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentSignUpBinding
import com.lutalic.luboard.model.accounts.entities.SignUpData
import com.lutalic.luboard.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment() : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel:SignUpViewModel by viewModels()

    private val args by navArgs<SignUpFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.createAccountButton.setOnClickListener { onCreateAccountButtonPressed() }

        if (savedInstanceState == null && getEmailArgument() != null) {
            binding.emailSignUp.setText(getEmailArgument())
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        observeState()
        observeGoBackEvent()
    }

    private fun onCreateAccountButtonPressed() {
        val signUpData = SignUpData(
            email = binding.emailSignUp.text.toString(),
            password = binding.passwordSignUp.text.toString(),
            repeatPassword = binding.repeatPasswordSignUp.text.toString(),
        )
        viewModel.signUp(signUpData)
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        binding.createAccountButton.isEnabled = state.enableViews
        binding.emailSignUp.isEnabled = state.enableViews
        binding.passwordSignUp.isEnabled = state.enableViews
        binding.repeatPasswordSignUp.isEnabled = state.enableViews

        fillError(binding.emailSignUp, state.emailErrorMessageRes)
        fillError(binding.passwordSignUp, state.passwordErrorMessageRes)
        fillError(binding.repeatPasswordSignUp, state.repeatPasswordErrorMessageRes)

    }

    private fun fillError(input: EditText, @StringRes stringRes: Int) {
        if (stringRes == SignUpViewModel.NO_ERROR_MESSAGE) {
            input.error = null
            //input.isErrorEnabled = false
        } else {
            input.error = getString(stringRes)
            //input.isErrorEnabled = true
        }
    }

    private fun observeGoBackEvent() = viewModel.goBackEvent.observeEvent(viewLifecycleOwner) {
        findNavController().popBackStack()
    }

    private fun getEmailArgument(): String? = args.email
}