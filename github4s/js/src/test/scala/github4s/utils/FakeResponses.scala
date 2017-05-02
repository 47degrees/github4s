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

  val emptyListResponse =
    """
      |[]
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

  val getCombinedStatusValidResponse =
    """
    {
      "state": "success",
      "statuses": [
        {
          "url": "https://api.github.com/repos/47deg/github4s/statuses/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
          "id": 1142923124,
          "state": "success",
          "description": "The Travis CI build passed",
          "target_url": "https://travis-ci.org/47deg/github4s/builds/220706044",
          "context": "continuous-integration/travis-ci/push",
          "created_at": "2017-04-10T20:28:21Z",
          "updated_at": "2017-04-10T20:28:21Z"
        },
        {
          "url": "https://api.github.com/repos/47deg/github4s/statuses/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
          "id": 1142928020,
          "state": "success",
          "description": "80.24% remains the same compared to a6f4c12",
          "target_url": "https://codecov.io/gh/47deg/github4s/commit/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
          "context": "codecov/project",
          "created_at": "2017-04-10T20:29:58Z",
          "updated_at": "2017-04-10T20:29:58Z"
        },
        {
          "url": "https://api.github.com/repos/47deg/github4s/statuses/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
          "id": 1142928022,
          "state": "success",
          "description": "Coverage not affected when comparing a6f4c12...e20eab9",
          "target_url": "https://codecov.io/gh/47deg/github4s/commit/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
          "context": "codecov/patch",
          "created_at": "2017-04-10T20:29:58Z",
          "updated_at": "2017-04-10T20:29:58Z"
        }
      ],
      "sha": "e20eab90fef0fc02abc96868713a57ac8e5eaf50",
      "total_count": 3,
      "repository": {
        "id": 53343599,
        "name": "github4s",
        "full_name": "47deg/github4s",
        "owner": {
          "login": "47deg",
          "id": 479857,
          "avatar_url": "https://avatars0.githubusercontent.com/u/479857?v=3",
          "gravatar_id": "",
          "url": "https://api.github.com/users/47deg",
          "html_url": "https://github.com/47deg",
          "followers_url": "https://api.github.com/users/47deg/followers",
          "following_url": "https://api.github.com/users/47deg/following{/other_user}",
          "gists_url": "https://api.github.com/users/47deg/gists{/gist_id}",
          "starred_url": "https://api.github.com/users/47deg/starred{/owner}{/repo}",
          "subscriptions_url": "https://api.github.com/users/47deg/subscriptions",
          "organizations_url": "https://api.github.com/users/47deg/orgs",
          "repos_url": "https://api.github.com/users/47deg/repos",
          "events_url": "https://api.github.com/users/47deg/events{/privacy}",
          "received_events_url": "https://api.github.com/users/47deg/received_events",
          "type": "Organization",
          "site_admin": false
        },
        "private": false,
        "html_url": "https://github.com/47deg/github4s",
        "description": "A GitHub API wrapper written in Scala",
        "fork": false,
        "url": "https://api.github.com/repos/47deg/github4s",
        "forks_url": "https://api.github.com/repos/47deg/github4s/forks",
        "keys_url": "https://api.github.com/repos/47deg/github4s/keys{/key_id}",
        "collaborators_url": "https://api.github.com/repos/47deg/github4s/collaborators{/collaborator}",
        "teams_url": "https://api.github.com/repos/47deg/github4s/teams",
        "hooks_url": "https://api.github.com/repos/47deg/github4s/hooks",
        "issue_events_url": "https://api.github.com/repos/47deg/github4s/issues/events{/number}",
        "events_url": "https://api.github.com/repos/47deg/github4s/events",
        "assignees_url": "https://api.github.com/repos/47deg/github4s/assignees{/user}",
        "branches_url": "https://api.github.com/repos/47deg/github4s/branches{/branch}",
        "tags_url": "https://api.github.com/repos/47deg/github4s/tags",
        "blobs_url": "https://api.github.com/repos/47deg/github4s/git/blobs{/sha}",
        "git_tags_url": "https://api.github.com/repos/47deg/github4s/git/tags{/sha}",
        "git_refs_url": "https://api.github.com/repos/47deg/github4s/git/refs{/sha}",
        "trees_url": "https://api.github.com/repos/47deg/github4s/git/trees{/sha}",
        "statuses_url": "https://api.github.com/repos/47deg/github4s/statuses/{sha}",
        "languages_url": "https://api.github.com/repos/47deg/github4s/languages",
        "stargazers_url": "https://api.github.com/repos/47deg/github4s/stargazers",
        "contributors_url": "https://api.github.com/repos/47deg/github4s/contributors",
        "subscribers_url": "https://api.github.com/repos/47deg/github4s/subscribers",
        "subscription_url": "https://api.github.com/repos/47deg/github4s/subscription",
        "commits_url": "https://api.github.com/repos/47deg/github4s/commits{/sha}",
        "git_commits_url": "https://api.github.com/repos/47deg/github4s/git/commits{/sha}",
        "comments_url": "https://api.github.com/repos/47deg/github4s/comments{/number}",
        "issue_comment_url": "https://api.github.com/repos/47deg/github4s/issues/comments{/number}",
        "contents_url": "https://api.github.com/repos/47deg/github4s/contents/{+path}",
        "compare_url": "https://api.github.com/repos/47deg/github4s/compare/{base}...{head}",
        "merges_url": "https://api.github.com/repos/47deg/github4s/merges",
        "archive_url": "https://api.github.com/repos/47deg/github4s/{archive_format}{/ref}",
        "downloads_url": "https://api.github.com/repos/47deg/github4s/downloads",
        "issues_url": "https://api.github.com/repos/47deg/github4s/issues{/number}",
        "pulls_url": "https://api.github.com/repos/47deg/github4s/pulls{/number}",
        "milestones_url": "https://api.github.com/repos/47deg/github4s/milestones{/number}",
        "notifications_url": "https://api.github.com/repos/47deg/github4s/notifications{?since,all,participating}",
        "labels_url": "https://api.github.com/repos/47deg/github4s/labels{/name}",
        "releases_url": "https://api.github.com/repos/47deg/github4s/releases{/id}",
        "deployments_url": "https://api.github.com/repos/47deg/github4s/deployments"
      },
      "commit_url": "https://api.github.com/repos/47deg/github4s/commits/e20eab90fef0fc02abc96868713a57ac8e5eaf50",
      "url": "https://api.github.com/repos/47deg/github4s/commits/e20eab90fef0fc02abc96868713a57ac8e5eaf50/status"
    }
    """

}
