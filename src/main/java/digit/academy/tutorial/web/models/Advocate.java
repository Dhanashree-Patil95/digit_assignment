package digit.academy.tutorial.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.workflow.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Advocate
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Advocate   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

        @Size(min=2,max=128)         private String tenantId = null;

        @JsonProperty("applicationNumber")

        @Size(min=2,max=64)         private String applicationNumber = null;

        @JsonProperty("barRegistrationNumber")

        @Size(min=2,max=64)         private String barRegistrationNumber = null;

        @JsonProperty("advocateType")

        @Size(min=2,max=64)         private String advocateType = null;

        @JsonProperty("organisationId")

          @Valid
                private UUID organisationId = null;

        @JsonProperty("individualId")

                private String individualId = null;

        @JsonProperty("isActive")

                private Boolean isActive = true;

        @JsonProperty("workflow")

          @Valid
                private Workflow workflow = null;

        @JsonProperty("documents")
          @Valid
                private List<Document> documents = null;

        @JsonProperty("auditDetails")

          @Valid
                private AuditDetails auditDetails = null;

        @JsonProperty("additionalDetails")

                private Object additionalDetails = null;


        public Advocate addDocumentsItem(Document documentsItem) {
            if (this.documents == null) {
            this.documents = new ArrayList<>();
            }
        this.documents.add(documentsItem);
        return this;
        }

}
