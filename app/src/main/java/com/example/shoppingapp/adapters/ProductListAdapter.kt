package com.example.shoppingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.GlideLoader
import kotlinx.android.synthetic.main.product_item_layout.view.*


class ProductListAdapter (val context: Context, val productList: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProductItemViewHolder(
            LayoutInflater.from(context).
            inflate(R.layout.product_item_layout,
                parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = productList[position]

        if (holder is ProductItemViewHolder){
            GlideLoader(context).loadPicture(model.image, holder.itemView.iv_product_item_image)
            holder.itemView.tv_product_item_title.text = model.title
            holder.itemView.tv_product_item_price.text = "Rs ${model.price}"
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }

}