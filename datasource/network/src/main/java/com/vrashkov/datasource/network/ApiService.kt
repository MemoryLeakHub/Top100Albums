package com.vrashkov.datasource.network

import com.vrashkov.datasource.network.response.MostPlayedRecordsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("us/music/most-played/{number_of_records}/albums.json")
    suspend fun mostPlayedRecords(
        @Path("number_of_records") numberOfRecords: Int
    ): Response<MostPlayedRecordsResponse>

}