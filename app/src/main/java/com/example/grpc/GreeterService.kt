package com.example.grpc

import android.net.Uri
import com.example.grpc.helloworld.GreeterGrpcKt
import com.example.grpc.helloworld.helloRequest
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.Closeable

class GreeterService(uri: Uri) : Closeable {

    private val channel = run {
        println("Connecting to ${uri.host}:${uri.port}")

        val builder = ManagedChannelBuilder.forAddress(uri.host, uri.port)
        if (uri.scheme == "https") {
            builder.useTransportSecurity()
        } else {
            builder.usePlaintext()
        }

        builder.executor(Dispatchers.IO.asExecutor()).build()
    }

    private val greeter = GreeterGrpcKt.GreeterCoroutineStub(channel)

    suspend fun sayHello(name: String): String {
        return try {
            val request = helloRequest { this.name = name }
            val response = greeter.sayHello(request)
            response.message
        } catch (e: Exception) {
            e.printStackTrace()
            e.message ?: "Unknown Error"
        }
    }

    override fun close() {
        channel.shutdownNow()
    }
}