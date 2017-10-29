# Hydrogen

Hydrogen is an unopinionated, immutable, minimalistic HTTP abstraction.
In breaking down a web server into small, self-sufficient components, Hydrogen
forms the bottom layer exposing HTTP communication as a simple mapping of HTTP
request to HTTP response.

	JettyServer.start(request -> TextResponse.ok("Hello, world!"));

Hydrogen builds on a principle of immutability, inspired by pure functional
programming languages like Haskell. The main method of a Hydrogen server serves
to build a Handler object containing the logic of the program, which is passed
to the server implementation of choice, which then executes the handler for each
request made to the server.

### Note
Hydrogen does not provide routing nor does it aspire to. That is the domain of
another package. A default implementation of a server runtime is provided in the
`org.hydrogen.jetty` package in the `JettyServer` class. Other implementations
may be added if necessary.

Hydrogen currently has no support for WebSockets. If WebSocket support is
necessary,
