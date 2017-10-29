# Hydrogen

Hydrogen is a *single-function server* library; or in other words, an immutable,
minimalistic HTTP abstraction mapping input requests to output responses. A basic
server can be created in just one line of code.

	JettyServer.start(request -> TextResponse.ok("Hello, world!"));

Hydrogen builds on a principle of immutability, inspired by pure functional
programming languages like Haskell. The main method of a Hydrogen server serves
to build a Handler object containing the logic of the program, which is passed
to the server implementation of choice, which then executes the handler for each
request made to the server.

### Notes
Hydrogen does not provide routing nor does it aspire to. That is the domain of
another package. A default implementation of a server runtime is provided in the
`org.hydrogen.jetty` package in the `JettyServer` class. Other implementations
may be added if necessary. There is currently no WebSocket support.