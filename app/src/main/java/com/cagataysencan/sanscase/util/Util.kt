package com.cagataysencan.sanscase.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.model.Match

fun createAlertDialogWithAction(context: Context, messageText : String , okButtonText : String , action : () -> Unit) {
    val alertDialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)

    alertDialogBuilder.setTitle(R.string.warning)
    alertDialogBuilder.setMessage(messageText)

    alertDialogBuilder.setPositiveButton(okButtonText) { dialog, which ->
        action()
        dialog.dismiss()
    }

    alertDialogBuilder.setCancelable(false)
    alertDialogBuilder.create()
    alertDialogBuilder.show()
}
// Extension function that is used to download an image from a URL into an ImageView. It uses the Glide library for image loading.
fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.favorited_match)

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
// Binding adapter that is used to download an image from a URL into an ImageView. With @BindingAdapter annotation
// is used to associate the downloadImage function with the android:downloadUrl
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}
// Same purpose as the downloadImage function, but this time it is used to change the image by isFavorite property.
@BindingAdapter("android:changeImageView")
fun changeImageView(view: ImageView, match: Match) {
    view.setImageResource(if (match.isFavorite) R.drawable.favorited_match else R.drawable.unfavorited_match)
}