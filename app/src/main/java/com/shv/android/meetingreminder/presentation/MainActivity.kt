package com.shv.android.meetingreminder.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.shv.android.meetingreminder.data.network.api.ApiFactory
import com.shv.android.meetingreminder.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val clients = ApiFactory.apiService.getContacts()
            Log.d("MainActivityTest", clients.toString())
        }
    }
}