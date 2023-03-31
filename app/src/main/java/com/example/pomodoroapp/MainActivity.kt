package com.example.pomodoroapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pomodoroapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvStart.setOnClickListener{
            val studyTime = binding.etStudy.text.toString()
            val breakTime = binding.etBreak.text.toString()
            val roundCount = binding.etRound.text.toString()

            if(studyTime.isNotEmpty() && breakTime.isNotEmpty() && roundCount.isNotEmpty())
            {
                val intent = Intent(this, FeedActivity::class.java)
                intent.putExtra("study", studyTime.toInt())
                intent.putExtra("break", breakTime.toInt())
                intent.putExtra("round", roundCount.toInt())
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Fil fields above", Toast.LENGTH_SHORT).show()
            }
        }

    }
}