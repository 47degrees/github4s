package com.fortysevendeg.github4s

trait ResponsesJSON {

  val getRepoJson =
    """
      |{
      |  "id": 23613922,
      |  "name": "scala-exercises",
      |  "full_name": "scala-exercises/scala-exercises"
      |}
    """.stripMargin

  val listCommitsJson =
    """
      |[{
      |    "sha": "e2583e81bc5af63e4210d6bf2507633895ceeb09",
      |    "commit": {
      |        "author": {
      |            "name": "Andy",
      |            "email": "andyscott@users.noreply.github.com",
      |            "date": "2016-03-31T15:50:21Z"
      |        },
      |        "message": "Merge pull request #329 from scala-exercises/al-cats-semigroup\nAdd Semigroup section to cats exercises",
      |        "url": "https://api.github.com/repos/scala-exercises/scala-exercises/git/commits/e2583e81bc5af63e4210d6bf2507633895ceeb09",
      |        "comment_count": 0
      |    },
      |    "url": "https://api.github.com/repos/scala-exercises/scala-exercises/commits/e2583e81bc5af63e4210d6bf2507633895ceeb09",
      |    "html_url": "https://github.com/scala-exercises/scala-exercises/commit/e2583e81bc5af63e4210d6bf2507633895ceeb09",
      |    "comments_url": "https://api.github.com/repos/scala-exercises/scala-exercises/commits/e2583e81bc5af63e4210d6bf2507633895ceeb09/comments",
      |    "author": {
      |        "login": "andyscott",
      |        "id": 310363,
      |        "avatar_url": "https://avatars.githubusercontent.com/u/310363?v=3",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/andyscott",
      |        "html_url": "https://github.com/andyscott",
      |        "site_admin": false
      |    }
      |}]
    """.stripMargin

  val newAuthorizationJson =
    """
      |{
      |  "id": 31958295,
      |  "url": "https://api.github.com/authorizations/31958295",
      |  "app": {
      |    "name": "Scala-exercises",
      |    "url": "http://scala-exercises.47deg.com/",
      |    "client_id": "e8e39175648c9db8c280"
      |  },
      |  "token": "b1720b3a075ce09e0dc32f07d18f5ee27e612505",
      |  "hashed_token": "cfd6f691c952ad71a801f535f1d421e5ce865999a00aebf5555e76ef517faa55",
      |  "token_last_eight": "7e612505",
      |  "note": "admin script",
      |  "note_url": null,
      |  "created_at": "2016-05-12T07:34:41Z",
      |  "updated_at": "2016-05-12T07:34:41Z",
      |  "scopes": [
      |    "public_repo"
      |  ],
      |  "fingerprint": null
      |}
    """.stripMargin

  val notFoundJson =
    """
      |{
      |  "message": "Not Found",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

  val badCredentialsJson =
    """
      |{
      |  "message": "Bad credentials",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

}
