package com.teamnative.bookon.core.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamnative.bookon.BuildConfig
import com.teamnative.bookon.core.network.auth.AuthorizationInterceptor
import com.teamnative.bookon.core.network.auth.SessionAuthenticator
import com.teamnative.bookon.core.network.auth.TokenRefreshApiService
import com.teamnative.bookon.feature.auth.data.AuthApiService
import com.teamnative.bookon.feature.auth.data.Read365ApiService
import com.teamnative.bookon.feature.book.data.BookAuthenticatedApiService
import com.teamnative.bookon.feature.book.data.BookPublicApiService
import com.teamnative.bookon.feature.my.data.MyApiService
import com.teamnative.bookon.feature.home.data.HomeApiService
import com.teamnative.bookon.feature.marathon.data.MarathonApiService
import com.teamnative.bookon.feature.ranking.data.RankingApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val Context.authDataStore by preferencesDataStore(name = "bookon_auth_session")

/** Retrofit·OkHttp·DataStore를 앱 전체에서 일관되게 제공한다. */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext applicationContext: Context,
    ): DataStore<Preferences> = applicationContext.authDataStore

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.ENABLE_NETWORK_LOG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    @Named(UNAUTHENTICATED_CLIENT)
    fun provideUnauthenticatedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .readTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .writeTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @Named(AUTHENTICATED_CLIENT)
    fun provideAuthenticatedOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        sessionAuthenticator: SessionAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .readTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .writeTimeout(BuildConfig.NETWORK_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .addInterceptor(authorizationInterceptor)
        .authenticator(sessionAuthenticator)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @Named(REFRESH_RETROFIT)
    fun provideRefreshRetrofit(
        @Named(UNAUTHENTICATED_CLIENT) okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(jsonContentType))
        .build()

    @Provides
    @Singleton
    @Named(PUBLIC_RETROFIT)
    fun providePublicRetrofit(
        @Named(UNAUTHENTICATED_CLIENT) okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(jsonContentType))
        .build()

    @Provides
    @Singleton
    fun provideTokenRefreshApiService(
        @Named(REFRESH_RETROFIT) retrofit: Retrofit,
    ): TokenRefreshApiService = retrofit.create(TokenRefreshApiService::class.java)

    @Provides
    @Singleton
    @Named(AUTHENTICATED_RETROFIT)
    fun provideAuthenticatedRetrofit(
        @Named(AUTHENTICATED_CLIENT) okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(jsonContentType))
        .build()

    @Provides
    @Singleton
    fun provideAuthApiService(
        @Named(PUBLIC_RETROFIT) retrofit: Retrofit,
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideRead365ApiService(
        @Named(AUTHENTICATED_RETROFIT) retrofit: Retrofit,
    ): Read365ApiService = retrofit.create(Read365ApiService::class.java)

    @Provides
    @Singleton
    fun provideBookPublicApiService(
        @Named(PUBLIC_RETROFIT) retrofit: Retrofit,
    ): BookPublicApiService = retrofit.create(BookPublicApiService::class.java)
    @Provides
    @Singleton
    fun provideBookAuthenticatedApiService(
        @Named(AUTHENTICATED_RETROFIT) retrofit: Retrofit,
    ): BookAuthenticatedApiService = retrofit.create(BookAuthenticatedApiService::class.java)

    @Provides
    @Singleton
    fun provideMyApiService(
        @Named(AUTHENTICATED_RETROFIT) retrofit: Retrofit,
    ): MyApiService = retrofit.create(MyApiService::class.java)

    @Provides
    @Singleton
    fun provideHomeApiService(
        @Named(PUBLIC_RETROFIT) retrofit: Retrofit,
    ): HomeApiService = retrofit.create(HomeApiService::class.java)

    @Provides
    @Singleton
    fun provideMarathonApiService(
        @Named(AUTHENTICATED_RETROFIT) retrofit: Retrofit,
    ): MarathonApiService = retrofit.create(MarathonApiService::class.java)

    @Provides
    @Singleton
    fun provideRankingApiService(
        @Named(PUBLIC_RETROFIT) retrofit: Retrofit,
    ): RankingApiService = retrofit.create(RankingApiService::class.java)

    private const val AUTHENTICATED_CLIENT = "authenticated_client"
    private const val AUTHENTICATED_RETROFIT = "authenticated_retrofit"
    private const val UNAUTHENTICATED_CLIENT = "unauthenticated_client"
    private const val REFRESH_RETROFIT = "refresh_retrofit"
    private const val PUBLIC_RETROFIT = "public_retrofit"
    private val jsonContentType = "application/json".toMediaType()
}
