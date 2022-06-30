package com.example.sample_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileContent.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileContentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Log.d("Fragment (ProfileContentFragment)","Called onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("Fragment (ProfileContentFragment)","Called onCreateView")
        return inflater.inflate(R.layout.fragment_profile_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("Fragment (ProfileContentFragment)", "Called onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Fragment (ProfileContentFragment)","Called onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Fragment (ProfileContentFragment)","Called onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Fragment (ProfileContentFragment)","Called onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Fragment (ProfileContentFragment)","Called onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Fragment (ProfileContentFragment)","Called onDestroyView")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("Fragment (ProfileContentFragment)","Called onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Fragment (ProfileContentFragment)","Called onDetach")
    }
}