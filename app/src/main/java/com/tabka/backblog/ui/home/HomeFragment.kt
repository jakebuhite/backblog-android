package com.tabka.backblog.ui.home

import GridAdapter
import ImageItem
import android.app.Activity
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tabka.backblog.R
import com.tabka.backblog.databinding.FragmentHomeBinding
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.app.ActivityCompat.recreate

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    // Assuming you have a data source for your RecyclerView
    private val myDataset: List<ImageItem> = emptyList()
/*    private val myDataset = listOf(
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
        ImageItem(R.drawable.img_placeholder),
    )*/

    // Adapter for the RecyclerView
    private lateinit var gridAdapter: GridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Add Log Popup
        val addLogButton: Button = root.findViewById(R.id.button_add_log);
        addLogButton.setOnClickListener {
            showAddLogPopUpMenu();
        }

        // Dynamically add each log to home page
        val recyclerView: RecyclerView = binding.myLogsRecycler // Make sure you have a RecyclerView in your fragment_home.xml with this ID
        recyclerView.layoutManager = object : GridLayoutManager(requireContext(), 2) {
            override fun canScrollVertically(): Boolean {
                return false // Disable scrolling
            }
        }
        gridAdapter = GridAdapter(myDataset) { holder ->
            val width = holder.imageView.resources.displayMetrics.widthPixels / 2
            val layoutParams = holder.imageView.layoutParams
            layoutParams.height = width
            holder.imageView.layoutParams = layoutParams
        }
        recyclerView.adapter = gridAdapter


        return root
    }

    private fun showAddLogPopUpMenu() {
        val popupView = layoutInflater.inflate(R.layout.fragment_popup_add_log, null)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        // Create the popup window
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.animationStyle = R.style.PopupWindowOpenAnimation
        // Show the popup window
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        // Cancel button is pressed
        popupView.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            popupWindow.animationStyle = R.style.PopupWindowCloseAnimation
            popupWindow.dismiss()
        }

        // Create button is pressed
        popupView.findViewById<Button>(R.id.create_button).setOnClickListener {
            val logName = popupView.findViewById<EditText>(R.id.nameEditText).text.toString()
            if (logName.isNotBlank()) {
                Log.d(TAG, logName)
                popupWindow.dismiss()
                activity?.recreate()
            } else {

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
