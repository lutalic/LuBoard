package com.lutalic.luboard.presentation.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.lutalic.luboard.R
import com.lutalic.luboard.databinding.ActivityMainBinding
import com.lutalic.luboard.presentation.main.tabs.TabsFragment
import com.lutalic.luboard.presentation.uiactions.SimpleUiActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress


/**
 * Container for all screens in the app.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // view-model is used for observing username to be displayed in the toolbar
    private val viewModel: MainActivityViewModel by viewModels()

    // nav controller of the current screen
    private var navController: NavController? = null

    private lateinit var preferences: SharedPreferences
    private val topLevelDestinations = setOf(getTabsDestination(), getSignInDestination())

    private var isFirst: Boolean = true

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) {
                return
            }
            onNavControllerActivated(f.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MDAA", "on create main")
        preferences = getSharedPreferences(TAG, MODE_PRIVATE)
        val email = preferences.getString(ACCOUNT_EMAIL, "Deny_Email")
        val pass = preferences.getString(ACCOUNT_PASS, "Deny_Pass")
        if (email != null && pass != null) {
            viewModel.setUser(email, pass)
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)

        viewModel.noConnection.observe(this) {
            if (!isFirst) {
                Toast.makeText(this, "Internet connection error!", Toast.LENGTH_SHORT).show()
            }
            isFirst = false
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

    }


    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    /**
     * Called when the user clicks the back button in the upper toolbar
     */
    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        preferences.edit()
            .putString(ACCOUNT_EMAIL, viewModel.getCurrentEmail())
            .putString(ACCOUNT_PASS, viewModel.getCurrentPassword())
            .apply()
    }


    override fun onSupportNavigateUp(): Boolean =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getSignInDestination()
            }
        )
        navController.graph = graph
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private val destinationListener =
        NavController.OnDestinationChangedListener { _, destination, arguments ->
            supportActionBar?.title = destination.label
            supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
        }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) {
            return false
        }
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun isSignedIn() = viewModel.isSignedIn()

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getSignInDestination(): Int = R.id.signInFragment

    companion object {
        const val TAG = "APP_PREF"
        const val ACCOUNT_EMAIL = "ACCOUNT_EMAIL"
        const val ACCOUNT_PASS = "ACCOUNT_PASS"
    }
}