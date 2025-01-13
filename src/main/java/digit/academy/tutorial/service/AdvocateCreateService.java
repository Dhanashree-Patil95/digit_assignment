package digit.academy.tutorial.service;

import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.enrichment.AdvocateEnrichment;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.util.DocumentUploadUtil;
import digit.academy.tutorial.util.WorkflowUtil;
import digit.academy.tutorial.validators.AdvocateValidator;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
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
public class AdvocateCreateService {


    @Autowired
    private AdvocateValidator validator;


    @Autowired
    private AdvocateEnrichment enrichmentUtil;

    @Autowired
    private Producer producer;

    @Autowired
    private ADConfiguration configuration;

    @Autowired
    private AdvocateRepository advocateRepository;

    @Autowired
    private DocumentUploadUtil documentUploadUtil;

    @Autowired
    private DocumestService documestService;

    @Autowired
    private WorkflowUtil workflowUtil;


    @Autowired
    WorkflowService workflowService;



    /**
     * Creates a new advocate.
     *
     * This method validates the advocate request, enriches the advocate with application details,
     * uploads associated documents, updates workflow status, and pushes the request to a Kafka topic for persistence.
     *
     * @param advocateRequest The advocate request containing advocate details.
     * @param files           The list of documents to be uploaded.
     * @return A list of created advocates.
     */
    public List<Advocate> createAdvocate(AdvocateRequest advocateRequest, List<MultipartFile> files) {

        try {
            //validates the advocate request
            validator.validateAdvocate(advocateRequest);
            //enriches advocate with applicationNumber created using Idgen service and auditDetails
            enrichmentUtil.enrichAdvocate(advocateRequest);
            Advocate advocate = advocateRequest.getAdvocates().get(0);
//            documestService.handleDocuments(files, advocate);
            //updates the workflow status of the advocate registration request
//            workflowService.updateWorkflowstatus(advocateRequest);
            // Push the advocate request to the topic for persister to listen and persist
            producer.push(configuration.getCreateTopic(), advocateRequest);

            return advocateRequest.getAdvocates();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Searches for advocates based on search criteria.
     *
     * This method retrieves a list of advocates from the repository using the specified search criteria.
     * If no advocates are found, an empty list is returned.
     *
     * @param requestInfo The request information.
     * @param criteria    The search criteria to filter advocates.
     * @return A list of advocates matching the search criteria.
     */
    public List<Advocate> searchAdvocates(RequestInfo requestInfo, List<AdvocateSearchCriteria> criteria) {

        try {
            // Retrieves the list of advocates from the repository based on criteria
            List<Advocate> advocates = advocateRepository.getApplications(criteria);
            // Returns an empty list if no advocates are found
            if (CollectionUtils.isEmpty(advocates)) return new ArrayList<>();
            return advocates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Updates an existing advocate.
     *
     * This method validates the existing advocate application, updates the workflow information,
     * enriches the advocate data, and pushes the update request to a Kafka topic.
     *
     * @param advocateRequest The advocate request containing updated advocate details.
     * @return The updated advocate.
     */
    public Advocate updateAdvocate(AdvocateRequest advocateRequest) {
        try {
            // Validates and retrieves the existing advocate application
            Advocate existingAdvocate = validator.validateExistingApplication(advocateRequest.getAdvocates().get(0));
            // Updates the workflow details in the existing advocate
            existingAdvocate.setWorkflow(advocateRequest.getAdvocates().get(0).getWorkflow());
            // Sets the updated advocate back in the request
            advocateRequest.setAdvocates(Collections.singletonList(existingAdvocate));
            // Enriches advocate data on update
            enrichmentUtil.enrichAdvocateOnUpdate(advocateRequest);
            // Updates the workflow status of the advocate
            workflowService.updateWorkflowstatus(advocateRequest);
            // Push the updated advocate request to the Kafka topic
            producer.push(configuration.getUpdateTopic(), advocateRequest);
            return advocateRequest.getAdvocates().get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
