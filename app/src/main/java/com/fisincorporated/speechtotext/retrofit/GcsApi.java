package com.fisincorporated.speechtotext.retrofit;

import com.fisincorporated.speechtotext.googlespeech.speechresponse.OperationResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GcsApi {

    /**
     * Upload (in this case audio) file to google cloud services
     * @param bearerToken
     * @param contentType
     * @param file
     * @param name
     * @return
     */
    @Multipart
    @POST("speechtotext-f653f.appspot.com/o?uploadType=media")
    Call<OperationResponse> callGcsUpload(
              @Header("Authorization") String bearerToken
            //, @Header("Content-Length") long contentLength - added via multipartBody.part
            , @Header("Content-Type") String contentType
            , @Part MultipartBody.Part file
            , @Query(value = "name") String name);

}
