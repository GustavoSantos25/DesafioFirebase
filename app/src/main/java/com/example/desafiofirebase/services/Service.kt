package com.example.desafiofirebase.services

import android.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dmax.dialog.SpotsDialog

var db : FirebaseFirestore = FirebaseFirestore.getInstance()
var cr : CollectionReference = db.collection("games")
