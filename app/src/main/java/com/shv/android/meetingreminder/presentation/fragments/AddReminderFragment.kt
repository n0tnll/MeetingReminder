package com.shv.android.meetingreminder.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentAddReminderBinding
import com.shv.android.meetingreminder.domain.entity.Client

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    private val args by navArgs<AddReminderFragmentArgs>()

    private val viewModel: AddReminderViewModel by lazy {

        ViewModelProvider(this)[AddReminderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReminderBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textWatchers()

        val client = args.selectedClient

        client?.let {
            with(binding) {
                etReminderClient.setText(getFullName(it))
                etReminderClientEmail.setText(it.email)
            }
        }

        binding.tiReminderClient.setEndIconOnClickListener {
            launchClientListFragment()
        }

        binding.btnSaveReminder.setOnClickListener {
            with(binding) {
                val title = etReminderTitle.text.toString()
                val selectedClient = args.selectedClient
                val date = etReminderDate.text.toString()
                //val time = etReminderTime.text.toString()

                viewModel.addReminder(
                    title,
                    selectedClient!!,
                    date
                )
                closeAddReminderFragment()
            }
        }
    }

    private fun closeAddReminderFragment() {
        viewModel.isCompleted.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun textWatchers() {
        with(binding) {
            etReminderTitle.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isBlank())
                        tiReminderTitle.error = getString(R.string.empty_field_error)
                    else
                        tiReminderTitle.error = null
                }
            }
            etReminderClient.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isBlank())
                        tiReminderClient.error = getString(R.string.empty_field_error)
                    else
                        tiReminderClient.error = null
                }
            }
            etReminderClientEmail.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isBlank())
                        tiReminderClientEmail.error = getString(R.string.empty_field_error)
                    else
                        tiReminderClientEmail.error = null
                }
            }
        }
    }

    private fun launchClientListFragment() {
        findNavController().navigate(R.id.action_addReminderFragment_to_clientListFragment)
    }

    private fun getFullName(client: Client): String {
        val fullName = with(client) {
            "$titleName $firstName $lastName"
        }
        return fullName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}