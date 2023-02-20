package com.chetra.reactivespring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class CombiningOperationTest {
    @Test
    void mergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500)); // Set delay between Flux items include index 0

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delayElements(Duration.ofMillis(500))
                // Making sure the second flux start after the first
                .delaySubscription(Duration.ofMillis(250)); // delay 250 ms before consumer call onSubscribe on publisher

        Flux<String> mergedFlux = Flux.merge(characterFlux, foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .expectComplete()
                .verify();
    }

    @Test
    public void zipFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250));

        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(
                        p -> p.getT1().equals("Garfield") &&
                                p.getT2().equals("Lasagna")
                )
                .expectNextMatches(
                        p -> p.getT1().equals("Kojak") &&
                                p.getT2().equals("Lollipops")
                )
                .expectNextMatches(
                        p -> p.getT1().equals("Barbossa") &&
                                p.getT2().equals("Apples")
                )
                .verifyComplete();
    }

    /**
     * Zip two fluxes together with call back
     * */
    @Test
    void zipFluxesToObject() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250));

        Flux<String> zippedFlux = Flux
                .zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();
    }

    /**
     * Selecting the first Flux that emits value
     * */
    @Test
    void firstFlux() {
        Flux<String> slowFlux = Flux
                .just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));

        Flux<String> fastFlux = Flux
                .just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }
}
