package com.shv.android.meetingreminder.presentation.fragments

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.FragmentReminderListBinding
import com.shv.android.meetingreminder.presentation.MeetingReminderApplication
import com.shv.android.meetingreminder.presentation.adapters.ReminderAdapter
import com.shv.android.meetingreminder.presentation.viewmodels.ReminderListViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.ViewModelFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

class ReminderListFragment : Fragment() {

    private var _binding: FragmentReminderListBinding? = null
    private val binding: FragmentReminderListBinding
        get() = _binding ?: throw RuntimeException("FragmentReminderListBinding is null.")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ReminderListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ReminderListViewModel::class.java]
    }

    private val adapter: ReminderAdapter by lazy {
        ReminderAdapter()
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
        _binding = FragmentReminderListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        binding.btnAddReminder.setOnClickListener {
            launchAddReminderFragment()
        }
    }

    private fun observeViewModel() {
        viewModel.reminderList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.llPasteReminder.visibility = View.VISIBLE
            } else
                binding.llPasteReminder.visibility = View.GONE
            adapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvReminderList.adapter = adapter
        setupToItemSwipe()
    }

    private fun setupToItemSwipe() {
        val swipeToItemCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val item = adapter.currentList[position]
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        viewModel.deleteReminderItem(item)
                        Snackbar.make(
                            binding.rvReminderList,
                            "Был удалён ${item.title}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ItemTouchHelper.LEFT -> {
                        //TODO: add edit reminder
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.holo_green_dark
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_done)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToItemCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvReminderList)
    }

    private fun launchAddReminderFragment() {
        findNavController().navigate(R.id.action_reminderListFragment_to_addReminderFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}