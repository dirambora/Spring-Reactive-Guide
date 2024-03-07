package org.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        //flux-a stream that can emit 0..n elements
        Flux<Integer> just = Flux.just(1, 2, 3, 4);

        //Mono- a stream that can emit 0..1 elements
//        Mono<Integer> just =Mono.just(1);

        //collecting elements
        //use the subscribe method for elements to start flowing
        List<Integer> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(elements::add);
    }
}