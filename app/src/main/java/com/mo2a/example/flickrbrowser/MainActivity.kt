package com.mo2a.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*

private const val TAG= "mainactivity"


class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener{

    private val flickrRecyclerViewAdapter= FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this,recyclerView, this))
        recyclerView.adapter= flickrRecyclerViewAdapter

        Log.d(TAG, "onCreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClickStarts")
        Toast.makeText(this, "long press to open photo details", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClickStarts")
        val photo= flickrRecyclerViewAdapter.getPhoto(position)
        if(photo!= null){
            val intent= Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }
    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean) :String{
        Log.d(TAG, "createUri starts")
        return  Uri.parse(baseURL).
                buildUpon().
                appendQueryParameter("tags", searchCriteria).
                appendQueryParameter("tagmode", if(matchAll) "ALL" else "ANY").
                appendQueryParameter("lang", lang).
                appendQueryParameter("format", "json").
                appendQueryParameter("nojsoncallback", "1").
                build().
                toString()  




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if(status== DownloadStatus.OK){
            Log.d(TAG, "odc called")
            val getFlickrJsonData= GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }else{
            Log.d(TAG, "odc failed with $status ")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable called")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable finished")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
    }

    override fun onResume() {
        super.onResume()
        val sharedPref= PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult= sharedPref.getString(FLICKR_QUERY, "")
        if(queryResult.isNotEmpty()){
            val url=  createUri("https://api.flickr.com/services/feeds/photos_public.gne", queryResult, "en-us", true)
            val getRawData= GetRawData(this)
            getRawData.execute(url)
        }
    }
}
