package com.lutalic.luboard.presentation.main.tabs

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding
    private var addNewPostFlag: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)

        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addNewPostFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    addNewPostFlag = true
                }
                R.id.editPostFragment ->{
                    binding.bottomNavigationView.visibility = View.GONE
                    addNewPostFlag = true
                }
                R.id.editPostFragment2 ->{
                    binding.bottomNavigationView.visibility = View.GONE
                    addNewPostFlag = true
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    addNewPostFlag = false
                }
            }
        }
        activity?.window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            activity?.window?.decorView?.getWindowVisibleDisplayFrame(r)
            val screenHeight = activity?.window?.decorView?.rootView?.height
                ?: return@addOnGlobalLayoutListener
            val keypadHeight: Int = screenHeight - r.bottom

            //Log.d(TAG, "keypadHeight = " + keypadHeight);
            if (keypadHeight > screenHeight * 0.15) {
                //Keyboard is opened
                binding.bottomNavigationView.visibility = View.GONE
            } else if(!addNewPostFlag){
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

}