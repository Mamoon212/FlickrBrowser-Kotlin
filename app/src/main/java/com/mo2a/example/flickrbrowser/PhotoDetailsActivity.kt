package com.mo2a.example.flickrbrowser

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

        val photo= intent?.extras?.getParcelable(PHOTO_TRANSFER) as Photo

        photoTitle.text= photo.title
        photoTags.text= photo.tags
        photoAuthor.text= photo.author
        Picasso.get()
            .load(photo.link)
            .error(R.drawable.baseline_image_black_48dp)
            .placeholder(R.drawable.baseline_image_black_48dp)
            .into(photoImage)
    }

}
