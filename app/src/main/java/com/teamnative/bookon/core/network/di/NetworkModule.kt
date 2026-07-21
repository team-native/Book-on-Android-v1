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
    @Named(UnauthenticatedClient)
    fun provideUnauthenticatedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideAuthenticatedOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        sessionAuthenticator: SessionAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authorizationInterceptor)
        .authenticator(sessionAuthenticator)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @Named(RefreshRetrofit)
    fun provideRefreshRetrofit(
        @Named(UnauthenticatedClient) okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(JsonContentType))
        .build()

    @Provides
    @Singleton
    fun provideTokenRefreshApiService(
        @Named(RefreshRetrofit) retrofit: Retrofit,
    ): TokenRefreshApiService = retrofit.create(TokenRefreshApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(JsonContentType))
        .build()

    private const val UnauthenticatedClient = "unauthenticated_client"
    private const val RefreshRetrofit = "refresh_retrofit"
    private val JsonContentType = "application/json".toMediaType()
}
