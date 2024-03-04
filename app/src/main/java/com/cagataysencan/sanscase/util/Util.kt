package com.cagataysencan.sanscase.util

import android.app.AlertDialog
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.model.MatchResponse
import com.cagataysencan.sanscase.service.NetworkResult

fun createAlertDialogWithAction(context: Context, messageText : String , okButtonText : String , action : () -> Unit) {
    val alertDialogBuilder = AlertDialog.Builder(context)

    // Set the title and message for the dialog
    alertDialogBuilder.setTitle(R.string.warning)
    alertDialogBuilder.setMessage(messageText)

    // Set a positive button and its click listener
    alertDialogBuilder.setPositiveButton(okButtonText) { dialog, which ->
        action()
        dialog.dismiss()
    }
    alertDialogBuilder.create()
    alertDialogBuilder.show()
}
fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}
fun placeholderProgressBar(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}