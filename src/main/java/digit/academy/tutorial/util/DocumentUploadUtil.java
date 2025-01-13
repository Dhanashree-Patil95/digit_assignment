package digit.academy.tutorial.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.Configuration;
import okhttp3.*;
import org.egov.common.contract.models.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DocumentUploadUtil {

    private final OkHttpClient httpClient;
//    private final ObjectMapper objectMapper;
    private final Configuration configuration;

    public DocumentUploadUtil( ObjectMapper objectMapper, Configuration configuration) {
        this.httpClient =  new OkHttpClient.Builder().build();;
//        this.objectMapper = objectMapper;
        this.configuration = configuration;
    }


    public Response uploadFile(MultipartFile multipartFile, String tenantId, String module, String tag) throws IOException {
        MediaType mediaType = MediaType.parse("multipart/mixed");

        StringBuilder url = new StringBuilder(configuration.getFileStoreHost()).append(configuration.getFileStorePath());

        RequestBody fileBody = RequestBody.create(
                MediaType.parse(multipartFile.getContentType()),
                multipartFile.getBytes()
        );

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", multipartFile.getOriginalFilename(), fileBody)
                .addFormDataPart("tenantId", tenantId)
                .addFormDataPart("module", module)
                .addFormDataPart("tag", tag)
                .build();

        Request request = new Request.Builder()
                .url(String.valueOf(url))
                .post(body)
                .addHeader("Content-Type", "multipart/mixed")
                .build();

        return httpClient.newCall(request).execute();
    }

}
