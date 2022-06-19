package com.lutalic.luboard.presentation.main.tabs.posts

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentPostsBinding
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.AllPostsAdapter
import com.lutalic.luboard.presentation.main.tabs.maintab.adapters.SpacesItemDecoration
import com.lutalic.luboard.presentation.main.tabs.maintab.board.BoardFragment
import com.lutalic.luboard.presentation.main.tabs.maintab.board.BoardFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.fragment_posts) {

    private lateinit var binding: FragmentPostsBinding

    private val viewModel: PostsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostsBinding.bind(view)
        viewModel.init()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }

    private fun initRecyclerView() {
        val postsAdapter = AllPostsAdapter(object : AllPostsAdapter.Listener {

            override fun onPostClick(post: Post) {
                //val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                findNavController().navigate(
                    PostsFragmentDirections.actionPostsFragmentToEditPostFragment2(
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
                                PostsFragmentDirections.actionPostsFragmentToEditPostFragment2(post)
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
        binding.postsRecyclerViewAll.adapter = postsAdapter
        binding.postsRecyclerViewAll.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.postsRecyclerViewAll.addItemDecoration(SpacesItemDecoration(10))
        postsAdapter.updateAdapter(viewModel.posts.value)
        viewModel.posts.observe(viewLifecycleOwner) {
            postsAdapter.updateAdapter(it)
        }
    }

    companion object {
        private const val ID_REMOVE = 1
        private const val ID_EDIT = 2

    }
}