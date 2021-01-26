package com.example.desafiofirebase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.desafiofirebase.R
import com.example.desafiofirebase.services.cr
import com.example.desafiofirebase.services.db
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_games.view.*


class DetailGamesFragment : Fragment() {

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
            findNavController().navigate(R.id.action_detailGamesFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_games, container, false)

        view.ib_backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_detailGamesFragment_to_homeFragment)
        }

        view.fb_updateGame.setOnClickListener {
            viewModel.newOrUpdate("Update")
            findNavController().navigate(R.id.action_detailGamesFragment_to_gameCreatorFragment)
        }

        viewModel.detailGame.observe(viewLifecycleOwner, {
            Picasso.get().load(viewModel.listGames.value!![it].img).into(view.iv_detailGame)
            view.tv_title_game_capa.text = viewModel.listGames.value!![it].name
            view.tv_titleDetail.text = viewModel.listGames.value!![it].name
            view.tv_lancamento.text = viewModel.listGames.value!![it].anoLancamento
            view.tv_descricaoGame.text = viewModel.listGames.value!![it].desc
        })

        return view
    }


}