package com.shv.android.meetingreminder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shv.android.meetingreminder.databinding.FragmentClientListBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.presentation.adapters.ClientAdapter

class ClientListFragment : Fragment() {

    private var _binding: FragmentClientListBinding? = null
    private val binding: FragmentClientListBinding
        get() = _binding ?: throw RuntimeException("FragmentClientListBinding is null")


    private val viewModel: ClientsViewModel by lazy {
        ViewModelProvider(this)[ClientsViewModel::class.java]
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

        viewModel.clientsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

    private fun launchAddReminderFragment(client: Client) {
        findNavController().navigate(
            ClientListFragmentDirections.actionClientListFragmentToAddReminderFragment(
                client
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}