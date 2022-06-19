package com.lutalic.luboard.presentation.main.tabs.maintab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lutalic.luboard.databinding.BoardPreviewViewBinding
import com.lutalic.luboard.model.database.entities.Table

class BoardsAdapter(
    private val onBoardClickListener: Listener
) : RecyclerView.Adapter<BoardsAdapter.BoardViewHolder>() {
    private val boards: MutableList<Table?> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BoardViewHolder {
        val view = BoardPreviewViewBinding.inflate(LayoutInflater.from(viewGroup.context))
        view.bodyOfBoardCard.setOnClickListener { v: View ->
            onBoardClickListener.onBoardClick(v.tag as Table)
        }
        view.bodyOfBoardCard.setOnLongClickListener { v:View ->
            onBoardClickListener.onBoardLongClick(v.tag as Table, v)
            true
        }
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: BoardViewHolder, position: Int) {
        val board = boards[position]
        viewHolder.bind(board)
        viewHolder.binding.bodyOfBoardCard.tag = board
    }

    override fun getItemCount() = boards.size

    fun updateAdapter(newList: List<Table?>) {
        boards.clear()
        boards.addAll(newList)
        notifyDataSetChanged() //TODO Is it possible to do better
    }

    class BoardViewHolder(val binding: BoardPreviewViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(board: Table?) {
            binding.apply {
                titleBoard.text = board?.name
            }
        }
    }

    interface Listener {
        fun onBoardClick(board: Table)
        //fun onLongBoardClick(board:Board)
        fun onBoardLongClick(board: Table, view: View)
    }
}