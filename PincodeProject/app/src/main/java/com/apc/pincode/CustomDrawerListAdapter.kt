package com.apc.pincode

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by akshay on 08/01/18.
 */

class CustomDrawerListAdapter(private val mContext: Activity,
                              private val listItemImage: IntArray,
                              private val listItemNames: Array<String>) : ArrayAdapter<String>(mContext, R.layout.drawer_item, listItemNames) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = mContext.layoutInflater


        val rowView = inflater.inflate(R.layout.drawer_item, null, true)


        val tvDrawerName = rowView.findViewById<TextView>(R.id.drawer_text)
        val ivDrawerItemImage = rowView.findViewById<ImageView>(R.id.drawer_item_image)



        tvDrawerName.text = listItemNames[position]
        ivDrawerItemImage.setImageResource(listItemImage[position])

        return rowView
    }
}
