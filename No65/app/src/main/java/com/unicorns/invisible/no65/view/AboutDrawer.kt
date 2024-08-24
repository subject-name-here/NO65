package com.unicorns.invisible.no65.view

import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityAboutBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class AboutDrawer(
    override val activity: MainActivity,
    private val binding: ActivityAboutBinding
) : FadeDrawer {
    override val screen: View
        get() = binding.screen

    private val emailTextView: TextView
        get() = binding.aboutContact

    private fun TextView.setHyperlinkStyle() {
        val spannable = SpannableString(text)
        for (urlSpan in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(object : URLSpan(urlSpan.url) {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), 0)
        }
        text = spannable
    }

    fun setEmailLink() = setLink(emailTextView)
    private fun setLink(view: TextView) = launchCoroutineOnMain {
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setHyperlinkStyle()
    }
}