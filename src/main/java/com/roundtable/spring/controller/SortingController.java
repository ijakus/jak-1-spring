package com.roundtable.spring.controller;

import com.roundtable.spring.request.SortingRequest;
import com.roundtable.spring.response.SortingResponse;
import com.roundtable.spring.util.SortingUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class SortingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortingController.class);

    private final MeterRegistry meterRegistry;

    private final WebClient.Builder webClientBuilder;

    @Value("#{'${target.services}'.split(',')}")
    private List<String> targetServices;

    @Autowired
    public SortingController(final WebClient.Builder webClientBuilder, final MeterRegistry meterRegistry) {
        this.webClientBuilder = webClientBuilder;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/sorting")
    public ResponseEntity<SortingResponse> sorting(@RequestBody SortingRequest sortingRequest){
        if(sortingRequest.getArraySize() == null || sortingRequest.getArraySize() < 0) {
            return ResponseEntity.badRequest().build();
        }

        meterRegistry.counter("demo.request.counter.total").increment();

        LOGGER.info("Received sorting request with array size {}", sortingRequest.getArraySize());

        long timeSpent = SortingUtil.generateAndSort(sortingRequest.getArraySize());

        LOGGER.info("Sorting with array size {} completed in {} milliseconds", sortingRequest.getArraySize(), timeSpent);

        return ResponseEntity.ok(new SortingResponse(timeSpent));
    }

    @PostMapping(value = "/delegated/sorting")
    public ResponseEntity<SortingResponse> delegatedSorting(@RequestBody SortingRequest sortingRequest) {
        long start = System.currentTimeMillis();

        meterRegistry.counter("demo.request.counter.total").increment();

        if(sortingRequest.getArraySize() == null || sortingRequest.getArraySize() < 0) {
            return ResponseEntity.badRequest().build();
        }

        LOGGER.info("Received delegate request with array size {}", sortingRequest.getArraySize());

        int randomIndex = (int) (Math.random() * (targetServices.size()));

        webClientBuilder
                .baseUrl(targetServices.get(randomIndex))
                .build()
                .post()
                .uri("/v1/sorting")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(sortingRequest), SortingRequest.class)
                .retrieve()
                .bodyToMono(SortingResponse.class)
                .subscribe(sortingResponse -> {
                    LOGGER.info("Delegated sorting finished in: {}", sortingResponse.getTimeSpentInMs());
                });

        return ResponseEntity.ok(new SortingResponse(System.currentTimeMillis() - start));
    }

}
