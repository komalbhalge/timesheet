package com.bnm.timesheet.ui.create

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.bnm.timesheet.R
import com.bnm.timesheet.ui.entry.AddEntryViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateTimesheet : Fragment() {
    val TAG = "CreateTimesheet"
    private lateinit var addEntryViewModel: AddEntryViewModel
    lateinit var calendarView: CalendarView
    private val mEventDays: MutableList<EventDay> = ArrayList()
    private val selectedDays: MutableList<Calendar> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addEntryViewModel =
            ViewModelProvider(this).get(AddEntryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_timesheet, container, false)
        val events: MutableList<EventDay> = ArrayList()
        calendarView = root.findViewById(R.id.calendarView) as CalendarView

        val calendar: Calendar = java.util.Calendar.getInstance()
        calendarView.setDate(calendar)
        calendarView.setOnDayClickListener(OnDayClickListener { eventDay -> previewNote(eventDay) })
        return root
    }

    private fun previewNote(eventDay: EventDay) {
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val date = format1.format(eventDay.calendar.time)
        Log.e(TAG, "Date seleected here... " + date)

        val dialogFrag: InputHoursDialogFragment? = InputHoursDialogFragment().newInstance(date)
        val fragmentManager: FragmentManager? = fragmentManager

        dialogFrag?.setTargetFragment(this, 1)

        fragmentManager?.let { dialogFrag?.show(it, InputHoursDialogFragment.FTAG) }
    }

    fun showDialog(date: String) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        val prev = requireFragmentManager().findFragmentByTag(InputHoursDialogFragment.FTAG)
        if (prev != null) {
            Log.e("Dialog Exist:", "")
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment: InputHoursDialogFragment? = InputHoursDialogFragment().newInstance(date)
        newFragment?.show(ft, "dialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        val myData: String? = data?.getStringExtra("time")

        // Stuff to do, dependent on requestCode and resultCode
        if (requestCode == 1) { // 1 is an arbitrary number, can be any int
            // This is the return result of your DialogFragment

            if (resultCode == 1) { // 1 is an arbitrary number, can be any int
                // Now do what you need to do after the dialog dismisses.
                Log.e(TAG, "Date:" + myData)
                val myEventDay = MyEventDay(
                    calendarView.selectedDate,
                    R.drawable.ic_clock_24dp, myData
                )
                calendarView.setDate(myEventDay.getCalendar());
                mEventDays.add(myEventDay)
                calendarView.setEvents(mEventDays);
                selectedDays.add(myEventDay.calendar)
                //calendarView.setDisabledDays(selectedDays)

            }
        }
    }
}