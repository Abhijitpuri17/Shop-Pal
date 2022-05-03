package com.example.shoppingapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Product
import com.example.shoppingapp.utils.Constants
import com.example.shoppingapp.utils.GlideLoader
import com.example.shoppingapp.view.activities.ProductDetailsActivity
import com.example.shoppingapp.view.fragments.ProductsFragment
import kotlinx.android.synthetic.main.product_item_layout.view.*


class ProductListAdapter (
    val context: Context,
    private val productList: ArrayList<Product>,
    private val fragment : ProductsFragment
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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

            holder.itemView.ib_delete_product.setOnClickListener{
                deleteProduct(model.product_id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.PRODUCT_DETAILS_ID, model.product_id)
                context.startActivity(intent)
            }
        }
    }

    private fun deleteProduct(productId : String) {
        fragment.deleteProduct(productId)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }

}