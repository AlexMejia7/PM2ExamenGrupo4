package com.example.pm2e1grupo4

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("insertar.php")
    fun uploadContacto(
        @Part("nombre") nombre: RequestBody,
        @Part("telefono") telefono: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lng") lng: RequestBody,
        @Part imagen: MultipartBody.Part?,
        @Part video: MultipartBody.Part?
    ): Call<ResponseData>

    @GET("listar.php")
    fun listar(): Call<List<Contacto>>

    @FormUrlEncoded
    @POST("actualizar.php")
    fun actualizar(
        @Field("id") id: Int,
        @Field("nombre") nombre: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String
    ): Call<ResponseData>

    @GET("eliminar.php")
    fun eliminar(@Query("id") id: Int): Call<ResponseData>
}
