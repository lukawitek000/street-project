package com.example.streetapp.fragments.favouriteTrainings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.streetapp.R

class FavouritesTrainings : Fragment() {

    companion object {
        fun newInstance() =
            FavouritesTrainings()
        val TAG = FavouritesTrainings::class.java.simpleName
    }

    private lateinit var viewModel: FavouritesTrainingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_trainings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouritesTrainingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}