package com.example.firebasetutorial

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MovieAdapter(
    context: Activity,
    private val movies: ArrayList<Movies>
) : ArrayAdapter<Movies>(context, R.layout.item, movies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val movie = movies[position]
        viewHolder.name.text = movie.name
        viewHolder.year.text = movie.year
        viewHolder.genre.text = movie.genre

        viewHolder.image.setImageResource(
            when (movie.genre?.lowercase()) {
                "action" -> R.drawable.action
                "drama" -> R.drawable.drama
                "comedy" -> R.drawable.comedy
                else -> R.drawable.default_image // Imagen por defecto si el género no coincide
            }
        )

        return view
    }

    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.name)
        val year: TextView = view.findViewById(R.id.year)
        val genre: TextView = view.findViewById(R.id.genre)
        val image: ImageView = view.findViewById(R.id.img)
    }
}
