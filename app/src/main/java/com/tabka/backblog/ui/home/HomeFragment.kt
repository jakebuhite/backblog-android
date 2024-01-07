

package com.tabka.backblog.ui.home

import android.annotation.SuppressLint
import com.tabka.backblog.adapters.GridAdapter
import com.tabka.backblog.adapters.ImageItem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tabka.backblog.R
import com.tabka.backblog.databinding.FragmentHomeBinding
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import com.tabka.backblog.adapters.DragManagerAdapter
import com.tabka.backblog.models.UserLog
import com.tabka.backblog.utilities.JsonUtility
import com.tabka.backblog.utilities.DesignUtility
import java.util.UUID

class HomeFragment : Fragment(), AddLogPopUpFragment.DialogListener {

    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Assuming you have a data source for your RecyclerView
    private var myDataset: List<ImageItem> = emptyList()
    private val jsonUtility = JsonUtility()
    private val designUtility = DesignUtility()

    // Adapter for the RecyclerView
    private lateinit var gridAdapter: GridAdapter

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Add Log PopUp Menu
        val addLogButton: ImageView = root.findViewById(R.id.button_add_log)
        addLogButton.setOnClickListener {
            showAddLogPopUpMenu()
        }

        // If logged in - set myDataset with data from db
        // TODO "Check if user is authenticated"
        // var logsMap: Map<String, FireBase.LogObject> = emptyMap()
        // If not logged in
        var logsMap: Map<String, UserLog>

        val logs = jsonUtility.readFromFile(requireContext())
        logsMap = logs.associateBy { it.id }
        myDataset = logs.map { log ->
            // Set default image if can't get image from TMDB
            val imageResId = R.drawable.img_placeholder_log_batman
            ImageItem(id=log.id, image=imageResId, log_name = log.name, priority = log.priority)
        }.sortedBy { it.priority }



        val numColumns = 2

        // Disable scrolling on the recycler view
        val recyclerView: RecyclerView = binding.myLogsRecycler
        recyclerView.layoutManager = object : GridLayoutManager(requireContext(), numColumns) {
            override fun canScrollVertically(): Boolean {
                return false // Disable scrolling
            }
        }

        // Dynamically add each log to home page
        gridAdapter = GridAdapter(myDataset) { holder, position ->
            val layoutParamsImage = holder.imageView.layoutParams
            val layoutParamsCard = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams

            // Define margins based on position
            if (position % 2 == 0) {
                layoutParamsCard.rightMargin = designUtility.dpToPx(holder.imageView.context, 5)
                layoutParamsCard.leftMargin = 0
            } else {

                layoutParamsCard.leftMargin = designUtility.dpToPx(holder.imageView.context, 5)
                layoutParamsCard.rightMargin = 0
            }

            // Make the logs square
            val width = holder.imageView.resources.displayMetrics.widthPixels / 2
            layoutParamsImage.height = width
            holder.imageView.layoutParams = layoutParamsImage

            // Set the TextWidth to wrap
            holder.cardView.post {
                val maxWidth = (holder.cardView.width * 0.75).toInt()
                holder.textView.maxWidth = maxWidth
            }

        }
        recyclerView.adapter = gridAdapter


        // Make items draggable
        val callback = DragManagerAdapter(gridAdapter, recyclerView) { imageItems ->
            val logs = imageItems.mapNotNull { item ->
                val log = logsMap[item.id] ?: return@mapNotNull null
                log.copy(priority = item.priority)
            }
            // If not logged in
            jsonUtility.overwriteJSON(requireContext(), logs)
            //else send the list to firebase to update the priorities (should only need to send ID and priority)
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        return root
    }

    private fun showAddLogPopUpMenu() {
        val addLogPopUpFragment = AddLogPopUpFragment()
        addLogPopUpFragment.listener = this
        addLogPopUpFragment.show(parentFragmentManager, "AddLogPopUpMenu")
    }

    override fun onDialogCreateButtonClick(logName: String) {
        // Implement what should happen when the button is clicked
        Log.d(TAG, "Create log for $logName")

        // If not logged in, write to JSON file
        val id = UUID.randomUUID().toString()
        val priority = (myDataset.maxByOrNull { it.priority }?.priority ?: 0) + 1
        val log = UserLog(
            id = id,
            name = logName,
            is_visible = true,
            movie_ids = emptyList(),
            watched_ids = emptyList(),
            priority = priority,
        )

        jsonUtility.appendToFile(requireContext(), log)
        activity?.recreate()

        // else create log in firebase
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

