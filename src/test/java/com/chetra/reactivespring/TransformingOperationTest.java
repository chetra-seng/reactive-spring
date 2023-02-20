package com.chetra.reactivespring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransformingOperationTest {

    /**
     * Filtering Flux by skipping a few items
     * based on flux number
     * */
    @Test
    void skipAFew() {
        Flux<String> skipFlux = Flux
                .just("one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }

    /**
     * Filtering Flux by skipping a few items
     * based on the time to emit value
     * */
    @Test
    void skipAFewSeconds() {
        Flux<String> skipFlux = Flux
                .just("one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                        .skip(Duration.ofSeconds(4));

        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }

    /**
     * Filtering Flux by taking a few items
     * */
    @Test
    void take() {
        Flux<String>  nationalParkFlux = Flux
                .just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .take(3);

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Grand Canyon")
                .verifyComplete();
    }

    /**
     * Filtering Flux by taking a few item based on duration
     * */
    @Test
    void takeAFewSeconds() {
        Flux<String>  nationalParkFlux = Flux
                .just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Grand Canyon")
                .verifyComplete();
    }

    /**
     * Filtering Flux using filter
     * */
    @Test
    void filter() {
        Flux<String> nationalParkFlux = Flux
                .just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete();
    }

    /**
     * Filtering Flux with distinct to select unique value
     * */
    @Test
    void distinct() {
        Flux<String> animalFlux = Flux
                .just("dog", "cat", "bird", "dog", "bird", "anteater")
                .distinct();

        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "anteater")
                .verifyComplete();
    }

    /**
     * Transforming data synchronously with map
     * */

    public record Player(String firstname, String lastname) {}
    @Test
    void map() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();
    }

    /**
     * Transformation data asynchronously using flatMap
     * */
    @Test
    void flatMap() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n) // flatMap can transform data into mono
                        .map(p -> {
                            String[] split = n.split("\\s");
                            return new Player(split[0], split[1]);
                        })
                        // invoke subscribeOn to run mono in parallel
                        .subscribeOn(Schedulers.parallel())
                );

        List<Player> players = List.of(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr")
        );

        StepVerifier.create(playerFlux)
                .expectNextMatches(players::contains)
                .expectNextMatches(players::contains)
                .expectNextMatches(players::contains)
                .verifyComplete();
    }

    /**
     * Buffering reactive data
     * */
    @Test
    void buffer() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(List.of("apple", "orange", "banana"))
                .expectNext(List.of("kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    void bufferIntoReactive() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");

        fruitFlux
                .buffer(3)
                .flatMap(
                        x -> Flux.fromIterable(x)
                                .map(String::toUpperCase)
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();
    }

    /**
     * Buffer all flux items into a single list
     * */
    @Test
    void collectList() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");

        Mono<List<String>> fruitListMono = fruitFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(List.of("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    /**
     * Buffer all flux item into one single list with buffer
     * */
    @Test
    void bufferAll() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");

        Flux<List<String>> fruitFluxList = fruitFlux.buffer();

        StepVerifier.create(fruitFluxList)
                .expectNext(Arrays.asList("apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    /**
     * Collect Flux item into a map
     * */
    @Test
    void collectMap() {
        Flux<String> animalFlux = Flux.just(
                "aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Map<Character, String>> animalMapMono = animalFlux
                .collectMap(a -> a.charAt(0));

        StepVerifier.create(animalMapMono)
                .expectNext(Map.of(
                        'a', "aardvark",
                        'e', "eagle",
                        'k', "kangaroo"
                ))
                .verifyComplete();
    }
}
