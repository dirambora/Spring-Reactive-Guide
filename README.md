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
Logging the sequence
    1. onSubscribe() – This is called when we subscribe to our stream.
    2. request(unbounded) – When we call subscribe, behind the scenes we’re
    creating a Subscription. This subscription requests elements from the
    stream. In this case, it defaults to unbounded, meaning it requests every
    single element available.
    3. onNext() – This is called on every single element.
    4. onComplete() – This is called last, after receiving the last element. There’s
    actually an onError() as well, which would be called if there’s an exception,
    but in this case, there isn’t.
    This is the flow laid out in the Subscriber interface as part of the Reactive
    Streams Specification. In reality, this is what’s been instantiated behind the
    scenes in our call to onSubscribe(). 

   COMPARISON TO JAVA8 STREAMS
   The core difference is that Reactive is a push model, whereas the Java 8
   Streams are a pull model. In a reactive approach, events are pushed to the
   subscribers as they come in.

BACKPRESSURE
Backpressure is when a downstream can tell an upstream to send it less
data in order to prevent it from being overwhelmed.

In our example, the subscriber is telling the producer to push every single element at once. This
could end up becoming overwhelming for the subscriber, consuming all of
its resources.

### Operating on a Stream
We can also perform operations on the data in our stream, responding to
events as we see fit.

#### Mapping Data in a Stream
A simple operation that we can perform is applying a transformation. In this
case, we’ll just double all the numbers in our stream:

Flux.just(1, 2, 3, 4)
 .log()
 .map(i -> i * 2)
 .subscribe(elements::add);

 #### Combining Two Streams

 We can then make things more interesting by combining another stream
 with this one. Let’s try this by using the zip() function:

### Hot Streams
These tyoe of streams are those that are always running and can be subscribed to at any point in time, missing the start of data.


##### Creating a Connectable Flux
One way to create a hot stream is by converting a cold stream into one.
n reactive programming with Project Reactor, the publish() method is used to convert a regular Flux into a ConnectableFlux. This operation effectively makes the Flux "hot", meaning it starts emitting items as soon as you call connect() on the ConnectableFlux.


##### Concurrency
The Parallel scheduler will cause our subscription to be run on a different 
thread, which can be proved by looking at the logs. 
