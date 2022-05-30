package com.sergioloc.hologram.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sergioloc.hologram.R

class Chip @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val root: ConstraintLayout
    private val tvTitle: TextView

    private var isChipSelected = false
    private var color = 0

    init {
        View.inflate(this.context, R.layout.chip, this) as ConstraintLayout

        root = findViewById(R.id.root)
        tvTitle = findViewById(R.id.tvTitle)

        attributeSet?.let {
            val typedArray = getTypedArray(it)
            val title = typedArray.getString(R.styleable.MyChip_text).toString()
            color = typedArray.getColor(R.styleable.MyChip_color, ContextCompat.getColor(context, R.color.background))
            tvTitle.text = title
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun getTypedArray(attributeSet: AttributeSet): TypedArray {
        return context.obtainStyledAttributes(attributeSet, R.styleable.MyChip,0,0)
    }

    fun setOnClickListener(onClickListener: () -> Unit) {
        root.setOnClickListener {
            if (isChipSelected)
                unselect()
            else
                select()
            isChipSelected = !isChipSelected
            onClickListener()
        }
    }

    private fun unselect() {
        root.backgroundTintList = ContextCompat.getColorStateList(context, R.color.background)
    }

    private fun select() {
        root.backgroundTintList = ContextCompat.getColorStateList(context, color)
    }

}