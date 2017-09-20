package com.fisincorporated.speechtotext.retrofit;

import com.fisincorporated.speechtotext.googlespeech.objectaccess.ObjectAccessList;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.OperationResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GcsApi {

    /**
     * Upload (in this case audio) file to google cloud services
     *
     * @param bearerToken
     * @param contentType
     * @param file
     * @param name
     * @return
     */
    @Multipart
    @POST("upload/storage/v1/b/{bucket}/o?uploadType=media")
    Call<OperationResponse> uploadToGcs(
            @Path("bucket") String bucket
            , @Header("Authorization") String bearerToken
            //, @Header("Content-Length") long contentLength - added via multipartBody.part
            , @Header("Content-Type") String contentType
            , @Part MultipartBody.Part file
            , @Query(value = "name") String name);



    @DELETE("storage/v1/b/{bucket}/o/{object}")
    Call<OperationResponse> deleteFromGcs(
            @Path("bucket") String bucket
            , @Path("object") String object
            , @Header("Authorization") String bearerToken);


    //GET https://www.googleapis.com/storage/v1/b/bucket/o/object/acl/entity
    @GET("storage/v1/b/{bucket}/o/{object}/acl")
    Call<ObjectAccessList> getGcsObjectAccessList(
             @Path("bucket") String bucket
            , @Path("object") String object
            , @Header("Authorization") String bearerToken);

}
