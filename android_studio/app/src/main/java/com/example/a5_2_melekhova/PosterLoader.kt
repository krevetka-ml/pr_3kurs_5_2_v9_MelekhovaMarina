package com.example.a5_2_melekhova

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley

private var queue: RequestQueue? = null

fun loadPoster(context: Context, url: String?, target: ImageView) {
    target.setImageDrawable(null)
    target.tag = url
    if (url == null || !url.startsWith("http")) return

    if (queue == null) {
        queue = Volley.newRequestQueue(context.applicationContext)
    }
    val request = ImageRequest(
        url,
        { bitmap ->
            if (target.tag == url) target.setImageBitmap(bitmap)
        },
        300, 450,
        ImageView.ScaleType.CENTER_CROP,
        Bitmap.Config.RGB_565,
        { }
    )
    queue!!.add(request)
}