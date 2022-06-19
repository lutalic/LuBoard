package com.lutalic.luboard.presentation.main.tabs.maintab.post

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FagmentAddNewPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewPostFragment : Fragment(R.layout.fagment_add_new_post) {

    private lateinit var binding: FagmentAddNewPostBinding
    private val args by navArgs<AddNewPostFragmentArgs>()
    private var selectedColor: String = "#97BAFF"

    private val viewModel: AddNewPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FagmentAddNewPostBinding.bind(view)
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textDateTime.text = viewModel.getCurrentDate()
        initMiscellaneous()
        createButtonProcessing()
        setInputColor()
    }

    private fun createButtonProcessing() {
        binding.imageSave.setOnClickListener {
            viewModel.addNewPost(
                name = binding.inputNoteTitle.text.toString(),
                description = binding.inputNoteText.text.toString(),
                colour = selectedColor,
                tableId = args.boardId,
                tableName = args.boardName
            )
            findNavController().popBackStack()
        }
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.includeMiscellaneous.layoutMiscellaneous
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)
        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        binding.includeMiscellaneous.imageColor1.setOnClickListener {
            selectedColor = "#97BAFF"
            clearAllCheckMarks()
            binding.includeMiscellaneous.imageColor1.setImageResource(R.drawable.ic_done)
            setInputColor()
        }
        binding.includeMiscellaneous.imageColor2.setOnClickListener {
            selectedColor = "#FDBE3B"
            clearAllCheckMarks()
            binding.includeMiscellaneous.imageColor2.setImageResource(R.drawable.ic_done)
            setInputColor()

        }
        binding.includeMiscellaneous.imageColor3.setOnClickListener {
            selectedColor = "#FF4842"
            clearAllCheckMarks()
            binding.includeMiscellaneous.imageColor3.setImageResource(R.drawable.ic_done)
            setInputColor()

        }
        binding.includeMiscellaneous.imageColor4.setOnClickListener {
            selectedColor = "#3A52FC"
            clearAllCheckMarks()
            binding.includeMiscellaneous.imageColor4.setImageResource(R.drawable.ic_done)
            setInputColor()

        }
        binding.includeMiscellaneous.imageColor5.setOnClickListener {
            selectedColor = "#000000"
            clearAllCheckMarks()
            binding.includeMiscellaneous.imageColor5.setImageResource(R.drawable.ic_done)
            setInputColor()

        }
    }

    private fun clearAllCheckMarks(){
        binding.includeMiscellaneous.imageColor1.setImageResource(0)
        binding.includeMiscellaneous.imageColor2.setImageResource(0)
        binding.includeMiscellaneous.imageColor3.setImageResource(0)
        binding.includeMiscellaneous.imageColor4.setImageResource(0)
        binding.includeMiscellaneous.imageColor5.setImageResource(0)
    }

    private fun setInputColor() {
        val gradientDrawable: GradientDrawable =
            binding.viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedColor))
    }
}