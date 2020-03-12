package nl.cinq.rabo.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nl.cinq.rabo.entities.Cards;
import nl.cinq.rabo.entities.PowerOfAttorneyAggregatedData;
import nl.cinq.rabo.service.AggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully processed the request"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found."),
        @ApiResponse(code = 500, message = "Internal error has occurred.")})
public class PowerOfAttorneyController {

    public AggregateService aggregateService;

    @Autowired
    public PowerOfAttorneyController(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
    }


    @ApiOperation(httpMethod = "GET", value = "Get all power of attorney aggregated details",
            response = PowerOfAttorneyAggregatedData.class, responseContainer = "List")
    @GetMapping(value = "/power_of_attorney")
    public Mono<List<PowerOfAttorneyAggregatedData>> allPowerOfAttorneyAggregatedDetails() {
        return aggregateService.getAllAggregatedDetails();
    }

    @ApiOperation(httpMethod = "GET", value = "Get power of attorney aggregated details for id",
            response = PowerOfAttorneyAggregatedData.class)
    @GetMapping(value = "/power_of_attorney/{id}")
    public Mono<PowerOfAttorneyAggregatedData> powerOfAttorneyAggregatedDetails(@PathVariable String id) {
        return aggregateService.getPowerOfAttorneyAggregatedDetails(id);
    }

    @ApiOperation(httpMethod = "GET", value = "Get the aggregated details of cards of power of attorney for id",
            response = PowerOfAttorneyAggregatedData.class)
    @GetMapping(value = "/cards/{id}")
    public Mono<Cards> cardsAggregatedDetails(@PathVariable String id) {
        return aggregateService.getCardsAggregatedDetails(id);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity handleWebClientResponseException(WebClientResponseException e) {
        boolean is4xxClientError = e.getStatusCode().is4xxClientError();
        String message;
        if (is4xxClientError) {
            message = "This id could not be found.";
            log.info(e.getMessage());
        } else {
            message = "Internal error, please contact us for assistance.";
            log.error(e.getMessage(), e.getStackTrace());
        }

        return new ResponseEntity(message, e.getStatusCode());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity handleConnectionException(ConnectException e) {
        String message = "Connection error, please contact us for assistance.";
        log.error(e.getMessage(), e.getStackTrace());
        return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
