package com.shv.android.meetingreminder.data.network.api

import com.shv.android.meetingreminder.data.network.model.ClientsListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getContacts(
        @Query(QUERY_PARAM_INCLUDE) include: String = PARAM_INCLUDES,
        @Query(QUERY_PARAM_NOINFO) noinfo: String = "noinfo",
        @Query(QUERY_PARAM_RESULTS) results: Int = 15
    ): ClientsListDto

    companion object {
        private const val QUERY_PARAM_INCLUDE = "?inc="
        private const val PARAM_INCLUDES = "name,email,picture"
        private const val QUERY_PARAM_NOINFO = "noinfo"
        private const val QUERY_PARAM_RESULTS = "results"
    }
}