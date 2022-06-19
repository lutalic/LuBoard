package com.lutalic.luboard.presentation.main.tabs.maintab.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lutalic.luboard.databinding.PostPreviewViewBinding
import com.lutalic.luboard.model.database.entities.Post
import java.util.*

class PostsAdapter(
    private val onBoardClickListener: Listener
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    private val posts: MutableList<Post?> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PostViewHolder {
        val view = PostPreviewViewBinding.inflate(LayoutInflater.from(viewGroup.context))
        view.bodyOfPostCard.setOnClickListener { v: View ->
            onBoardClickListener.onPostClick(v.tag as Post)
        }
        view.bodyOfPostCard.setOnLongClickListener { v: View ->
            onBoardClickListener.onPostLongClick(v.tag as Post, v)
            true
        }
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = posts[position]
        if (post != null) {
            viewHolder.bind(post)
        }
        viewHolder.binding.bodyOfPostCard.tag = post
    }

    override fun getItemCount() = posts.size

    fun updateAdapter(newList: List<Post?>?) {
        posts.clear()
        posts.addAll(newList ?: Collections.emptyList())
        notifyDataSetChanged() //TODO Is it possible to do better
    }

    class PostViewHolder(val binding: PostPreviewViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                val gradientDrawable: GradientDrawable =
                    binding.bodyOfPostCard.background as GradientDrawable
                gradientDrawable.setColor(Color.parseColor(post.colour))
                titlePost.text = post.name
                titleBoardForPost.text = "Board: ${post.tableName}"
            }
        }
    }

    interface Listener {
        fun onPostClick(post: Post)
        fun onPostLongClick(post: Post, view: View)
    }
}