package digit.academy.tutorial.validators;


import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.service.IndividualService;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AdvocateValidator {

    @Autowired
    private AdvocateRepository repository;


    @Autowired
    private IndividualService individualService;




    /**
     * Validates the AdvocateRequest object.
     *
     * - Checks if the tenantId is present in each Advocate object within the request (mandatory field).
     * - Checks if the individualId is present in each Advocate object (mandatory field).
     * - Collects all individualIds from the Advocate objects for further validation.
     *
     * @param advocateRequest the AdvocateRequest object containing the list of Advocate objects to be validated.
     * @throws CustomException if tenantId or individualId is missing in any Advocate object.
     */    public void validateAdvocate (AdvocateRequest advocateRequest)
    {
         List<String> individualIds = new ArrayList<>();
        advocateRequest.getAdvocates().forEach(application ->{
            // Check if tenantId is provided
            if(ObjectUtils.isEmpty(application.getTenantId()))
                throw new CustomException("AD_APP_ERR", "tenantId is mandatory for creating advocate");

            // Check if individualId is provided
            if(ObjectUtils.isEmpty(application.getIndividualId())){
                throw new CustomException("AD_APP_ERR","Individual id is mandatory");
            }
            individualIds.add(application.getIndividualId());
        });
//         Validate if individual IDs exist in the database
//        boolean isValidIndividualId = individualService .isValidIndividualId(individualIds,advocateRequest.getRequestInfo(),advocateRequest.getAdvocates().get(0).getTenantId());
//        if(!isValidIndividualId){
//            throw new CustomException("AD_APP_ERR","Individual does not exist");
//        }
    }



    /**
     * Validates if an existing application exists for the given Advocate object.
     *
     * - Searches for applications in the database based on the applicationNumber provided in the Advocate object.
     * - Throws an exception if no application is found with the specified criteria.
     *
     * @param advocate the Advocate object containing the applicationNumber to be validated.
     * @return the existing Advocate object retrieved from the database.
     * @throws IllegalStateException if no applications are found for the given criteria.
     */
    public Advocate validateExistingApplication (Advocate advocate){
        // Build search criteria using the applicationNumber from the Advocate object
        List<AdvocateSearchCriteria> searchCriteriaList = Collections.singletonList(
                AdvocateSearchCriteria.builder()
                        .applicationNumber(advocate.getApplicationNumber())
                        .build()
        );
        // Query the database to find matching applications
        List<Advocate> advocates = repository.getApplications(searchCriteriaList);
        // Validate if any application is found
        if (advocates == null || advocates.isEmpty()) {
            throw new IllegalStateException("No applications found for the given criteria.");
        }
        // Return the first matched Advocate object
        return advocates.get(0);
    }
}
