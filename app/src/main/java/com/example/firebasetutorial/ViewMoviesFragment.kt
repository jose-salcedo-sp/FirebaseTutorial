package com.example.firebasetutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var moviesList: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_movies, container, false)
        val listView = view.findViewById<ListView>(R.id.listView_MovieCatalog_ViewFrag)

        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)

        moviesViewModel.moviesList.observe(viewLifecycleOwner) { movies ->
            moviesList = movies
            movieAdapter = MovieAdapter(requireContext(), moviesList)
            listView.adapter = movieAdapter
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMovie = moviesList[position] // Get the clicked movie

            val editFragment = EditMoviesFragment()
            val bundle = Bundle().apply {
                putString("id", selectedMovie.id)
                putString("name", selectedMovie.name)
                putString("year", selectedMovie.year)
                putString("genre", selectedMovie.genre)
                putString("img", selectedMovie.img)
            }
            editFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView_Main, editFragment)
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<FloatingActionButton>(R.id.but_Add_ViewFrag).setOnClickListener {
            (activity as MainActivity).changeToAddMovies()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}