package digit.academy.tutorial.repository.rowmapper;

import digit.academy.tutorial.web.models.Advocate;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class AdvocateRowMapper implements ResultSetExtractor<List<Advocate>> {

//    application_number
//    bar_registration_number

/*    @Override
    public Advocate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Advocate.builder()
                .applicationNumber(rs.getString("application_number"))
                .barRegistrationNumber(rs.getString("bar_registration_number"))
                .build();
    }*/

    @Override
    public List<Advocate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String,Advocate> advocateMap = new LinkedHashMap<>();
        System.out.println(rs.getFetchSize());
        while (rs.next()){
            String uuid = rs.getString("id");
            Advocate advocate = advocateMap.get(uuid);

            if(advocate == null) {

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
                advocate = Advocate.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .applicationNumber(rs.getString("application_number"))
                        .barRegistrationNumber(rs.getString("bar_registration_number"))
                        .auditDetails(auditdetails)
                        .build();


            }

            advocateMap.put(uuid, advocate);
        }

        return List.of();
    }
}
