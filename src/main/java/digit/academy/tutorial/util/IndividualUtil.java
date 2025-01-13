package digit.academy.tutorial.util;

import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.repository.ServiceRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.IndividualResponse;
import org.egov.common.models.individual.IndividualSearch;
import org.egov.common.models.individual.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static digit.academy.tutorial.config.ServiceConstants.BUSINESS_SERVICES;
import static digit.academy.tutorial.config.ServiceConstants.TENANTID;


@Slf4j
@Component
public class IndividualUtil {

    @Autowired
    private ServiceRequestRepository restRepo;


    @Autowired
    ADConfiguration configuration;

    public Object getIndividualDetails (List<String> ids, RequestInfo requestInfo,String tenantId){
        String uri = getSearchURLWithParams(tenantId).toUriString();
        IndividualSearch individualSearch = IndividualSearch.builder().id(ids).build();
        IndividualSearchRequest individualSearchRequest= IndividualSearchRequest.builder().requestInfo(requestInfo).individual(individualSearch).build();
        return restRepo.fetchResult(new StringBuilder(uri),individualSearchRequest);
    }


    private UriComponentsBuilder getSearchURLWithParams(String tenantId) {
        StringBuilder url = new StringBuilder();
        url.append(configuration.getIndividualHost()).append(configuration.getIndividualSearchPath());
        System.out.println(url+"444444444444444444444444");
       return UriComponentsBuilder.fromHttpUrl(url.toString()).queryParam("limit",50).queryParam("offset",0).queryParam("tenantId",tenantId);
    }
}
