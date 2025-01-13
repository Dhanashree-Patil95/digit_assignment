package digit.academy.tutorial.enrichment;

import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class AdvocateEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;


    /**
     * Enriches the Advocate objects in the AdvocateRequest with additional details.
     *
     * - Generates unique application numbers for each Advocate using the IdgenUtil service.
     * - Adds audit details such as createdBy, createdTime, lastModifiedBy, and lastModifiedTime.
     * - Sets a unique ID for each Advocate.
     * - Marks each Advocate as inactive initially.
     *
     * @param advocateRequest the AdvocateRequest containing the list of Advocate objects to be enriched.
     */
    public void enrichAdvocate(AdvocateRequest advocateRequest) {
        // Generate application numbers for advocates using Idgen Service
        List<String> advocateIdList = idgenUtil.getIdList(advocateRequest.getRequestInfo(), advocateRequest.getAdvocates().get(0).getTenantId(), "adv.registrationid", "ADVOC_[SEQ_ADV_REG_ID]_[YYYY]", advocateRequest.getAdvocates().size());
        Integer index = 0;
        // Enrich each advocate with audit details, unique ID, and application number
        for (Advocate advocate : advocateRequest.getAdvocates()) {
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(advocateRequest.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis())
                    .build();
            advocate.setAuditDetails(auditDetails);
            advocate.setId(UUID.randomUUID());// Set unique ID for each advocate
            advocate.setIsActive(false);// Set the initial active status as false
            advocate.setApplicationNumber(advocateIdList.get(index++));// Assign application number

        }
    }


    /**
     * Updates the audit details of an Advocate object during an update operation.
     *
     * - Modifies the lastModifiedBy and lastModifiedTime fields in the audit details
     *   of the first Advocate in the AdvocateRequest.
     *
     * @param advocateRequest the AdvocateRequest containing the Advocate object to be updated.
     */
    public void enrichAdvocateOnUpdate(AdvocateRequest advocateRequest) {
        // Update audit details for the first advocate in the list
        advocateRequest.getAdvocates().get(0).getAuditDetails().setLastModifiedBy(advocateRequest.getRequestInfo().getUserInfo().getUuid());
        advocateRequest.getAdvocates().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
    }
}
