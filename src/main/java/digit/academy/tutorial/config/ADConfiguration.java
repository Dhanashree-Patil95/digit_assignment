package digit.academy.tutorial.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ADConfiguration {
    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    // User Config
    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;

    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${moduleName}")
    private String moduleName;


    //Workflow Config
    @Value("${egov.workflow.host}")
    private String wfHost;

    @Value("${egov.workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${egov.workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String wfProcessInstanceSearchPath;

    @Value("${is.workflow.enabled}")
    private Boolean isWorkflowEnabled;




    //Individual Config
    @Value("${egov.individual.host}")
    private String individualHost;


    @Value("${egov.individual.search.path}")
    private String individualSearchPath;


    // AD Variables

    @Value("${ad.kafka.create.topic}")
    private String createTopic;

    @Value("${ad.kafka.update.topic}")
    private String updateTopic;

    @Value("${ad.default.offset}")
    private Integer defaultOffset;

    @Value("${ad.default.limit}")
    private Integer defaultLimit;

    @Value("${ad.search.max.limit}")
    private Integer maxLimit;


    // AD-clerk Variables

    @Value("${ad-cl.kafka.create.topic}")
    private String createClerkTopic;

    @Value("${ad.kafka.update.topic}")
    private String updateClerkTopic;

    @Value("${ad-cl.default.offset}")
    private Integer defaulClerktOffset;

    @Value("${ad-cl.default.limit}")
    private Integer defaultClerkLimit;

    @Value("${ad-cl.search.max.limit}")
    private Integer maxClerkLimit;


    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //HRMS
    @Value("${egov.hrms.host}")
    private String hrmsHost;

    @Value("${egov.hrms.search.endpoint}")
    private String hrmsEndPoint;

    @Value("${egov.url.shortner.host}")
    private String urlShortnerHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlShortnerEndpoint;

    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;
}




