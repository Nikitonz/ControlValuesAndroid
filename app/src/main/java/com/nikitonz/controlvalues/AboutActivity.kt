package com.nikitonz.controlvalues

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity_view)

        var buttonReturn = findViewById<Button>(R.id.buttonReturn)
        buttonReturn.setOnClickListener {
            finish()
        }

        var imageViewGmailTo = findViewById<ImageView>(R.id.imageViewEmail)
        imageViewGmailTo.setOnClickListener{
            val email = "nikitoniy2468@gmail.com"
            val subject = "ControlValues Application"
            val body = ""

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, body)

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                val emailIntent = Intent(Intent.ACTION_VIEW)
                emailIntent.data = Uri.parse("https://mail.google.com/")

                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {

                    Toast.makeText(this, "Невозможно отправить письмо", Toast.LENGTH_SHORT).show()
                }
            }
        }

        var imageViewGithubGOTO = findViewById<ImageView>(R.id.imageViewGitHub)
        imageViewGithubGOTO.setOnClickListener {
            val githubUrl = "https://github.com/Nikitonz/ControlValuesAndroid/tree/master" // Замените на свой URL GitHub репозитория

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                Toast.makeText(this, "Невозможно открыть GitHub", Toast.LENGTH_SHORT).show()
            }
        }

/*
        val lolBanImageView = findViewById<ImageView>(R.id.lolBan)

        val audioDuration = getAudioDuration(R.raw.cave2)


        Handler(Looper.getMainLooper()).postDelayed({

            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = audioDuration


            fadeIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    playSound()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    lolBanImageView.visibility = ImageView.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            lolBanImageView.startAnimation(fadeIn)



        }, 5000)*/
    }

    private fun playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.cave2)
        mediaPlayer.start()
    }

    private fun getAudioDuration(audioResId: Int): Long {
        val mediaPlayer = MediaPlayer.create(this, audioResId)
        val duration = mediaPlayer.duration.toLong()
        mediaPlayer.release()
        return duration
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }




}