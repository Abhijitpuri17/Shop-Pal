package com.example.shoppingapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.ProductListAdapter
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.ui.activities.AddProductActivity
import com.example.shoppingapp.utils.FirestoreClass
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {
            R.id.add_product-> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getProductListFromFireStore()
    {
        showProgressDialog()
        FirestoreClass().getProducts(this)
    }



    fun getProductsSuccess(productList: ArrayList<Product>)
    {
        hideProgressDialog()

        if (productList.size > 0) {
            tv_no_product_found.visibility = View.GONE
            rv_products_items.visibility = View.VISIBLE

            rv_products_items.layoutManager = LinearLayoutManager(activity)

            rv_products_items.setHasFixedSize(true)

            val adapterProductsList = ProductListAdapter(requireActivity(), productList)

            rv_products_items.adapter = adapterProductsList
        }
        else {
            tv_no_product_found.visibility = View.VISIBLE
            rv_products_items.visibility  = View.GONE
        }

    }




}