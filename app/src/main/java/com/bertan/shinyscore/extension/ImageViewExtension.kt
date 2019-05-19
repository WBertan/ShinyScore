package com.bertan.shinyscore.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bertan.shinyscore.R
import com.squareup.picasso.Picasso

fun ImageView?.loadUrl(
    url: String?,
    @DrawableRes loadingPlaceholder: Int = R.drawable.image_loading_placeholder,
    @DrawableRes errorPlaceholder: Int = R.drawable.image_error_placeholder
) = this?.let { imageView ->
    Picasso
        .get()
        .load(url)
        .placeholder(loadingPlaceholder)
        .error(errorPlaceholder)
        .into(imageView)
}