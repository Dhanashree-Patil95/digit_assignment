package digit.academy.tutorial.repository.querybuilder;

import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AdvocateQueryBuilder {

    private static final String BASE_ADVOCATES_QUERY = " SELECT adv.id , adv.tenant_id , adv.application_number , adv.bar_registration_number , adv.advocate_type , adv.organisation_id , adv.individual_id , adv.is_active , adv.created_by , adv.last_modified_by , adv.created_time , adv.last_modified_time , adv.additional_details as adv_additional_details ,";

    private static final String FROM_TABLES = " FROM advocates adv  LEFT JOIN adv_documents doc ON adv.id = doc.advocate_id WHERE 1=1 ";

    private static final String DOCUMENTS_TABLE = " doc.id as doc_id , doc.file_store , doc.document_type , doc.document_uid , doc.advocate_id , doc.advocate_clerk_id , doc.additional_details ";


    public String getQueryAndParams(AdvocateSearchCriteria criteria, Map<Integer, Object> params) {

        int i = 0;
        StringBuilder query = new StringBuilder(BASE_ADVOCATES_QUERY);
        query.append(DOCUMENTS_TABLE);
        query.append(FROM_TABLES);

        if(!ObjectUtils.isEmpty(criteria.getId())) {
            query.append(" AND adv.id = ? ");
            String val = criteria.getId();
            params.put(++i, UUID.fromString(val));
        }

        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
            query.append(" AND adv.application_number = ? ");
            var val = criteria.getApplicationNumber();
            params.put(++i, val);
        }

        if(!ObjectUtils.isEmpty(criteria.getBarRegistrationNumber())) {
            query.append(" AND adv.bar_registration_number = ? ");
            var val = criteria.getBarRegistrationNumber();
            params.put(++i, val);
        }

        return query.toString();
    }



}
