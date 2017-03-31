/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.utils

trait FakeResponses {

  val getUserValidResponse =
    """
      |{
      |  "login": "rafaparadela",
      |  "id": 315070,
      |  "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |  "gravatar_id": "",
      |  "url": "https://api.github.com/users/rafaparadela",
      |  "html_url": "https://github.com/rafaparadela",
      |  "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |  "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |  "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |  "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |  "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |  "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |  "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |  "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |  "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |  "type": "User",
      |  "site_admin": false,
      |  "name": "Rafa Paradela",
      |  "company": "47 Degrees",
      |  "blog": "http://rafaparadela.github.io",
      |  "location": "Cádiz, Spain",
      |  "email": "rafa.p@47deg.com",
      |  "hireable": null,
      |  "bio": "Hola",
      |  "public_repos": 36,
      |  "public_gists": 13,
      |  "followers": 41,
      |  "following": 32,
      |  "created_at": "2010-06-26T07:59:52Z",
      |  "updated_at": "2016-05-17T15:43:17Z"
      |}
    """.stripMargin

  val getUsersValidResponse =
    """
      |[
      |  {
      |    "login": "aslakhellesoy",
      |    "id": 1000,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/1000?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/aslakhellesoy",
      |    "html_url": "https://github.com/aslakhellesoy",
      |    "followers_url": "https://api.github.com/users/aslakhellesoy/followers",
      |    "following_url": "https://api.github.com/users/aslakhellesoy/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/aslakhellesoy/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/aslakhellesoy/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/aslakhellesoy/subscriptions",
      |    "organizations_url": "https://api.github.com/users/aslakhellesoy/orgs",
      |    "repos_url": "https://api.github.com/users/aslakhellesoy/repos",
      |    "events_url": "https://api.github.com/users/aslakhellesoy/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/aslakhellesoy/received_events",
      |    "type": "User",
      |    "site_admin": false
      |  }
      | ]
    """.stripMargin

  val newAuthValidResponse =
    """
      |{
      |  "id": 32519338,
      |  "url": "https://api.github.com/authorizations/32519338",
      |  "app": {
      |    "name": "Scala-exercises",
      |    "url": "http://scala-exercises.47deg.com/",
      |    "client_id": "e8e39175648c9db8c280"
      |  },
      |  "token": "1234567890",
      |  "hashed_token": "1234567890",
      |  "token_last_eight": "123456789",
      |  "note": "admin script",
      |  "note_url": null,
      |  "created_at": "2016-05-24T22:39:53Z",
      |  "updated_at": "2016-05-24T22:39:53Z",
      |  "scopes": [
      |    "public_repo"
      |  ],
      |  "fingerprint": null
      |}
    """.stripMargin

  val getAccessTokenValidResponse =
    """
      |{
      |  "access_token": "1234567890",
      |  "token_type": "bearer",
      |  "scope": "public_repo"
      |}
    """.stripMargin

  val badVerificationResponse =
    """
      |{
      |  "error": "bad_verification_code",
      |  "error_description": "The code passed is incorrect or expired.",
      |  "error_uri": "https://developer.github.com/v3/oauth/#bad-verification-code"
      |}
    """.stripMargin

  val getRepoResponse =
    """
      |{
      |  "id": 53343599,
      |  "name": "github4s",
      |  "full_name": "47deg/github4s",
      |  "owner": {
      |    "login": "47deg",
      |    "id": 479857,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/479857?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/47deg",
      |    "html_url": "https://github.com/47deg",
      |    "followers_url": "https://api.github.com/users/47deg/followers",
      |    "following_url": "https://api.github.com/users/47deg/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/47deg/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/47deg/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/47deg/subscriptions",
      |    "organizations_url": "https://api.github.com/users/47deg/orgs",
      |    "repos_url": "https://api.github.com/users/47deg/repos",
      |    "events_url": "https://api.github.com/users/47deg/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/47deg/received_events",
      |    "type": "Organization",
      |    "site_admin": false
      |  },
      |  "private": false,
      |  "html_url": "https://github.com/47deg/github4s",
      |  "description": "A GitHub API wrapper written in Scala",
      |  "fork": false,
      |  "url": "https://api.github.com/repos/47deg/github4s",
      |  "forks_url": "https://api.github.com/repos/47deg/github4s/forks",
      |  "keys_url": "https://api.github.com/repos/47deg/github4s/keys{/key_id}",
      |  "collaborators_url": "https://api.github.com/repos/47deg/github4s/collaborators{/collaborator}",
      |  "teams_url": "https://api.github.com/repos/47deg/github4s/teams",
      |  "hooks_url": "https://api.github.com/repos/47deg/github4s/hooks",
      |  "issue_events_url": "https://api.github.com/repos/47deg/github4s/issues/events{/number}",
      |  "events_url": "https://api.github.com/repos/47deg/github4s/events",
      |  "assignees_url": "https://api.github.com/repos/47deg/github4s/assignees{/user}",
      |  "branches_url": "https://api.github.com/repos/47deg/github4s/branches{/branch}",
      |  "tags_url": "https://api.github.com/repos/47deg/github4s/tags",
      |  "blobs_url": "https://api.github.com/repos/47deg/github4s/git/blobs{/sha}",
      |  "git_tags_url": "https://api.github.com/repos/47deg/github4s/git/tags{/sha}",
      |  "git_refs_url": "https://api.github.com/repos/47deg/github4s/git/refs{/sha}",
      |  "trees_url": "https://api.github.com/repos/47deg/github4s/git/trees{/sha}",
      |  "statuses_url": "https://api.github.com/repos/47deg/github4s/statuses/{sha}",
      |  "languages_url": "https://api.github.com/repos/47deg/github4s/languages",
      |  "stargazers_url": "https://api.github.com/repos/47deg/github4s/stargazers",
      |  "contributors_url": "https://api.github.com/repos/47deg/github4s/contributors",
      |  "subscribers_url": "https://api.github.com/repos/47deg/github4s/subscribers",
      |  "subscription_url": "https://api.github.com/repos/47deg/github4s/subscription",
      |  "commits_url": "https://api.github.com/repos/47deg/github4s/commits{/sha}",
      |  "git_commits_url": "https://api.github.com/repos/47deg/github4s/git/commits{/sha}",
      |  "comments_url": "https://api.github.com/repos/47deg/github4s/comments{/number}",
      |  "issue_comment_url": "https://api.github.com/repos/47deg/github4s/issues/comments{/number}",
      |  "contents_url": "https://api.github.com/repos/47deg/github4s/contents/{+path}",
      |  "compare_url": "https://api.github.com/repos/47deg/github4s/compare/{base}...{head}",
      |  "merges_url": "https://api.github.com/repos/47deg/github4s/merges",
      |  "archive_url": "https://api.github.com/repos/47deg/github4s/{archive_format}{/ref}",
      |  "downloads_url": "https://api.github.com/repos/47deg/github4s/downloads",
      |  "issues_url": "https://api.github.com/repos/47deg/github4s/issues{/number}",
      |  "pulls_url": "https://api.github.com/repos/47deg/github4s/pulls{/number}",
      |  "milestones_url": "https://api.github.com/repos/47deg/github4s/milestones{/number}",
      |  "notifications_url": "https://api.github.com/repos/47deg/github4s/notifications{?since,all,participating}",
      |  "labels_url": "https://api.github.com/repos/47deg/github4s/labels{/name}",
      |  "releases_url": "https://api.github.com/repos/47deg/github4s/releases{/id}",
      |  "deployments_url": "https://api.github.com/repos/47deg/github4s/deployments",
      |  "created_at": "2016-03-07T17:10:37Z",
      |  "updated_at": "2016-05-20T10:46:21Z",
      |  "pushed_at": "2016-05-24T14:45:01Z",
      |  "git_url": "git://github.com/47deg/github4s.git",
      |  "ssh_url": "git@github.com:47deg/github4s.git",
      |  "clone_url": "https://github.com/47deg/github4s.git",
      |  "svn_url": "https://github.com/47deg/github4s",
      |  "homepage": "http://47deg.github.io/github4s",
      |  "size": 458,
      |  "stargazers_count": 0,
      |  "watchers_count": 0,
      |  "language": "Scala",
      |  "has_issues": true,
      |  "has_downloads": true,
      |  "has_wiki": true,
      |  "has_pages": true,
      |  "forks_count": 1,
      |  "mirror_url": null,
      |  "open_issues_count": 5,
      |  "forks": 1,
      |  "open_issues": 5,
      |  "watchers": 0,
      |  "default_branch": "master",
      |  "organization": {
      |    "login": "47deg",
      |    "id": 479857,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/479857?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/47deg",
      |    "html_url": "https://github.com/47deg",
      |    "followers_url": "https://api.github.com/users/47deg/followers",
      |    "following_url": "https://api.github.com/users/47deg/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/47deg/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/47deg/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/47deg/subscriptions",
      |    "organizations_url": "https://api.github.com/users/47deg/orgs",
      |    "repos_url": "https://api.github.com/users/47deg/repos",
      |    "events_url": "https://api.github.com/users/47deg/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/47deg/received_events",
      |    "type": "Organization",
      |    "site_admin": false
      |  },
      |  "network_count": 1,
      |  "subscribers_count": 8
      |}
    """.stripMargin

  val listCommitsValidResponse =
    """
      |[
      |  {
      |    "sha": "5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "commit": {
      |      "author": {
      |        "name": "Rafa Paradela",
      |        "email": "rafa.p@47deg.com",
      |        "date": "2016-05-23T14:00:25Z"
      |      },
      |      "committer": {
      |        "name": "Rafa Paradela",
      |        "email": "rafa.p@47deg.com",
      |        "date": "2016-05-23T14:00:25Z"
      |      },
      |      "message": "Moved/Renamed package to omit organization prefix",
      |      "tree": {
      |        "sha": "373377d6041f110396e5650c2a84b24745d677d0",
      |        "url": "https://api.github.com/repos/47deg/github4s/git/trees/373377d6041f110396e5650c2a84b24745d677d0"
      |      },
      |      "url": "https://api.github.com/repos/47deg/github4s/git/commits/5f919723674b46a8ade67e6a62348953d2bf4350",
      |      "comment_count": 0
      |    },
      |    "url": "https://api.github.com/repos/47deg/github4s/commits/5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "html_url": "https://github.com/47deg/github4s/commit/5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "comments_url": "https://api.github.com/repos/47deg/github4s/commits/5f919723674b46a8ade67e6a62348953d2bf4350/comments",
      |    "author": {
      |      "login": "rafaparadela",
      |      "id": 315070,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/rafaparadela",
      |      "html_url": "https://github.com/rafaparadela",
      |      "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |      "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |      "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |      "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |      "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "committer": {
      |      "login": "rafaparadela",
      |      "id": 315070,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/rafaparadela",
      |      "html_url": "https://github.com/rafaparadela",
      |      "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |      "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |      "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |      "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |      "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "parents": [
      |      {
      |        "sha": "e23072c0637e7ecc2ef6d9a6d090288dc63f6beb",
      |        "url": "https://api.github.com/repos/47deg/github4s/commits/e23072c0637e7ecc2ef6d9a6d090288dc63f6beb",
      |        "html_url": "https://github.com/47deg/github4s/commit/e23072c0637e7ecc2ef6d9a6d090288dc63f6beb"
      |      }
      |    ]
      |  },
      |  {
      |    "sha": "5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "commit": {
      |      "author": {
      |        "name": "Rafa Paradela",
      |        "email": "rafa.p@47deg.com",
      |        "date": "2016-05-23T14:00:25Z"
      |      },
      |      "committer": {
      |        "name": "Rafa Paradela",
      |        "email": "rafa.p@47deg.com",
      |        "date": "2016-05-23T14:00:25Z"
      |      },
      |      "message": "Moved/Renamed package to omit organization prefix",
      |      "tree": {
      |        "sha": "373377d6041f110396e5650c2a84b24745d677d0",
      |        "url": "https://api.github.com/repos/47deg/github4s/git/trees/373377d6041f110396e5650c2a84b24745d677d0"
      |      },
      |      "url": "https://api.github.com/repos/47deg/github4s/git/commits/5f919723674b46a8ade67e6a62348953d2bf4350",
      |      "comment_count": 0
      |    },
      |    "url": "https://api.github.com/repos/47deg/github4s/commits/5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "html_url": "https://github.com/47deg/github4s/commit/5f919723674b46a8ade67e6a62348953d2bf4350",
      |    "comments_url": "https://api.github.com/repos/47deg/github4s/commits/5f919723674b46a8ade67e6a62348953d2bf4350/comments",
      |    "author": null,
      |    "committer": {
      |      "login": "rafaparadela",
      |      "id": 315070,
      |      "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/rafaparadela",
      |      "html_url": "https://github.com/rafaparadela",
      |      "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |      "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |      "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |      "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |      "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "parents": [
      |      {
      |        "sha": "e23072c0637e7ecc2ef6d9a6d090288dc63f6beb",
      |        "url": "https://api.github.com/repos/47deg/github4s/commits/e23072c0637e7ecc2ef6d9a6d090288dc63f6beb",
      |        "html_url": "https://github.com/47deg/github4s/commit/e23072c0637e7ecc2ef6d9a6d090288dc63f6beb"
      |      }
      |    ]
      |  }
      |]
    """.stripMargin

  val listContributorsValidResponse =
    """
      |[
      |  {
      |    "login": "dialelo",
      |    "id": 409039,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/409039?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/dialelo",
      |    "html_url": "https://github.com/dialelo",
      |    "followers_url": "https://api.github.com/users/dialelo/followers",
      |    "following_url": "https://api.github.com/users/dialelo/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/dialelo/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/dialelo/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/dialelo/subscriptions",
      |    "organizations_url": "https://api.github.com/users/dialelo/orgs",
      |    "repos_url": "https://api.github.com/users/dialelo/repos",
      |    "events_url": "https://api.github.com/users/dialelo/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/dialelo/received_events",
      |    "type": "User",
      |    "site_admin": false,
      |    "contributions": 226
      |  },
      |  {
      |    "login": "raulraja",
      |    "id": 456796,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/456796?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/raulraja",
      |    "html_url": "https://github.com/raulraja",
      |    "followers_url": "https://api.github.com/users/raulraja/followers",
      |    "following_url": "https://api.github.com/users/raulraja/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/raulraja/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/raulraja/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/raulraja/subscriptions",
      |    "organizations_url": "https://api.github.com/users/raulraja/orgs",
      |    "repos_url": "https://api.github.com/users/raulraja/repos",
      |    "events_url": "https://api.github.com/users/raulraja/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/raulraja/received_events",
      |    "type": "User",
      |    "site_admin": false,
      |    "contributions": 11
      |  },
      |  {
      |    "login": "rafaparadela",
      |    "id": 315070,
      |    "avatar_url": "https://avatars.githubusercontent.com/u/315070?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/rafaparadela",
      |    "html_url": "https://github.com/rafaparadela",
      |    "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |    "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |    "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |    "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |    "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |    "type": "User",
      |    "site_admin": false,
      |    "contributions": 2
      |  }
      |]
    """.stripMargin

  val emptyListResponse =
    """
      |[]
    """.stripMargin

  val notFoundResponse =
    """
      |{
      |  "message": "Not Found",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

  val unauthorizedReponse =
    """
      |{
      |  "message": "Requires authentication",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

  val badCredentialsResponse =
    """
      |{
      |  "message": "Bad credentials",
      |  "documentation_url": "https://developer.github.com/v3"
      |}
    """.stripMargin

  val newGistValidResponse =
    """
      |{
      |  "url": "https://api.github.com/gists/aa5a315d61ae9438b18d",
      |  "forks_url": "https://api.github.com/gists/aa5a315d61ae9438b18d/forks",
      |  "commits_url": "https://api.github.com/gists/aa5a315d61ae9438b18d/commits",
      |  "id": "aa5a315d61ae9438b18d",
      |  "description": "description of gist",
      |  "public": true,
      |  "owner": {
      |    "login": "rafaparadela",
      |    "id": 1,
      |    "avatar_url": "hhttps://avatars.githubusercontent.com/u/315070?v=3",
      |    "gravatar_id": "",
      |    "url": "https://api.github.com/users/rafaparadela",
      |    "html_url": "https://github.com/rafaparadela",
      |    "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |    "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |    "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |    "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |    "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |    "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |    "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |    "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |    "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |    "type": "User",
      |    "site_admin": false
      |  },
      |  "user": null,
      |  "files": {
      |    "ring.erl": {
      |      "size": 932,
      |      "raw_url": "https://gist.githubusercontent.com/raw/365370/8c4d2d43d178df44f4c03a7f2ac0ff512853564e/ring.erl",
      |      "type": "text/plain",
      |      "language": "Erlang",
      |      "truncated": false,
      |      "content": "contents of gist"
      |    }
      |  },
      |  "truncated": false,
      |  "comments": 0,
      |  "comments_url": "https://api.github.com/gists/aa5a315d61ae9438b18d/comments/",
      |  "html_url": "https://gist.github.com/aa5a315d61ae9438b18d",
      |  "git_pull_url": "https://gist.github.com/aa5a315d61ae9438b18d.git",
      |  "git_push_url": "https://gist.github.com/aa5a315d61ae9438b18d.git",
      |  "created_at": "2010-04-14T02:15:15Z",
      |  "updated_at": "2011-06-20T11:34:15Z",
      |  "forks": [],
      |  "history": [
      |    {
      |      "url": "https://api.github.com/gists/aa5a315d61ae9438b18d/57a7f021a713b1c5a6a199b54cc514735d2d462f",
      |      "version": "57a7f021a713b1c5a6a199b54cc514735d2d462f",
      |      "user": {
      |        "login": "rafaparadela",
      |        "id": 1,
      |        "avatar_url": "hhttps://avatars.githubusercontent.com/u/315070?v=3",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/rafaparadela",
      |        "html_url": "https://github.com/rafaparadela",
      |        "followers_url": "https://api.github.com/users/rafaparadela/followers",
      |        "following_url": "https://api.github.com/users/rafaparadela/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/rafaparadela/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/rafaparadela/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/rafaparadela/subscriptions",
      |        "organizations_url": "https://api.github.com/users/rafaparadela/orgs",
      |        "repos_url": "https://api.github.com/users/rafaparadela/repos",
      |        "events_url": "https://api.github.com/users/rafaparadela/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/rafaparadela/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "change_status": {
      |        "deletions": 0,
      |        "additions": 180,
      |        "total": 180
      |      },
      |      "committed_at": "2010-04-14T02:15:15Z"
      |    }
      |  ]
      |}
    """.stripMargin

  val singleReference =
    """
      |{
      |  "ref": "refs/heads/featureA",
      |  "url": "https://api.github.com/repos/octocat/Hello-World/git/refs/heads/featureA",
      |  "object": {
      |    "type": "commit",
      |    "sha": "aa218f56b14c9653891f9e74264a383fa43fefbd",
      |    "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/aa218f56b14c9653891f9e74264a383fa43fefbd"
      |  }
      |}
    """.stripMargin

  val multipleReference =
    """
      |[
      |  {
      |    "ref": "refs/heads/feature-a",
      |    "url": "https://api.github.com/repos/octocat/Hello-World/git/refs/heads/feature-a",
      |    "object": {
      |      "type": "commit",
      |      "sha": "aa218f56b14c9653891f9e74264a383fa43fefbd",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/aa218f56b14c9653891f9e74264a383fa43fefbd"
      |    }
      |  },
      |  {
      |    "ref": "refs/heads/feature-b",
      |    "url": "https://api.github.com/repos/octocat/Hello-World/git/refs/heads/feature-b",
      |    "object": {
      |      "type": "commit",
      |      "sha": "612077ae6dffb4d2fbd8ce0cccaa58893b07b5ac",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/612077ae6dffb4d2fbd8ce0cccaa58893b07b5ac"
      |    }
      |  }
      |]
    """.stripMargin

  val commitResult =
    """
      |{
      |  "sha": "7638417db6d59f3c431d3e1f261cc637155684cd",
      |  "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/7638417db6d59f3c431d3e1f261cc637155684cd",
      |  "author": {
      |    "date": "2014-11-07T22:01:45Z",
      |    "name": "Scott Chacon",
      |    "email": "schacon@gmail.com"
      |  },
      |  "committer": {
      |    "date": "2014-11-07T22:01:45Z",
      |    "name": "Scott Chacon",
      |    "email": "schacon@gmail.com"
      |  },
      |  "message": "added readme, because im a good github citizen",
      |  "tree": {
      |    "url": "https://api.github.com/repos/octocat/Hello-World/git/trees/691272480426f78a0138979dd3ce63b77f706feb",
      |    "sha": "691272480426f78a0138979dd3ce63b77f706feb"
      |  },
      |  "parents": [
      |    {
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/1acc419d4d6a9ce985db7be48c6349a0475975b5",
      |      "sha": "1acc419d4d6a9ce985db7be48c6349a0475975b5"
      |    }
      |  ]
      |}
    """.stripMargin

  val blobResult =
    """
      |{
      |  "url": "https://api.github.com/repos/octocat/example/git/blobs/3a0f86fb8db8eea7ccbb9a95f325ddbedfb25e15",
      |  "sha": "3a0f86fb8db8eea7ccbb9a95f325ddbedfb25e15"
      |}
    """.stripMargin

  val treeResult =
    """
      |{
      |  "sha": "9fb037999f264ba9a7fc6274d15fa3ae2ab98312",
      |  "url": "https://api.github.com/repos/octocat/Hello-World/trees/9fb037999f264ba9a7fc6274d15fa3ae2ab98312",
      |  "tree": [
      |    {
      |      "path": "file.rb",
      |      "mode": "100644",
      |      "type": "blob",
      |      "size": 30,
      |      "sha": "44b4fc6d56897b048c772eb4087f854f46256132",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/blobs/44b4fc6d56897b048c772eb4087f854f46256132"
      |    },
      |    {
      |      "path": "subdir",
      |      "mode": "040000",
      |      "type": "tree",
      |      "sha": "f484d249c660418515fb01c2b9662073663c242e",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/blobs/f484d249c660418515fb01c2b9662073663c242e"
      |    },
      |    {
      |      "path": "exec_file",
      |      "mode": "100755",
      |      "type": "blob",
      |      "size": 75,
      |      "sha": "45b983be36b73c0788dc9cbcb76cbb80fc7bb057",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/blobs/45b983be36b73c0788dc9cbcb76cbb80fc7bb057"
      |    }
      |  ],
      |  "truncated": false
      |}
    """.stripMargin

  val createTreeResult =
    """
      |{
      |  "sha": "cd8274d15fa3ae2ab983129fb037999f264ba9a7",
      |  "url": "https://api.github.com/repos/octocat/Hello-World/trees/cd8274d15fa3ae2ab983129fb037999f264ba9a7",
      |  "tree": [
      |    {
      |      "path": "file.rb",
      |      "mode": "100644",
      |      "type": "blob",
      |      "size": 132,
      |      "sha": "7c258a9869f33c1e1e1f74fbb32f07c86cb5a75b",
      |      "url": "https://api.github.com/repos/octocat/Hello-World/git/blobs/7c258a9869f33c1e1e1f74fbb32f07c86cb5a75b"
      |    }
      |  ]
      |}
    """.stripMargin

  val listPullRequestsValidResponse =
    """
      |[
      |  {
      |    "id": 1,
      |    "url": "https://api.github.com/repos/octocat/Hello-World/pulls/1347",
      |    "html_url": "https://github.com/octocat/Hello-World/pull/1347",
      |    "diff_url": "https://github.com/octocat/Hello-World/pull/1347.diff",
      |    "patch_url": "https://github.com/octocat/Hello-World/pull/1347.patch",
      |    "issue_url": "https://api.github.com/repos/octocat/Hello-World/issues/1347",
      |    "commits_url": "https://api.github.com/repos/octocat/Hello-World/pulls/1347/commits",
      |    "review_comments_url": "https://api.github.com/repos/octocat/Hello-World/pulls/1347/comments",
      |    "review_comment_url": "https://api.github.com/repos/octocat/Hello-World/pulls/comments{/number}",
      |    "comments_url": "https://api.github.com/repos/octocat/Hello-World/issues/1347/comments",
      |    "statuses_url": "https://api.github.com/repos/octocat/Hello-World/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e",
      |    "number": 1347,
      |    "state": "open",
      |    "title": "new-feature",
      |    "body": "Please pull these awesome changes",
      |    "assignee": {
      |      "login": "octocat",
      |      "id": 1,
      |      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/octocat",
      |      "html_url": "https://github.com/octocat",
      |      "followers_url": "https://api.github.com/users/octocat/followers",
      |      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |      "organizations_url": "https://api.github.com/users/octocat/orgs",
      |      "repos_url": "https://api.github.com/users/octocat/repos",
      |      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/octocat/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    },
      |    "milestone": {
      |      "url": "https://api.github.com/repos/octocat/Hello-World/milestones/1",
      |      "html_url": "https://github.com/octocat/Hello-World/milestones/v1.0",
      |      "labels_url": "https://api.github.com/repos/octocat/Hello-World/milestones/1/labels",
      |      "id": 1002604,
      |      "number": 1,
      |      "state": "open",
      |      "title": "v1.0",
      |      "description": "Tracking milestone for version 1.0",
      |      "creator": {
      |        "login": "octocat",
      |        "id": 1,
      |        "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/octocat",
      |        "html_url": "https://github.com/octocat",
      |        "followers_url": "https://api.github.com/users/octocat/followers",
      |        "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |        "organizations_url": "https://api.github.com/users/octocat/orgs",
      |        "repos_url": "https://api.github.com/users/octocat/repos",
      |        "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/octocat/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "open_issues": 4,
      |      "closed_issues": 8,
      |      "created_at": "2011-04-10T20:09:31Z",
      |      "updated_at": "2014-03-03T18:58:10Z",
      |      "closed_at": "2013-02-12T13:22:01Z",
      |      "due_on": "2012-10-09T23:39:01Z"
      |    },
      |    "locked": false,
      |    "created_at": "2011-01-26T19:01:12Z",
      |    "updated_at": "2011-01-26T19:01:12Z",
      |    "closed_at": "2011-01-26T19:01:12Z",
      |    "merged_at": "2011-01-26T19:01:12Z",
      |    "head": {
      |      "label": "new-topic",
      |      "ref": "new-topic",
      |      "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
      |      "user": {
      |        "login": "octocat",
      |        "id": 1,
      |        "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/octocat",
      |        "html_url": "https://github.com/octocat",
      |        "followers_url": "https://api.github.com/users/octocat/followers",
      |        "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |        "organizations_url": "https://api.github.com/users/octocat/orgs",
      |        "repos_url": "https://api.github.com/users/octocat/repos",
      |        "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/octocat/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "repo": {
      |        "id": 1296269,
      |        "owner": {
      |          "login": "octocat",
      |          "id": 1,
      |          "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |          "gravatar_id": "",
      |          "url": "https://api.github.com/users/octocat",
      |          "html_url": "https://github.com/octocat",
      |          "followers_url": "https://api.github.com/users/octocat/followers",
      |          "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |          "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |          "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |          "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |          "organizations_url": "https://api.github.com/users/octocat/orgs",
      |          "repos_url": "https://api.github.com/users/octocat/repos",
      |          "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |          "received_events_url": "https://api.github.com/users/octocat/received_events",
      |          "type": "User",
      |          "site_admin": false
      |        },
      |        "name": "Hello-World",
      |        "full_name": "octocat/Hello-World",
      |        "description": "This your first repo!",
      |        "private": false,
      |        "fork": false,
      |        "url": "https://api.github.com/repos/octocat/Hello-World",
      |        "html_url": "https://github.com/octocat/Hello-World",
      |        "archive_url": "http://api.github.com/repos/octocat/Hello-World/{archive_format}{/ref}",
      |        "assignees_url": "http://api.github.com/repos/octocat/Hello-World/assignees{/user}",
      |        "blobs_url": "http://api.github.com/repos/octocat/Hello-World/git/blobs{/sha}",
      |        "branches_url": "http://api.github.com/repos/octocat/Hello-World/branches{/branch}",
      |        "clone_url": "https://github.com/octocat/Hello-World.git",
      |        "collaborators_url": "http://api.github.com/repos/octocat/Hello-World/collaborators{/collaborator}",
      |        "comments_url": "http://api.github.com/repos/octocat/Hello-World/comments{/number}",
      |        "commits_url": "http://api.github.com/repos/octocat/Hello-World/commits{/sha}",
      |        "compare_url": "http://api.github.com/repos/octocat/Hello-World/compare/{base}...{head}",
      |        "contents_url": "http://api.github.com/repos/octocat/Hello-World/contents/{+path}",
      |        "contributors_url": "http://api.github.com/repos/octocat/Hello-World/contributors",
      |        "deployments_url": "http://api.github.com/repos/octocat/Hello-World/deployments",
      |        "downloads_url": "http://api.github.com/repos/octocat/Hello-World/downloads",
      |        "events_url": "http://api.github.com/repos/octocat/Hello-World/events",
      |        "forks_url": "http://api.github.com/repos/octocat/Hello-World/forks",
      |        "git_commits_url": "http://api.github.com/repos/octocat/Hello-World/git/commits{/sha}",
      |        "git_refs_url": "http://api.github.com/repos/octocat/Hello-World/git/refs{/sha}",
      |        "git_tags_url": "http://api.github.com/repos/octocat/Hello-World/git/tags{/sha}",
      |        "git_url": "git:github.com/octocat/Hello-World.git",
      |        "hooks_url": "http://api.github.com/repos/octocat/Hello-World/hooks",
      |        "issue_comment_url": "http://api.github.com/repos/octocat/Hello-World/issues/comments{/number}",
      |        "issue_events_url": "http://api.github.com/repos/octocat/Hello-World/issues/events{/number}",
      |        "issues_url": "http://api.github.com/repos/octocat/Hello-World/issues{/number}",
      |        "keys_url": "http://api.github.com/repos/octocat/Hello-World/keys{/key_id}",
      |        "labels_url": "http://api.github.com/repos/octocat/Hello-World/labels{/name}",
      |        "languages_url": "http://api.github.com/repos/octocat/Hello-World/languages",
      |        "merges_url": "http://api.github.com/repos/octocat/Hello-World/merges",
      |        "milestones_url": "http://api.github.com/repos/octocat/Hello-World/milestones{/number}",
      |        "mirror_url": "git:git.example.com/octocat/Hello-World",
      |        "notifications_url": "http://api.github.com/repos/octocat/Hello-World/notifications{?since, all, participating}",
      |        "pulls_url": "http://api.github.com/repos/octocat/Hello-World/pulls{/number}",
      |        "releases_url": "http://api.github.com/repos/octocat/Hello-World/releases{/id}",
      |        "ssh_url": "git@github.com:octocat/Hello-World.git",
      |        "stargazers_url": "http://api.github.com/repos/octocat/Hello-World/stargazers",
      |        "statuses_url": "http://api.github.com/repos/octocat/Hello-World/statuses/{sha}",
      |        "subscribers_url": "http://api.github.com/repos/octocat/Hello-World/subscribers",
      |        "subscription_url": "http://api.github.com/repos/octocat/Hello-World/subscription",
      |        "svn_url": "https://svn.github.com/octocat/Hello-World",
      |        "tags_url": "http://api.github.com/repos/octocat/Hello-World/tags",
      |        "teams_url": "http://api.github.com/repos/octocat/Hello-World/teams",
      |        "trees_url": "http://api.github.com/repos/octocat/Hello-World/git/trees{/sha}",
      |        "homepage": "https://github.com",
      |        "language": null,
      |        "forks_count": 9,
      |        "stargazers_count": 80,
      |        "watchers_count": 80,
      |        "size": 108,
      |        "default_branch": "master",
      |        "open_issues_count": 0,
      |        "topics": [
      |          "octocat",
      |          "atom",
      |          "electron",
      |          "API"
      |        ],
      |        "has_issues": true,
      |        "has_projects": true,
      |        "has_wiki": true,
      |        "has_pages": false,
      |        "has_downloads": true,
      |        "pushed_at": "2011-01-26T19:06:43Z",
      |        "created_at": "2011-01-26T19:01:12Z",
      |        "updated_at": "2011-01-26T19:14:43Z",
      |        "permissions": {
      |          "admin": false,
      |          "push": false,
      |          "pull": true
      |        }
      |      }
      |    },
      |    "base": {
      |      "label": "master",
      |      "ref": "master",
      |      "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
      |      "user": {
      |        "login": "octocat",
      |        "id": 1,
      |        "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |        "gravatar_id": "",
      |        "url": "https://api.github.com/users/octocat",
      |        "html_url": "https://github.com/octocat",
      |        "followers_url": "https://api.github.com/users/octocat/followers",
      |        "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |        "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |        "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |        "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |        "organizations_url": "https://api.github.com/users/octocat/orgs",
      |        "repos_url": "https://api.github.com/users/octocat/repos",
      |        "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |        "received_events_url": "https://api.github.com/users/octocat/received_events",
      |        "type": "User",
      |        "site_admin": false
      |      },
      |      "repo": {
      |        "id": 1296269,
      |        "owner": {
      |          "login": "octocat",
      |          "id": 1,
      |          "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |          "gravatar_id": "",
      |          "url": "https://api.github.com/users/octocat",
      |          "html_url": "https://github.com/octocat",
      |          "followers_url": "https://api.github.com/users/octocat/followers",
      |          "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |          "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |          "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |          "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |          "organizations_url": "https://api.github.com/users/octocat/orgs",
      |          "repos_url": "https://api.github.com/users/octocat/repos",
      |          "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |          "received_events_url": "https://api.github.com/users/octocat/received_events",
      |          "type": "User",
      |          "site_admin": false
      |        },
      |        "name": "Hello-World",
      |        "full_name": "octocat/Hello-World",
      |        "description": "This your first repo!",
      |        "private": false,
      |        "fork": false,
      |        "url": "https://api.github.com/repos/octocat/Hello-World",
      |        "html_url": "https://github.com/octocat/Hello-World",
      |        "archive_url": "http://api.github.com/repos/octocat/Hello-World/{archive_format}{/ref}",
      |        "assignees_url": "http://api.github.com/repos/octocat/Hello-World/assignees{/user}",
      |        "blobs_url": "http://api.github.com/repos/octocat/Hello-World/git/blobs{/sha}",
      |        "branches_url": "http://api.github.com/repos/octocat/Hello-World/branches{/branch}",
      |        "clone_url": "https://github.com/octocat/Hello-World.git",
      |        "collaborators_url": "http://api.github.com/repos/octocat/Hello-World/collaborators{/collaborator}",
      |        "comments_url": "http://api.github.com/repos/octocat/Hello-World/comments{/number}",
      |        "commits_url": "http://api.github.com/repos/octocat/Hello-World/commits{/sha}",
      |        "compare_url": "http://api.github.com/repos/octocat/Hello-World/compare/{base}...{head}",
      |        "contents_url": "http://api.github.com/repos/octocat/Hello-World/contents/{+path}",
      |        "contributors_url": "http://api.github.com/repos/octocat/Hello-World/contributors",
      |        "deployments_url": "http://api.github.com/repos/octocat/Hello-World/deployments",
      |        "downloads_url": "http://api.github.com/repos/octocat/Hello-World/downloads",
      |        "events_url": "http://api.github.com/repos/octocat/Hello-World/events",
      |        "forks_url": "http://api.github.com/repos/octocat/Hello-World/forks",
      |        "git_commits_url": "http://api.github.com/repos/octocat/Hello-World/git/commits{/sha}",
      |        "git_refs_url": "http://api.github.com/repos/octocat/Hello-World/git/refs{/sha}",
      |        "git_tags_url": "http://api.github.com/repos/octocat/Hello-World/git/tags{/sha}",
      |        "git_url": "git:github.com/octocat/Hello-World.git",
      |        "hooks_url": "http://api.github.com/repos/octocat/Hello-World/hooks",
      |        "issue_comment_url": "http://api.github.com/repos/octocat/Hello-World/issues/comments{/number}",
      |        "issue_events_url": "http://api.github.com/repos/octocat/Hello-World/issues/events{/number}",
      |        "issues_url": "http://api.github.com/repos/octocat/Hello-World/issues{/number}",
      |        "keys_url": "http://api.github.com/repos/octocat/Hello-World/keys{/key_id}",
      |        "labels_url": "http://api.github.com/repos/octocat/Hello-World/labels{/name}",
      |        "languages_url": "http://api.github.com/repos/octocat/Hello-World/languages",
      |        "merges_url": "http://api.github.com/repos/octocat/Hello-World/merges",
      |        "milestones_url": "http://api.github.com/repos/octocat/Hello-World/milestones{/number}",
      |        "mirror_url": "git:git.example.com/octocat/Hello-World",
      |        "notifications_url": "http://api.github.com/repos/octocat/Hello-World/notifications{?since, all, participating}",
      |        "pulls_url": "http://api.github.com/repos/octocat/Hello-World/pulls{/number}",
      |        "releases_url": "http://api.github.com/repos/octocat/Hello-World/releases{/id}",
      |        "ssh_url": "git@github.com:octocat/Hello-World.git",
      |        "stargazers_url": "http://api.github.com/repos/octocat/Hello-World/stargazers",
      |        "statuses_url": "http://api.github.com/repos/octocat/Hello-World/statuses/{sha}",
      |        "subscribers_url": "http://api.github.com/repos/octocat/Hello-World/subscribers",
      |        "subscription_url": "http://api.github.com/repos/octocat/Hello-World/subscription",
      |        "svn_url": "https://svn.github.com/octocat/Hello-World",
      |        "tags_url": "http://api.github.com/repos/octocat/Hello-World/tags",
      |        "teams_url": "http://api.github.com/repos/octocat/Hello-World/teams",
      |        "trees_url": "http://api.github.com/repos/octocat/Hello-World/git/trees{/sha}",
      |        "homepage": "https://github.com",
      |        "language": null,
      |        "forks_count": 9,
      |        "stargazers_count": 80,
      |        "watchers_count": 80,
      |        "size": 108,
      |        "default_branch": "master",
      |        "open_issues_count": 0,
      |        "topics": [
      |          "octocat",
      |          "atom",
      |          "electron",
      |          "API"
      |        ],
      |        "has_issues": true,
      |        "has_projects": true,
      |        "has_wiki": true,
      |        "has_pages": false,
      |        "has_downloads": true,
      |        "pushed_at": "2011-01-26T19:06:43Z",
      |        "created_at": "2011-01-26T19:01:12Z",
      |        "updated_at": "2011-01-26T19:14:43Z",
      |        "permissions": {
      |          "admin": false,
      |          "push": false,
      |          "pull": true
      |        }
      |      }
      |    },
      |    "_links": {
      |      "self": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/pulls/1347"
      |      },
      |      "html": {
      |        "href": "https://github.com/octocat/Hello-World/pull/1347"
      |      },
      |      "issue": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/issues/1347"
      |      },
      |      "comments": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/issues/1347/comments"
      |      },
      |      "review_comments": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/pulls/1347/comments"
      |      },
      |      "review_comment": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/pulls/comments{/number}"
      |      },
      |      "commits": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/pulls/1347/commits"
      |      },
      |      "statuses": {
      |        "href": "https://api.github.com/repos/octocat/Hello-World/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e"
      |      }
      |    },
      |    "user": {
      |      "login": "octocat",
      |      "id": 1,
      |      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      |      "gravatar_id": "",
      |      "url": "https://api.github.com/users/octocat",
      |      "html_url": "https://github.com/octocat",
      |      "followers_url": "https://api.github.com/users/octocat/followers",
      |      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      |      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      |      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      |      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      |      "organizations_url": "https://api.github.com/users/octocat/orgs",
      |      "repos_url": "https://api.github.com/users/octocat/repos",
      |      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      |      "received_events_url": "https://api.github.com/users/octocat/received_events",
      |      "type": "User",
      |      "site_admin": false
      |    }
      |  }
      |]
    """.stripMargin
}
