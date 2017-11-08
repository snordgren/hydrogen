# Hydrogen

Hydrogen is a *server as a function* library: a composable, immutable,
minimal HTTP function mapping input requests to output responses. A basic
server using Jetty as the backend can be created in just one line of code.

	Jetty.start(request -> Response.ok().text("Hello, world!"));

Hydrogen is built with type-safety, immutability, and testability in mind.
A server built with Hydrogen can be tested without ever starting an actual
server runtime, because requests and responses are both simple POJOs.

## Features
 * **No Magic**. Hydrogen does not contain any static mutable state, runtime
 code generation, annotation processing, or any other kind of control-flow
 altering feature.
 * **Lightweight**. Use lightweight immutable POJOs instead of heavyweight,
 mutable server-specific objects.
 * **Backend independent**. Easily switch between multiple backends. Adding
 a new backend only requires adding support for handler management and
 request/response object conversion.

## Disclaimer
Hydrogen is not battle-tested, nor is it ready for production use (yet). Look
out for version 1.0.0, in the meantime, make sure to watch, star, and post
your suggestions in the issue tracker.
