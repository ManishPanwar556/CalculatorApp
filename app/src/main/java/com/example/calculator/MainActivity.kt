package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.models.HistoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import javax.script.ScriptEngineManager

class MainActivity : AppCompatActivity() {

    private var expression: String = ""
    private lateinit var binding: ActivityMainBinding
    private val auth by lazy {
        Firebase.auth
    }
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    private val maxOperatorStack=Stack<>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListener()

    }

    private fun setUpListener() {
        binding.num0.setOnClickListener {
            changeExpression("0")
        }
        binding.num1.setOnClickListener {
            changeExpression("1")
        }

        binding.num2.setOnClickListener {
            changeExpression("2")
        }

        binding.num3.setOnClickListener {
            changeExpression("3")
        }
        binding.num4.setOnClickListener {
            changeExpression("4")
        }
        binding.num5.setOnClickListener {
            changeExpression("5")
        }
        binding.num6.setOnClickListener {
            changeExpression("6")
        }
        binding.num7.setOnClickListener {
            changeExpression("7")
        }
        binding.num8.setOnClickListener {
            changeExpression("8")
        }
        binding.num9.setOnClickListener {
            changeExpression("9")
        }
        binding.add.setOnClickListener {
            changeExpression("+")
        }

        binding.subTract.setOnClickListener {
            changeExpression("-")
        }
        binding.multiply.setOnClickListener {
            changeExpression("*")
        }
        binding.divide.setOnClickListener {
            changeExpression("/")
        }
        binding.equalTo.setOnClickListener {
            calculateExpression()
        }

        binding.clearBtn.setOnClickListener {
            binding.resultText.text=""
            binding.expressionText.text=""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> {
                launchHistoryScreen()
                true
            }
            else -> {
                true
            }
        }
    }

    private fun launchHistoryScreen() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)

    }

    private fun calculateExpression() {
        var result: Double? = null
        val engine = ScriptEngineManager().getEngineByName("rhino")
        try {
            result = engine.eval(expression) as Double
        } catch (e: Exception) {

        }
        if (result != null) {
            binding.resultText.text = result.toString()
            saveHistory()
        }
    }

    private fun saveHistory() {
        val timeStamp = System.currentTimeMillis()
        val model = HistoryModel(
            binding.expressionText.text.toString(),
            binding.resultText.text.toString(),
            timeStamp
        )
        db.collection("history").document(auth.currentUser!!.uid).get().addOnCompleteListener {
            val history = it.result?.get("history")
            val arrayList = arrayListOf<HistoryModel>()

            if (history != null) {
                val list = history as List<HistoryModel>
                arrayList.addAll(list)
                arrayList.add(model)
                db.collection("history").document(auth.currentUser!!.uid)
                    .update("history", arrayList).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                val map = hashMapOf<String, Any>(
                    "history" to arrayList
                )
                arrayList.add(model)
                db.collection("history").document(auth.currentUser!!.uid).set(map)
                    .addOnSuccessListener {

                    }.addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun changeExpression(exp: String) {
        expression += exp
        binding.expressionText.text = expression

    }
}