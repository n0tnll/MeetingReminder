package com.shv.android.meetingreminder.presentation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.shv.android.meetingreminder.databinding.FragmentClientListBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import com.shv.android.meetingreminder.presentation.adapters.ClientAdapter
import javax.inject.Inject

class ClientListFragment : Fragment() {

    private var _binding: FragmentClientListBinding? = null
    private val binding: FragmentClientListBinding
        get() = _binding ?: throw RuntimeException("FragmentClientListBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ClientsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ClientsViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MeetingReminderApplication).component
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            with(binding) {
                if (intent?.action == NOT_CONNECTED) {
                    Toast.makeText(context, "Нет интернет соединения", Toast.LENGTH_SHORT)
                        .show()
                    progressBar.isVisible = false
                }
            }
        }
    }

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(requireActivity().application)
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientListBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ClientAdapter()
        binding.rvClientList.adapter = adapter

        adapter.onClientClickListener = object : ClientAdapter.OnClientClickListener {
            override fun onClientClick(client: Client) {
                launchAddReminderFragment(client)
            }
        }

        binding.btnRetryLoad.setOnClickListener {
            viewModel.retryLoadClients()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = it
                btnRetryLoad.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewModel.clientsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val intentFilter = IntentFilter().apply {
            addAction(NOT_CONNECTED)
        }
        localBroadcastManager.registerReceiver(receiver, intentFilter)

    }

    private fun launchAddReminderFragment(client: Client) {
        val saveStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        saveStateHandle?.set(AddReminderFragment.RESULT_FROM_CLIENT_LIST, client)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        localBroadcastManager.unregisterReceiver(receiver)
    }

    companion object {

        private const val NOT_CONNECTED = "not_connected"
    }
}