package digit.academy.tutorial.service;

import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.config.ServiceConstants;
import digit.academy.tutorial.kafka.Producer;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static digit.academy.tutorial.config.ServiceConstants.*;

@Service
public class NotificationService {

    @Autowired
    private Producer producer;

    @Autowired
    ServiceConstants constants;

    @Autowired
    IndividualService individualService;

    @Autowired
    ADConfiguration configuration;


    /**
     * Sends an SMS notification to the first individual in the list of IDs.
     *
     * - Fetches the list of Individuals based on the provided IDs, tenantId, and RequestInfo.
     * - Retrieves the mobile number of the first Individual from the list.
     * - Constructs a notification message using the application number.
     * - Pushes the notification to the configured Kafka topic for SMS notifications.
     *
     * @param ids               List of individual IDs to fetch the details of individuals.
     * @param requestInfo       RequestInfo object containing the context of the API request.
     * @param tenantId          Tenant ID to filter the individuals.
     * @param applicationNumber Application number used in the SMS message body.
     */
    public void sendNotification(List<String> ids, RequestInfo requestInfo,String tenantId,String applicationNumber){
        // Fetch the list of individuals based on IDs, request info, and tenant ID
        List<Individual> individuals = individualService.findIndividuals(ids,requestInfo,tenantId);
        // Retrieve the mobile number of the first individual in the list
        String mobileNumber =  individuals.get(0).getMobileNumber();
        // Construct the SMS message body using the application number
        String message = String.format(MESSAGE_BODY,applicationNumber);
        // Prepare the notification payload
        Map<String, String> notification = new HashMap<>();
        notification.put(MOBILE_NUMBER, mobileNumber);
        notification.put(MESSAGE, message);
        // Push the notification to the Kafka topic for SMS notifications
        producer.push(configuration.getSmsNotificationTopic(), notification);
    }
}
