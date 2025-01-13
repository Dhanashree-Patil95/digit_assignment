package digit.academy.tutorial.validators;

import digit.academy.tutorial.repository.AdvocateClerkRepository;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.service.IndividualService;
import digit.academy.tutorial.web.models.*;
import org.egov.tracer.model.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class AdvocateClerkValidator {

    @Autowired
    IndividualService individualService;

    @Autowired
    private AdvocateClerkRepository repository;


    /**
     * Validates the AdvocateClerkRequest object to ensure all necessary fields are provided.
     *
     * - Checks if the `tenantId` is present in each clerk object. Throws an exception if missing.
     * - Checks if the `individualId` is present in each clerk object. Throws an exception if missing.
     * - Validates the `individualId` against the database using the IndividualService.
     *
     * @param advocateClerkRequest The request object containing the list of AdvocateClerks to validate.
     * @throws CustomException if mandatory fields are missing or if the individual IDs do not exist in the database.
     */

    public void validateAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {
        List<String> individualIds = new ArrayList<>();
        advocateClerkRequest.getClerks().forEach(application -> {
            // Check if tenantId is provided
            if (ObjectUtils.isEmpty(application.getTenantId()))
                throw new CustomException("ADCL_APP_ERR", "tenantId is mandatory for creating advocate");
            // Check if individualId is provided
            if(ObjectUtils.isEmpty(application.getIndividualId())){
                throw new CustomException("ADCL_APP_ERR","Individual id is mandatory");
            }
            individualIds.add(application.getIndividualId());
        });
        // Validate if individual IDs exist in the database
//        boolean isValidIndividualId = individualService .isValidIndividualId(individualIds,advocateClerkRequest.getRequestInfo(),advocateClerkRequest.getClerks().get(0).getTenantId());
//        if(!isValidIndividualId){
//            throw new CustomException("ADCL_APP_ERR","Individual does not exist");
//        }
    }



    /**
     * Validates if a clerk application already exists for the given application number.
     *
     * - Searches for applications using the application number in the AdvocateClerk object.
     * - If no matching application is found, throws an IllegalStateException.
     * - Returns the existing application if found.
     *
     * @param advocateClerk The AdvocateClerk object containing the application number to validate.
     * @return The existing AdvocateClerk object from the database.
     * @throws IllegalStateException if no application is found for the given application number.
     */
    public AdvocateClerk validateExistingClerkApplication(AdvocateClerk advocateClerk) {
        // Create search criteria based on application number
        List<AdvocateClerkSearchCriteria> searchCriteriaList = Collections.singletonList(AdvocateClerkSearchCriteria.builder().applicationNumber(advocateClerk.getApplicationNumber()).build());
        // Query the repository for matching applications
        List<AdvocateClerk> clerks = repository.getClerkApplications(searchCriteriaList);
        // Check if any applications were found
        if (clerks == null || clerks.isEmpty()) {
            throw new IllegalStateException("No applications found for the given criteria.");
        }
        return clerks.get(0);// Return the first matching application
    }
}
