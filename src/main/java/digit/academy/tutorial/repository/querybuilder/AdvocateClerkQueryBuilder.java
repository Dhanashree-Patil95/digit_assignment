package digit.academy.tutorial.repository.querybuilder;


import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.UUID;

@Component
public class AdvocateClerkQueryBuilder {


    private static final String BASE_ADVOCATES_QUERY = " SELECT cl.id , cl.tenant_id , cl.application_number , cl.state_regn_number , cl.individual_id , cl.is_active , cl.created_by , cl.last_modified_by , cl.created_time , cl.last_modified_time , cl.additional_details as cl_additional_details ,";

    private static final String FROM_TABLES = " FROM advocate_clerks cl  LEFT JOIN adv_documents doc ON cl.id = doc.advocate_clerk_id WHERE 1=1 ";

    private static final String DOCUMENTS_TABLE = " doc.id as doc_id , doc.file_store , doc.document_type , doc.document_uid , doc.advocate_id , doc.advocate_clerk_id , doc.additional_details ";

    public String getClerkQueryAndParams(AdvocateClerkSearchCriteria criteria, Map<Integer, Object> params) {

        int i = 0;
        StringBuilder query = new StringBuilder(BASE_ADVOCATES_QUERY);
        query.append(DOCUMENTS_TABLE);
        query.append(FROM_TABLES);

        if(!ObjectUtils.isEmpty(criteria.getId())) {
            query.append(" AND cl.id = ? ");
            String val = criteria.getId();
            params.put(++i, UUID.fromString(val));
        }

        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
            query.append(" AND cl.application_number = ? ");
            var val = criteria.getApplicationNumber();
            params.put(++i, val);
        }

        if(!ObjectUtils.isEmpty(criteria.getStateRegnNumber())) {
            query.append(" AND cl.state_regn_number = ? ");
            var val = criteria.getStateRegnNumber();
            params.put(++i, val);
        }


        return query.toString();
    }

}
