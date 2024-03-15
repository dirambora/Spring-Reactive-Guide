# Spring-Reactive-Guide

# Reactor Core
Reactor Core is a Java 8 library that implements the reactive programming
model. It’s built on top of the Reactive Streams specification, which is a
standard for building reactive applications.

Essentially, Reactive Streams is a specification for asynchronous stream
processing.

Step 1
Add Dependencies
 -Project Reactor Dependencies
 -LogBack Dependencies- We will be logging the output of the reactor in order to understand the flow of data.

 
Subscribe- Use the subscribe mthod to emmit the elements.
Logging the sequence<br>
    1.  onSubscribe() – This is called when we subscribe to our stream.<br>
    2.  request(unbounded) – When we call subscribe, behind the scenes we’re
    creating a Subscription.<br>
    3. onNext() – This is called on every single element.<br>
    4. onComplete() – This is called last, after receiving the last element. <br>
    5. onError() which is called if there’s an exception.<br>
   
   
### Comparison to Java8 Streams
The core difference is that Reactive is a push model, whereas the Java 8
Streams are a pull model. In a reactive approach, events are pushed to the subscribers as they come in.

### Backpressure
Backpressure is when a downstream can tell an upstream to send it less
data in order to prevent it from being overwhelmed.

In our example, the subscriber is telling the producer to push every single element at once. This
could end up becoming overwhelming for the subscriber, consuming all of
its resources.

### Operating on a Stream
We can also perform operations on the data in our stream, responding to
events as we see fit.

#### -Mapping Data in a Stream
A simple operation that we can perform is applying a transformation. In this
case, we’ll just double all the numbers in our stream:

Flux.just(1, 2, 3, 4)
 .log()
 .map(i -> i * 2)
 .subscribe(elements::add);

 #### -Combining Two Streams

 We can then make things more interesting by combining another stream
 with this one. Let’s try this by using the zip() function:

### Hot Streams
These tyoe of streams are those that are always running and can be subscribed to at any point in time, missing the start of data.


##### -Creating a Connectable Flux
One way to create a hot stream is by converting a cold stream into one.
n reactive programming with Project Reactor, the publish() method is used to convert a regular Flux into a ConnectableFlux. This operation effectively makes the Flux "hot", 
meaning it starts emitting items as soon as you call connect() on the ConnectableFlux.

<br> <br>


##### Concurrency
The Parallel scheduler will cause our subscription to be run on a different 
thread, which can be proved by looking at the logs. 

<br><br<>

## Debugging reactive streams in java

#### Logging Information With the doOnErrorMethod or Using the Subscribe Parameter 

         flux.doOnError(error -> {
             logger.error(“The following error happened on processFoo
              method!”, error);
          }).subscribe();

Now we’ll have some guidance on where the error might be coming from, even though we still don’t have
much information about the actual element that generated the exception.

### Activating Reactor's Global Debug Configuration
The reactor library provides a HOOKS class that lets us configure the behaviour of flux and mono operators.
By simply adding the statement below, our application will instrument cthe calls to publisher's methods,
wrap the construction of the operator and captuire a strack trace.

Hooks.onOperatorDebug();

   public static void main(String[] args) {
        Hooks.onOperatorDebug(); // Enable debugging

        Flux<Integer> flux = Flux.range(1, 5)
            .map(i -> i * 2)
            .filter(i -> i % 3 == 0)
            .log(); // Log operator

        flux.subscribe();
    }
}

Enabling operator debugging can provide valuable insights into the execution flow of reactive streams,
including information about operators' names, stack traces, and context information. 
This can be particularly useful for troubleshooting and understanding how reactive applications behave.

### Activating the Debug Output on a Single Process
Instrumenting and generating a stack trace in every single reactive process is costly, so we should implement the former 
approach only in crticial cases.
Reactor provides a way to enebal the debug mode on single crucial processes, which is less memory-consuming.
  This is the <b> checkpoint operator <b>

                  public void processFoo(Flux<Foo> flux) {

                   // ...
                   flux.checkpoint(“Observed error on processFoo”, true)
                   .subscribe();
                  }

We should implement the checkpoint method towards the end of the reactive chain.

#### Logging a Sequence of Elements
Reactor publishers offer one more method thsat could potentially ome in handy in sime casdes. Calling the log method
in our reactive chain, the application will log each element in the flow with the state that i has at that stage.

                  public void processFoo(Flux<Foo> flux) {
                            flux.map(FooNameHelper::concatFooName)
                            .map(FooNameHelper::substringFooName)
                            .log();
                            .map(FooReporter::reportResult)
                            .doOnError(error -> {
                            logger.error(“The following error happened on processFoo
                           method!”, error);
                            })
                            .subscribe();
                  }

<br<<br>

# Guide to Spring 5 WebFlux
Spring Webflux uses Project Reactor and its publisher implementations, Flux and Mono.
The new framework supports two programming models:
   - Annotation-based reactive components
   - Functional routing and handling

1) Add Dependency
      -This pulls in dependencies from spring webflux framework




## Introduction to the Functional Web Franework in Spring5
-  Spring WebFlux is a new functional web framework built using reactive principles.
The functiona; web framework introduces a new programming model whre we use functions to route and handdle requests.
     HandlerFunction and RouterFunctions are used.

### Handler Functions

The HandlerFunction represents    a function that generates responses for requests route to them.

                      @FunctionalInterface
                      public interface HandlerFunction<T extends ServerResponse> {
                       Mono<T> handle(ServerRequest request);
                      }
Function<Request, Response<T>> behaves very much like a servlet.HandlerFunction does not take a reposnde as an input parameter.

### Router Function
It serves as an alternative to the @RequestMappping annotation.It can be used to route requests to the handler functions.

                @FunctionalInterface
                public interface RouterFunction<T extends ServerResponse> {
                 Mono<HandlerFunction<T>> route(ServerRequest request);
                 // ...
                }
Routes are registered as SPring beans, and can be created inside any configuration class.




## SPRING 5 WEBCLIENT
Webclient is an interface representing the main entry point for performing web requests.

In order to work properly with the client we need to:
  - create an instance
  - make a request
  - handle the response

Creating a Web Client Instance
  -There are 3 options when creating a Webclient instance:
   - Creating a webclient object with default settings
    
          WebClient client = WebClient.create();
          
   -Creating  a Webclient Instance with a Given URI
    
            WebClient client = WebClient.
            create(“http://localhost:8080”);
            
   - building a client by using the DefaultWebClientBuilder class, which allows full customization: 
    
            WebClient client = WebClient.builder()
           .baseUrl(“http://localhost:8080”)
           .defaultCookie(“cookieKey”, “cookieValue”)
           .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
           .defaultUriVariables(Collections.singletonMap(“url”, “http://localhost:8080”))
           .build();



