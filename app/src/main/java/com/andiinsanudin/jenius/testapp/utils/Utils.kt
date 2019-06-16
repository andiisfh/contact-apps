package com.andiinsanudin.jenius.testapp.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Andi Insanudin on 2019-06-17.
 */
class Utils {

    /**
     *
     * Function for add text to clipboard
     *
     * **/
    fun copyToClipBoard(context: Context, content: String): CharSequence {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("ID is copied to clipboard", content)
        clipboard.primaryClip = clip

        return clip.description.label
    }

    /**
     *
     * Function for show snackbar message
     *
     * **/
    fun showSnackBar(context: Context, view: View, text: CharSequence, bgColor: Int = android.R.color.holo_green_light, textColor: Int = android.R.color.black) {
        val snackbar = Snackbar.make(view, text, 3000)
        val snackbarView = snackbar.view
        snackbar.setTextColor(ContextCompat.getColor(context, textColor))
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, bgColor))
        snackbar.show()
    }


}