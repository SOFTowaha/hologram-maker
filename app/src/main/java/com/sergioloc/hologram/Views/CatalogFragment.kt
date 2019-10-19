package com.sergioloc.hologram.Views

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.flexbox.FlexboxLayout
import com.sergioloc.hologram.Interfaces.CatalogInterface
import com.sergioloc.hologram.Presenters.CatalogPresenterImpl
import com.sergioloc.hologram.R
import com.vpaliy.chips_lover.ChipView
import kotlinx.android.synthetic.main.fragment_list.*


@SuppressLint("ValidFragment")
class CatalogFragment(var guest: Boolean): Fragment(), CatalogInterface.View, SearchView.OnQueryTextListener, View.OnClickListener {

    //region Variables
    private var myView: View? = null
    private var myContext: Context? = null
    private var presenter: CatalogPresenterImpl? = null
    private var fbMenu: FloatingActionsMenu? = null
    private var fbFav: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var tagSelected: Boolean = false
    private var tagsOpen:Boolean = false
    private var arrowDown:Boolean = false
    private var chip0IsChecked:Boolean = false
    private var chip1IsChecked:Boolean = false
    private var chip2IsChecked:Boolean = false
    private var chip3IsChecked: Boolean = false
    private var chip4IsChecked:Boolean = false
    private var chip5IsChecked:Boolean = false
    private var chip6IsChecked:Boolean = false
    private var chip7IsChecked:Boolean = false
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
    private var favSelected: Boolean = false
    private var loading: ProgressBar? = null
    private var ivNoConnection: ImageView? = null
    private var tvNoConnection: TextView? = null

    private var searchView: SearchView? = null

    //endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_list, container, false)
        setHasOptionsMenu(true)

        initVariables()
        showLoading()

        presenter = CatalogPresenterImpl(this, myView!!, guest, myContext!!)
        initRecyclerView()

        presenter?.callInitFirebaseList()
        searchView = SearchView(context)
        initFb()
        initTags()

        return myView
    }

    override fun initVariables(){
        val activity = activity as AppCompatActivity?
        myContext = activity?.applicationContext
        activity?.title = resources.getString(R.string.title_catalog)
        loading = myView?.findViewById(R.id.loading) as ProgressBar
        ivNoConnection = myView?.findViewById(R.id.ivNoConnection) as ImageView
        tvNoConnection = myView?.findViewById(R.id.tvNoConnection) as TextView
    }

    override fun showConnectionError() {
        loading?.visibility = View.INVISIBLE
        ivNoConnection?.visibility = View.VISIBLE
        tvNoConnection?.visibility = View.VISIBLE
    }

    override fun hideConnectionError() {
        ivNoConnection?.visibility = View.GONE
        tvNoConnection?.visibility = View.GONE
    }

    override fun changeCountText(number: Int) {
        tvCount.text =  "$number videos"
    }

    override fun showLoading() {
        loading?.visibility = View.VISIBLE
        recyclerView?.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        loading?.visibility = View.INVISIBLE
        recyclerView?.visibility = View.VISIBLE
    }

    override fun showFirebaseError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun initRecyclerView(){
        recyclerView = myView?.findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = presenter?.callAdapter()
    }

    override fun initFb() {
        fbMenu = myView?.findViewById(R.id.menu_fab) as FloatingActionsMenu
        fbFav = myView?.findViewById(R.id.fb_fav) as FloatingActionButton
        fbMenu?.scaleX = 1f
        fbMenu?.scaleY = 1f


        //Hide FAB on Scroll
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fbMenu?.visibility == View.VISIBLE) {
                    animFab(1)
                    fbMenu?.collapse()
                    fbMenu?.visibility = View.INVISIBLE
                } else if (dy < 0 && fbMenu?.visibility != View.VISIBLE) {
                    animFab(2)
                    fbMenu?.visibility = View.VISIBLE
                }
                /*
                if(recyclerView.getScrollState()<10)
                    chipUp.setVisibility(View.VISIBLE);
                else
                    chipUp.setVisibility(View.INVISIBLE);
                    */
                presenter?.callCloseSwipe()
            }
        })

        fbFav?.setOnClickListener {
            if (!guest) {
                pressFavButton()
                fbMenu?.collapse()
            } else {
                Toast.makeText(context, R.string.registered_fav, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun pressFavButton(){
        selectChip(0)
        deselectChip(1)
        deselectChip(2)
        deselectChip(3)
        deselectChip(4)
        deselectChip(5)
        deselectChip(6)
        deselectChip(7)

        if (favSelected) {
            activity?.title = resources.getString(R.string.title_catalog)
            presenter?.callInitFirebaseList()
        }
        else {
            activity?.title = resources.getString(R.string.favourites)
            presenter?.callFavList()
        }
        favSelected = !favSelected
    }

    //region ChipsControl

    fun initTags(){
        flexboxLayout = myView?.findViewById(R.id.controlPanel) as FlexboxLayout
        //text = myView?.findViewById(R.id.tvCount) as TextView
        blackSpace = myView?.findViewById(R.id.blackSpace) as TextView
        controlsHandle = myView?.findViewById(R.id.controlHandle)
        //arrowTag = myView?.findViewById(R.id.arrowTag) as ImageView
        arrowDown = true
        tagsOpen = false

        chip0IsChecked = true
        chip1IsChecked = false
        chip2IsChecked = false
        chip3IsChecked = false
        chip4IsChecked = false
        chip5IsChecked = false
        chip6IsChecked = false
        chip7IsChecked = false

        chip0 = myView?.findViewById(R.id.chip0) as ChipView
        chip1 = myView?.findViewById(R.id.chip1) as ChipView
        chip2 = myView?.findViewById(R.id.chip2) as ChipView
        chip3 = myView?.findViewById(R.id.chip3) as ChipView
        chip4 = myView?.findViewById(R.id.chip4) as ChipView
        chip5 = myView?.findViewById(R.id.chip5) as ChipView
        chip6 = myView?.findViewById(R.id.chip6) as ChipView
        chip7 = myView?.findViewById(R.id.chip7) as ChipView

        chip0?.setOnClickListener(this)
        chip1?.setOnClickListener(this)
        chip2?.setOnClickListener(this)
        chip3?.setOnClickListener(this)
        chip4?.setOnClickListener(this)
        chip5?.setOnClickListener(this)
        chip6?.setOnClickListener(this)
        chip7?.setOnClickListener(this)
        selectChip(0)

        controlsHandle?.setOnClickListener {
            if (tagsOpen) {
                movePanel(false)
            } else {
                movePanel(true)
            }
            switchArrow()
        }
    }


    override fun selectChip(i: Int) {
        when (i) {
            0 -> {
                chip0IsChecked = true
                chip0?.backgroundColor = context?.resources!!.getColor(R.color.red)
                chip0?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            1 -> {
                chip1IsChecked = true
                chip1?.backgroundColor = context?.resources!!.getColor(R.color.orange)
                chip1?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            2 -> {
                chip2IsChecked = true
                chip2?.backgroundColor = context?.resources!!.getColor(R.color.blue)
                chip2?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            3 -> {
                chip3IsChecked = true
                chip3?.backgroundColor = context?.resources!!.getColor(R.color.pink)
                chip3?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            4 -> {
                chip4IsChecked = true
                chip4?.backgroundColor = context?.resources!!.getColor(R.color.green)
                chip4?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            5 -> {
                chip5IsChecked = true
                chip5?.backgroundColor = context?.resources!!.getColor(R.color.cyan)
                chip5?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            6 -> {
                chip6IsChecked = true
                chip6?.backgroundColor = context?.resources!!.getColor(R.color.yellow)
                chip6?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
            7 -> {
                chip7IsChecked = true
                chip7?.backgroundColor = context?.resources!!.getColor(R.color.purple)
                chip7?.textColor = context?.resources!!.getColor(R.color.colorWhite)
            }
        }
    }

    override fun deselectChip(i: Int) {
        when (i) {
            0 -> {
                chip0IsChecked = false
                chip0?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip0?.textColor = context?.resources!!.getColor(R.color.black)
            }
            1 -> {
                chip1IsChecked = false
                chip1?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip1?.textColor = context?.resources!!.getColor(R.color.black)
            }
            2 -> {
                chip2IsChecked = false
                chip2?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip2?.textColor = context?.resources!!.getColor(R.color.black)
            }
            3 -> {
                chip3IsChecked = false
                chip3?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip3?.textColor = context?.resources!!.getColor(R.color.black)
            }
            4 -> {
                chip4IsChecked = false
                chip4?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip4?.textColor = context?.resources!!.getColor(R.color.black)
            }
            5 -> {
                chip5IsChecked = false
                chip5?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip5?.textColor = context?.resources!!.getColor(R.color.black)
            }
            6 -> {
                chip6IsChecked = false
                chip6?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip6?.textColor = context?.resources!!.getColor(R.color.black)
            }
            7 -> {
                chip7IsChecked = false
                chip7?.backgroundColor = context?.resources!!.getColor(R.color.colorWhite)
                chip7?.textColor = context?.resources!!.getColor(R.color.black)
            }
        }
    }

    override fun areAllChipsSelected(): Boolean {
        if (chip1IsChecked && chip2IsChecked && chip3IsChecked && chip4IsChecked && chip5IsChecked
                && chip6IsChecked && chip7IsChecked) {
            selectChip(0)
            deselectChip(1)
            deselectChip(2)
            deselectChip(3)
            deselectChip(4)
            deselectChip(5)
            deselectChip(6)
            deselectChip(7)
            chip0IsChecked = true
            return true
        }
        return false
    }

    override fun onClick(v: View) {
        tagSelected = true
        var allButtonSelected = false
        when (v.id) {
            R.id.chip0 -> if (chip0IsChecked) {
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
                allButtonSelected = true
            }
            R.id.chip1 -> if (chip1IsChecked) {
                deselectChip(1)
            } else {
                selectChip(1)
            }
            R.id.chip2 -> if (chip2IsChecked) {
                deselectChip(2)
            } else {
                selectChip(2)
            }
            R.id.chip3 -> if (chip3IsChecked) {
                deselectChip(3)
            } else {
                selectChip(3)
            }
            R.id.chip4 -> if (chip4IsChecked) {
                deselectChip(4)
            } else {
                selectChip(4)
            }
            R.id.chip5 -> if (chip5IsChecked) {
                deselectChip(5)
            } else {
                selectChip(5)
            }
            R.id.chip6 -> if (chip6IsChecked) {
                deselectChip(6)
            } else {
                selectChip(6)
            }
            R.id.chip7 -> if (chip7IsChecked) {
                deselectChip(7)
            } else {
                selectChip(7)
            }
        }
        if (chip1IsChecked || chip2IsChecked || chip3IsChecked || chip4IsChecked || chip5IsChecked
                || chip6IsChecked || chip7IsChecked) {
            chip0IsChecked = false
            deselectChip(0)
        }

        if (areAllChipsSelected() || allButtonSelected) {
            presenter?.callFullList()
        }
        else {
            presenter?.callTagList(chip1IsChecked, chip2IsChecked, chip3IsChecked, chip4IsChecked, chip5IsChecked, chip6IsChecked, chip7IsChecked)
        }

    }

    //endregion

    override fun movePanel(open: Boolean) {
        val translationY: Float
        if (open) {
            translationY = (if (flexboxLayout?.translationY == 0f) flexboxLayout?.height else 0)!!.toFloat()
        } else {
            translationY = (if (flexboxLayout?.translationY == 0f) 0 else flexboxLayout?.height)!!.toFloat()
        }
        controlHandle.animate().translationY(translationY).start()
        recyclerView?.animate()?.translationY(translationY)?.start()
        arrowTag.animate().translationY(translationY).start()
        tvCount.animate().translationY(translationY).start()
        blackSpace?.animate()?.translationY(translationY)?.start()
        toolbarShadow.animate().translationY(translationY).start()
        tagsOpen = !tagsOpen
    }

    override fun switchArrow() {
        if (arrowDown) {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_up)
            arrowDown = false
        } else {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_down)
            arrowDown = true
        }
    }

    private fun animFab(i: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Entrada lenta
            val interpolador = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in)
            //Botar
            val interpolador2 = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.bounce)

            if (i == 1) { // Desaparece
                fbMenu?.animate()
                        ?.scaleX(0f)
                        ?.scaleY(0f)
                        ?.setInterpolator(interpolador)
                        ?.setDuration(600)
                        ?.setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {}
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
            } else { // Aparece
                fbMenu?.animate()
                        ?.scaleX(1f)
                        ?.scaleY(1f)
                        ?.setInterpolator(interpolador)
                        ?.setDuration(600)
                        ?.setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {}
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
            }

        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        fbMenu?.visibility = View.VISIBLE
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var myNewText = newText?.toLowerCase()

        myNewText?.let {
            when {
                favSelected -> presenter?.callSearchInFav(it)
                chip0IsChecked -> presenter?.callSearchInFull(it)
                else -> presenter?.callSearchInMerge(it)
            }
        }
        return false
    }

    /**Menu */

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            fbMenu?.visibility = View.INVISIBLE
            val searchView = MenuItemCompat.getActionView(item) as SearchView
            searchView.setOnQueryTextListener(this)
        }
        return super.onOptionsItemSelected(item)
    }


}