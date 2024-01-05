package com.tabka.backblog.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragManagerAdapter(
    private val adapter: GridAdapter,
    private val recyclerView: RecyclerView,
    /*    private val scrollView: ScrollView,*/
    private val onMoveComplete: (List<ImageItem>) -> Unit
) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) : Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        onMoveComplete(adapter.getItems())
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do something when it is swiped

        // MARK AS WATCHED//
    }

    private val scrollThreshold = 100 // Threshold in pixels

/*    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            handleScroll(viewHolder)
        }
    }*/

/*    private fun handleScroll(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder ?: return

        val itemView = viewHolder.itemView
        val itemTop = itemView.top
        val itemBottom = itemView.bottom

        val scrollAmount = when {
            itemTop < scrollThreshold -> -scrollThreshold
            itemBottom > recyclerView.height - scrollThreshold -> scrollThreshold
            else -> 0
        }

        if (scrollAmount != 0) {
            scrollView.scrollBy(0, scrollAmount)
            // Schedule the next scroll
            itemView.postDelayed({ handleScroll(viewHolder) }, 100)
        }
    }*/

}