package com.example.firebasetutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var myRef: DatabaseReference
    private lateinit var nameField: EditText
    private lateinit var yearField: EditText
    private lateinit var genreField: EditText
    private lateinit var imgField: EditText
    private var movieLocation: String? = null

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
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_add_movies, container, false)

        myRef = FirebaseDatabase.getInstance().getReference("movies")
        (activity as MainActivity).getLocation { locationString ->
            movieLocation = locationString
        }

        nameField = view.findViewById(R.id.edtTxtName_AddFrag)
        yearField = view.findViewById(R.id.edtTxtYear_AddFrag)
        genreField = view.findViewById(R.id.edtTxtGenre_AddFrag)
        imgField = view.findViewById(R.id.edtTxtImage_AddFrag)

        view.findViewById<Button>(R.id.butCancel_AddFrag).setOnClickListener {
            (activity as MainActivity).changeToViewMovies()
        }

        view.findViewById<Button>(R.id.butAdd_AddFrag).setOnClickListener {
            val name = nameField.text.toString().trim()
            val year = yearField.text.toString().trim()
            val genre = genreField.text.toString().trim()
            val img = imgField.text.toString().trim()

            if (name.isEmpty() || year.isEmpty() || genre.isEmpty() || img.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movieId = myRef.push().key // Generate a unique ID
            if (movieId != null) {
                val movie = MovieAux(name, year, genre, img, movieLocation ?: "Unknown Location")

                myRef.child(movieId).setValue(movie)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Movie Added", Toast.LENGTH_SHORT).show()
                            (activity as MainActivity).changeToViewMovies()
                        } else {
                            Toast.makeText(requireContext(), "Failed to add movie: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
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
         * @return A new instance of fragment AddMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}