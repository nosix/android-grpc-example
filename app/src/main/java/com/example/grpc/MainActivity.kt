package com.example.grpc

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val uri = Uri.parse("http://10.0.2.2:50051/")
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val greeterService by lazy { GreeterService(uri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        coroutineScope.launch {
            val messageView = findViewById<TextView>(R.id.message)
            messageView.text = greeterService.sayHello("gRPC")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        greeterService.close()
    }
}