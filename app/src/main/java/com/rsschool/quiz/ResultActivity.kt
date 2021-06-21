package com.rsschool.quiz

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rsschool.quiz.databinding.ActivityResultBinding
import kotlin.system.exitProcess

class ResultActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences= getSharedPreferences("answer", MODE_PRIVATE) !!
        fun clearRadioCheck() {
            for(i in 1..10)
                sharedPreferences.edit().putInt(i.toString(),-1).apply()
        }

        val result = intent.getIntExtra("Result",0)
        val list = intent.getStringArrayListExtra("Answer")
        val textMessage= "Your result: $result of 10 (${(result*100)/10}%)"
        binding.textView.text = textMessage

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,textMessage +"\n"+ list?.joinToString("\n"))
        sendIntent.type = "text/plain"

        val intent2 = Intent(this,MainActivity::class.java)
        intent2.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent2.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        intent2.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK

        binding.share.setOnClickListener {
            startActivity(sendIntent)
        }

        binding.back.setOnClickListener {
            clearRadioCheck()
            startActivity(intent2)
            finish()

        }
        binding.close.setOnClickListener {
            clearRadioCheck()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}