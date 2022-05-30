package com.sergioloc.hologram.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sergioloc.hologram.R

class Chip @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val ivBackground: ImageView
    private val tvTitle: TextView

    private var isChipSelected = false
    private var color = 0

    init {
        View.inflate(this.context, R.layout.chip, this) as ConstraintLayout

        ivBackground = findViewById(R.id.ivBackground)
        tvTitle = findViewById(R.id.tvTitle)

        attributeSet?.let {
            val typedArray = getTypedArray(it)
            val title = typedArray.getString(R.styleable.MyChip_chipTitle).toString()
            color = typedArray.getColor(R.styleable.MyChip_chipColor, ContextCompat.getColor(context, R.color.background))
            isChipSelected = typedArray.getBoolean(R.styleable.MyChip_chipSelected, false)
            tvTitle.text = title
            if (isChipSelected)
                select()
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun getTypedArray(attributeSet: AttributeSet): TypedArray {
        return context.obtainStyledAttributes(attributeSet, R.styleable.MyChip,0,0)
    }

    fun setOnClickListener(onClickListener: () -> Unit) {
        ivBackground.setOnClickListener {
            if (isChipSelected)
                unselect()
            else
                select()
            onClickListener()
        }
    }

    fun unselect() {
        isChipSelected = false
        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
        ivBackground.backgroundTintList = ContextCompat.getColorStateList(context, R.color.background)
    }

    private fun select() {
        isChipSelected = true
        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        ivBackground.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun isChipSelected(): Boolean {
        return isChipSelected
    }

}