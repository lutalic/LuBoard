package com.lutalic.luboard.presentation.main.tabs.maintab.board

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentBoardBinding
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.PostsAdapter
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.SpacesItemDecoration
import com.lutalic.luboard.presentation.main.tabs.maintab.boardslist.BoardListFragment
import com.lutalic.luboard.presentation.main.tabs.maintab.boardslist.BoardListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardFragment : Fragment(R.layout.fragment_board) {

    private lateinit var binding: FragmentBoardBinding

    private val viewModel: BoardViewModel by viewModels()

    private val args by navArgs<BoardFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoardBinding.bind(view)
        viewModel.init()
        initRecyclerView()
        binding.floatingButtonAddPost.setOnClickListener {
            onAddClick(args.board.id, args.board.name)
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        hideButtonOnScroll()
    }


    private fun onAddClick(boardId: Int, boardName: String) {
        findNavController().navigate(
            BoardFragmentDirections.actionBoardFragmentToAddNewPostFragment(boardId, boardName)
        )
    }

    private fun hideButtonOnScroll() {
        binding.postsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.floatingButtonAddPost.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.floatingButtonAddPost.isShown) {
                    binding.floatingButtonAddPost.hide()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }

    private fun initRecyclerView() {
        val postsAdapter = PostsAdapter(object : PostsAdapter.Listener {


            override fun onPostClick(post: Post) {
                //val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                findNavController().navigate(
                    BoardFragmentDirections.actionBoardFragmentToEditPostFragment(
                        post
                    )
                )
            }

            override fun onPostLongClick(post: Post, view: View) {
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")
                //popupMenu.menu.add(0, ID_EDIT, Menu.NONE, "Edit")
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        /*ID_EDIT -> {
                            findNavController().navigate(
                                BoardFragmentDirections.actionBoardFragmentToEditPostFragment(post)
                            )
                        }*/
                        ID_REMOVE -> {
                            viewModel.removePost(post)
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        })
        binding.postsRecyclerView.adapter = postsAdapter
        binding.postsRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.postsRecyclerView.addItemDecoration(SpacesItemDecoration(10))
        postsAdapter.updateAdapter(viewModel.posts.value)
        viewModel.posts.observe(viewLifecycleOwner) {
            postsAdapter.updateAdapter(it)
        }
    }

    companion object {
        private const val ID_REMOVE = 3
        private const val ID_EDIT = 4

    }
}