package org.example;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> elements = new ArrayList<>();

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
                elements.add(integer);
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


        System.out.println("Hello world!");

        //flux-a stream that can emit 0..n elements
        Flux<Integer> just = Flux.just(1, 2, 3, 4);

        //Mono- a stream that can emit 0..1 elements
//        Mono<Integer> just =Mono.just(1);

        //collecting elements
        //use the subscribe method for elements to start flowing
        Flux.just(1, 2, 3, 4).log().subscribe(elements::add);

    }


}