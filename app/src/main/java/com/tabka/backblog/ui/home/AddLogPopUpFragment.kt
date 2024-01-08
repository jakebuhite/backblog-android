package com.tabka.backblog.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tabka.backblog.R
import java.util.UUID

class AddLogPopUpFragment : BottomSheetDialogFragment () {
    private val TAG = "PopupFragment"
    var listener: DialogListener? = null
    interface DialogListener {
        fun onDialogCreateButtonClick(inputText: String)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_popup_add_log, container, false)

        // Create New Log
        view.findViewById<Button>(R.id.create_button).setOnClickListener {
            val editText = view.findViewById<EditText>(R.id.new_log_name_edit_text)
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.error = "This must not be empty"
            } else {
                listener?.onDialogCreateButtonClick(editText.text.toString())
                dismiss()
            }
        }

        // Close Dialog
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dismiss()
        }

        return view
    }

    // Override the dialog to create the animation
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.fragment_popup_add_log)
        dialog.window?.setWindowAnimations(R.style.PopupWindowOpenAnimation)

        return dialog
    }

    companion object {
        fun newInstance(): AddLogPopUpFragment {
            return AddLogPopUpFragment()
        }
    }
}