package com.example.rempahpedia

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rempahpedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.showToastMessage("Permission request granted")
            } else {
                viewModel.showToastMessage("Permission request denied")
            }
            viewModel.permissionGranted.value = isGranted
        }

    companion object {
        const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        viewModel.permissionGranted.observe(this, Observer { isGranted ->
            if (isGranted == false) {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
        })

        viewModel.checkPermission(REQUIRED_PERMISSION)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.scan -> {
                    replaceFragment(ScanFragment())
                    true
                }

                R.id.list -> {
                    replaceFragment(ListFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
