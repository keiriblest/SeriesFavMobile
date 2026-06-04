package com.seriesfav.mobile.data.network

import com.google.gson.*
import com.seriesfav.mobile.data.model.VerItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class VerItemListDeserializer : JsonDeserializer<List<VerItem>> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): List<VerItem> {
        return when {
            // verURL es un String directo (no array)
            json.isJsonPrimitive -> listOf(VerItem(url = json.asString))

            // verURL es un array
            json.isJsonArray -> json.asJsonArray.map { elem ->
                when {
                    // Elemento es String
                    elem.isJsonPrimitive -> VerItem(url = elem.asString)
                    // Elemento es Objeto {temporada, capitulo, url}
                    elem.isJsonObject -> {
                        val obj = elem.asJsonObject
                        VerItem(
                            temporada = obj.get("temporada")?.asString ?: "",
                            capitulo  = obj.get("capitulo")?.asString ?: "",
                            url       = obj.get("url")?.asString ?: ""
                        )
                    }
                    else -> VerItem()
                }
            }
            else -> emptyList()
        }
    }
}

object RetrofitInstance {
    private val gson = GsonBuilder()
        .registerTypeHierarchyAdapter(
            List::class.java,
            VerItemListDeserializer()
        )
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val api: GistApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GistApi::class.java)
    }
}
