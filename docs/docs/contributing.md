---
layout: page
title: Contributing
section: home
position: 2
permalink: contributing
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
    "url": "https://api.github.com/repos/octocat/Hello-World/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e",
    "avatar_url": "https://github.com/images/error/hubot_happy.gif",
    "id": 1,
    "node_id": "MDY6U3RhdHVzMQ==",
    "state": "success",
    "description": "Build has completed successfully",
    "target_url": "https://ci.example.com/1000/output",
    "context": "continuous-integration/jenkins",
    "created_at": "2012-07-20T01:19:13Z",
    "updated_at": "2012-07-20T01:19:13Z",
    "creator": {
      "login": "octocat",
      "id": 1,
      "node_id": "MDQ6VXNlcjE=",
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

We can define our `Status` case class as:

```scala mdoc:silent
case class Status(
    url: String,
    avatar_url: String,
    id: Long,
    node_id: String,
    state: String,
    description: Option[String],
    target_url: Option[String],
    context: Option[String],
    created_at: String,
    updated_at: String
)
```

We can put it in the [github4s.domain package][domain-pkg] in the file corresponding to the
API, here: [Repository][repos-domain].


## Algebra

Next, we need to extend the algebra for the corresponding API so it can support our endpoint.
Because our endpoint is part of the repository API, we need to extend the `Repositories[F[_]]` algebra:

```scala
def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String] = Map()
  ): F[GHResponse[List[Status]]]
```
This code belongs to the [github4s.algebras package][algebra-pkg] in the file corresponding
to the API, here: [Repositories][repos-algebra].

## Interpreter

We're now ready to make [our repository interpreter][repos-interpreter] deal with `listStatuses`:

```scala
override def listStatuses(
      owner: String,
      repo: String,
      ref: String,
      headers: Map[String, String] = Map()): F[GHResponse[List[Status]]] =
    client.get[List[Status]](accessToken, s"repos/$owner/$repo/commits/$ref/statuses", headers)
```

This method makes the HTTP call with the help of [HttpClient][httpclient]


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
we'll be writing our tests in [GHReposSpec][repos-integ-spec]:

```scala
"Repos >> ListStatus" should "return a non empty list when a valid ref is provided" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, validCommitSha, headers = headerUserAgent)
      .unsafeRunSync()

    testIsRight[List[Status]](response, { r =>
      r.nonEmpty shouldBe true
    })
    response.statusCode shouldBe okStatusCode
  }

  it should "return an error when an invalid ref is provided" taggedAs Integration in {
    val response = Github[IO](accessToken).repos
      .listStatuses(validRepoOwner, validRepoName, invalidRef, headers = headerUserAgent)
      .unsafeRunSync()
    testIsLeft(response)
    response.statusCode shouldBe notFoundStatusCode
  }
```

Be aware that integration tests are only required for GET endpoints (not POST or PATCH) to avoid creating useless stuff on GitHub.

## Unit tests

We can now move on to the unit tests which reside in the [github4s.unit package][unit-pkg]. We're
going to test our [Intepreter][repos-interpreter]. Here too, the unit tests are
grouped by API which means we'll be working on [ReposSpec][repos-interpreter-spec].


### Interpreter spec

We're just checking that our API defined above hits the right endpoint, here:
`s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses"`:

```scala
"Repos.listStatuses" should "call htppClient.get with the right parameters" in {
    val response: IO[GHResponse[List[Status]]] =
      IO(GHResult(List(status).asRight, okStatusCode, Map.empty))

    implicit val httpClientMock = httpClientMockGet[List[Status]](
      url = s"repos/$validRepoOwner/$validRepoName/commits/$validRefSingle/statuses",
      response = response
    )

    val repos = new RepositoriesInterpreter[IO]

    repos.listStatuses(validRepoOwner, validRepoName, validRefSingle, headerUserAgent)
  }
```

# Documentation

Finally, we can add documentation to http://47deg.github.io/github4s/. Github4s uses
[sbt-microsites](https://github.com/47degrees/sbt-microsites) and [mdoc](https://github.com/scalameta/mdoc)
to generate and publish its documentation.

It shouldn't come as a surprise at this point, but the documentation is grouped by API. As a result,
we'll add documentation to [repository.md][repos-md]:

```text
### List statuses for a specific ref

You can also list statuses through `listStatuses`; it take as arguments:

- the repository coordinates (`owner` and `name` of the repository).
- a git ref (a `SHA`, a branch `name` or a tag `name`).

To list the statuses for a specific ref:

{triple backtick}scala mdoc:silent
val listStatuses =
  Github[IO](accessToken).repos.listStatuses("47degrees", "github4s", "heads/master")

val response = listStatuses.unsafeRunSync()
response.result match {
  case Left(e) => println(s"Something went wrong: ${e.getMessage}")
  case Right(r) => println(r)
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
[repos-domain]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/Repository.scala
[domain-pkg]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/domain/
[repos-algebra]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/algebras/Repositories.scala
[algebra-pkg]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/algebras/
[repos-interpreter]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/interpreters/RepositoriesInterpreter.scala
[httpclient]: https://github.com/47degrees/github4s/blob/master/github4s/src/main/scala/github4s/http/HttpClient.scala
[integ-pkg]: https://github.com/47degrees/github4s/blob/master/github4s/src/test/scala/github4s/integration/
[repos-integ-spec]: https://github.com/47degrees/github4s/blob/master/github4s/src/test/scala/github4s/integration/GHReposSpec.scala
[unit-pkg]: https://github.com/47degrees/github4s/tree/master/github4s/src/test/scala/github4s/unit
[repos-interpreter-spec]: https://github.com/47degrees/github4s/blob/master/github4s/src/test/scala/github4s/unit/ReposSpec.scala
[repos-md]: https://github.com/47degrees/github4s/blob/master/docs/docs/repository.md
