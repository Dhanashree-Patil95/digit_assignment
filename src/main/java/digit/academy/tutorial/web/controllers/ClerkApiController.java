package digit.academy.tutorial.web.controllers;


import digit.academy.tutorial.service.AdvocateClerkCreateService;
import digit.academy.tutorial.web.models.*;
import digit.academy.tutorial.util.ResponseInfoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ClerkApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    AdvocateClerkCreateService advocateClerkCreateService;


    public ClerkApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/clerk/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1CreatePost(@ApiParam(value = "Details for the AdvocateClerk registration + RequestInfo meta data.", required = true) @Valid @RequestPart("application") AdvocateClerkRequest advocateClerkRequest, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        List<AdvocateClerk> advocateClerks = advocateClerkCreateService.createAdvocateClerk(advocateClerkRequest, files);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateClerkRequest.getRequestInfo(), true);
        AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(advocateClerks).responseInfo(responseInfo).build();

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/clerk/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true) @Valid @RequestBody AdvocateClerkSearchRequest advocateClerkSearchRequest) {
        List<AdvocateClerk> applications = advocateClerkCreateService.searchAdvocateClerks(advocateClerkSearchRequest.getRequestInfo(), advocateClerkSearchRequest.getCriteria());
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateClerkSearchRequest.getRequestInfo(), true);
        AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/clerk/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<AdvocateClerkResponse> clerkV1UpdatePost(@ApiParam(value = "Details of the registered advocate + RequestInfo meta data.", required = true) @Valid @RequestBody AdvocateClerkRequest advocateClerkRequest) {
        AdvocateClerk advocateClerk = advocateClerkCreateService.updateAdvocateClerk(advocateClerkRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateClerkRequest.getRequestInfo(), true);
        AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(Collections.singletonList(advocateClerk)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
