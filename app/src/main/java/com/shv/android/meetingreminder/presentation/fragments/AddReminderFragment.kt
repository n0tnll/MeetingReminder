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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentAddReminderBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.presentation.AddReminderFormEvent
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import com.shv.android.meetingreminder.presentation.viewmodels.AddReminderState
import com.shv.android.meetingreminder.presentation.viewmodels.AddReminderViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.ViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AddReminderViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddReminderViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MeetingReminderApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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
        textWatchers()
        observeViewModel()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            etReminderClient.setOnClickListener {
                selectClient()
            }
            etReminderDate.setText(getDateFromPickerToString(calendar))
            btnSaveReminder.setOnClickListener {
                selectReminderFields()
            }
            etReminderDate.setOnClickListener {
                showDatePicker()
            }
            etReminderTime.setOnClickListener {
                showTimePicker()
            }
        }
    }

    private fun FragmentAddReminderBinding.selectReminderFields() {
        val title = etReminderTitle.text.toString()
        val selectedClient = viewModel.client.value
        val date = etReminderDate.text.toString()
        val time = etReminderTime.text.toString()
        viewModel.addReminder(
            title,
            selectedClient ?: throw RuntimeException("Client is null"),
            date,
            time
        )
    }

    private fun selectClient() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Client>(RESULT_FROM_CLIENT_LIST)
            ?.observe(currentBackStackEntry) {
                viewModel.setClient(it)
            }
        launchClientListFragment()
    }

    private fun observeViewModel() {
        viewModel.client.observe(viewLifecycleOwner) {
            if (it != null) {
                val clientText = String.format("ФИО: %s\nEmail: %s", it.fullName, it.email)
                binding.etReminderClient.setText(clientText)
            }
        }
        viewModel.state.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is AddReminderState -> {
                        tiReminderTitle.error = it.titleError
                        tiReminderClient.error = it.clientError
                        tiReminderDate.error = it.dateError
                        tiReminderTime.error = it.timeError
                        btnSaveReminder.isEnabled = it.formValid
                        if (it.isFinished) closeAddReminderFragment()
                    }
                }
            }
        }
    }

    private fun textWatchers() {
        with(binding) {
            etReminderTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.TitleChange(text.toString()))
            }
            etReminderClient.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.ClientChange(text.toString()))
            }
            etReminderDate.doOnTextChanged { _, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.DateChange(calendar))
            }
            etReminderTime.doOnTextChanged { _, _, _, count ->
                if (count > 0) {
                    viewModel.onEvent(AddReminderFormEvent.TimeChange(calendar))
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
            calendar.timeInMillis = it
            val date = getDateFromPickerToString(calendar)
            binding.etReminderDate.setText(date)
        }
    }

    private fun showTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker = MaterialTimePicker.Builder()
            .setTheme(R.style.ThemeOverlay_MeetingReminder_TimePicker)
            .setTimeFormat(clockFormat)
            .setHour(LocalDateTime.now().hour)
            .setMinute(LocalDateTime.now().minute)
            .setTitleText(getString(R.string.time_picker_title))
            .build()

        timePicker.show(parentFragmentManager, TIME_PICKER_TAG)

        timePicker.addOnPositiveButtonClickListener {
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
                set(Calendar.MILLISECOND, 0)
            }
            val timeText = getTimeFromPickerToString(calendar)
            binding.etReminderTime.setText(timeText)
        }
    }

    private fun getDateFromPickerToString(date: Calendar): String {
        val formatter = SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault())
        return formatter.format(date.time)
    }

    private fun getTimeFromPickerToString(time: Calendar): String {
        val timeHour = time[Calendar.HOUR_OF_DAY]
        val timeMinute = time[Calendar.MINUTE]
        val hour = if (timeHour in 0..9) "0$timeHour" else timeHour
        val minute = if (timeMinute in 0..9) "0$timeMinute" else timeMinute
        return String.format(getString(R.string.time_pattern), hour, minute)
    }

    private fun closeAddReminderFragment() {
        findNavController().popBackStack()
    }

    private fun launchClientListFragment() {
        findNavController()
            .navigate(AddReminderFragmentDirections.actionAddReminderFragmentToClientListFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val calendar = Calendar.getInstance()
        private const val TIME_PICKER_TAG = "time"
        private const val DATE_PICKER_TAG = "date"
        const val RESULT_FROM_CLIENT_LIST = "chosen_client"
    }
}