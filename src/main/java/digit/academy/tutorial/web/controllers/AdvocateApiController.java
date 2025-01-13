package digit.academy.tutorial.web.controllers;


import digit.academy.tutorial.config.ADConfiguration;
import digit.academy.tutorial.service.AdvocateCreateService;
import digit.academy.tutorial.util.ResponseInfoFactory;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import digit.academy.tutorial.web.models.AdvocateSearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.models.individual.Individual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-24T13:05:19.176845619+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class AdvocateApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private AdvocateCreateService advocateCreateService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;




    public AdvocateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }



    @RequestMapping(value = "/advocate/v1/_create", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<AdvocateResponse> advocateV1CreatePost(@ApiParam(value = "Details for the user registration + RequestInfo meta data.", required = true) @Valid @RequestPart("application") AdvocateRequest advocateRequest, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        List<Advocate> advocate = advocateCreateService.createAdvocate(advocateRequest, files);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateRequest.getRequestInfo(), true);
        AdvocateResponse response = AdvocateResponse.builder().advocates(advocate).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

    @RequestMapping(value = "/advocate/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1SearchPost(@ApiParam(value = "Details for the user registration + RequestInfo meta data.", required = true) @Valid @RequestBody AdvocateSearchRequest advocateSearchRequest) {
        List<Advocate> applications = advocateCreateService.searchAdvocates(advocateSearchRequest.getRequestInfo(), advocateSearchRequest.getCriteria());
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateSearchRequest.getRequestInfo(), true);
        AdvocateResponse response = AdvocateResponse.builder().advocates(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/advocate/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(@ApiParam(value = "Details of the registered advocate + RequestInfo meta data.", required = true) @Valid @RequestBody AdvocateRequest advocateRequest) {
        Advocate advocate = advocateCreateService.updateAdvocate(advocateRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(advocateRequest.getRequestInfo(), true);
        AdvocateResponse response = AdvocateResponse.builder().advocates(Collections.singletonList(advocate)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

}
