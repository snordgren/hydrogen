# Hydrogen

A no-cruft, super light web framework for Java. Get started in one line of code:

	Jetty.start(request -> Response.ok().text("Hello, world!"));

## Features
 * **No Magic**. No static mutable state, runtime code generation, annotation
 processing, or any other control flow modifications.
 * **Lightweight**. Use lightweight immutable POJOs instead of heavyweight,
 mutable server-specific objects. Test your code without starting a web
 server.
 * **Composable**. Requests and responses are immutable, handlers are 
 `Request -> Response` functional interfaces, and filters are 
 `Handler -> Handler` functional interfaces. Maximal code reuse!

## Examples
### Routing

	Router router = Router.builder()
    	.get("/", request -> Response.ok().html("<h1>index</h1>"))
     	.get("/user/:name", request -> {
    		String user = request.getRouteParam("name");
    		return Response.ok().html("<h1>" + user + "</h1>");
        })
    	.build();
	Jetty.start(3000, router);

## Installation
Hydrogen has not yet reached version 0.1.0, in the meantime you can use 
[Jitpack](https://jitpack.io/#snordgren/hydrogen) with Gradle.
Just add Jitpack to repositories:

	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
	
Then add the dependency.

	dependencies {
		...
		compile 'com.github.snordgren:hydrogen:-SNAPSHOT'
	}

Please post your feedback and suggestions in the issue tracker. 
Contributions welcome!
