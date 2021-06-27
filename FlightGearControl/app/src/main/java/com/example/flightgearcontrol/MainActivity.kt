package com.example.flightgearcontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

// View - passes data from user to 'view model'
class MainActivity : AppCompatActivity() {
    lateinit var vm: FgcViewModel // The model
    lateinit var sbListener: SbListener // Listeners to get notified when data changes
    lateinit var jsListener: JsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize 'view model' and listeners
        this.vm = FgcViewModel(FgcModel())
        this.sbListener = SbListener(this.vm)


        // Start listening to data changes
        this.jsListener = JsListener(this.vm)
        joystick.addListener(jsListener)
        throttle_sb.setOnSeekBarChangeListener(sbListener)
        rudder_sb.setOnSeekBarChangeListener(sbListener)

        // Connect button
        connect_button.setOnClickListener {
            vm.connect(ip_ti.editableText.toString(), Integer.parseInt(port_ti.editableText.toString()))
        }
    }
}

//1 Seek bar listener
class SbListener constructor(private val vm: FgcViewModel): SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.tag) {
            "throttle_sb" -> this.vm.throttle = progress
            "rudder_sb" -> this.vm.rudder = progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}

// Joystick listener
class JsListener constructor(private val vm: FgcViewModel) : JoystickListener {
    override fun centerChanged(x: Float, y: Float) {
        this.vm.aileron = x
        this.vm.elevator = y
    }
}