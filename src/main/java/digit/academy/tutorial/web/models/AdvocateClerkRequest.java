package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.AdvocateClerk;
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
 * AdvocateClerkRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkRequest   {
        @JsonProperty("requestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("clerks")
          @Valid
                private List<AdvocateClerk> clerks = null;


        public AdvocateClerkRequest addClerksItem(AdvocateClerk clerksItem) {
            if (this.clerks == null) {
            this.clerks = new ArrayList<>();
            }
        this.clerks.add(clerksItem);
        return this;
        }

}
