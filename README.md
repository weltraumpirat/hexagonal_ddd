# Variations of DDD with hexagonal architecture.

## Introduction

Hexagonal Architecture (a.k.a. Ports and Adapters) is a design pattern conceived by Alistair Cockburn.
It elegantly solves some of the biggest problems in larger software projects, by 
1) isolating the business logic from the rest of a software system, 
2) moving it into the very center of the architecture and 
3) attaching all the other parts like plug-in extensions around it. 

I made this example project as a supplement to a blog post about the pattern, to demonstrate some of the many shapes and variations this pattern can be implemented in - 
even not considering languages, platforms and tools, there must be quite a few distinct versions, 
each with its own advantages and disadvantages - and of course, it falls short: 
- **The business case is naive, at best.** \
Indeed. It's the smallest example I could think of, that would leave enough complexity to show some of the key decisions we face in real-life projects. 
- **The domain core is OK, but leaves a lot to be improved.** \
Agreed. I think I gave it a respectable first shot, but I am aware that there are shortcomings that might be solved with some love and a day or... 
sixteen. This is a demo, and I am not getting paid for it. So, thank you for pointing that out - I am always happy to accept pull requests.
- **Not all the code has proper tests.** \
True, and I regret that. However, the important parts (the core) have the important coverage, and GUIs are often best tested manually, itfp. 
Again, I welcome pull requests.
- **I fail to see why I should go to all the troubles.** \
I understand. Sadly, this is something I cannot solve for you. 
Long term benefits will neither become obvious, nor easily understandable, unless you actually work and evolve a system with DDD and Hexagonal in a real life scenario: 
But even if it's a pet project, it may only takes weeks, before you face the first learning that leads to a pivot and makes you want to re-design your entire system - which, now, you actually _can_, without losing much sleep over it.
- **It would have been much better in Haskell.**. \
Well, admittedly, according to some people I have much respect for, everything would be.
However, I chose React, because I am foolishly biased towards the JavaScript ecosystem and the way it makes me progress and enjoy programming again.
And for the same reason, I probably would have chosen NodeJS over Java in an instant, but alas, instead I picked Maven and SpringBoot for demo purposes - 
mostly, because some of the issues that arise from persistence handling in layered architectures are quite apparent in Java, 
and in combination with widely used frameworks from the Spring family. 
 
## The Scenario

It's a web shop. Simple as that.

It contains some master data (the products; there are some default ones, but you can add your own as you please), 
a single bounded context (adding and removing items to the shopping cart, as well as taking it to check-out), 
and finally produces orders. \
It is extremely naive, of course - only one user, and one administrator, no security, no payment, no complex validation 
(except one where we check if an item is a valid product). However, it is enough to show how to handle inter-context dependencies, mapping between serialized "DTO" type of objects and domain entities, and how data storage structures can end up looking quite different from domain objects.

## How do I use it?

There are branches that contain the different implementations, and they are named:
- **1_monolith**: \
The most straightforward example, containing a single backend server and the two frontend apps.
--- 
####COMING SOON:

- **2_shared_kernel**: \
The same application, but with "microservices", or rather, a few small services that share the same business logic as a library, as well as the database. _Warning:_ This is not an approach I would recommend as a target architecture, but rather the "step in between" a monolith and actual microservices, when you are aiming to transition to a distributed system.
- **3_microservices** \
Same partitioning as 2, but this time without shared anything, and the domain core properly split into several libraries.
- **4_event_sourcing_and_cqrs** \
Further separated commands and queries into individual services, adds a simple event bus. This example shows how Ports and Adapters can also apply at network level, rather than in micro architecture.
---

Each branch contains docker containers for all the relevant parts, that can be easily built and started using the 
`build.sh` and `run.sh` scripts I have included - unless you're on Windows, in which case I can give no guarantees they will work with PowerShell. 
Sorry. \
You can always deploy manually, following the instructions in [INSTALL.md](INSTALL.md) for every branch.

Once everything runs, you should be able to access the store and administration GUIs at http://localhost/ and http://localhost/admin, respectively.

## I have feedback...

Awesome! You can reach me on [Twitter](https://twitter.com/w3ltraumpirat), or open an issue or submit pull requests, 
and I'll do my very best to respond swiftly. :) 
