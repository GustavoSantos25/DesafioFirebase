package com.example.desafiofirebase.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.desafiofirebase.R
import com.example.desafiofirebase.domain.Game
import com.example.desafiofirebase.services.cr
import com.example.desafiofirebase.services.db
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_detail_games.*
import kotlinx.android.synthetic.main.fragment_game_creator.*
import kotlinx.android.synthetic.main.fragment_game_creator.view.*


class GameCreatorFragment : Fragment() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference : StorageReference
    private val CODE_IMG = 1000
    private var uriImgGame : String = ""
    private val TAG = "CREATOR_GAMER"

    val viewModel by activityViewModels<MainViewModel>{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(db, cr) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config()
        requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.action_gameCreatorFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_creator, container, false)

        view.llFotoGame.setOnClickListener {
            setIntent()
        }

        if(viewModel.newOrUpdate.value == "Update"){
            val game = viewModel.getDetailGame()
            setData(game, view)
        }

        view.btn_saveGame.setOnClickListener {
            val mapGame = getData()
            val game = Game(mapGame["nome"] as String, mapGame["lançamento"] as String, mapGame["descrição"] as String, mapGame["img"] as String)
            viewModel.chooseOption(mapGame, game)
            viewModel.readColectionUpdate()
            findNavController().navigate(R.id.action_gameCreatorFragment_to_homeFragment)
        }


        return view
    }

    fun config(){
        alertDialog = SpotsDialog.Builder().setContext(context).build()
    }

    fun setData(game : Game, view : View){
        view.edNameGameCreate.setText(game.name, TextView.BufferType.EDITABLE)
        view.edLancamentoCreate.setText(game.anoLancamento, TextView.BufferType.EDITABLE)
        view.edDescricaoCreate.setText(game.desc, TextView.BufferType.EDITABLE)
        uriImgGame = game.img
        Picasso.get().load(uriImgGame).into(view.iv_fotoGameCreate)
    }

    fun getData() : MutableMap<String, Any>{
        val game : MutableMap<String, Any> = HashMap()

        game["nome"] = edNameGameCreate.text.toString()
        game["lançamento"] = edLancamentoCreate.text.toString()
        game["descrição"] = edDescricaoCreate.text.toString()
        game["img"] = uriImgGame

        return game
    }



    fun setIntent(){
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "captura imagem"), CODE_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CODE_IMG){
            storageReference = FirebaseStorage.getInstance().getReference(data!!.dataString!!)
            alertDialog.show()
            val uploadTask = storageReference.putFile(data.data!!)
            uploadTask.continueWithTask{
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    uriImgGame = it.result.toString()
                    Picasso.get().load(uriImgGame).into(requireView().iv_fotoGameCreate)
                    alertDialog.dismiss()
                }
            }
        }
    }



}