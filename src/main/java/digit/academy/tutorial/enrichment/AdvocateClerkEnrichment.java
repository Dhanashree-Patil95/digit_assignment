package digit.academy.tutorial.enrichment;


import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AdvocateClerkRequest;
import digit.academy.tutorial.web.models.AdvocateRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class AdvocateClerkEnrichment {
    @Autowired
    private IdgenUtil idgenUtil;


    /**
     * Enriches the AdvocateClerkRequest object with additional metadata and IDs.
     *
     * - Generates unique application numbers for each clerk using the IdgenUtil service.
     * - Creates and sets audit details (createdBy, createdTime, lastModifiedBy, lastModifiedTime).
     * - Sets a unique identifier (UUID) for each clerk.
     * - Sets the `isActive` flag to `false` for each clerk.
     *
     * @param advocateClerkRequest The AdvocateClerkRequest object containing the list of clerks to enrich.
     */
    public void enrichAdvocateClerk(AdvocateClerkRequest advocateClerkRequest) {
        // Generate a list of unique application numbers
        List<String> advocateIdList = idgenUtil.getIdList(advocateClerkRequest.getRequestInfo(), advocateClerkRequest.getClerks().get(0).getTenantId(), "advcl.registrationid", "ADVOC_CLERK_[SEQ_ADV_REG_ID]_[YYYY]", advocateClerkRequest.getClerks().size());
        Integer index = 0;
        for (AdvocateClerk advocateClerk : advocateClerkRequest.getClerks()) {
            // Create audit details for each clerk
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis())
                    .build();
            advocateClerk.setAuditDetails(auditDetails);

            // Set other enrichment data
            advocateClerk.setId(UUID.randomUUID()); // Generate unique ID
            advocateClerk.setIsActive(false); // Set active status to false
            advocateClerk.setApplicationNumber(advocateIdList.get(index++)); // Assign application number
        }


    }


    /**
     * Updates the `lastModifiedBy` and `lastModifiedTime` fields in the AuditDetails of the first clerk.
     *
     * - Used during update operations to reflect the latest modification details.
     *
     * @param advocateClerkRequest The AdvocateClerkRequest object containing the clerk to update.
     */
    public void enrichAdvocateClerkOnUpdate(AdvocateClerkRequest advocateClerkRequest){
        // Update audit details for the first clerk in the list
        advocateClerkRequest.getClerks().get(0).getAuditDetails().setLastModifiedBy(advocateClerkRequest.getRequestInfo().getUserInfo().getUuid());
        advocateClerkRequest.getClerks().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
    }

}