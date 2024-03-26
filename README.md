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


### Creating a WebCliebt Instance with timeouts

-Often, the default HTTP timeouts of 30 seconds are too slow for our needs. To customize this behavious,
we can create a HttpClient and configure the webclient to use it.
We can do that by:
   - Setting the connection timout via e ChannelOption.CONNECT_TIMEOUT_MILLIS option
   - Setting the read and write timeouts using a ReadTimeoutHandler and a WriteTimeoutHandler, respectively
   - Configuring a response timeout using the responseTimeout directive

               HttpClient httpClient = HttpClient.create()
                   .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                   .responseTimeout(Duration.ofMillis(5000))
                   .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.
                    MILLISECONDS))
                  .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.
                   MILLISECONDS))) ;
                   WebClient client = WebClient.builder()
                   .clientConnector(new ReactorClientHttpConnector(httpClient))
                   .build()


### Preparing a Request- Define the Method
- Specify a HTTP method of a request by invoking method(HttpMethod method);
 
            UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
  
-We can also call shortcut methods, such as get, post and delete
           
            UriSpec<RequestBodySpec> uriSpec = client.post()

### Prepparing a Request- Define the URL
Next Step is to provide a URL. There are fifferent ways of doing this:

We can:
pass it to the uri API as a string

             RequestBodySpec bpodySpec = uriSpec.uri(*/resource*);
             
 Using a UriBuilder Function

             RequestBodySpec bodySpec = uriSpec.
             uri(
             uriBuilder -> uriBuilder.
             pathSegment(“/resource”).build());
             
As a jave .net URL instamce

           Request BodySpec bodySpec = uriSpec
           uri(URI.create("/resource"));


### Preparing a Request- Define the Body

We can set a request bofy by:
1. Using the body value method

       RequestHeadersSpec<?> headersSpec = 
       bodySpec.bodyValue(“data”)

2.by presenting a publisher to the body methpd

       RequestHeadersSpec<?> headersSpec = bodySpec.body(
       Mono.just(new Foo(“name”)), Foo.class);

3. We can also make use of BodyInserterd utility class

       RequestHeadersSpec<?> headersSpec = bodySpec.body(
       BodyInserters.fromValue(“data”))
   
 5. we can use the BodyInserters#fromPublisher method if we’re using a Reactor instance    

        RequestHeadersSpec headersSpec = bodySpec.body(
        BodyInserters.fromPublisher(Mono.just(“data”)),
        String.class);

This class also offers other intuitive functions to cover more advanced 
scenarios. For instance, if we have to send multipart requests.

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add(“key1”, “value1”);
        map.add(“key2”, “value2”);
        RequestHea4.
        dersSpec<?> headersSpec = bodySpec.body(
        BodyInserters.fromMultipartData(map));


- The BodyInserter is an interface responsible for populating a ReactiveHttpOutputMessage body with a given output message and a 
context used during the insertion.
- A Publisher is a reactive component in charge of providing a potentially unbounded number of sequenced elements. It’s an interface too, and the most popular implementations are Mono and Flux.

### Preparing a Request- Define the Headers

After setting the body, we can set the headers, cookies, and acceptable media types.There is an additional support for the most common;y used headers,like "If-None-Match", "If-Modified-Since", "Accept", and "Accept Charset"
Here is an example of how they can be used:

       HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .ifNoneMatch(“*”)
        .ifModifiedSince(ZonedDateTime.now())
        .retrieve();

  ### Geeting a Response
  The final stage is sending the request and receiving a response.We can use exchangeToMono/exchangeToFlux or the retrieve method.
  The exchangeToMono and exchangeToFlux methods allow access to the ClientResponse, along with its status and headers:

             Mono<String> response = headersSpec.exchangeToMono(response -> {
             if (response.statusCode().equals(HttpStatus.OK)) {
             return response.bodyToMono(String.class);
             } else if (response.statusCode().is4xxClientError()) {
             return Mono.just(“Error response”);
             } else {
             return response.createException()
             .flatMap(Mono::error);
             }
            });

   ## Working with the WebTestClient

   The WebTestClient is the main entry point for testing WebFlux server endpoints.It delegatesmost of the work to an Internal 
   Webclient instance focusing mainly on providing a test context.The DefaultWebTestClient xkass is a single interface 
   implementation.
   The client for testing can be bound to a real server, or work with specific controllers or functions.

   ### Binding to a Server
   To complete end-to-end intergration tests with actual requests to a running server, we can use the bindToServer method.

               WebTestClient testClient = WebTestClient
               .bindToServer()
               .baseUrl(“http://localhost:8080”)
               .build();

   ### Binding to a Router

   We test a RouterFunction by passing it to the bind the bindToRouterFunction method:

              RouterFunction function = RouterFunctions.route(
              RequestPredicates.GET(“/resource”),
              request -> ServerResponse.ok().build()
             );

              WebTestClient
             .bindToRouterFunction(function)
             .build().get().uri(“/resource”)
             .exchange()
             .expectStatus().isOk()
             .expectBody().isEmpty();


   ### Binding to a Web Handler
   The same behavior can be achieved with the bindToWebHandler method, which takes a WebHandler instance:

            WebHandler handler = exchange -> Mono.empty();
            WebTestClient.bindToWebHandler(handler).build();      
         
    
   ### Binding to an Application Context
   A more interesting situation occurs when we’re using the bindToApplicationContext method. It takes an ApplicationContext
   and analyzes the context for controller beans and @EnableWebFlux configurations.
   a simple code snippet may look like this:

              @Autowired
              private ApplicationContext context;
              WebTestClient testClient = WebTestClient.
              bindToApplicationContext(context)
              .build()

   ### Binding to a Controller
   Another approach would be providing an array of controllers we want to test with the bindToController method.

              @Autowired
             private Controller controller;
             WebTestClient testClient = WebTestClient.
             bindToController(controller).build()
   
  

   ## Concurency in Spring WebFlux
   ### The Motivation for Reactive Programming

  - A typical web application has many interactions that are blocking in nature such as those involving
    a database call to fetch or update data.\propagation of change through them.
    In a completely non-blocking environment, this can enables us to acheive higher concurrency with
    better resource utilization.

   - Reactive programming certainly has a very different approach to the usage of threads to achieve 
    concurrency. So the fundamental difference that reactive programming brings on is 
    <b>asynchronicity<b>

   - For instace,Under the reactive model, a read call to the database doesn’t block the calling thread 
     while data is fetched. The call immediately returns a publisher that others can subscribe to. The 
     subscriber can process the event after it occurs, and may even further generate events itself.
   
   ### Event Loop
   One reactive asynchronous programming model for servers is the eventloop model

   - The event loop runs continuously in a single thread, although we can 
   have as many event loops as the number of available cores.
   - The event loop processes the events from an event queue sequentially, 
   and returns immediately after registering the callback with the platform.
   - The platform can trigger the completion of an operation, like a database 
   call or an external service invocation.
   - The event loop can trigger the callback on the operation completion 
   notification, and send back the result to the original caller

   ### Reactive Programming with Spring WebFlux
   WebFlux is Spring's reactive-stack web framework, which was added in version 5.0
   Spring WebFlux sits parallel to the traditional web framework in Spring and doesn't replace it.

   • Spring WebFlux extends the traditional annotation-based programming 
   model with functional routing.
   • It adapts the underlying HTTP runtimes to the Reactive Streams API, 
   making the runtimes interoperable.
   • It’s able to support a wide variety of reactive runtimes, including Servlet 
   3.1+ containers, like Tomcat, Reactor, Netty, or Undertow.
   • It includes WebClient, a reactive and non-blocking client for HTTP 
   requests offering functional and fluent APIs.
  - Spring WebFlux can adapt to different runtimes through a common API provided by HttpHandler
  - While Netty is the default server in a WebFlux application, it’s just a matter 
   of declaring the right dependency to switch to any other supported server
   
   ### Reactor Netty
   Reactor Netty is the default embedded server in the Spring Boot WebFlux starter.
   Apart from a normal thread for the server, netty has a bunch of worker threads for request processing,
   these are typically available CPU Cores.
   Netty used the event loop model to provide highly scalableconcurrency in a reactive asynchronous 
   manner.
       
   ### Apache Tomcat
   Spring WebFlux is also supported on a traditional Servlet Container, like Apache Tomcat.
   WebFlux relies on the Servelet 3.1 API with non-blocking I/O. While it uses servelet API behind a 
   low-level adapter.

  - Tomcat 5 and onward supports NIO in its Connector component, which is primarily responsible for 
    receiving the requests 
  - The other Tomcat component is the Container component, which is responsible for the container 
    management functions

    ### Threading Model in WebClient
    WebClient is the reactive HTTP Client thats part of spring Webflux.

    ### Using WebClient
    Using WebClient is quite simple as well. We don’t need to include any specific dependencies, as it’s 
    part of Spring WebFlux.
    Here is a simple REST endpoint that returns a mono
    
              @GetMapping(“/index”)
                public Mono<String> getIndex() {
                 return Mono.just(“Hello World!”);
                }
               // we will use the webclient to call this RESR endpoint and consume the data reactively.
               WebClient.create(“http://localhost:8080/index”).get()
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(s -> printThreads());

     ### Understanding the Threading Model
    



    ### SPRING SECURITY 5 FOR REACTIVE APPLICATIONS

    SETUP-The basic setup requires a parent declaration, web starter, and security starter dependencies. We’ll also need the Spring Security test framework:

    #### Boostrapping the Reactive Application

    We’ll configure a Netty-based web server. Netty is an asynchronous NIO-based framework that’s a good foundation for reactive applications.
    The @EnableWebFlux annotation enables the standard Spring Web Reactive configuration for the application.

    For our basic Spring Security, configuration we'll create a configuration class SecurityConfig
    To enable WebFlux support in Spring Security 5, we only need to specify the @EnableWebFluxSecurity annotation.
    
        @EnableWebFluxSecurity
        public class SecurityConfig {
         // ...
        }
            











   
        





        
