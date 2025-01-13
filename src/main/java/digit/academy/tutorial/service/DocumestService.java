package digit.academy.tutorial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.util.DocumentUploadUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.egov.common.contract.models.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static digit.academy.tutorial.config.ServiceConstants.*;

@Service
public class DocumestService {


    private final ObjectMapper objectMapper;

    @Autowired
    ADConfiguration configuration;

    @Autowired
    private DocumentUploadUtil documentUploadUtil;

    public DocumestService(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

    }

    /**
     * Processes and uploads a list of files and associates them with an Advocate.
     *
     * - Checks if the provided list of files is empty; if yes, it returns early.
     * - Uploads the files using the `uploadDocuments` method.
     * - Associates the uploaded documents with the Advocate object.
     *
     * @param files   the list of files to be uploaded.
     * @param advocate the Advocate object to associate the uploaded documents with.
     */

    public void handleDocuments(List<MultipartFile> files, Advocate advocate) {
        if (ObjectUtils.isEmpty(files)) {

            return; // No files to process, return early
        }
        // Call the existing uploadDocuments method
        List<Document> documents = uploadDocuments(files, advocate, configuration.getModuleName()

        );

        // Set the uploaded documents to the Advocate object
        advocate.setDocuments(documents);
    }


    /**
     * Uploads files to a remote server and returns the corresponding Document objects.
     *
     * - Iterates over the files to upload them one by one.
     * - Handles errors during the upload process and throws an exception if a file upload fails.
     * - Extracts the `fileStoreId` from the server response and creates Document objects for each file.
     *
     * @param files    the list of files to be uploaded.
     * @param advocate the Advocate object to associate the uploaded documents with.
     * @param moduleName the module name used during the file upload process.
     * @return a list of Document objects representing the uploaded files.
     */
    public List<Document> uploadDocuments(List<MultipartFile> files, Advocate advocate, String moduleName) {

        if (ObjectUtils.isEmpty(files)) {
            return new ArrayList<>();
        }

        List<Document> documents = new ArrayList<>();
        int fileIndex = 0;
        // Iterate through the documents in the Advocate object
        for (int i = 0; i < advocate.getDocuments().size(); i++) {
            MultipartFile file = files.get(fileIndex);
            try {

                // Upload the file to the server
                Response uploadResponse = documentUploadUtil.uploadFile(file, advocate.getTenantId(), moduleName, advocate.getApplicationNumber());

                if (!uploadResponse.isSuccessful()) {
                    throw new IOException(FILE_UPLOAD_FAILED + file.getOriginalFilename());
                }
                // Parse the server response to extract file details
                JsonNode responseBody = objectMapper.readTree(uploadResponse.body().string());
                JsonNode filesArray = responseBody.path("files");

                if (filesArray.isEmpty()) {
                    throw new IOException(NO_FILE_DATA + file.getOriginalFilename());
                }
                // Create and add Document objects
                String fileStoreId = filesArray.get(0).path("fileStoreId").asText();
                for (Document doc : advocate.getDocuments()) {
                    Document document = Document.builder().fileStore(fileStoreId).id(UUID.randomUUID().toString()).documentType(doc.getDocumentType()).documentUid(doc.getDocumentUid()).additionalDetails(doc.getAdditionalDetails()).build();

                    documents.add(document);
                    fileIndex++;
                }

            } catch (Exception e) {
                throw new RuntimeException(ERROR_UPLOADING_FILE + file.getOriginalFilename(), e);
            }
        }

        return documents;
    }

    /**
     * Processes and uploads a list of files and associates them with an AdvocateClerk.
     *
     * - Checks if the provided list of files is empty; if yes, it returns early.
     * - Uploads the files using the `uploadClerkDocuments` method.
     * - Associates the uploaded documents with the AdvocateClerk object.
     *
     * @param files the list of files to be uploaded.
     * @param advocateClerk the AdvocateClerk object to associate the uploaded documents with.
     */
    public void handleClerkDocuments(List<MultipartFile> files, AdvocateClerk advocateClerk) {
        if (ObjectUtils.isEmpty(files)) {
            return; // No files to process, return early
        }

        // Call the existing uploadDocuments method
        List<Document> documents = uploadClerkDocuments(files, advocateClerk, configuration.getModuleName()

        );

        // Set the uploaded documents to the Advocate object
        advocateClerk.setDocuments(documents);
    }


    /**
     * Uploads files to a remote server and returns the corresponding Document objects for an AdvocateClerk.
     *
     * - Iterates over the files to upload them one by one.
     * - Handles errors during the upload process and throws an exception if a file upload fails.
     * - Extracts the `fileStoreId` from the server response and creates Document objects for each file.
     *
     * @param files the list of files to be uploaded.
     * @param advocateClerk the AdvocateClerk object to associate the uploaded documents with.
     * @param moduleName the module name used during the file upload process.
     * @return a list of Document objects representing the uploaded files.
     */
    public List<Document> uploadClerkDocuments(List<MultipartFile> files, AdvocateClerk advocateClerk, String moduleName) {
        if (ObjectUtils.isEmpty(files)) {
            return new ArrayList<>();
        }

        List<Document> documents = new ArrayList<>();
        int fileIndex = 0;
        // Iterate through the documents in the AdvocateClerk object
        for (int i = 0; i < advocateClerk.getDocuments().size(); i++) {
            MultipartFile file = files.get(fileIndex);
            try {
                // Upload the file to the server
                Response uploadResponse = documentUploadUtil.uploadFile(file, advocateClerk.getTenantId(), moduleName, advocateClerk.getApplicationNumber());

                if (!uploadResponse.isSuccessful()) {
                    throw new IOException(FILE_UPLOAD_FAILED+ file.getOriginalFilename());
                }
                // Parse the server response to extract file details
                JsonNode responseBody = objectMapper.readTree(uploadResponse.body().string());
                JsonNode filesArray = responseBody.path("files");

                if (filesArray.isEmpty()) {
                    throw new IOException(NO_FILE_DATA + file.getOriginalFilename());
                }
                // Create and add Document objects
                String fileStoreId = filesArray.get(0).path("fileStoreId").asText();
                for (Document doc : advocateClerk.getDocuments()) {
                    Document document = Document.builder().fileStore(fileStoreId).id(UUID.randomUUID().toString()).documentType(doc.getDocumentType()).documentUid(doc.getDocumentUid()).additionalDetails(doc.getAdditionalDetails()).build();

                    documents.add(document);
                    fileIndex++;
                }

            } catch (Exception e) {
                throw new RuntimeException(ERROR_UPLOADING_FILE + file.getOriginalFilename(), e);
            }
        }

        return documents;
    }


}
