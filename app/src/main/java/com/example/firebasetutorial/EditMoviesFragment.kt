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
 * Use the [EditMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var myRef: DatabaseReference
    private lateinit var nameField: EditText
    private lateinit var yearField: EditText
    private lateinit var genreField: EditText
    private lateinit var imgField: EditText
    private var movieId: String? = null
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
        val view:View = inflater.inflate(R.layout.fragment_edit_movies, container, false)

        myRef = FirebaseDatabase.getInstance().getReference("movies")
        (activity as MainActivity).getLocation { locationString ->
            movieLocation = locationString
        }

        nameField = view.findViewById(R.id.edtTxtName_EditFrag)
        yearField = view.findViewById(R.id.edtTxtYear_EditFrag)
        genreField = view.findViewById(R.id.edtTxtGenre_EditFrag)
        imgField = view.findViewById(R.id.edtTxtImage_EditFrag)

        arguments?.let {
            movieId = it.getString("id")
            nameField.setText(it.getString("name"))
            yearField.setText(it.getString("year"))
            genreField.setText(it.getString("genre"))
            imgField.setText(it.getString("img"))
        }

        view.findViewById<Button>(R.id.butCancel_EditFrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.butEdit_EditFrag).setOnClickListener {
            var movie = MovieAux(nameField.text.toString(),
                yearField.text.toString(),
                genreField.text.toString(),
                imgField.text.toString(),
                movieLocation)
            myRef.child(movieId!!).setValue(movie).addOnCompleteListener{
                    task->
                if(task.isSuccessful){
                    Toast.makeText(requireContext(), "Edited Movie", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }else{
                    Toast.makeText(requireContext(), task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment EditMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}