package com.bnm.timesheet.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bnm.timesheet.R
import com.google.android.material.button.MaterialButton

class FragmentCreateAccount : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_account, container, false)
        initView(root)
        return root
    }
    private fun initView(root:View){
        var btnDone = root.findViewById<MaterialButton>(R.id.btn_done)
        btnDone.setOnClickListener(View.OnClickListener {  Toast.makeText(context,
            "Account created successfully!", Toast.LENGTH_LONG).show()
        })
    }
}