package com.example.xixixi.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


//音乐播放器
class MainActivity : AppCompatActivity() {
    val infoBean: MusicInfoBean by lazy { MusicInfoBean("https://p2.music.126.net/2bmVAQM9-KBMg9QOkWcXKQ==/109951162865916994.jpg","草东没有派对","山海","http://www.ytmp3.cn/down/55959.mp3","251000","04:11") }
    val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }
    val songData by lazy { Date() }
    private var ducationTotal = 0
    private var isPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutView = View.inflate(this, R.layout.activity_main, null)
        setContentView(layoutView)
        val windowPrograms = window.attributes
        windowPrograms.flags = windowPrograms.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        window.attributes = windowPrograms
        layoutView.setPadding(0, resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android")), 0, 0)
        layoutView.layoutParams = layoutView.layoutParams
        playMusic()
        initListener()

    }



     fun initListener() {
        mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
            override fun onPrepared(p0: MediaPlayer?) {
                isPlay = true
                mediaPlayer.start()
                cb_play.isChecked=false
                ducationTotal = infoBean.time.toInt()
                sb_music_progress.max=infoBean.time.toInt()
                updataSeekbar()
            }
        })
        sb_music_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, isFromUser: Boolean) {
                if (mediaPlayer!=null&&isFromUser){
                    mediaPlayer.seekTo(p1)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }
            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        cb_play.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (mediaPlayer!=null){
                    if (p1){
                        isPlay=false
                        mediaPlayer.pause()
                    }else{
                        isPlay=true
                        mediaPlayer.start()
                        updataSeekbar()
                    }
                }
            }

        })
        btn_replay.setOnClickListener {
            mediaPlayer.seekTo(0)
        }
    }

    private fun playMusic() {
        iv_album.loadPic(applicationContext,infoBean.album_pic_url)
        tv_music_name.text = infoBean.song_name
        tv_music_singer.text = infoBean.artist_name
        tv_music_time_total.text = infoBean.time_format
        tv_music_time_progress.text = "00:00"
        mediaPlayer.reset()
        mediaPlayer.isLooping=true
        mediaPlayer.setDataSource(infoBean.song_url)
        mediaPlayer.prepareAsync()
    }


    //更新seekbar
    private fun updataSeekbar() {
        object : Thread() {
            override fun run() {
                super.run()
                while (isPlay) {
                    Thread.sleep(800)
                    runOnUiThread {
                        sb_music_progress.progress=mediaPlayer.currentPosition
                        songData.time= mediaPlayer.currentPosition.toLong()
                        tv_music_time_progress.text= SimpleDateFormat("mm:ss").format(songData)
                    }
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer!=null){
            mediaPlayer.reset()
        }
    }
}

fun ImageView.loadPic(context:Context,url:String){
    Glide.with(context).load(url).into(this)
//    Glide.with(AppContext.contextInstance).load(url).into(this)
}
