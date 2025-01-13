package digit.academy.tutorial.service;

import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.enrichment.AdvocateClerkEnrichment;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.repository.AdvocateClerkRepository;
import digit.academy.tutorial.util.DocumentUploadUtil;
import digit.academy.tutorial.util.WorkflowUtil;
import digit.academy.tutorial.validators.AdvocateClerkValidator;
import digit.academy.tutorial.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class AdvocateClerkCreateService {

    @Autowired
    private AdvocateClerkValidator validator;

    @Autowired
    private ADConfiguration configuration;


    @Autowired
    AdvocateClerkEnrichment enrichmentUtil;


    @Autowired
    private DocumentUploadUtil documentUploadUtil;


    @Autowired
    private DocumestService documestService;


    @Autowired
    private Producer producer;

    @Autowired
    AdvocateClerkRepository advocateClerkRepository;


    @Autowired
    private WorkflowUtil workflowUtil;


    @Autowired
    WorkflowService workflowService;



    /**
     * Creates a new AdvocateClerk.
     * It validates, enriches, and processes the AdvocateClerk data, handles document uploads,
     * updates workflow status, and sends the request to the Kafka producer.
     *
     * @param advocateClerkRequest The request object containing the AdvocateClerk details.
     * @param files The list of files to be uploaded for the clerk.
     * @return A list of created AdvocateClerk objects.
     */
    public List<AdvocateClerk> createAdvocateClerk(AdvocateClerkRequest advocateClerkRequest, List<MultipartFile> files) {
        try {
            // Validating the AdvocateClerkRequest
            validator.validateAdvocateClerk(advocateClerkRequest);
            // Enriching the AdvocateClerkRequest with additional data
            enrichmentUtil.enrichAdvocateClerk(advocateClerkRequest);
            // Processing the first AdvocateClerk in the request
            AdvocateClerk advocateClerk = advocateClerkRequest.getClerks().get(0);
            // Handling document uploads for the AdvocateClerk
            documestService.handleClerkDocuments(files, advocateClerk);
            // Updating the workflow status of the Clerk
            workflowService.updateClerkWorkflowstatus(advocateClerkRequest);
            // Pushing the created Clerk application to the Kafka topic
            producer.push(configuration.getCreateClerkTopic(), advocateClerkRequest);
            // Returning the list of created AdvocateClerk objects
            return advocateClerkRequest.getClerks();
        } catch (Exception e) {
            // Handling any exceptions that occur during the process
            throw new RuntimeException(e);
        }
    }


    /**
     * Searches for AdvocateClerk based on search criteria.
     *
     * This method retrieves a list of AdvocateClerk from the repository using the specified search criteria.
     * If no advocates are found, an empty list is returned.
     *
     * @param requestInfo The request information.
     * @param criteria    The search criteria to filter AdvocateClerk.
     * @return A list of AdvocateClerk matching the search criteria.
     */
    public List<AdvocateClerk> searchAdvocateClerks(RequestInfo requestInfo, List<AdvocateClerkSearchCriteria> criteria) {
        try {
            // Fetching the AdvocateClerk from the repository based on the search criteria
            List<AdvocateClerk> advocateClerks = advocateClerkRepository.getClerkApplications(criteria);
            // Returning an empty list if no results were found
            if (CollectionUtils.isEmpty(advocateClerks))
                return new ArrayList<>();
            // Returning the list of matching AdvocateClerk objects
            return advocateClerks;
        } catch (Exception e) {
            // Handling any exceptions that occur during the search process
            throw new RuntimeException(e);
        }
    }


    /**
     * Updates an existing AdvocateClerk.
     *
     * This method validates the existing AdvocateClerk application, updates the workflow information,
     * enriches the AdvocateClerk data, and pushes the update request to a Kafka topic.
     *
     * @param advocateClerkRequest The AdvocateClerk request containing updated advocate details.
     * @return The updated AdvocateClerk.
     */
    public AdvocateClerk updateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {
        try {
            // Validating the existing AdvocateClerk application
            AdvocateClerk existingAdvocateClerk = validator.validateExistingClerkApplication(advocateClerkRequest.getClerks().get(0));
            // Setting the updated workflow status
            existingAdvocateClerk.setWorkflow(advocateClerkRequest.getClerks().get(0).getWorkflow());
            // Updating the AdvocateClerkRequest with the existing clerk
            advocateClerkRequest.setClerks(Collections.singletonList(existingAdvocateClerk));
            // Enriching the updated AdvocateClerk data
            enrichmentUtil.enrichAdvocateClerkOnUpdate(advocateClerkRequest);
            // Updating the workflow status after the update
            workflowService.updateClerkWorkflowstatus(advocateClerkRequest);
            // Pushing the update request to the Kafka topic
            producer.push(configuration.getUpdateTopic(), advocateClerkRequest);
            // Returning the updated AdvocateClerk object
            return advocateClerkRequest.getClerks().get(0);
        } catch (Exception e) {
            // Handling any exceptions that occur during the update process
            throw new RuntimeException(e);
        }
    }


}
