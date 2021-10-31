package com.example.shoppingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val text_dashboard = root.findViewById<TextView>(R.id.text_dashboard)
        text_dashboard.text = "This is dashboard"
        return root
    }

}