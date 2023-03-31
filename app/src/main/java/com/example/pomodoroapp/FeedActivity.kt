package com.example.pomodoroapp

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.pomodoroapp.databinding.ActivityFeedBinding
import java.lang.Exception

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    private var studyMinute : Int? = null
    private var breakMinute : Int? = null
    private var roundCount : Int? = null

    private var restTimer : CountDownTimer?= null
    private var studyTimer : CountDownTimer?= null
    private var breakTimer : CountDownTimer?= null

    private var mRound = 1

    private var isStudy = true

    private var isStop = false

    private var mp : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studyMinute = intent.getIntExtra("study", 0) * 60 * 1000
        breakMinute = intent.getIntExtra("break", 0) * 60 * 1000
        roundCount = intent.getIntExtra("round", 0)

        binding.tvRound.text = "$mRound/$roundCount"
        setRestTimer()

        binding.ivStop.setOnClickListener {
            resetOrStart()
        }
    }

    private fun setRestTimer(){
        playSound()
        binding.tvStatus.text = "Get Ready"
        binding.progressBar.progress = 0
        binding.progressBar.max = 10
        restTimer = object : CountDownTimer(10500, 1000)
        {
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0/1000).toInt()
                binding.tvTimer.text = (p0 /1000).toString()
            }

            override fun onFinish() {
                mp?.reset()
                if(isStudy){
                    setupStudyView()
                }else{
                    setupBreakView()
                }
            }
        }.start()
    }

    private fun setupBreakView() {
        binding.tvStatus.text = "Break Time"
        binding.progressBar.max = breakMinute!!/1000

        if(breakTimer != null)
            breakTimer = null

        setBreakTimer()
    }

    private fun setBreakTimer() {
        breakTimer = object : CountDownTimer(breakMinute!!.toLong() + 500, 1000){
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0 / 1000).toInt()
                binding.tvTimer.text = createTimeLabel((p0/1000).toInt())
            }

            override fun onFinish() {
                isStudy = true
                setRestTimer()
            }
        }.start()
    }

    private fun setupStudyView() {
        binding.tvRound.text = "$mRound/$roundCount"
        binding.tvStatus.text = "Study Time"
        binding.progressBar.max = studyMinute!!/1000

        if(studyTimer != null)
            studyTimer = null

        setStudyTimer()
    }

    private fun playSound() {
        try{
            val soundUrl = Uri.parse("android.resouce://com.lauren.newpomodoroapp/" + R.raw.countdown)
            mp = MediaPlayer.create(this, soundUrl)
            mp?.isLooping = false
            mp?.start()
        }
        catch(e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setStudyTimer(){
        studyTimer = object : CountDownTimer(studyMinute!!.toLong() + 500, 1000){
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0/1000).toInt()
                binding.tvTimer.text = createTimeLabel((p0/1000).toInt())
            }

            override fun onFinish() {
                if(mRound < roundCount!!)
                {
                    isStudy = false
                    setRestTimer()
                    mRound++
                }
                else{
                    clearAttribute()
                    binding.tvStatus.text = "You have finished your rounds"
                }
            }
        }.start()
    }

    private fun clearAttribute() {
        binding.tvStatus.text = "Press Play Button to Restart"
        binding.ivStop.setImageResource(R.drawable.baseline_play_arrow_24)
        binding.progressBar.progress = 0
        binding.tvTimer.text = "0"
        mRound = 1
        binding.tvRound.text = "$mRound/$roundCount"
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
        isStop = true

    }

    private fun createTimeLabel(time : Int): String {
        var timeLabel = ""
        val minutes = time / 60
        val seconds = time % 60

        if(minutes < 10) timeLabel += "0"
        timeLabel += "$minutes:"

        if(seconds < 10) timeLabel += "0"
            timeLabel+= seconds

        return timeLabel
    }

    private fun resetOrStart()
    {
        if(isStop){
            binding.ivStop.setImageResource(R.drawable.baseline_stop_24)
            setRestTimer()
            isStop = false
        }else{
            clearAttribute()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
    }
}