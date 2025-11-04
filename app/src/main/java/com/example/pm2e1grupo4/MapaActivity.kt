package com.example.pm2e1grupo4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.pm2e1grupo4.databinding.ActivityMapaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapaBinding
    private var lat = 0.0
    private var lng = 0.0
    private var nombre: String? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir datos del contacto
        lat = intent.getDoubleExtra("lat", 0.0)
        lng = intent.getDoubleExtra("lng", 0.0)
        nombre = intent.getStringExtra("nombre")

        // Configurar el mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Botón Atrás
        binding.btnAtras.setOnClickListener {
            finish() // Regresa a MainActivity o a la pantalla anterior
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val posicion = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(posicion).title(nombre ?: "Ubicación"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15f))
    }
}
