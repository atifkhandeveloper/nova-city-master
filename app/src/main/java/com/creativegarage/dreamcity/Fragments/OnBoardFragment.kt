package com.creativegarage.dreamcity.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.creativegarage.dreamcity.MainActivity
import com.creativegarage.dreamcity.OnBoardItem
import com.creativegarage.dreamcity.OnBoard_Adapter
import com.creativegarage.dreamcity.R
import java.util.*


class OnBoardFragment : Fragment(), View.OnClickListener {
    var v: View? = null
    var pager_indicator: LinearLayout? = null
    var dotsCount = 0
    var dots = arrayOfNulls<ImageView>(3)
    var onboard_pager: ViewPager? = null
    var mAdapter: OnBoard_Adapter? = null
    var btn_get_started: Button? = null
    var btn_onborad: RelativeLayout? = null
    var previous_pos = 0
    val onBoardItems: ArrayList<OnBoardItem> = ArrayList<OnBoardItem>()
    var btn_next: TextView? = null
    var btn_skip: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_on_board, container, false)
        bindView()
        return v
    }

    open fun bindView() {
        btn_next = v!!.findViewById<View>(R.id.btn_next) as TextView
        btn_next!!.setOnClickListener(this)
        btn_skip = v!!.findViewById<View>(R.id.btn_skip) as TextView
        btn_skip!!.setOnClickListener(this)
        btn_onborad = v!!.findViewById<View>(R.id.btn_onborads) as RelativeLayout
        btn_get_started = v!!.findViewById<View>(R.id.btn_get_started) as Button
        btn_get_started!!.setOnClickListener(this)
        onboard_pager = v!!.findViewById<View>(R.id.pager_introduction) as ViewPager
        pager_indicator = v!!.findViewById<View>(R.id.viewPagerCountDots) as LinearLayout
        loadData()
        mAdapter = OnBoard_Adapter(v!!.context, onBoardItems)
        onboard_pager!!.adapter = mAdapter
        onboard_pager!!.currentItem = 0
        onboard_pager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                // Change the current position intimation
                for (i in 0 until dotsCount) {
                    dots!!.get(i)?.setImageDrawable(
                        ContextCompat.getDrawable(
                            v!!.context,
                            R.drawable.non_selected_item_dot
                        )
                    )
                }
                dots[position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        v!!.context,
                        R.drawable.selected_item_dot
                    )
                )
                val pos = position + 1
                if (pos == dotsCount && previous_pos == dotsCount - 1) show_animation() else if (pos == dotsCount - 1 && previous_pos == dotsCount) hide_animation()
                previous_pos = pos
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

//        btn_get_started.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Redirect to wherever you want", Toast.LENGTH_LONG).show();
//            }
//        });
        setUiPageViewController()
    }


    fun loadData() {
        val header = intArrayOf(R.string.ob_header1, R.string.ob_header2, R.string.ob_header3)
        val desc = intArrayOf(R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3)
        val imageId =
            intArrayOf(R.drawable.onboard1, R.drawable.onboard2, R.drawable.onboard3)
        for (i in imageId.indices) {
            val item = OnBoardItem()
            item.setImageID(imageId[i])
            item.setTitle(getResources().getString(header[i]))
            item.setDescription(getResources().getString(desc[i]))
            onBoardItems.add(item)
        }
    }

    // Button bottomUp animation

    // Button bottomUp animation
    fun show_animation() {
        val show = AnimationUtils.loadAnimation(v!!.context, R.anim.slide_up_anim)
        val hide = AnimationUtils.loadAnimation(v!!.context, R.anim.slide_down_anim)
        btn_get_started!!.startAnimation(show)
        btn_onborad!!.startAnimation(hide)
        show.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                btn_get_started!!.visibility = View.VISIBLE
                btn_onborad!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                btn_onborad!!.clearAnimation()
                btn_get_started!!.clearAnimation()
            }
        })
    }

    // Button Topdown animation

    // Button Topdown animation
    fun hide_animation() {
        val hide = AnimationUtils.loadAnimation(v!!.context, R.anim.slide_down_anim)
        val show = AnimationUtils.loadAnimation(v!!.context, R.anim.slide_up_anim)
        btn_get_started!!.startAnimation(hide)
        btn_onborad!!.startAnimation(show)
        hide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                btn_get_started!!.clearAnimation()
                btn_onborad!!.clearAnimation()
                btn_get_started!!.visibility = View.GONE
                btn_onborad!!.visibility = View.VISIBLE
            }
        })
    }

    // setup the
    open fun setUiPageViewController() {
        dotsCount = mAdapter!!.getCount()
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(v!!.context)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    v!!.context,
                    R.drawable.non_selected_item_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(15, 0, 15, 0)
            pager_indicator!!.addView(dots[i], params)
        }
        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                v!!.context,
                R.drawable.selected_item_dot
            )
        )
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_get_started -> {
                activity?.startActivity(Intent(activity, MainActivity::class.java))
                activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                activity?.finish()
            }
            R.id.btn_next -> if (previous_pos == 0) {
                onboard_pager!!.currentItem = 1
            } else {
                onboard_pager!!.currentItem = previous_pos
            }
            R.id.btn_skip -> {
                activity?.startActivity(Intent(activity, MainActivity::class.java))
                activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                activity?.finish()
            }
        }
    }
}