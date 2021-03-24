package oicar.burzaHumanosti.API

import oicar.burzaHumanosti.model.LoginResponse
import oicar.burzaHumanosti.model.SignInCredentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApplicationApi {
    @POST("account/login")
    fun signIn(@Body credentials: SignInCredentials) : Call<LoginResponse>
}

class RetrofitInstance {
    companion object {
        private const val BASE_URL: String = "https://api.bh-app.xyz/"

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        fun getRetrofitInstance(): ApplicationApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApplicationApi::class.java)
        }
    }
}