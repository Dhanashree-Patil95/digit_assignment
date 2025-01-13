package digit.academy.tutorial.service;


import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.config.ServiceConstants;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.util.WorkflowUtil;
import digit.academy.tutorial.web.models.AdvocateClerkRequest;
import digit.academy.tutorial.web.models.AdvocateRequest;
import org.egov.common.contract.workflow.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static digit.academy.tutorial.config.ServiceConstants.*;

@Service
public class WorkflowService {

    @Autowired
    WorkflowUtil workflowUtil;


    @Autowired
    ADConfiguration configuration;


    @Autowired
    NotificationService notificationService;

    @Autowired
    Producer producer;


    /**
     * Updates the workflow status for each Advocate in the given AdvocateRequest.
     *
     * - Checks if the workflow feature is enabled in the configuration. If not, it returns early.
     * - Iterates through the list of Advocates in the AdvocateRequest.
     * - Updates the workflow status for each Advocate using the `workflowUtil`.
     * - If the updated workflow status is "USER_REGISTERED," sends a notification to the associated Individual ID.
     *
     * @param advocateRequest the AdvocateRequest object containing Advocates and their workflow details.
     */
    public void updateWorkflowstatus (AdvocateRequest advocateRequest){
        if(!configuration.getIsWorkflowEnabled())
            return;// Return early if workflow is not enabled

        // Iterate through each Advocate and update their workflow status
        advocateRequest.getAdvocates().forEach(advocate ->{
          String status =   workflowUtil.updateWorkflowStatus(advocateRequest.getRequestInfo(), advocate.getTenantId(),advocate.getApplicationNumber(),BUSINESS_SERVICE_CODE_ADVOCATE,advocate.getWorkflow(),WORKFLOW_MODULE_NAME );
            // Send notification if the workflow status indicates the user is registered
            if (status.equals(USER_REGISTERED)){
                notificationService.sendNotification(Collections.singletonList(advocate.getIndividualId()),advocateRequest.getRequestInfo(),advocate.getTenantId(),advocate.getApplicationNumber());
            }
        });
    }


    /**
     * Updates the workflow status for each Clerk in the given AdvocateClerkRequest.
     *
     * - Checks if the workflow feature is enabled in the configuration. If not, it returns early.
     * - Iterates through the list of Clerks in the AdvocateClerkRequest.
     * - Updates the workflow status for each Clerk using the `workflowUtil`.
     * - If the updated workflow status is "USER_REGISTERED," sends a notification to the associated Individual ID.
     *
     * @param advocateClerkRequest the AdvocateClerkRequest object containing Clerks and their workflow details.
     */
    public void updateClerkWorkflowstatus (AdvocateClerkRequest advocateClerkRequest){
        if(!configuration.getIsWorkflowEnabled())
            return; // Return early if workflow is not enabled

        // Iterate through each Clerk and update their workflow status
        advocateClerkRequest.getClerks().forEach(clerk ->{
            String status =   workflowUtil.updateWorkflowStatus(advocateClerkRequest.getRequestInfo(), clerk.getTenantId(),clerk.getApplicationNumber(),BUSINESS_SERVICE_CODE_ADVOCATE_CLERK,clerk.getWorkflow(),WORKFLOW_MODULE_NAME );
            // Send notification if the workflow status indicates the user is registered
            if (status.equals(USER_REGISTERED)){
           notificationService.sendNotification(Collections.singletonList(clerk.getIndividualId()),advocateClerkRequest.getRequestInfo(), clerk.getTenantId(), clerk.getApplicationNumber());
         }
        });
    }
}
