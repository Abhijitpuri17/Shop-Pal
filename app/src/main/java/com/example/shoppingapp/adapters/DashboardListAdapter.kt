package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.GlideLoader
import com.example.shoppingapp.view.activities.ProductDetailsActivity
import kotlinx.android.synthetic.main.dashboard_item_layout.view.*

class DashboardListAdapter(val context: Context, val productList: ArrayList<Product>): RecyclerView.Adapter<DashboardListAdapter.DashboardItemViewHolder>()
{

    class DashboardItemViewHolder(val view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemViewHolder {
        return DashboardItemViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dashboard_item_layout, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DashboardItemViewHolder, position: Int) {

        val item = productList[position]

        GlideLoader(context).loadPicture(item.image, holder.itemView.iv_dashboard_item_image)

        holder.itemView.tv_dashboard_item_title.text =  item.title
        holder.itemView.tv_dashboard_item_price.text = "Rs.${item.price}"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.PRODUCT_DETAILS_ID, item.product_id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}