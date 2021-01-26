package com.example.desafiofirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.desafiofirebase.R
import com.example.desafiofirebase.adapter.GamesAdapter
import com.example.desafiofirebase.domain.Game
import com.example.desafiofirebase.services.cr
import com.example.desafiofirebase.services.db
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), GamesAdapter.OnClickGameListener {

    private lateinit var gamesAdapter: GamesAdapter
    val viewModel by activityViewModels<MainViewModel>{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(db, cr) as T
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        view.fb_add.setOnClickListener {
            viewModel.newOrUpdate("New")
            findNavController().navigate(R.id.action_homeFragment_to_gameCreatorFragment)
        }


        viewModel.listGames.observe(viewLifecycleOwner, {
            gamesAdapter.addList(it)
        })

        gamesAdapter = GamesAdapter(this)
        view.rv_Games.layoutManager = GridLayoutManager(view.context, 2)
        view.rv_Games.adapter = gamesAdapter
        view.rv_Games.hasFixedSize()

        return view
    }





    override fun onClickGame(position: Int) {
        viewModel.updateDetailGame(position)
        findNavController().navigate(R.id.action_homeFragment_to_detailGamesFragment)
    }


}