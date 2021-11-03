package com.example.shoppingapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.ui.activities.SettingsActivity

class DashboardFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }


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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id  = item.itemId

        when(id)
        {
            R.id.action_settings->{
                startActivity(Intent(activity, SettingsActivity::class.java))

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}







