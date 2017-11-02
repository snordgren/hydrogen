# Hydrogen

Hydrogen is a *single-function server* library; or in other words, an immutable,
minimalistic HTTP abstraction mapping input requests to output responses. A basic
server can be created in just one line of code.

	JettyServer.start(request -> TextResponse.ok("Hello, world!"));

Hydrogen builds on a principle of type-safety and immutability, inspired by pure
functional programming languages like Haskell. The main method of a Hydrogen server
serves to build a Handler object containing the logic of the program, which is passed
to the server implementation of choice, which then executes the handler for each
request made to the server.

## No Magic
Hydrogen does not contain any static mutable state, runtime code generation,
annotation processing, or any other kind of control-flow altering feature.
