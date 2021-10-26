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
import co.team.blue.medicare.databinding.ActivityDetallePedidoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DetallePedido : AppCompatActivity(), AudioRecordListener, MediaPlayListener {

    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var recorder: Recorder
    private lateinit var player: Player
    private lateinit var binding: ActivityDetallePedidoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

    fun openDialog(@Suppress("UNUSED_PARAMETER") view: View) {
        val dialog = VoiceSenderDialog(this)
        dialog.setBeepEnabled(true)
        dialog.show(supportFragmentManager, "VOICE")
        binding.playRecordButton.visibility = GONE
    }

    override fun onRecordFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        binding.playRecordButton.visibility = GONE
    }

    override fun onReadyForRecord() {
        //READY FOR RECORD DO NOT CALL STOP RECORD BEFORE THIS CALLBACK
    }

    override fun onAudioReady(audioUri: String?) {
        Toast.makeText(this, audioUri, Toast.LENGTH_SHORT).show()
        binding.playRecordButton.visibility = GONE
        player = Player(this)
        player.injectMedia(audioUri)
    }

    fun playRecord(@Suppress("UNUSED_PARAMETER") view: View) {
        if (player.player!!.isPlaying)
            player.stopPlaying()
        else player.startPlaying()
    }

    @SuppressLint("SetTextI18n")
    override fun onStopMedia() {
        binding.playRecordButton.text = getString(R.string.play_recordd)
    }

    override fun onStartMedia() {
        binding.playRecordButton.text = getString(R.string.stop_play_recordd)
    }
}