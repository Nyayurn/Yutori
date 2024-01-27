package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.FrameworkContainer
import com.github.nyayurn.yutori.SimpleSatoriProperties
import com.github.nyayurn.yutori.WebSocketEventService
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val chronoProperties = SimpleSatoriProperties(token = "chronocat")
val koishiProperties = SimpleSatoriProperties(port = 5140, path = "/satori", token = "koishi")

fun main() {
    val container = FrameworkContainer.of {
        login.added += LoginListener.onAdded
        login.removed += LoginListener.onRemoved
        login.updated += LoginListener.onUpdated
        message.created += CommandListener
        message.created += OpenGraphListener
        message.created += AtListener
        message.created += MailListener
        friend.request += UserListener
    }
    val chronoClient = WebSocketEventService.of("chronocat") {
        this.container = container
        this.properties = chronoProperties
    }.connect()
    val koishiClient = WebSocketEventService.of("koishi") {
        this.container = container
        this.properties = koishiProperties
    }.connect()
    while (readln() != "exit") continue
    chronoClient.close()
    koishiClient.close()
}

val trustAllCerts: TrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {} // Noncompliant
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {} // Noncompliant
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}