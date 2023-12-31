/*
 * RentController.java
 * 7/6/2023
 * Ian Percy
 * 
 * Controller for the renting Tools. Interfaces with the RentService.
 * Handles the logic when users are wanting to rent tools and will generate
 * the rental agreements.
 */
package com.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.services.RentToolService;

@RestController
@RequestMapping("/api/rentTool")
public class RentToolController {
    private static final Logger logger = LoggerFactory.getLogger(RentToolController.class);
    private Map<String, String> errorResponseMap;
    @Autowired
    RentToolService rentToolService;

    public RentToolController() {
        // Initalize mapping that will contain all error mappings with response messages
        this.errorResponseMap = new HashMap<String, String>();
        this.errorResponseMap.put("ERROR_PERCENT_OUT_OF_RANGE", "Please Give Percent That Is 0-100");
        this.errorResponseMap.put("ERROR_RENTAL_DAY_COUNT_OUT_OF_RANGE", "Please Rental Day Count That Is 1 Day Or More");
        this.errorResponseMap.put("ERROR_TOOL_CHOICE", "Cannot Find Tool Choice, Please Try a Valid Tool Choice");
        this.errorResponseMap.put("ERROR_TOOL_CHARGE", "Cannot Find Tool Charges For The Chosen Tool");
        this.errorResponseMap.put("ERROR_PERCENT_FORMAT", "Please Give Whole Number For Discount (0-100)");
        this.errorResponseMap.put("ERROR_RENTAL_DAYS_FORMAT", "Please Give Whole Number For Rental Days");
        this.errorResponseMap.put("ERROR_CHECKOUT_DATE", "Invalid Checkout Date");
        this.errorResponseMap.put("ERROR_GENERAL", "Tool Rental Failed, Please Try Again");
    }
    /*
     * Returns a List of Strings representing the rental agreement
     * Accepts an object with the details of the tool rental and then generates the
     * agreement
     * 
     * @param objectNode object with the tool rental request
     * 
     * @return List of Strings with the rental agreement
     */
    @PostMapping("")
    public ResponseEntity<String> rentTool(@RequestBody ObjectNode objectNode) {
        try {
            // Pass parameters to the RentToolService
            String charges = rentToolService.rentTool(objectNode.get("code").asText(),
                    objectNode.get("startDate").asText(),
                    objectNode.get("days").asText(),
                    objectNode.get("discount").asText());

            // Return error string if match
            if (this.errorResponseMap.containsKey(charges)){
                return new ResponseEntity<String>(this.errorResponseMap.get(charges), HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<String>(charges, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in rentTool() " + e);
            return new ResponseEntity<>("ERROR_RENTING_TOOL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}