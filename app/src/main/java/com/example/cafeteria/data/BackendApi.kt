package com.example.cafeteria.data

import com.example.cafeteria.util.AppPreferences
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

private interface ApiService {
	@Multipart
	@POST("/recognize")
	suspend fun upload(@Part image: MultipartBody.Part): RequestIdResponse

	@GET("/result/{id}")
	suspend fun result(@Path("id") id: String): ResultResponse
}

data class RequestIdResponse(val requestId: String)

data class ResultResponse(val result: String)

object BackendApi {
	private fun retrofit(baseUrl: String): Retrofit {
		val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
		val client = OkHttpClient.Builder().addInterceptor(logging).build()
		return Retrofit.Builder()
			.baseUrl(baseUrl)
			.addConverterFactory(MoshiConverterFactory.create())
			.client(client)
			.build()
	}

	suspend fun uploadImage(bytes: ByteArray): String {
		val temp = File.createTempFile("upload", ".jpg")
		temp.writeBytes(bytes)
		val baseUrl = AppPreferences.serverUrlFlow().first().ifBlank { "http://10.0.2.2:8000" }
		val service = retrofit(ensureEndsWithSlash(baseUrl)).create(ApiService::class.java)
		val body = MultipartBody.Part.createFormData(
			name = "image",
			filename = temp.name,
			body = temp.asRequestBody("image/jpeg".toMediaType())
		)
		return service.upload(body).requestId
	}

	suspend fun fetchResult(requestId: String): String {
		val baseUrl = AppPreferences.serverUrlFlow().first().ifBlank { "http://10.0.2.2:8000" }
		val service = retrofit(ensureEndsWithSlash(baseUrl)).create(ApiService::class.java)
		return service.result(requestId).result
	}

	private fun ensureEndsWithSlash(url: String): String = if (url.endsWith('/')) url else "$url/"
}