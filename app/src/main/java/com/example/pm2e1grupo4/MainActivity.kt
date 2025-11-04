package com.example.pm2e1grupo4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pm2e1grupo4.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var videoUri: Uri? = null
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var captureVideoLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermsLauncher: ActivityResultLauncher<Array<String>>

    private val apiService by lazy { ApiClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLaunchers()
        setupButtons()
    }

    private fun setupLaunchers() {
        captureVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                videoUri = result.data?.data
                binding.tvVideo.text = videoUri?.lastPathSegment ?: "Video listo"
                Toast.makeText(this, "Video capturado", Toast.LENGTH_SHORT).show()
                // Obtener ubicación justo después de grabar
                getLastLocation()
            }
        }

        requestPermsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val granted = perms.values.all { it }
            if (granted) openCamera()
            else Toast.makeText(this, "Permisos requeridos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnGrabarVideo.setOnClickListener { requestPermissionsThen() }

        binding.btnPlay.setOnClickListener {
            videoUri?.let {
                startActivity(Intent(Intent.ACTION_VIEW).apply { setDataAndType(it, "video/*") })
            } ?: Toast.makeText(this, "No hay video para reproducir", Toast.LENGTH_SHORT).show()
        }

        binding.btnGuardar.setOnClickListener { guardarContacto() }
        binding.btnVerLista.setOnClickListener { startActivity(Intent(this, ListaActivity::class.java)) }
    }

    private fun requestPermissionsThen() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermsLauncher.launch(perms)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        captureVideoLauncher.launch(intent)
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        lat = it.latitude
                        lng = it.longitude
                        binding.etLatitud.setText(lat.toString())
                        binding.etLongitud.setText(lng.toString())
                    } ?: Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { Toast.makeText(this, "Error al obtener ubicación", Toast.LENGTH_SHORT).show() }
        } catch (ex: SecurityException) {
            Toast.makeText(this, "Permiso de ubicación requerido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarContacto() {
        val nombre = binding.etNombre.text.toString().trim()
        if (nombre.isEmpty()) { binding.etNombre.error = "Ingrese nombre"; return }

        val rbNombre = RequestBody.create("text/plain".toMediaTypeOrNull(), nombre)
        val rbTelefono = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etTelefono.text.toString())
        val rbLat = RequestBody.create("text/plain".toMediaTypeOrNull(), lat?.toString() ?: "0")
        val rbLng = RequestBody.create("text/plain".toMediaTypeOrNull(), lng?.toString() ?: "0")

        var partVideo: MultipartBody.Part? = null
        videoUri?.let { uri ->
            try {
                val stream: InputStream? = contentResolver.openInputStream(uri)
                val req = RequestBody.create("video/*".toMediaTypeOrNull(), stream!!.readBytes())
                partVideo = MultipartBody.Part.createFormData("video", "video.mp4", req)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error procesando video: ${e.message}", Toast.LENGTH_LONG).show()
                return
            }
        }

        binding.progressBar.show()

        apiService.uploadContacto(rbNombre, rbTelefono, rbLat, rbLng, null, partVideo)
            .enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    binding.progressBar.hide()
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                        binding.etNombre.setText("")
                        binding.etTelefono.setText("")
                        binding.etLatitud.setText("")
                        binding.etLongitud.setText("")
                        videoUri = null
                        binding.tvVideo.text = "Sin video"
                    } else {
                        Toast.makeText(this@MainActivity, "Respuesta inválida del servidor", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    binding.progressBar.hide()
                    Toast.makeText(this@MainActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun android.view.View.show() { this.visibility = android.view.View.VISIBLE }
    private fun android.view.View.hide() { this.visibility = android.view.View.GONE }
}
