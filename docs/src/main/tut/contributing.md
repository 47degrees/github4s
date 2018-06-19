---
layout: page
title: Contributing
section: contributing
position: 1
---

# Contributing

This is a small guide documenting the best way to add support for a new endpoint in Github4s.

As an example, we'll assume that the endpoint listing the statuses for a specific ref of
the repository API is not part of Github4s and we want Github4s to support it. Documentation for
this endpoint can be found on [developer.github.com][api-doc].

This endpoint is fairly simple; we need to make a GET request with the repository's owner and name
as well as the ref for which we want the status in the URL's path, and Github will send us back a
list of statuses.

## Source

### Domain

The first step will be to define the domain for our endpoint which is just a mapping between the
JSONs returned by the Github API and Github4s' own case classes.

From [the documentation][api-doc], Github sends a list of statuses which looks like the following:

```json
[
  {
    "created_at": "2012-07-20T01:19:13Z",
    "updated_at": "2012-07-20T01:19:13Z",
    "state": "success",
    "target_url": "https://ci.example.com/1000/output",
    "description": "Build has completed successfully",
    "id": 1,
    "url": "https://api.github.com/repos/octocat/Hello-World/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e",
    "context": "continuous-integration/jenkins",
    "creator": {
      "login": "octocat",
      "id": 1,
      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      "gravatar_id": "",
      "url": "https://api.github.com/users/octocat",
      "html_url": "https://github.com/octocat",
      "followers_url": "https://api.github.com/users/octocat/followers",
      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      "organizations_url": "https://api.github.com/users/octocat/orgs",
      "repos_url": "https://api.github.com/users/octocat/repos",
      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      "received_events_url": "https://api.github.com/users/octocat/received_events",
      "type": "User",
      "site_admin": false
    }
  }
]
```

Leveraging the existing [User][user-domain-scala] case class, we can define our `Status` case class
as:

```scala
case class Status(
    id: Int,
    url: String,
    state: String,
    target_url: Option[String],
    description: Option[String],
    context: Option[String],
    creator: Option[User],
    created_at: String,
    updated_at: String
)
```

We can put it in the [github4s.free.domain package][domain-pkg] in the file corresponding to the
API, here: [Repository][repos-domain-scala].

## API

Next, we can define the method actually making the HTTP call with the help of
[HttpClient][httpclient-scala]. All the HTTP calls are grouped by API in the
[github4s.free.api package][api-pkg] and, since our endpoint belongs to the repository API, we'll
add our call to [Repos][repos-api-scala]:

```scala
def listStatuses(
    accessToken: Option[String] = None,
    headers: Map[String, String] = Map(),
    owner: String,
    repo: String,
    ref: String): M[GHResponse[List[Status]]] =
  httpClient.get[List[Status]](accessToken, s"repos/$owner/$repo/commits/$ref/statuses", headers)
```

## Algebra

Then, we need to extend the algebra for the corresponding API so it can support our endpoint.
Because our endpoint is part of the repository API, we need to extend the
[RepositoryOp][repos-algebra-scala] ADT:

```scala
final case class ListStatuses(
    owner: String,
    repo: String,
    ref: String,
    accessToken: Option[String] = None
) extends RepositoryOp[GHResponse[List[Status]]]
```

We can now expose our endpoint as a [cats.free.Free](http://typelevel.org/cats/datatypes/freemonad.html):

```scala
class RepositoryOps[F[_]](implicit I: Inject[RepositoryOp, F]) {
  // ...
  def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      accessToken: Option[String] = None
  ): Free[F, GHResponse[List[Status]]] =
    Free.inject[RepositoryOp, F](ListStatuses(owner, repo, ref, accessToken))
  // ...
}
```

This code belongs to the [github4s.free.algebra package][algebra-pkg] in the file corresponding
to the API, here: [RepositoryOps][repos-algebra-scala].

## Interpreter

We're now ready to make [our repository interpreter][interpreter-scala] deal with `ListStatuses`:

```scala
def repositoryOpsInterpreter: RepositoryOp ~> K = new (RepositoryOp ~> K) {
  val repos = new Repos()
  def apply[A](fa: RepositoryOp[A]): K[A] = Kleisli[M, Map[String, String], A] { headers =>
    fa match {
      // ...
      case ListStatuses(owner, repo, ref, accessToken) =>
        repos.listStatus(accessToken, headers, owner, repo, ref)
      // ...
    }
  }
}
```

## Endpoint wrapper

Finally, we're ready to add our endpoint to the [GHRepos][apis-scala] wrapper:

```scala
class GHRepos(accessToken: Option[String] = None)(implicit O: RepositoryOps[GitHub4s]) {
  // ...
  def listStatuses(
      owner: String,
      repo: String,
      ref: String
  ): GHIO[GHResponse[List[Status]]] =
    O.listStatuses(owner, repo, ref, accessToken)
}
```

## Coproduct

Github4s represents the supported APIs as a `Coproduct` of ADTs. As a result, if you're adding
support for an API which is not yet taken into account by Github4s, in our case that would be the
repository API, you will need to add this API to the [Github4s Coproduct][app-scala]:

```scala
object app {
  // ...
  type GitHub4s[A] = Coproduct[RepositoryOp, COGHX, A]
}
```

and an entry point to your [wrapper](#endpoint-wrapper) in [the Github class][github-scala]:

```scala
class Github(accessToken: Option[String] = None) {
  // ...
  lazy val repos        = new GHRepos(accessToken)
  // ...
}
```

# Test

Now that we've written our source code, we're ready to write the tests.

## Token

The first step we need to take in order to run the tests is a valid token which we can provide through an
environment variable:

```bash
export GITHUB4S_ACCESS_TOKEN=aaaa
```

You can create a token on Github: <https://github.com/settings/tokens>.

## Integration tests

The integration tests are grouped by API in [github4s.integration package][integ-pkg]. As a result,
we'll be writing our tests in [GHReposSpec][repos-integ-spec-scala]:

```scala
"Repos >> ListStatuses" should "return a non empty list when a valid ref is provided" in {
  val response = Github(accessToken).repos
    .listStatuses(validRepoOwner, validRepoName, validCommitSha)
    .execFuture[T](headerUserAgent)

  testFutureIsRight[List[Status]](response, { r =>
    r.result.nonEmpty shouldBe true
    r.statusCode shouldBe okStatusCode
  })
}

it should "return an empty list when an invalid ref is provided" in {
  val response = Github(accessToken).repos
    .listStatuses(validRepoOwner, validRepoName, invalidRef)
    .execFuture[T](headerUserAgent)

  testFutureIsRight[List[Status]](response, { r =>
    r.result.isEmpty shouldBe true
    r.statusCode shouldBe okStatusCode
  })
}
```

Be aware that integration tests are only required for GET endpoints (not POST or PATCH) to avoid creating useless stuff on GitHub.

## Unit tests

We can now move on to the unit tests which reside in the [github4s.unit package][unit-pkg]. We're
going to test two things: our [algebra](#algebra) and our [API](#api). Here too, the unit tests are
grouped by API which means we'll be working on [GHReposSpec][repos-algebra-spec-scala] and
[ReposSpec][repos-api-spec-scala] respectively.

### Algebra spec

We're just verifying that our algebra gets the right parameters from our wrapper defined above:

```scala
"GHRepos.listStatuses" should "call to RepositoryOps with the right parameters" in {
  val response: Free[GitHub4s, GHResponse[List[Status]]] =
    Free.pure(Right(GHResult(List(status), okStatusCode, Map.empty)))

  val repoOps = mock[RepositoryOpsTest]
  (repoOps.listStatuses _)
    .expects(validRepoOwner, validRepoName, validRefSingle, sampleToken)
    .returns(response)
  val ghReposData = new GHRepos(sampleToken)(repoOps)
  ghReposData.listStatuses(validRepoOwner, validRepoName, validRefSingle)
}
```

### API spec

We're just checking that our API defined above hits the right endpoint, here:
`s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses"`:

```scala
"Repos.listStatuses" should "call htppClient.get with the right parameters" in {
  val response: GHResponse[List[Status]] = Right(GHResult(List(status), okStatusCode, Map.empty))

  val httpClientMock = httpClientMockGet[List[Status]](
    url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
    response = response
  )

  val repos = new Repos[String, Id] {
    override val httpClient: HttpClient[String, Id] = httpClientMock
  }
  repos.listStatuses(sampleToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
}
```

## JVM-specific unit tests

Finally, there are unit tests specific to Github4s on the JVM leveraging [mock server][mockserver].

### Mocking the responses

The first step will be to define the responses that will be sent back by the mock server in
[MockGithubApiServer][mockserver-scala]:

```scala
//Repos >> listStatus
mockServer
  .when(
    request
      .withMethod("GET")
      .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses")
      .withHeader("Authorization", tokenHeader))
  .respond(response.withStatusCode(okStatusCode).withBody(listStatusValidResponse))

mockServer
  .when(
    request
      .withMethod("GET")
      .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses")
      .withHeader(not("Authorization")))
  .respond(response.withStatusCode(unauthorizedStatusCode).withBody(unauthorizedResponse))

mockServer
  .when(
    request
      .withMethod("GET")
      .withPath(s"/repos/$validRepoOwner/$validRepoName/commits/$invalidRef/statuses")
      .withHeader(not("Authorization")))
  .respond(response.withStatusCode(okStatusCode).withBody(emptyListResponse))
```

### Unit tests

We can now write the unit tests making use of the previous mocks in [ApiSpec][api-spec-scala]:

```scala
"Repos >> ListStatus" should "return the expected statuses when a valid ref is provided" in {
  val response =
    repos.listStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  response should be('right)

  response.toOption map { r ⇒
    r.result.nonEmpty shouldBe true
    r.statusCode shouldBe okStatusCode
  }
}

it should "return an error if no tokens are provided" in {
  val response =
    repos.listStatus(None, headerUserAgent, validRepoOwner, validRepoName, validRefSingle)
  response should be('left)
}

it should "return an empty list when an invalid ref is passed" in {
  val response =
    repos.listStatus(accessToken, headerUserAgent, validRepoOwner, validRepoName, invalidRef)
  response should be('right)

  response.toOption map { r ⇒
    r.result.isEmpty shouldBe true
    r.statusCode shouldBe okStatusCode
  }
}
```

# Documentation

Finally, we can add documentation to http://47deg.github.io/github4s/. Github4s uses
[sbt-microsites](https://github.com/47deg/sbt-microsites) and [tut](https://github.com/tpolecat/tut)
to generate and publish its documentation.

It shouldn't come as a surprise at this point, but the documentation is grouped by API. As a result,
we'll add documentation to [repository.md][repos-md]:

```text
### List statuses for a specific Ref


You can also list status through `listStatuses`, it takes as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a git ref (a `SHA`, a branch `name` or a tag `name`).

To list the statuses for a specific ref:

{triple backtick}tut:silent
val listStatuses =
  Github(accessToken).repos.listStatuses("47deg", "github4s", "heads/master")

listStatuses.exec[cats.Id, HttpResponse[String]]() match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r.result)
}
{triple backtick}

The `result` on the right is the corresponding [List[Status]][repository-scala].

See [the API doc](https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref)
for full reference.
```

Once the documentation is written, we can build it locally with:

```bash
sbt "project docs" makeMicrosite
cd docs/target/site/ && jekyll serve
```

[api-doc]: https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref
[user-domain-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/User.scala
[repos-domain-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Repository.scala
[domain-pkg]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/
[repos-algebra-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/algebra/RepositoryOps.scala
[algebra-pkg]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/algebra/
[interpreter-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/interpreters/Interpreters.scala
[httpclient-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/HttpClient.scala
[api-pkg]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/api/
[repos-api-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/api/Repos.scala
[integ-pkg]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/test/scala/github4s/integration/
[repos-integ-spec-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/test/scala/github4s/integration/GHReposSpec.scala
[unit-pkg]: https://github.com/47deg/github4s/tree/master/github4s/shared/src/test/scala/github4s/unit
[repos-api-spec-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/test/scala/github4s/unit/ReposSpec.scala
[repos-algebra-spec-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/test/scala/github4s/unit/GHReposSpec.scala
[mockserver]: https://github.com/jamesdbloom/mockserver
[mockserver-scala]: https://github.com/47deg/github4s/blob/master/github4s/jvm/src/test/scala/github4s/utils/MockGithubApiServer.scala
[api-spec-scala]: https://github.com/47deg/github4s/blob/master/github4s/jvm/src/test/scala/github4s/unit/ApiSpec.scala
[repos-md]: https://github.com/47deg/github4s/blob/master/docs/src/main/tut/repository.md
[apis-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/GithubAPIs.scala
[app-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/app.scala
[github-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/Github.scala
