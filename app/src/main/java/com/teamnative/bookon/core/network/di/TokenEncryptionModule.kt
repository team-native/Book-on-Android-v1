package com.teamnative.bookon.core.network.di

import com.teamnative.bookon.core.network.auth.TokenCipher
import com.teamnative.bookon.core.network.auth.TokenEncryption
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenEncryptionModule {
    @Binds
    abstract fun bindTokenEncryption(tokenCipher: TokenCipher): TokenEncryption
}
