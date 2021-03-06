package com.bnm.timesheet.ui.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnm.timesheet.R

class AddEntryFragment : Fragment() {

  private lateinit var addEntryViewModel: AddEntryViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    addEntryViewModel =
            ViewModelProvider(this).get(AddEntryViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_gallery, container, false)
    val textView: TextView = root.findViewById(R.id.text_gallery)
    addEntryViewModel.text.observe(viewLifecycleOwner, Observer {
      //textView.text = it
      textView.text = "Development in progress!"
    })
    return root
  }
}