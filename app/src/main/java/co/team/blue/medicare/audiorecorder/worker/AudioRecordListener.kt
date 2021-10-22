package co.team.blue.medicare.audiorecorder.worker

interface AudioRecordListener {
    fun onAudioReady(audioUri: String?)
    fun onRecordFailed(errorMessage: String?)
    fun onReadyForRecord()
}