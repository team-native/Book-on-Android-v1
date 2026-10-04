package com.teamnative.bookon.core.network.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.teamnative.bookon.core.network.ApiEnvelope
import com.teamnative.bookon.core.network.ApiExecutor
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SessionConcurrencyTest {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `old refresh success cannot replace a new login`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val expected = manager.snapshot.value
        val response = CompletableDeferred<Response<ApiEnvelope<RefreshTokenResponseDto>>>()
        val service = service(manager) {
            response.await()
        }
        val refresh = async {
            service.refresh(expected)
        }
        runCurrent()
        manager.clear()
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        response.complete(success("old-new"))
        assertEquals(
            TokenRefreshResult.Superseded,
            refresh.await()
        )
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `old refresh failure cannot clear a new login`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val expected = manager.snapshot.value
        val response = CompletableDeferred<Response<ApiEnvelope<RefreshTokenResponseDto>>>()
        val service = service(manager) {
            response.await()
        }
        val refresh = async {
            service.refresh(expected)
        }
        runCurrent()
        manager.clear()
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        response.complete(failure(4010))
        assertEquals(
            TokenRefreshResult.Superseded,
            refresh.await()
        )
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `same session rotates tokens without changing epoch`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val expected = manager.snapshot.value
        val service = service(manager) {
            success("new")
        }
        assertTrue(service.refresh(expected) is TokenRefreshResult.Success)
        assertEquals(
            expected.epoch,
            manager.snapshot.value.epoch
        )
        assertEquals(
            "new",
            manager.accessToken()
        )
        assertTrue(service.refresh(expected) is TokenRefreshResult.Success)
    }

    @Test
    fun `only documented invalid JWT clears the current session`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        assertTrue(service(manager) {
                failure(4013)
            }.refresh(manager.snapshot.value) is TokenRefreshResult.RetryableFailure)
        assertEquals(
            "a",
            manager.accessToken()
        )
        assertEquals(
            TokenRefreshResult.InvalidToken,
            service(manager) {
                failure(4010)
            }.refresh(manager.snapshot.value)
        )
        assertNull(manager.accessToken())
    }

    @Test
    fun `restore never reloads storage over a newer login`() = runTest {
        val manager = manager()
        manager.restore()
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        val expected = manager.snapshot.value
        assertEquals(
            "b",
            manager.restore()?.accessToken
        )
        assertEquals(
            expected.epoch,
            manager.snapshot.value.epoch
        )
    }

    @Test
    fun `late login and expiry writes are refused`() = runTest {
        val manager = manager()
        manager.restore()
        val expected = manager.snapshot.value
        manager.clear()
        assertFalse(manager.loginIfCurrent(
                expected,
                AuthTokens(
                    "a",
                    "ar"
                )
        ))
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        assertFalse(manager.clearIfCurrent(expected))
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `logout removes local tokens before cleanup and allows login while registration waits`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val cleanup = manager.beginLogout()
        assertNull(manager.accessToken())
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        val waiter = async {
            manager.awaitCleanup()
        }
        runCurrent()
        assertFalse(waiter.isCompleted)
        cleanup.dispose()
        manager.finishCleanup(cleanup)
        waiter.await()
        assertNull(cleanup.accessToken())
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `external and unknown 401 preserve response body and never refresh`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        var requests = 0
        val authenticator = SessionAuthenticator(
            manager,
            service(manager) {
                requests++
                success("new")
            },
            json
        )
        for (body in listOf(
                "{\"errorCode\":4012}",
                "{\"errorCode\":4013}",
                "bad json",
                "{}"
        )) {
            val response = unauthorized(
                manager.snapshot.value,
                body
            )
            assertNull(authenticator.authenticate(
                    null,
                    response
            ))
            assertEquals(
                body,
                response.body?.string()
            )
        }
        assertEquals(
            0,
            requests
        )
        assertEquals(
            "a",
            manager.accessToken()
        )
    }

    @Test
    fun `old account 401 never retries with new account token`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val response = unauthorized(
            manager.snapshot.value,
            "{\"errorCode\":4010}"
        )
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        var requests = 0
        val authenticator = SessionAuthenticator(
            manager,
            service(manager) {
                requests++
                success("new")
            },
            json
        )
        assertNull(authenticator.authenticate(
                null,
                response
        ))
        assertEquals(
            0,
            requests
        )
    }

    @Test
    fun `rotated token retry keeps the same session snapshot`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val original = manager.snapshot.value
        val response = unauthorized(
            original,
            "{\"errorCode\":4010}"
        )
        manager.saveIfCurrent(
            original,
            AuthTokens(
                "rotated",
                "rotated-r"
            )
        )
        var requests = 0
        val authenticator = SessionAuthenticator(
            manager,
            service(manager) {
                requests++
                success("new")
            },
            json
        )
        val retry = authenticator.authenticate(
            null,
            response
        )
        assertEquals(
            "Bearer rotated",
            retry?.header("Authorization")
        )
        assertEquals(
            "rotated",
            retry?.tag(SessionSnapshot::class.java)?.tokens?.accessToken
        )
        assertEquals(
            0,
            requests
        )
    }

    @Test
    fun `cancelled cleanup completes the registration barrier`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val started = CompletableDeferred<Unit>()
        val logout = launch {
            val cleanup = manager.beginLogout()
            started.complete(Unit)
            try {
                awaitCancellation()
            } finally {
                cleanup.dispose()
                manager.finishCleanup(cleanup)
            }
        }
        started.await()
        logout.cancel()
        logout.join()
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        manager.awaitCleanup()
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `failed token write never publishes authentication or overwrites the old snapshot`() = runTest {
        val storage = MemoryPreferences()
        val manager = manager(storage)
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val expected = manager.snapshot.value
        storage.failWrites = true
        try {
            manager.loginIfCurrent(
                expected,
                AuthTokens(
                    "b",
                    "br"
                )
            )
            fail("expected storage failure")
        } catch (exception: SessionStorageException) {
            assertEquals(
                "a",
                manager.accessToken()
            )
            assertEquals(
                expected.epoch,
                manager.snapshot.value.epoch
            )
        }
    }

    @Test
    fun `restore storage failure allows a genuine storage retry`() = runTest {
        val storage = MemoryPreferences().apply {
            failWrites = true
        }
        val manager = manager(storage)
        try {
            manager.restore()
            fail("expected storage failure")
        } catch (exception: SessionStorageException) {
            assertNull(manager.accessToken())
        }
        storage.failWrites = false
        manager(storage).save(AuthTokens(
                "persisted",
                "persisted-r"
        ))
        assertEquals(
            "persisted",
            manager.restore()?.accessToken
        )
    }

    @Test
    fun `repeated JWT rejection expires only the request session`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val original = unauthorized(
            manager.snapshot.value,
            "{\"errorCode\":4010}"
        )
        val repeated = original.newBuilder().priorResponse(original.newBuilder().body(null).build()).build()
        val authenticator = SessionAuthenticator(
            manager,
            service(manager) {
                success("new")
            },
            json
        )
        assertNull(authenticator.authenticate(
                null,
                repeated
        ))
        assertNull(manager.accessToken())
        manager.save(AuthTokens(
                "b",
                "br"
        ))
        assertNull(authenticator.authenticate(
                null,
                repeated
        ))
        assertEquals(
            "b",
            manager.accessToken()
        )
    }

    @Test
    fun `redirect before first JWT failure still refreshes rather than expiring`() = runTest {
        val manager = manager()
        manager.save(AuthTokens(
                "a",
                "ar"
        ))
        val unauthorized = unauthorized(
            manager.snapshot.value,
            "{\"errorCode\":4010}"
        )
        val redirected = unauthorized.newBuilder().code(302).message("Redirect").body(null).build()
        val response = unauthorized.newBuilder().priorResponse(redirected).build()
        var refreshes = 0
        val authenticator = SessionAuthenticator(
            manager,
            service(manager) {
                refreshes++
                success("new")
            },
            json
        )
        val retry = authenticator.authenticate(
            null,
            response
        )
        assertEquals(
            1,
            refreshes
        )
        assertEquals(
            "Bearer new",
            retry?.header("Authorization")
        )
        assertEquals(
            "new",
            manager.accessToken()
        )
    }

    private fun manager(storage: MemoryPreferences = MemoryPreferences()) = TokenSessionManager(storage, object : TokenEncryption {
            override fun encrypt(plainText: String) = "encrypted:$plainText"
            override fun decrypt(cipherText: String) = cipherText.removePrefix("encrypted:")
    })

    private fun service(
        manager: TokenSessionManager,
        response: suspend () -> Response<ApiEnvelope<RefreshTokenResponseDto>>
    ) =
    TokenRefreshService(object : TokenRefreshApiService {
            override suspend fun refresh(request: RefreshTokenRequestDto) = response()
        }, ApiExecutor(json), manager)

    private fun success(token: String) = Response.success(ApiEnvelope(
            0,
            "ok",
            RefreshTokenResponseDto(
                token,
                "$token-r"
            )
    ))
    private fun failure(code: Int): Response<ApiEnvelope<RefreshTokenResponseDto>> =
    Response.error(
        401,
        "{\"errorCode\":$code,\"message\":\"invalid\"}".toResponseBody("application/json".toMediaType())
    )

    private fun unauthorized(
        snapshot: SessionSnapshot,
        body: String
    ): okhttp3.Response = okhttp3.Response.Builder()
    .request(Request.Builder().url("https://example.com/me")
        .header(
            "Authorization",
            "Bearer ${snapshot.tokens?.accessToken}"
        )
        .tag(
            SessionSnapshot::class.java,
            snapshot
        ).build())
    .protocol(Protocol.HTTP_1_1).code(401).message("Unauthorized")
    .body(body.toResponseBody("application/json".toMediaType())).build()
}

private class MemoryPreferences : DataStore<Preferences> {
    var failWrites = false
    override val data = MutableStateFlow(emptyPreferences())
    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        if (failWrites) {
            throw java.io.IOException("disk unavailable")
        }
        val updated = transform(data.value)
        data.value = updated
        return updated
    }
}
