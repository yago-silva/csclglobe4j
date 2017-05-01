# CSCLGlobe4j

CSCLGlobe4j is a [Docker Registry](https://docs.docker.com/registry/) client for Java applications.

With CSCLGlobe4j you can interact with [Docker Registry](https://docs.docker.com/registry/) and perform actions like:
 - List all available images
 - List all available repositories
 - List all images of a specific repository

![alt text](https://raw.githubusercontent.com/yago-silva/csclglobe4j/master/docs/images/cscl_globe.jpg "csclglobe4j image")

#### Requirements
- Java 8 or later

## How to use

The top abstraction of CSCLGlobe4j is the ``` DockerRegistry ``` class. This class offer methods for all possible
operations.

The most simple way to create an instance of ``` DockerRegistry ``` is using the ```DockerRegistryBuilder``` like this:

```
DockerRegistry dockerRegistry = new DockerRegistryBuilder()
                .withBaseUrl("http://localhost:5000")
                .withTimeoutInMilliseconds(1000l)
                .build();
```

Obs.: Timeout is not required and if you does not pass one, the default value of 5000 will be used instead.

Now that we have an instance of ``` DockerRegistry ``` we can use available methods to perform operations like:

-  Listing all available images:
```
List<DockerImage> dockerImages = dockerRegistry.listAllAvailableImages();
```


- Listing all available repositories:
```
List<String> repositories =  dockerRegistry.listRepositories();
```

- Listing all images of a specific repository:
```
List<DockerImage> dockerImages = dockerRegistry.listImagesByRepository("my/repository");
```


#### Using a different http client library

In the core of CSCLGlobe4j there are two available http clients libraries:
- ``` RetrofitDockerRegistryClient ``` that uses[retrofit](http://square.github.io/retrofit/) to perform http requests
to [Docker Registry](https://docs.docker.com/registry/)
- ``` FeignDockerRegistryClient ``` that uses [feign](https://github.com/OpenFeign/feign) to perform http requests
to [Docker Registry](https://docs.docker.com/registry/)


If you create ``` DockerRegistry ``` using ``` DockerRegistryBuilder ``` as explained above, CSCLGlobe4j will use
``` RetrofitDockerRegistryClient ``` by default. To use other client libraries you need to create the instance of
``` DockerRegistry ``` using it's constructor (that receiver an instance of ``` DockerRegistryClient ```) like this:


```
    FeignDockerRegistryStub stub = FeignDockerRegistryStubFactory.get("http://localhost:5000", 1000);
    DockerRegistryClient dockerRegistryClient = new FeignDockerRegistryClient(stub);

    DockerRegistry dockerRegistry = new DockerRegistry(dockerRegistryClient);
```

<b>Important:</b>

Note that as ``` DockerRegistryClient ``` is an interface, so you can create your own implementation using your prefered
http client library if you does not want to use [feign](https://github.com/OpenFeign/feign) or
[retrofit](http://square.github.io/retrofit/). All you need is one concrete class that implements
``` DockerRegistryClient ```.


## Contributing

You can contribute opening or solving [issues](https://github.com/yago-silva/csclglobe4j/issues).
After solve an issue, you can open a pull request to the master branch of this repository.

#### Requirements for development proccess

- [Gradle](https://gradle.org/)

This projet uses [gradle](https://gradle.org/) as build automation tool. So you will need gradle installed in
development time.

- [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or later

We use java 8 new features like lambda and streams in this project. So you will need jdk in version 8 or later.

#### How to build

To build this project you must go to the root directory of this repository (where you clone this repository) and run
the following command:

``` ./gradlew clean build ```

#### How to run tests

To run tests you must go to the root directory of this repository (where you clone this repository) and run
the following command:

``` ./gradlew test ```