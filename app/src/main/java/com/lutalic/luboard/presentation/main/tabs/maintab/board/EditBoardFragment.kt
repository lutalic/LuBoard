package com.lutalic.luboard.presentation.main.tabs.maintab.board

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FagmentEditBoardBinding
import com.lutalic.luboard.utils.toEditable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBoardFragment : Fragment(R.layout.fagment_edit_board) {
    private lateinit var binding: FagmentEditBoardBinding

    private val viewModel:EditBoardViewModel by viewModels()

    private val args by navArgs<EditBoardFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FagmentEditBoardBinding.bind(view)
        binding.nameOfBoardEdit.text = args.table.name.toEditable()
        binding.editBoardBtn.setOnClickListener {
            viewModel.editTable(
                args.table.copy(
                    name = binding.nameOfBoardEdit.text.toString()
                )
            )
            findNavController().popBackStack()
        }
    }


}