

package com.tabka.backblog.ui.home

import android.annotation.SuppressLint
import com.tabka.backblog.adapters.GridAdapter
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
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.tabka.backblog.adapters.DragManagerAdapter
import com.tabka.backblog.databinding.FragmentSearchBinding
import com.tabka.backblog.models.HomeImageItem
import com.tabka.backblog.models.UserLog
import com.tabka.backblog.ui.search.SearchViewModel
import com.tabka.backblog.utils.JsonUtility
import com.tabka.backblog.utils.DesignUtility
import java.util.UUID

class HomeFragment : Fragment(), AddLogPopUpFragment.DialogListener {

    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Assuming you have a data source for your RecyclerView
    private var myDataset: List<HomeImageItem> = emptyList()



    private lateinit var homeViewModel: HomeViewModel
    private lateinit var gridAdapter: GridAdapter

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.imageItemList.observe(viewLifecycleOwner, Observer { logs ->
            setupRecyclerView(logs)
        })

        // Add Log PopUp Menu
        val addLogButton: ImageView = root.findViewById(R.id.button_add_log)
        addLogButton.setOnClickListener {
            showAddLogPopUpMenu()
        }

        return root
    }

    private fun setupRecyclerView(imageItems: List<HomeImageItem>) {

        val numColumns = 2
        val recyclerView: RecyclerView = binding.myLogsRecycler
        recyclerView.layoutManager = object : GridLayoutManager(requireContext(), numColumns) {
            override fun canScrollVertically(): Boolean {
                return false // Disable scrolling
            }
        }

        gridAdapter = GridAdapter(imageItems) { holder, position ->
            val layoutParamsImage = holder.imageView.layoutParams
            val layoutParamsCard = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
            val designUtility = DesignUtility()

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

        createDraggableLogs(gridAdapter, recyclerView)
    }

    private fun createDraggableLogs(gridAdapter: GridAdapter, recyclerView: RecyclerView) {

        val callback = DragManagerAdapter(gridAdapter, recyclerView) { imageItems ->
            imageItems.forEachIndexed { index, imageItem ->
                imageItem.priority = index
            }
            // Call ViewModel method to update and save logs
            homeViewModel.updatePrioritiesAndSaveLogs(imageItems)
        }
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

    }


    private fun showAddLogPopUpMenu() {
        val addLogPopUpFragment = AddLogPopUpFragment()
        addLogPopUpFragment.listener = this
        addLogPopUpFragment.show(parentFragmentManager, "AddLogPopUpMenu")
    }

    override fun onDialogCreateButtonClick(logName: String) {
        // Implement what should happen when the button is clicked
        homeViewModel.createLog(logName)
/*        Log.d(TAG, "Create log for $logName")

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
        )*/

        /*jsonUtility.appendToFile(log)*/
        /*activity?.recreate()*/

        // else create log in firebase
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

