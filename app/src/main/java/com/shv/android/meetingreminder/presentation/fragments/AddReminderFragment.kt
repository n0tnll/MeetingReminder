package com.shv.android.meetingreminder.presentation.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.presentation.AddReminderFormEvent
import com.shv.android.meetingreminder.presentation.br.AlarmReceiver
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import com.shv.android.meetingreminder.presentation.viewmodels.AddReminderState
import com.shv.android.meetingreminder.presentation.viewmodels.AddReminderViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.ViewModelFactory
import java.text.SimpleDateFormat
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

    private val calendar by lazy {
        Calendar.getInstance().apply {
            add(Calendar.SECOND, 0)
        }
    }

    private lateinit var client: Client

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
        val timeFieldIsEmpty = etReminderTime.text.toString().isBlank()
        val reminder = Reminder(
            title = title,
            fullName = client.clientFullName,
            status = false,
            dateTime = calendar.timeInMillis,
            client = client
        )
        viewModel.addReminder(reminder)
        Log.d("btnSaveReminder", "from btnSaveReminder -> reminderId: ${reminder.id}")
        Log.d("btnSaveReminder", "from btnSaveReminder -> reminderTitle: ${reminder.title}")
        Log.d("btnSaveReminder", "from btnSaveReminder -> reminderClient: ${reminder.client.clientFullName}")
        Log.d("btnSaveReminder", "from btnSaveReminder -> reminderDateTime: ${reminder.dateTime}")
        setReminderAlarm(reminder, timeFieldIsEmpty)
    }

    private fun setReminderAlarm(reminder: Reminder, timeFieldIsEmpty: Boolean) {
        val alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val meetingTime = Calendar.getInstance().apply {
            timeInMillis = reminder.dateTime
            add(Calendar.HOUR_OF_DAY, -1)
            set(Calendar.SECOND, 0)
        }
        val intent = AlarmReceiver.newIntent(requireContext()).apply {
            putExtra("reminder", reminder)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                reminder.id,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    PendingIntent.FLAG_IMMUTABLE
                else 0
            )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, meetingTime.timeInMillis, pendingIntent)

        Toast.makeText(context, "Notification set", Toast.LENGTH_SHORT).show()
    }


    private fun selectClient() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Client>(RESULT_FROM_CLIENT_LIST)
            ?.observe(currentBackStackEntry) {
                viewModel.setClient(it)
                client = it
            }
        launchClientListFragment()
    }

    private fun observeViewModel() {
        viewModel.client.observe(viewLifecycleOwner) {
            if (it != null) {
                val clientText = String.format("ФИО: %s\nEmail: %s", it.clientFullName, it.email)
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
            .setHour(calendar[Calendar.HOUR_OF_DAY])
            .setMinute(calendar[Calendar.MINUTE])
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

    private fun getDateFromPickerToString(calendar: Calendar): String {
        val formatter = SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun getTimeFromPickerToString(calendar: Calendar): String {
        val formatter =
            SimpleDateFormat(getString(R.string.time_format_pattern), Locale.getDefault())
        return formatter.format(calendar.time)
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
        private const val TIME_PICKER_TAG = "time"
        private const val DATE_PICKER_TAG = "date"
        const val RESULT_FROM_CLIENT_LIST = "chosen_client"
        const val CLIENT_NAME = "client_name"
        const val REMINDER_TITLE = "reminder_title"
    }
}