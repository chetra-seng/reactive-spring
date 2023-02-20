package com.chetra.reactivespring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class CreationOperationTest {
    @Test
    void createAFluxWith_just() {
        Flux<String> fruitFlux = Flux
                .just("Apple", "Orange", "Grape", "Banana", "StrawBerry");

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("StrawBerry")
                .verifyComplete();
    }

    @Test
    void createAFluxWith_fromArray() {
        String[] fruits =
                new String[] {"Apple", "Orange", "Grape", "Banana", "StrawBerry"};

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("StrawBerry")
                .verifyComplete();
    }

    @Test
    void createAFluxWith_fromIterable() {
        List<String> fruitList =
                List.of("Apple", "Orange", "Grape", "Banana", "StrawBerry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitList);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("StrawBerry")
                .verifyComplete();
    }

    @Test
    void createAFluxWith_fromStream() {
        Stream<String> fruitStream =
                Stream.of("Apple", "Orange", "Grape", "Banana", "StrawBerry");

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("StrawBerry")
                .verifyComplete();
    }

    /**
     * Generating Flux data using range()
     * */
    @Test
    void createAFluxWith_range() {
        Flux<Integer> intervalFlux =
                Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    /**
     * Generating Flux data using interval()
     * */
    @Test
    void createAFluxWith_interval() {
        Flux<Long> intervalFlux =
                Flux.interval(Duration.ofSeconds(1)) // Infinite flex item with 1S interval from index 0-n
                        .take(5); // Limit the item to only 5

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .expectComplete()
                .verify();

    }
}
