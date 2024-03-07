package org.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        //flux-a stream that can emit 0..n elements
        Flux<Integer> just = Flux.just(1, 2, 3, 4);

        //Mono- a stream that can emit 0..1 elements
//        Mono<Integer> just =Mono.just();

    }
}