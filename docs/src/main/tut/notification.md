---
layout: docs
title: Notification API
---

# Notification API

Github4s supports the [Notification API](https://developer.github.com/v3/activity/notifications/). As a result,
with github4s, you can:

- [Set a thread subscription](#set-a-thread-subscription)

The following examples assume the following imports and token:

```tut:silent
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import scalaj.http.HttpResponse
// if you're using ScalaJS, replace occurrences of HttpResponse by SimpleHttpResponse
//import github4s.js.Implicits._
//import fr.hmil.roshttp.response.SimpleHttpResponse

val accessToken = sys.env.get("GITHUB4S_ACCESS_TOKEN")
```

They also make use of `cats.Id` but any type container implementing `MonadError[M, Throwable]` will
do.

Support for `cats.Id`, `cats.Eval` and `Future` (the only supported option for scala-js) are
provided out of the box when importing `github4s.{js,jvm}.Implicits._`.

Set a Thread Subscription
This lets you subscribe or unsubscribe from a conversation. Unsubscribing from a conversation mutes all future notifications (until you comment or get @mentioned once more).

PUT /notifications/threads/:id/subscription
Parameters
Name	Type	Description
subscribed	boolean	Determines if notifications should be received from this thread
ignored	boolean	Determines if all notifications should be blocked from this thread
Response
Status: 200 OK
{
  "subscribed": true,
  "ignored": false,
  "reason": null,
  "created_at": "2012-10-06T21:34:12Z",
  "url": "https://api.github.com/notifications/threads/1/subscription",
  "thread_url": "https://api.github.com/notifications/threads/1"
}