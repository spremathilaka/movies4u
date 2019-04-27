package com.zotikos.m4u.util.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zotikos.m4u.util.extension.getParentActivity
import com.zotikos.m4u.util.extension.load
import timber.log.Timber


object DataBindingUtils {

    @JvmStatic
    @BindingAdapter("showOrHide")
    fun setVisibility(view: View, visibility: Boolean) {
        Timber.d("visibility = $visibility")
        view.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("mutableText")
    fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
        val parentActivity: AppCompatActivity? = view.getParentActivity()
        if (parentActivity != null && text != null) {
            text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
        }
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("bind:imageUrl")
    fun loadImage(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl)
    }

    /* @JvmStatic
     @BindingAdapter("bindWithCallBack:imageUrlWithCallBack")
     fun loadImageWithCalBack(imageView: ImageView, imageUrl: String, callBack: ImageLoadingCallback) {
         imageView.load(imageUrl, callBack)
     }*/
}