package com.mo2a.example.flickrbrowser

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

private const val TAG= "BaseActivity"
internal const val FLICKR_QUERY= "FLICKR_QUERY"
internal const val PHOTO_TRANSFER= "PHOTO_TRANSFER"

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {
    internal fun activateToolbar( enableHome: Boolean){
        Log.d(TAG,"activateToolbar called")
        var toolbar= findViewById<View>(R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}

