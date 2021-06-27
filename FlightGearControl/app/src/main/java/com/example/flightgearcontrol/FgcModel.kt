package com.example.flightgearcontrol

import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

// The model - opens connection with fg server and send the data
class FgcModel {
    lateinit var socket: Socket
    lateinit var output: PrintWriter

    fun setAileron(value: String) {
        if (this::output.isInitialized) {
            thread {
                output.print("set /controls/flight/aileron $value\r\n")
                output.flush()
            }
        }
    }

    fun setElevator(value: String) {
        if (this::output.isInitialized) {
            thread {
                output.print("set /controls/flight/elevator $value\r\n")
                output.flush()
            }
        }
    }

    fun setRudder(value: String) {
        if (this::output.isInitialized) {
            thread {
                output.print("set /controls/flight/rudder $value\r\n")
                output.flush()
            }
        }
    }

    fun setThrottle(value: String) {
        if (this::output.isInitialized) {
            thread {
                output.print("set /controls/engines/current-engine/throttle $value\r\n")
                output.flush()
            }
        }
    }

    fun connect(ip: String, port: Int) {
        thread {
            this.socket = Socket(ip, port)
            this.output = PrintWriter(socket.getOutputStream(), true)
        }
    }
}