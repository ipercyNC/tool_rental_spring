/*
 * ToolChargesController.java
 * 7/5/2023
 * Ian Percy
 * 
 * Controller for the ToolCharges objects. Implements the CRUD and other
 * endpoints for the ToolCharges model
 */
package com.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.models.ToolCharges;
import com.services.ToolChargesService;

@RestController
@RequestMapping("/api/toolCharges")
public class ToolChargesController {
    private static final Logger logger = LoggerFactory.getLogger(ToolChargesController.class);
    @Autowired
    ToolChargesService toolChargesService;

    /*
     * Returns all ToolCharges objects from the database
     * Gathers all the ToolCharges objects from the service layer
     * 
     * @return List of ToolCharges objects
     */
    @GetMapping("")
    public ResponseEntity<List<ToolCharges>> getAllToolCharges() {
        try {
            // Call ToolChargesService to gather all ToolCharges
            List<ToolCharges> toolCharges = toolChargesService.findAllToolCharges();
            // If empty, return NO_CONTENT
            if (toolCharges.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<List<ToolCharges>>(toolCharges, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error when getting all ToolCharges " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Creates a new ToolCharges through the service layer
     * 
     * @param objectNode contains the id of the ToolType to add into the database
     * 
     * @return status of the insert success
     */
    @PostMapping("")
    public ResponseEntity<String> createToolCharges(@RequestBody ObjectNode objectNode) {
        try {
            // Call ToolChargesService to create a new ToolCharge
            boolean result = toolChargesService.createToolCharge(objectNode.get("typeId").asInt());
            if (result) {
                return new ResponseEntity<String>("ToolCharges created successully.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<String>("ToolCharges not created.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Error when creating ToolCharges " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Returns a matching ToolCharges from the service layer if it exists
     * 
     * @param id int to search for in the database
     * 
     * @return ToolCharges object
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ToolCharges> getToolChargesById(@PathVariable("id") int id) {
        try {
            // Call ToolChargesService to find ToolCharges by id
            ToolCharges toolCharges = toolChargesService.findToolChargesById(id);
            return new ResponseEntity<ToolCharges>(toolCharges, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error when getting ToolCharges by id " + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Indicate call to delete all ToolCharges from the database
     * 
     * @return status of the delete success
     */
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllToolCharges() {
        try {
            // Call to ToolBrandService to delete rows from table and return number of rows
            // Returns number of rows deleted
            int numRowsDeleted = toolChargesService.deleteAllToolCharges();
            return new ResponseEntity<>("Deleted " + numRowsDeleted + " ToolCharges deleted", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error when deleting ToolCharges " + e);
            return new ResponseEntity<>("Cannot delete ToolCharges.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
