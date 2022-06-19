package com.lutalic.luboard.presentation.main.tabs.maintab.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {
    private lateinit var binding: FragmentPostBinding

    private val viewModel: PostViewModel by viewModels()

    private val args by navArgs<PostFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostBinding.bind(view)
        binding.namePost.text = args.name
        binding.descriptionForPost.text = args.description
        binding.dateTextView.text = args.date
    }
}