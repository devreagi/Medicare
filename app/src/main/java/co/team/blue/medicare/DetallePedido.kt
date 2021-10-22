package co.team.blue.medicare


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import co.team.blue.medicare.audiorecorder.uikit.VoiceSenderDialog
import co.team.blue.medicare.audiorecorder.worker.AudioRecordListener
import co.team.blue.medicare.audiorecorder.worker.MediaPlayListener
import co.team.blue.medicare.audiorecorder.worker.Player
import co.team.blue.medicare.audiorecorder.worker.Recorder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.android.synthetic.main.activity_detalle_pedido.*

class DetallePedido : AppCompatActivity(), AudioRecordListener, MediaPlayListener {

    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var recorder: Recorder
    private lateinit var player: Player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)

        bottomNavigationView = findViewById(R.id.footer)
        bottomNavigationView.isSelected = true

        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener setOnItemSelectedListener@{ item: MenuItem ->
            when (item.itemId) {
                R.id.pedidos -> {
                    startActivity(Intent(applicationContext, Pedido::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.carrito -> {
                    startActivity(Intent(applicationContext, Carrito::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        })

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO),
            1
        )

    }

    fun openDialog(view: View) {
        val dialog = VoiceSenderDialog(this)
        dialog.setBeepEnabled(true)
        dialog.show(supportFragmentManager, "VOICE")
        playRecordButton.visibility = GONE
    }

    override fun onRecordFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        playRecordButton.visibility = GONE
    }

    override fun onReadyForRecord() {
        //READY FOR RECORD DO NOT CALL STOP RECORD BEFORE THIS CALLBACK
    }

    override fun onAudioReady(audioUri: String?) {
        Toast.makeText(this, audioUri, Toast.LENGTH_SHORT).show()
        playRecordButton.visibility = GONE
        player = Player(this)
        player.injectMedia(audioUri)
    }

    fun playRecord(view: View) {
        if (player.player!!.isPlaying)
            player.stopPlaying()
        else player.startPlaying()
    }

    @SuppressLint("SetTextI18n")
    override fun onStopMedia() {
        playRecordButton.text = getString(R.string.play_recordd)
    }

    override fun onStartMedia() {
        playRecordButton.text = getString(R.string.stop_play_recordd)
    }
}