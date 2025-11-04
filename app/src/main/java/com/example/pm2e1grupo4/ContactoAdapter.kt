package com.example.pm2e1grupo4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pm2e1grupo4.databinding.ItemContactoBinding
import java.util.*

class ContactoAdapter(
    private var items: MutableList<Contacto>,
    private val listener: OnActionListener
) : RecyclerView.Adapter<ContactoAdapter.VH>() {

    private val original = ArrayList<Contacto>(items)

    interface OnActionListener {
        fun onPlay(contacto: Contacto)
        fun onMap(contacto: Contacto)
        fun onEdit(contacto: Contacto)
        fun onDelete(contacto: Contacto)
    }

    fun setData(newItems: MutableList<Contacto>) {
        items = newItems
        original.clear()
        original.addAll(newItems)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val q = query.trim().lowercase(Locale.getDefault())
        items.clear()
        if (q.isEmpty()) {
            items.addAll(original)
        } else {
            for (c in original) {
                if (!c.nombre.isNullOrEmpty() && c.nombre.lowercase(Locale.getDefault()).contains(q)) {
                    items.add(c)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemContactoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemContactoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contacto: Contacto) {
            binding.tvNombre.text = contacto.nombre ?: "Sin nombre"
            binding.tvLatLng.text = if (contacto.lat != null && contacto.lng != null)
                "Lat: ${contacto.lat}, Lng: ${contacto.lng}" else "Sin coordenadas"

            binding.btnPlay.setOnClickListener { listener.onPlay(contacto) }
            binding.btnMap.setOnClickListener { listener.onMap(contacto) }
            binding.btnEdit.setOnClickListener { listener.onEdit(contacto) }
            binding.btnDelete.setOnClickListener { listener.onDelete(contacto) }
        }
    }
}
