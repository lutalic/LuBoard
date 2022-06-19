package com.lutalic.luboard.presentation.main.tabs.maintab.boardslist

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.AddUserDialogBinding
import com.lutalic.luboard.databinding.FragmentBoardListBinding
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.BoardsAdapter
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.BottomOffsetDecoration
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.SpacesItemDecoration
import com.lutalic.luboard.utils.toEditable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardListFragment : Fragment(R.layout.fragment_board_list) {

    private val viewModel: BoardListViewModel by viewModels()

    private lateinit var binding: FragmentBoardListBinding

    override fun onResume() {
        super.onResume()
        viewModel.initMethod()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoardListBinding.bind(view)
        initRecyclerView()
        binding.floatingButtonAddBoard.setOnClickListener {
            onAddClick()
        }

        hideButtonOnScroll()
        viewModel.initMethod()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun hideButtonOnScroll() {
        binding.boardsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.floatingButtonAddBoard.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.floatingButtonAddBoard.isShown) {
                    binding.floatingButtonAddBoard.hide()
                }
            }
        })
    }

    private fun initRecyclerView() {
        val boardsAdapter = BoardsAdapter(object : BoardsAdapter.Listener {
            override fun onBoardClick(board: Table) {
                findNavController().navigate(
                    BoardListFragmentDirections.actionDashboardFragmentToBoxFragment(
                        board
                    )
                )
            }

            override fun onBoardLongClick(board: Table, view: View) {
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")
                popupMenu.menu.add(0, ID_EDIT, Menu.NONE, "Edit")
                popupMenu.menu.add(0, ID_ADD_TO_USER, Menu.NONE, "Add user")
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        ID_EDIT -> {
                            findNavController().navigate(
                                BoardListFragmentDirections.actionListBoardFragmentToEditBoardFragment(
                                    board
                                )
                            )
                        }
                        ID_REMOVE -> {
                            viewModel.removeBoard(board)
                        }
                        ID_ADD_TO_USER -> {
                            showCustomInputAlertDialog(board)
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        })
        binding.boardsRecyclerView.adapter = boardsAdapter
        binding.boardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.boardsRecyclerView.addItemDecoration(SpacesItemDecoration(10))
        binding.boardsRecyclerView.addItemDecoration(BottomOffsetDecoration(85))
        boardsAdapter.updateAdapter(viewModel.boards.value ?: mutableListOf<Table>())
        viewModel.boards.observe(viewLifecycleOwner) {
            boardsAdapter.updateAdapter(it)
        }
    }

    private fun onAddClick() {
        findNavController().navigate(
            BoardListFragmentDirections.actionListBoardFragmentToAddNewBoardFragment()
        )
    }

    private fun showCustomInputAlertDialog(table: Table) {
        val dialogBinding = AddUserDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add new user")
            .setView(dialogBinding.root)
            .setPositiveButton("Confirm", null)
            .create()
        dialog.setOnShowListener {
            dialogBinding.emailForAddInput.requestFocus()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogBinding.emailForAddInput.text.toString()
                viewModel.addTableToUser(table = table, enteredText)
                dialog.dismiss()
                Toast.makeText(requireContext(), "Done!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_EDIT = 2
        private const val ID_ADD_TO_USER = 3

    }
}