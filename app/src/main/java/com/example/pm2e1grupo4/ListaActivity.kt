package com.example.pm2e1grupo4

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListaActivity : AppCompatActivity() {

    private lateinit var btnAtras: Button
    private lateinit var etBuscar: EditText
    private lateinit var lvContactos: ListView
    private lateinit var btnEliminar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnVerVideo: Button

    private val apiService by lazy { ApiClient.instance.create(ApiService::class.java) }
    private val contactos = mutableListOf<Contacto>()
    private lateinit var adapter: ArrayAdapter<String>
    private var contactoSeleccionado: Contacto? = null

    // Cambia aquí a tu IP local donde está el API
    private val baseUrlLocal = "http://192.168.0.9/PM2ExamenAPI/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        // Referencias
        btnAtras = findViewById(R.id.btnAtras)
        etBuscar = findViewById(R.id.etBuscar)
        lvContactos = findViewById(R.id.lvContactos)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnVerVideo = findViewById(R.id.btnVerVideo)

        // Adapter
        lvContactos.choiceMode = ListView.CHOICE_MODE_SINGLE
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, contactos.map { it.nombre ?: "" })
        lvContactos.adapter = adapter

        // Selección simple
        lvContactos.setOnItemClickListener { _, _, position, _ ->
            contactoSeleccionado = contactos[position]
        }

        // Mantener presionado: abrir mapa
        lvContactos.setOnItemLongClickListener { _, _, position, _ ->
            val contacto = contactos[position]
            AlertDialog.Builder(this)
                .setTitle("Ver ubicación")
                .setMessage("¿Deseas ir a  la ubicación de ${contacto.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(this, MapaWebActivity::class.java).apply {
                        putExtra("lat", contacto.lat ?: 0.0)
                        putExtra("lng", contacto.lng ?: 0.0)
                        putExtra("nombre", contacto.nombre)
                    }
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        // Buscar contactos
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Botones
        btnEliminar.setOnClickListener { eliminarContacto() }
        btnActualizar.setOnClickListener { actualizarContacto() }
        btnVerVideo.setOnClickListener { verVideo() }
        btnAtras.setOnClickListener { finish() }

        fetchList()
    }

    private fun fetchList() {
        apiService.listar().enqueue(object : Callback<List<Contacto>> {
            override fun onResponse(call: Call<List<Contacto>>, response: Response<List<Contacto>>) {
                if (response.isSuccessful) {
                    contactos.clear()
                    contactos.addAll(response.body() ?: emptyList())
                    actualizarAdapter(contactos)
                } else {
                    Toast.makeText(this@ListaActivity, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Contacto>>, t: Throwable) {
                Toast.makeText(this@ListaActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun actualizarAdapter(lista: List<Contacto>) {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, lista.map { it.nombre ?: "" })
        lvContactos.adapter = adapter
    }

    private fun filtrar(texto: String) {
        val query = texto.trim().lowercase(Locale.getDefault())
        val filtrados = if (query.isEmpty()) contactos else contactos.filter {
            !it.nombre.isNullOrEmpty() && it.nombre.lowercase(Locale.getDefault()).contains(query)
        }
        actualizarAdapter(filtrados)
    }

    private fun eliminarContacto() {
        contactoSeleccionado?.let { contacto ->
            AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Eliminar contacto ${contacto.nombre}?")
                .setPositiveButton("Sí") { _, _ ->
                    apiService.eliminar(contacto.id).enqueue(object : Callback<ResponseData> {
                        override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                            Toast.makeText(this@ListaActivity, "Eliminado", Toast.LENGTH_SHORT).show()
                            fetchList()
                        }
                        override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                            Toast.makeText(this@ListaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }
                .setNegativeButton("No", null)
                .show()
        } ?: Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarContacto() {
        contactoSeleccionado?.let { contacto ->
            val edit = EditText(this).apply { setText(contacto.nombre ?: "") }
            AlertDialog.Builder(this)
                .setTitle("Editar nombre")
                .setView(edit)
                .setPositiveButton("Guardar") { _, _ ->
                    val newName = edit.text.toString().trim()
                    apiService.actualizar(
                        contacto.id,
                        newName,
                        contacto.lat?.toString() ?: "",
                        contacto.lng?.toString() ?: ""
                    ).enqueue(object : Callback<ResponseData> {
                        override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                            Toast.makeText(this@ListaActivity, "Actualizado", Toast.LENGTH_SHORT).show()
                            fetchList()
                        }
                        override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                            Toast.makeText(this@ListaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } ?: Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show()
    }

    private fun verVideo() {
        contactoSeleccionado?.video_url?.let { videoUrl ->
            val urlCompleta = if (!videoUrl.startsWith("http")) "$baseUrlLocal$videoUrl" else videoUrl
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("videoUrl", urlCompleta)
            startActivity(intent)
        } ?: Toast.makeText(this, "Selecciona un contacto con video", Toast.LENGTH_SHORT).show()
    }
}
