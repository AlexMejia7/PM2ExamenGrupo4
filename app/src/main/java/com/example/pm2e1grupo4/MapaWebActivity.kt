package com.example.pm2e1grupo4

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebViewClient
import android.widget.Button

class MapaWebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var btnAtras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_web)

        webView = findViewById(R.id.webViewMapa)
        btnAtras = findViewById(R.id.btnAtras)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Obtener datos enviados desde ListaActivity
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val nombre = intent.getStringExtra("nombre") ?: "Ubicación"

        // HTML con Leaflet
        val html = """
            <!doctype html>
            <html>
            <head>
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
              <style> html,body,#map{ height:100%; margin:0; padding:0 } </style>
            </head>
            <body>
              <div id="map"></div>
              <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
              <script>
                var map = L.map('map').setView([$lat, $lng], 15);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
                  maxZoom:19, attribution: '&copy; OpenStreetMap contributors'
                }).addTo(map);
                L.marker([$lat,$lng]).addTo(map).bindPopup("$nombre").openPopup();
              </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)

        btnAtras.setOnClickListener { finish() }
    }
}
