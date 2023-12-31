package com.tabka.backblog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tabka.backblog.R
import com.tabka.backblog.models.HomeImageItem
import com.tabka.backblog.utils.DesignUtility
import java.util.Collections

class GridAdapter(private val mData: List<HomeImageItem>,
                  private val customizeView: (ViewHolder, position: Int) -> Unit) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {
    private val TAG = "Grid Adapter"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.card_view)
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val textView: TextView = view.findViewById(R.id.image_view_title)
    }

    // Create new views for each grid item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_image_view, parent, false)
        return ViewHolder(view)
    }

    // Bind data to each view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mData[position]
        holder.imageView.setImageResource(item.image)
        holder.textView.text = item.log_name
        customizeView(holder, position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)

        mData.forEachIndexed { index, imageItem ->
            imageItem.priority = index
        }
    }


    fun getItems(): List<HomeImageItem> {
        return mData
    }
    override fun getItemCount() = mData.size
}

