package oicar.burzaHumanosti.API

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import oicar.burzaHumanosti.BurzaHumanostiApp
import oicar.burzaHumanosti.model.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApplicationApi {
    @POST("account/login")
    fun signIn(@Body credentials: SignInCredentials): Call<LoginResponse>

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("condition")
    fun getConditions(): Call<List<Condition>>

    @GET("delivery/type")
    fun getDeliveryTypes(): Call<List<DeliveryType>>

    @Multipart
    @POST("article")
    fun postArticle(
        @Part imageFile: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("subCategoryId") subCategoryId: RequestBody,
        @Part("articleConditionId") articleConditionId: RequestBody,
        @Part("deliveryTypeId") deliveryTypeId: RequestBody
    ): Call<Article>

    @POST("demand")
    fun postDemand(@Body demand: Demand): Call<Demand>
}

class RetrofitInstance {

    companion object {

        private const val BASE_URL: String = "https://api.bh-app.xyz/"

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(JwtInterceptor())
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

class JwtInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val prefs: SharedPreferences =
            BurzaHumanostiApp.applicationContext().getSharedPreferences("loginPref", MODE_PRIVATE)
        val token = prefs.getString("access_token", null)

        var request = chain.request()

        token?.let {
            request = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $it")
                .build()
        }

        return chain.proceed(request)
    }

}