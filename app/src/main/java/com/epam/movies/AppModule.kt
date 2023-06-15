package com.epam.movies

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import com.epam.movies.data.network.MoviesApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(20, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(20, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(HttpUrl.Builder().scheme("https").host(BuildConfig.API_HOST).build())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideMoviesApi(
        retrofit: Retrofit
    ): MoviesApi = retrofit.create()

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
    ): ImageLoader {
        val diskCacheFactory = {
            val rootDir = context.externalCacheDir ?: context.cacheDir
            DiskCache.Builder()
                .directory(rootDir.resolve("images"))
                .build()
        }
        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient.newBuilder().cache(null).build())
            .interceptorDispatcher(Dispatchers.Default)
            .fetcherDispatcher(Dispatchers.IO)
            .decoderDispatcher(Dispatchers.Default)
            .transformationDispatcher(Dispatchers.Default)
            .diskCache(diskCacheFactory)
            .build()
    }
}