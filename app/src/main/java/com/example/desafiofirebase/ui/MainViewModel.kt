package com.example.desafiofirebase.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desafiofirebase.domain.Game
import com.example.desafiofirebase.services.cr
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_game_creator.*

class MainViewModel(db : FirebaseFirestore, cr : CollectionReference) : ViewModel() {
    val listGames = MutableLiveData<ArrayList<Game>>()
    val detailGame = MutableLiveData<Int>()
    val newOrUpdate = MutableLiveData<String>()
    var idGames = MutableLiveData<Int>()

    private val TAG = "MAIN_VIEW_MODEL"

    init {
        listGames.value = arrayListOf()
    }

    fun newOrUpdate(option: String){
        newOrUpdate.value = option
    }



    fun addGame(game : Game) = listGames.value!!.add(game)
    fun updateList(newList : ArrayList<Game>){
        listGames.value = newList
    }


    fun updateDetailGame(position : Int){
        detailGame.value = position
    }


    fun getDetailGame() : Game = listGames.value!![detailGame.value!!]




    fun sendGame(game : MutableMap<String, Any>){
        cr.document(idGames.value.toString()).set(game).addOnSuccessListener {
            idGames.value = idGames.value?.plus(1)
        }.addOnFailureListener {
            Log.i(TAG, it.toString())
        }
    }

    fun updateGame(game : MutableMap<String, Any>){
        cr.document(detailGame.value.toString()).update(game).addOnSuccessListener {
            Log.i(TAG, "ATUALIZOU!!")
            listGames.value!![detailGame.value!!] = Game(game["nome"].toString(), game["lançamento"].toString(), game["descrição"].toString(), game["img"].toString())
        }
    }

    fun readColectionBegin(){
        cr.get().addOnCompleteListener {
            if(it.isSuccessful){
                for(document in it.result!!){
                    addGame(Game(document.data["nome"].toString(), document.data["lançamento"].toString(), document.data["descrição"].toString(), document.data["img"].toString()))
                }
                idGames.value = listGames.value!!.size
            }
        }
    }

    fun readColectionUpdate(){
        cr.get().addOnCompleteListener{
            if(it.isSuccessful){
                val newList = arrayListOf<Game>()
                for(document in it.result!!){
                    newList.add(Game(document.data["nome"].toString(), document.data["lançamento"].toString(), document.data["descrição"].toString(), document.data["img"].toString()))
                }
                updateList(newList)
            }
        }
    }

    fun chooseOption(mapGame : MutableMap<String, Any>, game : Game){
        when(newOrUpdate.value){
            "New" -> {
                addGame(game)
                sendGame(mapGame)
            }
            "Update" -> updateGame(mapGame)
        }
    }



}