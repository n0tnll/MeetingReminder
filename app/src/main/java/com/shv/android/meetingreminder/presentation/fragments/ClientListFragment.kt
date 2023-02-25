package com.shv.android.meetingreminder.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentClientListBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import com.shv.android.meetingreminder.presentation.adapters.ClientAdapter
import com.shv.android.meetingreminder.presentation.viewmodels.*
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

    private val adapter by lazy {
        ClientAdapter()
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
        binding.rvClientList.adapter = adapter
        adapter.onClientClickListener = object : ClientAdapter.OnClientClickListener {
            override fun onClientClick(client: Client) {
                launchAddReminderFragment(client)
            }
        }
        binding.btnRetryLoad.setOnClickListener {
            viewModel.loadClients()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            with(binding) {
                progressBar.isVisible = false
                btnRetryLoad.visibility = View.GONE
                when (it) {
                    is Loading -> {
                        progressBar.isVisible = true
                    }
                    is ClientsList -> {
                        adapter.submitList(it.clientsList)
                    }
                    is LoadingError -> {
                        progressBar.isVisible = false
                        btnRetryLoad.visibility = View.VISIBLE
                        Toast.makeText(
                            context,
                            getString(R.string.load_clients_error_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun launchAddReminderFragment(client: Client) {
        val saveStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        saveStateHandle?.set(AddReminderFragment.RESULT_FROM_CLIENT_LIST, client)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}