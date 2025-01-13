package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.AdvocateClerkSearchCriteria;
import org.egov.common.contract.request.RequestInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * AdvocateClerkSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkSearchRequest   {
        @JsonProperty("requestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("criteria")
          @Valid
                private List<AdvocateClerkSearchCriteria> criteria = null;


        public AdvocateClerkSearchRequest addCriteriaItem(AdvocateClerkSearchCriteria criteriaItem) {
            if (this.criteria == null) {
            this.criteria = new ArrayList<>();
            }
        this.criteria.add(criteriaItem);
        return this;
        }

}
