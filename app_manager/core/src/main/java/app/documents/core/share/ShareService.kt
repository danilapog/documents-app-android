package app.documents.core.share

import app.documents.core.network.ApiContract
import app.documents.core.network.models.Base
import app.documents.core.network.models.share.request.RequestDeleteShare
import app.documents.core.network.models.share.request.RequestExternal
import app.documents.core.network.models.share.request.RequestShare
import app.documents.core.network.models.share.response.ResponseExternal
import app.documents.core.network.models.share.response.ResponseGroups
import app.documents.core.network.models.share.response.ResponseShare
import app.documents.core.network.models.share.response.ResponseUsers
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ShareService {

    /*
     * Get share folder
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @GET("api/" + ApiContract.API_VERSION + "/files/folder/{folder_id}/share" + ApiContract.RESPONSE_FORMAT)
    fun getShareFolder(@Path(value = "folder_id") folderId: String): Observable<ResponseShare>

    /*
     * Get share file
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @GET("api/" + ApiContract.API_VERSION + "/files/file/{file_id}/share" + ApiContract.RESPONSE_FORMAT)
    fun getShareFile(@Path(value = "file_id") fileId: String): Observable<ResponseShare>

    /*
     * Get external link
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @PUT("api/" + ApiContract.API_VERSION + "/files/{file_id}/sharedlink" + ApiContract.RESPONSE_FORMAT)
    fun getExternalLink(
        @Path(value = "file_id") fileId: String,
        @Body body: RequestExternal
    ): Observable<Response<ResponseExternal>>

    /*
     * Set access for folder
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @PUT("api/" + ApiContract.API_VERSION + "/files/folder/{folder_id}/share" + ApiContract.RESPONSE_FORMAT)
    fun setFolderAccess(
        @Path(value = "folder_id") fileId: String,
        @Body body: RequestShare
    ): Observable<ResponseShare>

    /*
     * Set access for file
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @PUT("api/" + ApiContract.API_VERSION + "/files/file/{file_id}/share" + ApiContract.RESPONSE_FORMAT)
    fun setFileAccess(
        @Path(value = "file_id") fileId: String,
        @Body body: RequestShare
    ): Observable<ResponseShare>

    /*
     * Delete share setting
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @HTTP(
        method = "DELETE",
        path = "api/" + ApiContract.API_VERSION + "/files/share" + ApiContract.RESPONSE_FORMAT,
        hasBody = true
    )
    fun deleteShare(
        @Header(ApiContract.HEADER_AUTHORIZATION) token: String,
        @Body body: RequestDeleteShare
    ): Observable<Base>

    /*
     * Get groups
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @GET("api/" + ApiContract.API_VERSION + "/group" + ApiContract.RESPONSE_FORMAT)
    fun getGroups(
        @QueryMap options: Map<String, String> = mapOf()
    ): Observable<ResponseGroups>

    /*
     * Get users
     * */
    @Headers(
        ApiContract.HEADER_CONTENT_TYPE + ": " + ApiContract.VALUE_CONTENT_TYPE,
        ApiContract.HEADER_ACCEPT + ": " + ApiContract.VALUE_ACCEPT
    )
    @GET("api/" + ApiContract.API_VERSION + "/people" + ApiContract.RESPONSE_FORMAT)
    fun getUsers(
        @QueryMap options: Map<String, String> = mapOf()
    ): Observable<ResponseUsers>

}