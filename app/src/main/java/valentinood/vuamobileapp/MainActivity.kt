package valentinood.vuamobileapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import valentinood.vuamobileapp.databinding.ActivityMainBinding
import valentinood.vuamobileapp.fragment.AboutFragment
import valentinood.vuamobileapp.fragment.UserViewFragment
import valentinood.vuamobileapp.fragment.UsersHistoryFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentHome: UserViewFragment
    private lateinit var fragmentHistory: UsersHistoryFragment
    private lateinit var fragmentAbout: AboutFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentHome = UserViewFragment(intent)
        fragmentAbout = AboutFragment()

        fragmentHistory = UsersHistoryFragment {
            fragmentHome.setUser(it)
            setCurrentFragment(fragmentHome)
        }

        setCurrentFragment(fragmentHome)
        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    setCurrentFragment(fragmentHome)
                }

                R.id.item_history -> {
                    setCurrentFragment(fragmentHistory)
                }

                R.id.item_about -> {
                    setCurrentFragment(fragmentAbout)
                }
            }

            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
    }
}