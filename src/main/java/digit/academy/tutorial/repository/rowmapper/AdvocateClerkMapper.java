package digit.academy.tutorial.repository.rowmapper;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AdvocateClerkMapper implements RowMapper<AdvocateClerk> {
    @Override
    public AdvocateClerk mapRow(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("Row number: " + rowNum);  // Print row number for tracking
        System.out.println("Mapping row from ResultSet");  // Debugging log

        // Print values from the ResultSet


        Long lastModifiedTime = rs.getLong("last_modified_time");
        if (rs.wasNull()) {
            lastModifiedTime = null;
        }

        AuditDetails auditdetails = AuditDetails.builder()
                .createdBy(rs.getString("created_by"))
                .createdTime(rs.getLong("created_time"))
                .lastModifiedBy(rs.getString("last_modified_by"))
                .lastModifiedTime(lastModifiedTime)
                .build();
        AdvocateClerk advocateClerk = AdvocateClerk.builder()
                .id(UUID.fromString(rs.getString("id")))
                .applicationNumber(rs.getString("application_number"))
                .stateRegnNumber(rs.getString("state_regn_number"))
                .isActive(rs.getBoolean("is_active"))
                .documents(new ArrayList<>())
                .auditDetails(auditdetails)
                .build();

        if (rs.getString("doc_id") != null) {
            Document document = Document.builder()
                    .id(rs.getString("doc_id"))
                    .fileStore(rs.getString("file_store"))
                    .documentType(rs.getString("document_type"))
                    .documentUid(rs.getString("document_uid"))
                    .build();
            advocateClerk.getDocuments().add(document);
        }
        return advocateClerk;

    }
}
