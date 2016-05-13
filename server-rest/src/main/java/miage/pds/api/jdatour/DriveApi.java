package miage.pds.api.jdatour;

import retrofit2.Call;
import retrofit2.http.*;

public interface DriveApi {
    public String BASE_URL = "https://www.googleapis.com";

    @GET("/drive/v3/files/{fileId}?alt=media")
    public Call<byte[]> downloadFile (@Header("Authorization") String authorization, @Path("fileId") String fileId);

    @POST("/upload/drive/v3/files?uploadType=media")
    public Call<UploadFileResponse> updloadFile (@Header("Authorization") String authorization, @Body byte[] datas);
}
