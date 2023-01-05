package com.shv.android.meetingreminder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentReminderListBinding

class ReminderListFragment : Fragment() {

    private var _binding: FragmentReminderListBinding? = null
    private val binding: FragmentReminderListBinding
        get() = _binding ?: throw RuntimeException("FragmentReminderListBinding is null.")

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