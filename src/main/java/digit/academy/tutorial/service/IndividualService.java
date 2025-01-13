package digit.academy.tutorial.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.util.IndividualUtil;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.egov.common.models.individual.IndividualBulkResponse;
import org.egov.common.models.individual.IndividualResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class IndividualService {

    @Autowired
    IndividualUtil individualUtil;

    private final ObjectMapper mapper;

    public IndividualService(IndividualUtil individualUtil, ObjectMapper mapper) {
this.individualUtil = individualUtil;
this.mapper = mapper;
    }

    /**
     * Checks whether the provided individual IDs are valid by fetching the corresponding individual data.
     *
     * @param ids The list of individual IDs to validate.
     * @param requestInfo The request information object, typically containing request metadata.
     * @param tenantId The tenant ID associated with the request.
     * @return A boolean value indicating whether the individual IDs are valid (true) or not (false).
     */
    public boolean isValidIndividualId(List<String> ids, RequestInfo requestInfo, String tenantId) {
        // Fetching individual details based on the provided IDs, request info, and tenant ID.
        List<Individual> individuals = findIndividuals(ids, requestInfo, tenantId);
        if (CollectionUtils.isEmpty(individuals) && individuals.isEmpty()) ;
        {
            return false;
        }
    }


    /**
     * Fetches individual details based on a list of individual IDs.
     *
     * @param ids The list of individual IDs to search for.
     * @param requestInfo The request information object, typically containing request metadata.
     * @param tenantId The tenant ID associated with the request.
     * @return A list of Individual objects corresponding to the provided IDs. If no individuals are found, returns an empty list.
     */
    public List<Individual> findIndividuals(List<String> ids, RequestInfo requestInfo, String tenantId) {
        // Calling the individual utility to fetch individual data using the provided IDs, request info, and tenant ID.
        Object response = individualUtil.getIndividualDetails(ids, requestInfo, tenantId);
        // If no response is found, returning an empty list.
        if (ObjectUtils.isEmpty(response)) {
            return Collections.emptyList();
        }
        // Converting the response to an IndividualBulkResponse object using ObjectMapper.
        IndividualBulkResponse individualBulkResponse = mapper.convertValue(response, IndividualBulkResponse.class);
        // Returning the list of individuals from the bulk response.
        return individualBulkResponse.getIndividual();
    }
}
