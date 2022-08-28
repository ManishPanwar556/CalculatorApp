package com.example.calculator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.adapter.HistoryAdapter
import com.example.calculator.databinding.ActivityHistoryBinding
import com.example.calculator.models.HistoryModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class HistoryActivity : AppCompatActivity() {
private val auth by lazy {
    Firebase.auth
}
private lateinit var adapter:HistoryAdapter
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private lateinit var binding:ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        loadHistory()
    }

    private fun setUpRecyclerView(){
        binding.apply {
            adapter= HistoryAdapter()
            historyRev.layoutManager=LinearLayoutManager(this@HistoryActivity,LinearLayoutManager.VERTICAL,false)
            historyRev.adapter=adapter
        }
    }


    private fun loadHistory(){
           db.collection("history").document(auth.currentUser!!.uid).get().addOnSuccessListener {doc->

               binding.progressBar.visibility= View.GONE
               val historyMap=doc.get("history") as List<HashMap<String,Any>>?
               if(historyMap!=null) {
                   val historyList = arrayListOf<HistoryModel>()
                   historyMap.forEach {
                       historyList.add(
                           HistoryModel(
                               it.get("exp") as String,
                               it.get("result") as String,
                               it.get("timeStamp") as Long
                           )
                       )
                   }
                   historyList.sortWith { p0, p1 -> (p1!!.timeStamp - p0!!.timeStamp).toInt() }
                   if (historyList.size == 0) {
                       binding.text.visibility = View.VISIBLE
                   } else if (historyList.size <= 10)
                       adapter.addData(historyList)
                   else
                       adapter.addData(historyList.subList(0, 10))
               }
               else{
                   binding.text.visibility=View.VISIBLE
               }
           }.addOnFailureListener {

               binding.progressBar.visibility= View.GONE
               binding.text.visibility=View.VISIBLE
           }
    }
}