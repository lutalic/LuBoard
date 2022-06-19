package com.lutalic.luboard.presentation.main.tabs.maintab.boardslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FagmentAddNewBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewBoardFragment : Fragment(R.layout.fagment_add_new_board) {

    private lateinit var binding: FagmentAddNewBoardBinding

    private val viewModel: AddNewBoardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FagmentAddNewBoardBinding.bind(view)
        createButtonProcessing()
    }

    private fun createButtonProcessing() {
        binding.createNewBoardBtn.setOnClickListener {
            viewModel.addNewBoard(
                name = binding.nameOfBoardInput.text.toString(),
                id = 3,
            )
            findNavController().popBackStack()
        }
    }

}