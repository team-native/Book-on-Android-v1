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
import com.teamnative.bookon.feature.community.data.CommunityAuthenticatedApiService
import com.teamnative.bookon.feature.community.data.CommunityPublicApiService
import com.teamnative.bookon.feature.fcm.data.FcmApiService
import com.teamnative.bookon.feature.notification.data.NotificationApiService
import com.teamnative.bookon.feature.oauth.data.OAuthAuthenticatedApiService
import com.teamnative.bookon.feature.oauth.data.OAuthPublicApiService
import com.teamnative.bookon.feature.school.data.SchoolApiService
import com.teamnative.bookon.feature.system.data.HealthApiService
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
    @Named(UnauthenticatedClient)
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
    @Named(PublicRetrofit)
    fun providePublicRetrofit(
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

    @Provides @Singleton
    fun provideAuthApiService(@Named(PublicRetrofit) retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)
    @Provides @Singleton
    fun provideRead365ApiService(retrofit: Retrofit): Read365ApiService = retrofit.create(Read365ApiService::class.java)
    @Provides @Singleton
    fun provideBookPublicApiService(
        @Named(PublicRetrofit) retrofit: Retrofit,
    ): BookPublicApiService = retrofit.create(BookPublicApiService::class.java)
    @Provides @Singleton
    fun provideBookAuthenticatedApiService(
        retrofit: Retrofit,
    ): BookAuthenticatedApiService = retrofit.create(BookAuthenticatedApiService::class.java)
    @Provides @Singleton
    fun provideMyApiService(retrofit: Retrofit): MyApiService = retrofit.create(MyApiService::class.java)
    @Provides @Singleton
    fun provideHomeApiService(@Named(PublicRetrofit) retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)
    @Provides @Singleton
    fun provideMarathonApiService(retrofit: Retrofit): MarathonApiService = retrofit.create(MarathonApiService::class.java)
    @Provides @Singleton
    fun provideRankingApiService(@Named(PublicRetrofit) retrofit: Retrofit): RankingApiService = retrofit.create(RankingApiService::class.java)
    @Provides @Singleton
    fun provideHealthApiService(@Named(PublicRetrofit) retrofit: Retrofit): HealthApiService = retrofit.create(HealthApiService::class.java)
    @Provides @Singleton
    fun provideSchoolApiService(@Named(PublicRetrofit) retrofit: Retrofit): SchoolApiService = retrofit.create(SchoolApiService::class.java)
    @Provides @Singleton
    fun provideOAuthAuthenticatedApiService(retrofit: Retrofit): OAuthAuthenticatedApiService = retrofit.create(OAuthAuthenticatedApiService::class.java)
    @Provides @Singleton
    fun provideOAuthPublicApiService(@Named(PublicRetrofit) retrofit: Retrofit): OAuthPublicApiService = retrofit.create(OAuthPublicApiService::class.java)
    @Provides @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService = retrofit.create(NotificationApiService::class.java)
    @Provides @Singleton
    fun provideFcmApiService(retrofit: Retrofit): FcmApiService = retrofit.create(FcmApiService::class.java)
    @Provides @Singleton
    fun provideCommunityAuthenticatedApiService(retrofit: Retrofit): CommunityAuthenticatedApiService = retrofit.create(CommunityAuthenticatedApiService::class.java)
    @Provides @Singleton
    fun provideCommunityPublicApiService(@Named(PublicRetrofit) retrofit: Retrofit): CommunityPublicApiService = retrofit.create(CommunityPublicApiService::class.java)

    private const val UnauthenticatedClient = "unauthenticated_client"
    private const val RefreshRetrofit = "refresh_retrofit"
    private const val PublicRetrofit = "public_retrofit"
    private val JsonContentType = "application/json".toMediaType()
}
