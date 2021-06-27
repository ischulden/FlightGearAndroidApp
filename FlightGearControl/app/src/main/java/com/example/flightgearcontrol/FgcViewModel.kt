package com.example.flightgearcontrol

// 'View model' - passes data from view to model
class FgcViewModel constructor(var model: FgcModel) {
    var aileron: Float = 0.0f
        set(value) {
            field = value
            this.model.setAileron(value.toString())
        }

    var elevator: Float = 0.0f
        set(value) {
            field = value
            this.model.setElevator(value.toString())
        }

    var rudder: Int = 0
        set(value) {
            field = value
            this.model.setRudder(((value - 1000000).toFloat() / 1000000).toString())
        }

    var throttle: Int = 0
        set(value) {
            field = value
            this.model.setThrottle((value.toFloat() / 1000000).toString())
        }

    fun connect(ip: String, port: Int) {
        this.model.connect(ip, port)
    }
}