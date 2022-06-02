package com.needle.app.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.color(@ColorRes color: Int) = ContextCompat.getColor(context, color)

fun View.drawable(@DrawableRes drawable: Int) = ContextCompat.getDrawable(context, drawable)

fun View.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, length).show()
}

fun View.setOnElevationAnimation(event: MotionEvent, function: () -> Unit): Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN ->
            this.elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)
        MotionEvent.ACTION_UP -> {
            this.elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
            function()
        }
    }
    return true
}

fun setImageFromURL(context: Context, url: String, onResourceReady: (drawable: Drawable) -> Unit) {
    Glide.with(context).load(url).into(object : CustomTarget<Drawable?>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
            onResourceReady(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}
    })
}

fun setImageFromURL(context: Context, url: String, size: Int, onResourceReady: (drawable: Drawable) -> Unit) {
    val options = RequestOptions().fitCenter().override(size, size)
    Glide.with(context).load(url).apply(options).into(object : CustomTarget<Drawable?>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
            onResourceReady(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}
    })
}

fun View.setOnSingleClickListener(block: () -> Unit) {
    setOnClickListener(OnSingleClickListener(block))
}

class OnSingleClickListener(private val block: () -> Unit) : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        block()
    }
}