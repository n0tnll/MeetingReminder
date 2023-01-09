package com.shv.android.meetingreminder.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentAddReminderBinding
import com.shv.android.meetingreminder.domain.entity.Client
import java.text.SimpleDateFormat
import java.util.*

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    private val args by navArgs<AddReminderFragmentArgs>()
    private val viewModel: AddReminderViewModel by lazy {
        ViewModelProvider(this)[AddReminderViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("AddReminderFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AddReminderFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AddReminderFragment", "onCreateView")
        _binding = FragmentAddReminderBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AddReminderFragment", "onViewCreated")
        val client = args.selectedClient
        textWatchers()
        fillFields(client)

        binding.etReminderClient.setOnClickListener {
            launchClientListFragment()
        }

        viewModel.reminderValid.observe(viewLifecycleOwner) {
            binding.btnSaveReminder.isEnabled = it
        }

        binding.btnSaveReminder.setOnClickListener {
            with(binding) {
                val title = etReminderTitle.text.toString()
                val selectedClient = args.selectedClient
                val date = etReminderDate.text.toString()
                val time = etReminderTime.text.toString()

                viewModel.addReminder(
                    title,
                    selectedClient!!,
                    date,
                    time
                )
                closeAddReminderFragment()
            }
        }

        binding.etReminderDate.setOnClickListener {
            showDatePicker()
        }

        binding.etReminderTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun fillFields(client: Client?) {
        with(binding) {
            btnSaveReminder.isEnabled = false
            etReminderDate.setText(getCurrentDateToString())
            client?.let {
                val clientText = String.format("ФИО: %s\nEmail: %s", it.fullName, it.email)
                etReminderClient.setText(clientText)
            }
        }
    }

    private fun getCurrentDateToString(): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun textWatchers() {
        with(binding) {
            etReminderTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.validateTitle(text.toString())
                text?.let {
                    viewModel.errorInputTitle.observe(viewLifecycleOwner) { isNotValid ->
                        if (isNotValid) {
                            tiReminderTitle.error = getString(R.string.empty_field_error)
                        } else {
                            tiReminderTitle.error = null
                        }
                    }
                }
            }

            etReminderClient.doOnTextChanged { text, _, _, _ ->
                viewModel.validateClient(text.toString())
                text?.let {
                    viewModel.errorClientField.observe(viewLifecycleOwner) { isNotValid ->
                        if (isNotValid) {
                            tiReminderClient.error = getString(R.string.empty_field_error)
                        } else {
                            tiReminderClient.error = null
                        }
                    }
                }
            }

            etReminderDate.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    val dateFromEditText = it.toString()
                    val timeFromEditText = etReminderTime.text.toString()
                    viewModel.validateDate(dateFromEditText)
                    viewModel.validateTime(dateFromEditText, timeFromEditText)
                    viewModel.errorDate.observe(viewLifecycleOwner) { isNotValid ->
                        if (isNotValid) {
                            tiReminderDate.error = "Неверная дата"
                        } else {
                            tiReminderDate.error = null
                        }
                    }
                }
            }

            etReminderTime.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isNotBlank()) {
                        val dateFromEditText = etReminderDate.text.toString()
                        val timeFromEditText = it.toString()

                        viewModel.validateTime(dateFromEditText, timeFromEditText)
                        viewModel.errorTime.observe(viewLifecycleOwner) { isNotValid ->
                            if (isNotValid) {
                                tiReminderTime.error = "Неверное время"
                            } else {
                                tiReminderTime.error = null
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_MeetingReminder_DatePicker)
            .setTitleText(getString(R.string.date_picker_title))
            .build()

        datePickerDialog.show(parentFragmentManager, DATE_PICKER_TAG)

        datePickerDialog.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = dateFormatter.format(Date(it))
            binding.etReminderDate.setText(date)
        }
    }

    private fun showTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker = MaterialTimePicker.Builder()
            .setTheme(R.style.ThemeOverlay_MeetingReminder_TimePicker)
            .setTimeFormat(clockFormat)
            .setHour(TIME_PICKER_DEFAULT_HOUR)
            .setMinute(TIME_PICKER_DEFAULT_MINUTE)
            .setTitleText(getString(R.string.time_picker_title))
            .build()

        timePicker.show(parentFragmentManager, TIME_PICKER_TAG)

        timePicker.addOnPositiveButtonClickListener {
            var hour = timePicker.hour.toString()
            var minute = timePicker.minute.toString()
            if (timePicker.hour < 10 || timePicker.hour == 0)
                hour = "0${timePicker.hour}"
            if (timePicker.minute < 10 || timePicker.minute == 0)
                minute = "0${timePicker.minute}"
            binding.etReminderTime.setText("$hour:$minute")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("AddReminderFragment", "onSaveInstanceState")
        with(binding) {
            outState.apply {
                putString(BUNDLE_TITLE, etReminderTitle.text.toString())
            }
        }
    }

    private fun closeAddReminderFragment() {
        viewModel.isCompleted.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun launchClientListFragment() {
        findNavController().navigate(R.id.action_addReminderFragment_to_clientListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddReminderFragment", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AddReminderFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("AddReminderFragment", "onDetach")
    }

    companion object {
        private const val TIME_PICKER_DEFAULT_HOUR = 12
        private const val TIME_PICKER_DEFAULT_MINUTE = 0

        private const val TIME_PICKER_TAG = "time"
        private const val DATE_PICKER_TAG = "time"

        private const val BUNDLE_TITLE = "title"
        private const val BUNDLE_DATE = "date"
        private const val BUNDLE_TIME = "time"
    }
}