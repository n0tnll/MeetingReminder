package com.shv.android.meetingreminder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentReminderListBinding
import com.shv.android.meetingreminder.presentation.adapters.ReminderAdapter

class ReminderListFragment : Fragment() {

    private var _binding: FragmentReminderListBinding? = null
    private val binding: FragmentReminderListBinding
        get() = _binding ?: throw RuntimeException("FragmentReminderListBinding is null.")

    private val viewModel: ReminderListViewModel by lazy {
        ViewModelProvider(this)[ReminderListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReminderAdapter()
        binding.rvReminderList.adapter = adapter

        viewModel.reminderList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.llPasteReminder.visibility = View.VISIBLE
            } else
                binding.llPasteReminder.visibility = View.GONE
            adapter.submitList(it)
        }

        binding.btnAddReminder.setOnClickListener {
            launchAddReminderFragment()
        }
    }

    private fun launchAddReminderFragment() {
        findNavController().navigate(R.id.action_reminderListFragment_to_addReminderFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}