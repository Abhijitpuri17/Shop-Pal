package com.example.shoppingapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.DashboardListAdapter
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.FirestoreClass
import com.example.shoppingapp.view.activities.SettingsActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {


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

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

    private fun getDashboardItemsList()
    {
        showProgressDialog("Please wait...")
        FirestoreClass().getDashboardItems(this)
    }

    fun successDashboardItemsList(dashboardList: ArrayList<Product>)
    {
        hideProgressDialog()
        if (dashboardList.size > 0)
        {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_items_found_dashboard.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardListAdapter(requireActivity(), dashboardList)

            rv_dashboard_items.adapter = adapter
        }
        else{
            rv_dashboard_items.visibility = View.GONE
            tv_no_items_found_dashboard.visibility = View.VISIBLE
        }
    }

}







