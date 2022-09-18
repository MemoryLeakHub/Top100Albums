package com.vrashkov.domain

import app.cash.turbine.test
import com.vrashkov.core.base.DataState
import com.vrashkov.domain.model.AlbumsResult
import com.vrashkov.domain.repository.AlbumsRepositoryLocal
import com.vrashkov.domain.repository.AlbumsRepositoryRemote
import com.vrashkov.domain.usecase.GetAlbumsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAlbumsUseCaseTest {

    @MockK
    private lateinit var albumsRepositoryLocal: AlbumsRepositoryLocal

    @MockK
    private lateinit var albumsRepositoryRemote: AlbumsRepositoryRemote

    private lateinit var useCase: GetAlbumsUseCase
    private val numberOfRecords = 100
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private fun produceUseCase() = GetAlbumsUseCase(
        albumsRepositoryLocal = albumsRepositoryLocal,
        albumsRepositoryRemote = albumsRepositoryRemote
    )

    @Test
    fun `on successfully returned records`() = runBlockingTest {
        val fakeSuccessData = AlbumsResult()
        coEvery {
            albumsRepositoryRemote.mostPlayedRecords(numberOfRecords)
        } returns RequestResult.Success(fakeSuccessData)

        coEvery {
            albumsRepositoryLocal.deleteAll()
        } returns RequestResult.Success(true)

        coEvery {
            albumsRepositoryLocal.insertAll(any())
        } returns RequestResult.Success(true)
        useCase = produceUseCase()

        useCase.executeRemote(numberOfRecords).test {
            val firstValue = awaitItem()
            assertTrue(firstValue is DataState.Loading)

            val secondValue = awaitItem()
            assertTrue(secondValue is DataState.Data)
            assertTrue((secondValue as DataState.Data).data == fakeSuccessData)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { albumsRepositoryRemote.mostPlayedRecords(numberOfRecords) }
    }


    @Test
    fun `on api request failure`() = runBlockingTest {
        val fakeException = Exception()
        coEvery {
            albumsRepositoryRemote.mostPlayedRecords(numberOfRecords)
        } returns RequestResult.Error(fakeException)

        useCase = produceUseCase()

        useCase.executeRemote(numberOfRecords).test {
            val firstValue = awaitItem()
            assertTrue(firstValue is DataState.Loading)

            val secondValue = awaitItem()
            assertTrue(secondValue is DataState.Error)
            assertTrue((secondValue as DataState.Error).error == fakeException)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { albumsRepositoryRemote.mostPlayedRecords(numberOfRecords) }
    }
}