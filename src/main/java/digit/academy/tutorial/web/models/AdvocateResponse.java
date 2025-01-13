package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.Pagination;
import org.egov.common.contract.response.ResponseInfo;
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
 * AdvocateResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("advocates")
          @Valid
                private List<Advocate> advocates = null;

        @JsonProperty("pagination")

          @Valid
                private Pagination pagination = null;


        public AdvocateResponse addAdvocatesItem(Advocate advocatesItem) {
            if (this.advocates == null) {
            this.advocates = new ArrayList<>();
            }
        this.advocates.add(advocatesItem);
        return this;
        }

}
