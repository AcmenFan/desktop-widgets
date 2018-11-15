package ai.advance.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {
        animFinish(false)
    }

    private fun animFinish(hideKeyBoardAuto: Boolean = true) {
        if (hideKeyBoardAuto) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(0, 0)
        }
        val anim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_out)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                mEditView.visibility = View.GONE
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        mEditView.startAnimation(anim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainRootView.setOnClickListener {
            animFinish()
        }
        mSearchView.setOnClickListener {
            val input = mEditView.text.toString()
            var url =
                if (input.endsWith(".com") || input.endsWith(".cn")) {
                    if (input.startsWith("http://") || input.startsWith("https://"))
                        input
                    else
                        "http://$input"
                } else {
                    "https://www.baidu.com${if (input.isNullOrEmpty()) "" else "/s?ie=UTF-8&wd=$input"}"
                }
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(url)
            startActivity(intent)
            finish()
        }
        mEditView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                mSearchView.performClick()
            }
            false
        }
        mEditView.postDelayed(
            {
                mEditView.requestFocus()
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(mEditView, 0)

            }, 200
        )
    }
}
