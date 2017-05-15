---
layout: docs
title: Authentication API
---

# Authentication API

Github4s supports the [Authentication API](https://developer.github.com/v3/oauth_authorizations/). As a result,
with github4s, you can:

- [New authentication](#new-authentication)
- [Authorize url](#authorize-url)
- [Get access token](#get-access-token)

The following examples assume the following imports:

```tut:silent
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import scalaj.http.HttpResponse
// if you're using ScalaJS, replace occurrences of HttpResponse by SimpleHttpResponse
//import github4s.js.Implicits._
//import fr.hmil.roshttp.response.SimpleHttpResponse

```

They also make use of `cats.Id` but any type container implementing `MonadError[M, Throwable]` will
do.

Support for `cats.Id`, `cats.Eval` and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.

## Authentication

### New authentication


 Call to request a new authorization given a basic authentication, the returned object Authorization includes an
 access token

- `username`: the username of the user
- `password`: the password of the user
- `scopes`: attached to the token
- `note`: to remind you what the OAuth token is for
- `client_id`: the 20 character OAuth app client key for which to create the token
- `client_secret`: the 40 character OAuth app client secret for which to create the token
- `headers`: optional user headers to include in the request

def newAuth(
  username: String,
  password: String,
  scopes: List[String],
  note: String,
  client_id: String,
  client_secret: String
): GHIO[GHResponse[Authorization]] =
O.newAuth(username, password, scopes, note, client_id, client_secret)

The `result` on the right is the created [Authorization][auth-scala].

See [the API doc](https://developer.github.com/v3/oauth_authorizations/#create-a-new-authorization) for full reference.


### Authorize url


 Generates the authorize url with a random state, both are returned within Authorize object

- `client_id`: the 20 character OAuth app client key for which to create the token
- `redirect_uri`: the URL in your app where users will be sent after authorization
- `scopes`: attached to the token

def authorizeUrl(
  client_id: String,
  redirect_uri: String,
  scopes: List[String]
): GHIO[GHResponse[Authorize]] =
O.authorizeUrl(client_id, redirect_uri, scopes)

The `result` on the right is the created [Authorize][auth-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.


### Get access token

  
    Requests an access token based on the code retrieved in the first step of the oAuth process
   
   - `client_id`: the 20 character OAuth app client key for which to create the token
   - `client_secret`: the 40 character OAuth app client secret for which to create the token
   - `code`: the code you received as a response to Step 1
   - `redirect_uri`: the URL in your app where users will be sent after authorization
   - `state`: the unguessable random string you optionally provided in Step 1
   - `headers`: optional user headers to include in the request
   
  def getAccessToken(
      client_id: String,
      client_secret: String,
      code: String,
      redirect_uri: String,
      state: String
  ): GHIO[GHResponse[OAuthToken]] =
    O.getAccessToken(client_id, client_secret, code, redirect_uri, state)

The `result` on the right is the corresponding [OAuthToken][auth-scala].

See [the API doc](https://developer.github.com/v3/activity/notifications/#set-a-thread-subscription) for full reference.

As you can see, a few features of the activity endpoint are missing.

As a result, if you'd like to see a feature supported, feel free to create an issue and/or a pull request!

[auth-scala]: https://github.com/47deg/github4s/blob/master/github4s/shared/src/main/scala/github4s/free/domain/Authorization.scala