/*
 * Copyright (c) 2016 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package github4s

import github4s.free.domain._
import io.circe._, io.circe.generic.auto._, io.circe.jawn._, io.circe.syntax._

/** Implicit circe decoders of domains objects */
object Decoders {
  case class Author(login: Option[String], avatar_url: Option[String], html_url: Option[String])

  implicit val decodeCommit: Decoder[Commit] = Decoder.instance { c ⇒
    for {
      sha     ← c.downField("sha").as[String]
      message ← c.downField("commit").downField("message").as[String]
      date    ← c.downField("commit").downField("author").downField("date").as[String]
      url     ← c.downField("html_url").as[String]
      author  ← c.downField("author").as[Option[Author]]
    } yield
      Commit(
        sha = sha,
        message = message,
        date = date,
        url = url,
        login = author.map(_.login).flatten,
        avatar_url = author.map(_.avatar_url).flatten,
        author_url = author.map(_.html_url).flatten
      )
  }

  implicit val decodeRepository: Decoder[Repository] = Decoder.instance { c ⇒
    for {
      id                ← c.downField("id").as[Int]
      name              ← c.downField("name").as[String]
      full_name         ← c.downField("full_name").as[String]
      owner             ← c.downField("owner").as[User]
      priv              ← c.downField("private").as[Boolean]
      description       ← c.downField("description").as[String]
      fork              ← c.downField("fork").as[Boolean]
      created_at        ← c.downField("created_at").as[String]
      updated_at        ← c.downField("updated_at").as[String]
      pushed_at         ← c.downField("pushed_at").as[String]
      homepage          ← c.downField("homepage").as[Option[String]]
      language          ← c.downField("language").as[Option[String]]
      organization      ← c.downField("organization").as[Option[User]]
      size              ← c.downField("size").as[Int]
      stargazers_count  ← c.downField("stargazers_count").as[Int]
      watchers_count    ← c.downField("watchers_count").as[Int]
      forks_count       ← c.downField("forks_count").as[Int]
      open_issues_count ← c.downField("open_issues_count").as[Int]
      open_issues       ← c.downField("open_issues").as[Int]
      watchers          ← c.downField("watchers").as[Int]
      network_count     ← c.downField("network_count").as[Int]
      subscribers_count ← c.downField("subscribers_count").as[Int]
      has_issues        ← c.downField("has_issues").as[Boolean]
      has_downloads     ← c.downField("has_downloads").as[Boolean]
      has_wiki          ← c.downField("has_wiki").as[Boolean]
      has_pages         ← c.downField("has_pages").as[Boolean]
      url               ← c.downField("url").as[String]
      html_url          ← c.downField("html_url").as[String]
      git_url           ← c.downField("git_url").as[String]
      ssh_url           ← c.downField("ssh_url").as[String]
      clone_url         ← c.downField("clone_url").as[String]
      svn_url           ← c.downField("svn_url").as[String]
      forks_url         ← c.downField("forks_url").as[Option[String]]
      keys_url          ← c.downField("keys_url").as[Option[String]]
      collaborators_url ← c.downField("collaborators_url").as[Option[String]]
      teams_url         ← c.downField("teams_url").as[Option[String]]
      hooks_url         ← c.downField("hooks_url").as[Option[String]]
      issue_events_url  ← c.downField("issue_events_url").as[Option[String]]
      events_url        ← c.downField("events_url").as[Option[String]]
      assignees_url     ← c.downField("assignees_url").as[Option[String]]
      branches_url      ← c.downField("branches_url").as[Option[String]]
      tags_url          ← c.downField("tags_url").as[Option[String]]
      blobs_url         ← c.downField("blobs_url").as[Option[String]]
      git_tags_url      ← c.downField("git_tags_url").as[Option[String]]
      git_refs_url      ← c.downField("git_refs_url").as[Option[String]]
      trees_url         ← c.downField("trees_url").as[Option[String]]
      statuses_url      ← c.downField("statuses_url").as[Option[String]]
      languages_url     ← c.downField("languages_url").as[Option[String]]
      stargazers_url    ← c.downField("stargazers_url").as[Option[String]]
      contributors_url  ← c.downField("contributors_url").as[Option[String]]
      subscribers_url   ← c.downField("subscribers_url").as[Option[String]]
      subscription_url  ← c.downField("subscription_url").as[Option[String]]
      commits_url       ← c.downField("commits_url").as[Option[String]]
      git_commits_url   ← c.downField("git_commits_url").as[Option[String]]
      comments_url      ← c.downField("comments_url").as[Option[String]]
      issue_comment_url ← c.downField("issue_comment_url").as[Option[String]]
      contents_url      ← c.downField("contents_url").as[Option[String]]
      compare_url       ← c.downField("compare_url").as[Option[String]]
      merges_url        ← c.downField("merges_url").as[Option[String]]
      archive_url       ← c.downField("archive_url").as[Option[String]]
      downloads_url     ← c.downField("downloads_url").as[Option[String]]
      issues_url        ← c.downField("issues_url").as[Option[String]]
      pulls_url         ← c.downField("pulls_url").as[Option[String]]
      milestones_url    ← c.downField("milestones_url").as[Option[String]]
      notifications_url ← c.downField("notifications_url").as[Option[String]]
      labels_url        ← c.downField("labels_url").as[Option[String]]
      releases_url      ← c.downField("releases_url").as[Option[String]]
      deployments_url   ← c.downField("deployments_url").as[Option[String]]
    } yield
      Repository(
        id = id,
        name = name,
        full_name = full_name,
        owner = owner,
        `private` = priv,
        description = description,
        fork = fork,
        created_at = created_at,
        updated_at = updated_at,
        pushed_at = pushed_at,
        homepage = homepage,
        language = language,
        organization = organization,
        status = RepoStatus(
          size = size,
          stargazers_count = stargazers_count,
          watchers_count = watchers_count,
          forks_count = forks_count,
          open_issues_count = open_issues_count,
          open_issues = open_issues,
          watchers = watchers,
          network_count = network_count,
          subscribers_count = subscribers_count,
          has_issues = has_issues,
          has_downloads = has_downloads,
          has_wiki = has_wiki,
          has_pages = has_pages
        ),
        urls = RepoUrls(
          url = url,
          html_url = html_url,
          git_url = git_url,
          ssh_url = ssh_url,
          clone_url = clone_url,
          svn_url = svn_url,
          forks_url = forks_url,
          keys_url = keys_url,
          collaborators_url = collaborators_url,
          teams_url = teams_url,
          hooks_url = hooks_url,
          issue_events_url = issue_events_url,
          events_url = events_url,
          assignees_url = assignees_url,
          branches_url = branches_url,
          tags_url = tags_url,
          blobs_url = blobs_url,
          git_tags_url = git_tags_url,
          git_refs_url = git_refs_url,
          trees_url = trees_url,
          statuses_url = statuses_url,
          languages_url = languages_url,
          stargazers_url = stargazers_url,
          contributors_url = contributors_url,
          subscribers_url = subscribers_url,
          subscription_url = subscription_url,
          commits_url = commits_url,
          git_commits_url = git_commits_url,
          comments_url = comments_url,
          issue_comment_url = issue_comment_url,
          contents_url = contents_url,
          compare_url = compare_url,
          merges_url = merges_url,
          archive_url = archive_url,
          downloads_url = downloads_url,
          issues_url = issues_url,
          pulls_url = pulls_url,
          milestones_url = milestones_url,
          notifications_url = notifications_url,
          labels_url = labels_url,
          releases_url = releases_url,
          deployments_url = deployments_url
        )
      )
  }

  implicit val decodeGist: Decoder[Gist] = Decoder.instance { c ⇒
    for {
      url         ← c.downField("url").as[String]
      id          ← c.downField("id").as[String]
      description ← c.downField("description").as[String]
      public      ← c.downField("public").as[Boolean]
    } yield
      Gist(
        url = url,
        id = id,
        description = description,
        public = public
      )
  }

}
