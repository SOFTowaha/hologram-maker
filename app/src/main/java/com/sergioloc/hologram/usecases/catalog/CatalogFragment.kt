package com.sergioloc.hologram.usecases.catalog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.flexbox.FlexboxLayout
import com.sergioloc.hologram.R
import com.vpaliy.chips_lover.ChipView

class CatalogFragment: Fragment() {

    private var myView: View? = null
    private var myContext: Context? = null

    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var tagsOpen:Boolean = false
    private var arrowDown:Boolean = false

    private var flexboxLayout: FlexboxLayout? = null
    private var chip0: ChipView? = null
    private var chip1: ChipView? = null
    private var chip2: ChipView? = null
    private var chip3: ChipView? = null
    private var chip4: ChipView? = null
    private var chip5: ChipView? = null
    private var chip6: ChipView? = null
    private var chip7: ChipView? = null

    private var controlsHandle: View? = null
    private var blackSpace: TextView? = null
    private var loading: ProgressBar? = null
    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_catalog, container, false)
        myContext = myView?.context

        initVariables()
        showLoading()

        initRecyclerView()

        searchView = context?.let { SearchView(it) }
        initTags()

        return myView
    }

    private fun initVariables(){
        val activity = activity as AppCompatActivity?
        activity?.title = resources.getString(R.string.title_catalog)
        loading = myView?.findViewById(R.id.loading) as ProgressBar
    }

    private fun showLoading() {
        loading?.visibility = View.VISIBLE
        recyclerView?.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        loading?.visibility = View.INVISIBLE
        recyclerView?.visibility = View.VISIBLE
    }

    private fun showFirebaseError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView(){
        recyclerView = myView?.findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = layoutManager
        //recyclerView?.adapter = presenter?.callAdapter()
    }

    private fun initTags() {
        flexboxLayout = myView?.findViewById(R.id.controlPanel) as FlexboxLayout
        //text = myView?.findViewById(R.id.tvCount) as TextView
        blackSpace = myView?.findViewById(R.id.blackSpace) as TextView
        controlsHandle = myView?.findViewById(R.id.controlHandle)
        //arrowTag = myView?.findViewById(R.id.arrowTag) as ImageView
        arrowDown = true
        tagsOpen = false

        chip0 = myView?.findViewById(R.id.chip0) as ChipView
        chip1 = myView?.findViewById(R.id.chip1) as ChipView
        chip2 = myView?.findViewById(R.id.chip2) as ChipView
        chip3 = myView?.findViewById(R.id.chip3) as ChipView
        chip4 = myView?.findViewById(R.id.chip4) as ChipView
        chip5 = myView?.findViewById(R.id.chip5) as ChipView
        chip6 = myView?.findViewById(R.id.chip6) as ChipView
        chip7 = myView?.findViewById(R.id.chip7) as ChipView

        chip0?.setOnClickListener {
            if (chip0?.isActivated!!) {
                deselectChip(0)
            } else {
                selectChip(0)
                deselectChip(1)
                deselectChip(2)
                deselectChip(3)
                deselectChip(4)
                deselectChip(5)
                deselectChip(6)
                deselectChip(7)
            }
        }
        chip1?.setOnClickListener {
            if (chip1?.isActivated!!)
                deselectChip(1)
            else
                selectChip(1)
        }
        chip2?.setOnClickListener {
            if (chip2?.isActivated!!)
                deselectChip(2)
            else
                selectChip(2)
        }
        chip3?.setOnClickListener {
            if (chip3?.isActivated!!)
                deselectChip(3)
            else
                selectChip(3)
        }
        chip4?.setOnClickListener {
            if (chip4?.isActivated!!)
                deselectChip(4)
            else
                selectChip(4)
        }
        chip5?.setOnClickListener {
            if (chip5?.isActivated!!)
                deselectChip(5)
            else
                selectChip(5)
        }
        chip6?.setOnClickListener {
            if (chip6?.isActivated!!)
                deselectChip(6)
            else
                selectChip(6)
        }
        chip7?.setOnClickListener {
            if (chip7?.isActivated!!)
                deselectChip(7)
            else
                selectChip(7)
        }
        selectChip(0)

        controlsHandle?.setOnClickListener {
            if (tagsOpen) {
                //movePanel(false)
            } else {
                //movePanel(true)
            }
            //switchArrow()
        }
    }

    private fun selectChip(i: Int) {
        when (i) {
            0 -> {
                chip0?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.red)
                chip0?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            1 -> {
                chip1?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.orange)
                chip1?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            2 -> {
                chip2?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.blue)
                chip2?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            3 -> {
                chip3?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.pink)
                chip3?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            4 -> {
                chip4?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.green)
                chip4?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            5 -> {
                chip5?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.cyan)
                chip5?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            6 -> {
                chip6?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                chip6?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
            7 -> {
                chip7?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.purple)
                chip7?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
            }
        }
    }

    private fun deselectChip(i: Int) {
        when (i) {
            0 -> {
                chip0?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip0?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            1 -> {
                chip1?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip1?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            2 -> {
                chip2?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip2?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            3 -> {
                chip3?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip3?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            4 -> {
                chip4?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip4?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            5 -> {
                chip5?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip5?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            6 -> {
                chip6?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip6?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
            7 -> {
                chip7?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                chip7?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
            }
        }
    }

    /*endregion

    private fun movePanel(open: Boolean) {
        val translationY: Float = if (open) {
            (if (flexboxLayout?.translationY == 0f) flexboxLayout?.height else 0)!!.toFloat()
        } else {
            (if (flexboxLayout?.translationY == 0f) 0 else flexboxLayout?.height)!!.toFloat()
        }
        controlHandle.animate().translationY(translationY).start()
        recyclerView?.animate()?.translationY(translationY)?.start()
        arrowTag.animate().translationY(translationY).start()
        tvCount.animate().translationY(translationY).start()
        blackSpace?.animate()?.translationY(translationY)?.start()
        toolbarShadow.animate().translationY(translationY).start()
        tagsOpen = !tagsOpen
    }

    private fun switchArrow() {
        arrowDown = if (arrowDown) {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_up)
            false
        } else {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_down)
            true
        }
    }

     */

}