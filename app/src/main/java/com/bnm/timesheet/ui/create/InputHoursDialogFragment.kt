package com.bnm.timesheet.ui.create

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bnm.timesheet.R
import com.bnm.timesheet.util.Constants.Companion.KEY_SELECTED_DATE
import java.text.SimpleDateFormat
import java.util.*


class InputHoursDialogFragment : DialogFragment() {
    val TAG = "InputHoursDialogFrag"
    lateinit var timePicker: TimePickerHelper
    lateinit var selectedDate: String
    var startTime = ""
    var endTime = ""
    lateinit var txTotalHours: TextView

    companion object {
        const val FTAG = "DialogWithData"
    }

    private lateinit var viewModel: InputHoursSharedViewModel
    fun newInstance(selected_date: String): InputHoursDialogFragment? {
        val f = InputHoursDialogFragment()

        // Supply num input as an argument.
        val args = Bundle()
        args.putString(KEY_SELECTED_DATE, selected_date)
        f.setArguments(args)
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedDate = getArguments()?.getString(KEY_SELECTED_DATE).toString()
        val root = inflater.inflate(R.layout.fragment_settime_dialog, container, false)
        initView(root)
        return root
    }

    private fun initView(root: View) {
        var txDate = root.findViewById<TextView>(R.id.tx_date)
        txDate.text = selectedDate
        txTotalHours = root.findViewById(R.id.tx_hrs)
        timePicker = context?.let { TimePickerHelper(it, true, false) }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(InputHoursSharedViewModel::class.java)
        setupClickListeners(view)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        val txStartTime: TextView = view.findViewById(R.id.tx_time_start)
        val txEndTime: TextView = view.findViewById(R.id.tx_time_end)
        val btnStart: AppCompatButton = view.findViewById(R.id.btn_starttime)
        val btnEndTime: AppCompatButton = view.findViewById(R.id.btn_endtime)
        val btnSubmit: AppCompatButton = view.findViewById(R.id.btn_submit)
        val btnClose: ImageFilterButton = view.findViewById(R.id.btn_close)
        /*close dialog*/
        btnClose.setOnClickListener(View.OnClickListener { dismiss() })

        /*Start time click*/
        btnStart.setOnClickListener {
            showStartTimePickerDialog(txStartTime)
        }

        /*End time click*/
        btnEndTime.setOnClickListener {
            showEndTimePickerDialog(txEndTime)
        }

        /*Submit click*/
        btnSubmit.setOnClickListener {
            var totalHrs = ""
            if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                totalHrs = getTotalHours(startTime, endTime)
                Log.e(TAG, "Total hrs: " + totalHrs)
                val intent = Intent()
                intent.putExtra("time", totalHrs)
                targetFragment!!.onActivityResult(targetRequestCode, 1, intent)
                dismiss();
            } else {
                activity?.let { it1 -> basicAlert(it1) }
            }

            //String date to cal
            /*val cal = Calendar.getInstance()
           // val sdf = SimpleDateFormat("EEE dd-MMM-yyyy", Locale.ENGLISH)
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            cal.time = sdf.parse(selectedDate) // all done*/

        }
    }

    private fun basicAlert(context: Context) {
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setTitle(R.string.alert_title)
            setMessage(R.string.alert_message)
            setPositiveButton(
                android.R.string.ok,
                DialogInterface.OnClickListener(function = positiveButtonClick)
            )
            show()
        }


    }

    private fun showStartTimePickerDialog(txTime: TextView) {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                //Append 0 as 'second' value
                startTime = "${hourOfDay}:${minuteStr}" + ":00"
                txTime.visibility = View.VISIBLE
                txTime.text = startTime

            }

        })
    }

    private fun showEndTimePickerDialog(txTime: TextView) {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                //Append 00 as 'second' value
                endTime = "${hourOfDay}:${minuteStr}" + ":00"
                txTime.visibility = View.VISIBLE
                txTime.text = endTime

                //Display total worked hours
                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    var totalHrs = getTotalHours(startTime, endTime)
                    txTotalHours.text = totalHrs + " Hrs"
                }
            }
        })
    }

    fun getTotalHours(start: String, stop: String): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val startDate: Date = simpleDateFormat.parse(start)
        val endDate: Date = simpleDateFormat.parse(stop)

        var difference = endDate.time - startDate.time
        if (difference < 0) {
            val dateMax: Date = simpleDateFormat.parse("24:00:00")
            val dateMin: Date = simpleDateFormat.parse("00:00:00")
            difference = dateMax.time - startDate.time + (endDate.time - dateMin.time)
        }
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
        val min =
            (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
        val sec =
            (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours - 1000 * 60 * min).toInt() / 1000
        //Log.e("log_tag", "Hours: $hours, Mins: $min, Secs: $sec")
        var strHrs = "$hours"
        var strMins = "$min"
        if (hours < 10) strHrs = "0" + strHrs
        if (min < 10) strMins = "0" + strMins

        return strHrs + ":" + strMins
    }

}