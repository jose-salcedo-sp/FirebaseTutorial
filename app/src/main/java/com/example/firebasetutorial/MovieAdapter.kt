package com.example.firebasetutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MovieAdapter (private val context: Context, private val moviesList
: ArrayList<Movie>):ArrayAdapter<Movie>(context, R.layout.item, moviesList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater:LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.item, null)
        view.findViewById<TextView>(R.id.txtView_Name).text = moviesList[position].name.toString()
        view.findViewById<TextView>(R.id.txtView_Year).text = moviesList[position].year.toString()
        view.findViewById<TextView>(R.id.txtView_Genre).text = moviesList[position].genre.toString()
        view.findViewById<TextView>(R.id.txtView_Location).text = moviesList[position].location.toString()
        val imageView = view.findViewById<ImageView>(R.id.imgView_Poster)
        val imageUrl = moviesList[position].img.toString()
        Glide.with(view)
            .load(imageUrl)
            .into(imageView)
        return view
    }
}