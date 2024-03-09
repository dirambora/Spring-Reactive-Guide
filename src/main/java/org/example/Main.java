package org.example;

import org.assertj.core.api.Assertions;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static reactor.core.publisher.Signal.subscribe;

public class Main {
    public static void main(String[] args) {

        List<Integer> element = new ArrayList<>();

        Flux.just(1, 2, 3, 4).log().subscribe(new Subscriber<Integer>() {
            private Subscription s;
            int onNextAmount;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(2);
            }


            @Override
            public void onNext(Integer integer) {
                element.add(integer);
                onNextAmount++;
                if (onNextAmount % 2 == 0) {
                    s.request(2);
                }
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onComplete() {
            }
        });


        //operating on a stream
        //create a Flux emitting integers 1, 2, 3, 4, and 5.
        Flux.just(1,2,3,4,5)
                //log the emitted elements
                .log()
                //multiply each emmitted element by 2
                .map(i-> i*2)
                //collect the transformed elements
                .subscribe(element::add);


        //combining two streams

        List<String> elements = new ArrayList<>();

        Flux.just(1,4,5,6)
                .log()
                .map(i -> i * 2)
                        .zipWith(Flux.range(0,Integer.MAX_VALUE),
                                (one, two) -> String.format("First Flux: %d, Second Flux: %d", one,two))
                                .subscribe(elements::add);

        Assertions.assertThat(elements).containsExactly(
                "First Flux: 2, Secong Flux:0",
                "First Flux: 4, Secong Flux:0",
                "First Flux: 6, Secong Flux:0",
                "First Flux: 8, Secong Flux:0");


        System.out.println("Hello world!");

        List<String> elementss = new ArrayList<>();
        //flux-a stream that can emit 0..n elements
        Flux<Integer> just = Flux.just(1, 2, 3, 4);

        //Mono- a stream that can emit 0..1 elements
//        Mono<Integer> just =Mono.just(1);

        //collecting elements
        //use the subscribe method for elements to start flowing
//        Flux.just(1, 2, 3, 4).log().subscribe(elementss::add);

    }


}