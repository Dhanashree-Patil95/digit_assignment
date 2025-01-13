package digit.academy.tutorial.repository.rowmapper;


import digit.academy.tutorial.web.models.Advocate;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.UUID;

public class NewAdvocateMapper implements RowMapper<Advocate> {
    @Override
    public Advocate mapRow(ResultSet rs, int rowNum) throws SQLException {

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
        Advocate advocate = Advocate.builder()
                .id(UUID.fromString(rs.getString("id")))
                .applicationNumber(rs.getString("application_number"))
                .barRegistrationNumber(rs.getString("bar_registration_number"))
                .advocateType(rs.getString("advocate_type"))
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
            advocate.getDocuments().add(document);
        }
        return advocate;

    }
}

