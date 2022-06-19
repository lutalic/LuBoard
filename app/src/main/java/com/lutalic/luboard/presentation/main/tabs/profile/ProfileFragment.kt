package com.lutalic.luboard.presentation.main.tabs.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navOptions
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentProfileBinding
import com.lutalic.luboard.utils.findTopNavController
import com.lutalic.luboard.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.logoutButton.setOnClickListener { onLogoutButtonPressed() }
        binding.sendFeedbackButton.setOnClickListener {
            sendEmailToLutalic()
        }
        observeAccountDetails()
        observeRestartAppFromLoginScreenEvent()
    }

    private fun sendEmailToLutalic() {
        val emailIntent =
            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:lutalic.comp@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        startActivity(Intent.createChooser(emailIntent, "Title"))
    }

    private fun observeAccountDetails() {
        viewModel.account.observe(viewLifecycleOwner) { account ->
            if (account == null)
                return@observe
            binding.emailTextView.text = account.email
        }
    }

    private fun observeRestartAppFromLoginScreenEvent() {
        viewModel.restartWithSignInEvent.observeEvent(viewLifecycleOwner) {
            findTopNavController().navigate(R.id.signInFragment, null, navOptions {
                popUpTo(R.id.tabsFragment) {
                    inclusive = true
                }
            })
        }
    }

    private fun onLogoutButtonPressed() {
        viewModel.logout()
    }
}