# Changelog

## [Unreleased](https://github.com/47degrees/github4s/tree/HEAD)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.23.0...HEAD)

⚠️ **Breaking changes**

- Unseal GithubAPIs and make it part of the algebra \[fixes \#392\] [\#421](https://github.com/47degrees/github4s/pull/421) ([dcsobral](https://github.com/dcsobral))
- Introduces custom GitHub configuration [\#384](https://github.com/47degrees/github4s/pull/384) ([satorg](https://github.com/satorg))
- Require an http4s client to provide more flexibility [\#373](https://github.com/47degrees/github4s/pull/373) ([BenFradet](https://github.com/BenFradet))

🚀 **Features**

- Better exception messages/classes [\#118](https://github.com/47degrees/github4s/issues/118)

📈 **Dependency updates**

- Update sbt-scalafmt to 2.3.3 [\#445](https://github.com/47degrees/github4s/pull/445) ([scala-steward](https://github.com/scala-steward))

**Closed issues:**

- Integration tests are run when the token is not present [\#396](https://github.com/47degrees/github4s/issues/396)
- Sealed GithubAPIs prevents extension [\#392](https://github.com/47degrees/github4s/issues/392)
- Add support for create / update / delete file [\#388](https://github.com/47degrees/github4s/issues/388)
- Support for private GitHub Enterprise installations [\#379](https://github.com/47degrees/github4s/issues/379)
- Shutting down connection pool info messages [\#371](https://github.com/47degrees/github4s/issues/371)
- Clarify error handling design [\#363](https://github.com/47degrees/github4s/issues/363)
- GHException extends Throwable [\#362](https://github.com/47degrees/github4s/issues/362)
- Remove list statuses test flakiness [\#300](https://github.com/47degrees/github4s/issues/300)
- Traversing with Pagination [\#285](https://github.com/47degrees/github4s/issues/285)
- Wrong API endpoint for listUserRepos [\#255](https://github.com/47degrees/github4s/issues/255)
- Add create milestone [\#248](https://github.com/47degrees/github4s/issues/248)

**Merged pull requests:**

- Removes sbt-org-policies [\#436](https://github.com/47degrees/github4s/pull/436) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Make GHException extend Exception [\#430](https://github.com/47degrees/github4s/pull/430) ([BenFradet](https://github.com/BenFradet))
- Remove sbt-org-policies dependency syntax [\#429](https://github.com/47degrees/github4s/pull/429) ([BenFradet](https://github.com/BenFradet))
- Rename integration specs [\#428](https://github.com/47degrees/github4s/pull/428) ([BenFradet](https://github.com/BenFradet))
- Remove mockserver dependency [\#427](https://github.com/47degrees/github4s/pull/427) ([BenFradet](https://github.com/BenFradet))
- Make all case classes final [\#426](https://github.com/47degrees/github4s/pull/426) ([BenFradet](https://github.com/BenFradet))
- Renames GH token env var [\#405](https://github.com/47degrees/github4s/pull/405) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Updated and delete milestone [\#402](https://github.com/47degrees/github4s/pull/402) ([anamariamv](https://github.com/anamariamv))
- Don't propagate dummy configuration to integration tests [\#401](https://github.com/47degrees/github4s/pull/401) ([BenFradet](https://github.com/BenFradet))
- Get single milestone [\#400](https://github.com/47degrees/github4s/pull/400) ([anamariamv](https://github.com/anamariamv))
- Fix API documentation link for the user repos endpoint [\#399](https://github.com/47degrees/github4s/pull/399) ([BenFradet](https://github.com/BenFradet))
- Renames Token Env Var [\#397](https://github.com/47degrees/github4s/pull/397) ([juanpedromoreno](https://github.com/juanpedromoreno))
- API: Create milestone [\#391](https://github.com/47degrees/github4s/pull/391) ([anamariamv](https://github.com/anamariamv))
- feat: add Create / Update / Delete requests [\#390](https://github.com/47degrees/github4s/pull/390) ([kalexmills](https://github.com/kalexmills))
- Update http4s-blaze-client, http4s-circe to 0.21.2 [\#387](https://github.com/47degrees/github4s/pull/387) ([scala-steward](https://github.com/scala-steward))
- Update mockserver-netty to 5.10 [\#386](https://github.com/47degrees/github4s/pull/386) ([scala-steward](https://github.com/scala-steward))
- Update sbt-microsites to 1.1.5 [\#383](https://github.com/47degrees/github4s/pull/383) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.5 [\#382](https://github.com/47degrees/github4s/pull/382) ([scala-steward](https://github.com/scala-steward))
- Update sbt-microsites to 1.1.4 [\#380](https://github.com/47degrees/github4s/pull/380) ([scala-steward](https://github.com/scala-steward))
- Mergify: configuration update [\#378](https://github.com/47degrees/github4s/pull/378) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Tag test for listing starrers as integration [\#376](https://github.com/47degrees/github4s/pull/376) ([BenFradet](https://github.com/BenFradet))
- Update sbt-mdoc to 2.1.4 [\#375](https://github.com/47degrees/github4s/pull/375) ([scala-steward](https://github.com/scala-steward))
- Update sbt-org-policies to 0.13.2 [\#374](https://github.com/47degrees/github4s/pull/374) ([scala-steward](https://github.com/scala-steward))

## [v0.23.0](https://github.com/47degrees/github4s/tree/v0.23.0) (2020-03-16)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.22.0...v0.23.0)

🚀 **Features**

- Integrate a new API: Get the list of cards for a project \(given a column id\) [\#361](https://github.com/47degrees/github4s/issues/361)
- Integrate a new API: Get the list of projects in a repository [\#360](https://github.com/47degrees/github4s/issues/360)
- Integrate a new API: Get the list of milestones in a project [\#340](https://github.com/47degrees/github4s/issues/340)
- Integrate a new API: Get the list of columns in a project [\#339](https://github.com/47degrees/github4s/issues/339)
- Integrate a new API: Get the list of projects in an organization [\#338](https://github.com/47degrees/github4s/issues/338)

**Closed issues:**

- Remove unnecessary folder structure [\#357](https://github.com/47degrees/github4s/issues/357)
- Include an example using F in the docs [\#356](https://github.com/47degrees/github4s/issues/356)
- Integrate a new API: Get the list of labels in a project [\#341](https://github.com/47degrees/github4s/issues/341)

**Merged pull requests:**

- Releases 0.23.0 [\#372](https://github.com/47degrees/github4s/pull/372) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Migrates from 47deg to 47degrees GH organization [\#370](https://github.com/47degrees/github4s/pull/370) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Update sbt-mdoc to 2.1.3 [\#369](https://github.com/47degrees/github4s/pull/369) ([scala-steward](https://github.com/scala-steward))
- Introduce sbt-tpolecat [\#368](https://github.com/47degrees/github4s/pull/368) ([BenFradet](https://github.com/BenFradet))
- Rework GHResponse to incorporate useful data on unhappy path [\#367](https://github.com/47degrees/github4s/pull/367) ([BenFradet](https://github.com/BenFradet))
- Update sbt-microsites to 1.1.3 [\#366](https://github.com/47degrees/github4s/pull/366) ([scala-steward](https://github.com/scala-steward))
- List repository projects  [\#365](https://github.com/47degrees/github4s/pull/365) ([anamariamv](https://github.com/anamariamv))
- List cards [\#364](https://github.com/47degrees/github4s/pull/364) ([bond15](https://github.com/bond15))
- List milestone and List Labels in a project [\#359](https://github.com/47degrees/github4s/pull/359) ([anamariamv](https://github.com/anamariamv))
- Example using F [\#358](https://github.com/47degrees/github4s/pull/358) ([BenFradet](https://github.com/BenFradet))
- Project API  [\#354](https://github.com/47degrees/github4s/pull/354) ([anamariamv](https://github.com/anamariamv))
- Update cats-effect to 2.1.1 [\#342](https://github.com/47degrees/github4s/pull/342) ([scala-steward](https://github.com/scala-steward))

## [v0.22.0](https://github.com/47degrees/github4s/tree/v0.22.0) (2020-02-28)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.21.0...v0.22.0)

🚀 **Features**

- Integrate a new API: Get the list of teams in an organization [\#337](https://github.com/47degrees/github4s/issues/337)
- Replace HTTP client/server library [\#332](https://github.com/47degrees/github4s/issues/332)
- Rewrite algebras composition [\#331](https://github.com/47degrees/github4s/issues/331)
- Migrate http requests to hammock [\#157](https://github.com/47degrees/github4s/issues/157)

**Closed issues:**

- Lower code coverage threshold [\#349](https://github.com/47degrees/github4s/issues/349)
- Replace tut by mdoc [\#329](https://github.com/47degrees/github4s/issues/329)
- sbt-microsites 1.0 adaptation [\#315](https://github.com/47degrees/github4s/issues/315)
- SVG logo [\#314](https://github.com/47degrees/github4s/issues/314)

**Merged pull requests:**

- Releases 0.22.0 [\#355](https://github.com/47degrees/github4s/pull/355) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Lower the minimum code coverage threshold to 75% [\#353](https://github.com/47degrees/github4s/pull/353) ([BenFradet](https://github.com/BenFradet))
- Update sbt-org-policies to 0.13.1 [\#352](https://github.com/47degrees/github4s/pull/352) ([BenFradet](https://github.com/BenFradet))
- Update cats-core, cats-free to 2.1.1 [\#351](https://github.com/47degrees/github4s/pull/351) ([scala-steward](https://github.com/scala-steward))
- Get the list of teams in an organization. [\#350](https://github.com/47degrees/github4s/pull/350) ([anamariamv](https://github.com/anamariamv))
- Update sbt-microsites to 1.1.2 [\#348](https://github.com/47degrees/github4s/pull/348) ([scala-steward](https://github.com/scala-steward))
- Update scalatest to 3.1.1 [\#347](https://github.com/47degrees/github4s/pull/347) ([scala-steward](https://github.com/scala-steward))
- Update http4s-blaze-client, http4s-circe to 0.21.1 [\#346](https://github.com/47degrees/github4s/pull/346) ([scala-steward](https://github.com/scala-steward))
- Tagless final refactor [\#344](https://github.com/47degrees/github4s/pull/344) ([bond15](https://github.com/bond15))
- Update circe-core, circe-generic, ... to 0.13.0 [\#343](https://github.com/47degrees/github4s/pull/343) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.8 [\#336](https://github.com/47degrees/github4s/pull/336) ([scala-steward](https://github.com/scala-steward))
- Update mockserver-netty to 5.9.0 [\#335](https://github.com/47degrees/github4s/pull/335) ([scala-steward](https://github.com/scala-steward))
- Replace tut by mdoc and other clean ups [\#330](https://github.com/47degrees/github4s/pull/330) ([BenFradet](https://github.com/BenFradet))
- Removes unused parameter [\#327](https://github.com/47degrees/github4s/pull/327) ([rafaparadela](https://github.com/rafaparadela))
- Re-formats for 2020 copyright [\#326](https://github.com/47degrees/github4s/pull/326) ([rafaparadela](https://github.com/rafaparadela))
- Update sbt to 1.3.7 [\#325](https://github.com/47degrees/github4s/pull/325) ([scala-steward](https://github.com/scala-steward))
- Update sbt-microsites to 1.1.0 [\#324](https://github.com/47degrees/github4s/pull/324) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.6 [\#322](https://github.com/47degrees/github4s/pull/322) ([scala-steward](https://github.com/scala-steward))
- Update mockserver-netty to 5.8.1 [\#321](https://github.com/47degrees/github4s/pull/321) ([scala-steward](https://github.com/scala-steward))
- Update cats-core, cats-free to 2.1.0 [\#320](https://github.com/47degrees/github4s/pull/320) ([BenFradet](https://github.com/BenFradet))
- Active sidebar items [\#319](https://github.com/47degrees/github4s/pull/319) ([AntonioMateoGomez](https://github.com/AntonioMateoGomez))
- Sbt microsite adaptation [\#317](https://github.com/47degrees/github4s/pull/317) ([AntonioMateoGomez](https://github.com/AntonioMateoGomez))
- Avoid gem system update [\#316](https://github.com/47degrees/github4s/pull/316) ([calvellido](https://github.com/calvellido))
- Publish microsite with G4S [\#313](https://github.com/47degrees/github4s/pull/313) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.21.0](https://github.com/47degrees/github4s/tree/v0.21.0) (2019-12-18)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.20.1...v0.21.0)

🚀 **Features**

- Add tests to github4s-scalaz [\#159](https://github.com/47degrees/github4s/issues/159)

**Closed issues:**

- Migrate -\> arrows [\#304](https://github.com/47degrees/github4s/issues/304)
- Migrate Unicode Arrows [\#296](https://github.com/47degrees/github4s/issues/296)
- Deploy travis stage fails with stack overflow [\#294](https://github.com/47degrees/github4s/issues/294)
- Scala 2.13 Release [\#261](https://github.com/47degrees/github4s/issues/261)
- Update dependency org.spire-math:kind-projector:plugin-\>default\(compile\) [\#184](https://github.com/47degrees/github4s/issues/184)
- Make the integration tests skippable [\#178](https://github.com/47degrees/github4s/issues/178)
- Possible duplicity [\#136](https://github.com/47degrees/github4s/issues/136)
- Create package object to handle current imports [\#45](https://github.com/47degrees/github4s/issues/45)

**Merged pull requests:**

- Release 0.21.0 [\#312](https://github.com/47degrees/github4s/pull/312) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Cross-build Scala 2.13 [\#311](https://github.com/47degrees/github4s/pull/311) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Upgrades build [\#310](https://github.com/47degrees/github4s/pull/310) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Removes Scalajs & Scala 2.11 support [\#309](https://github.com/47degrees/github4s/pull/309) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Update sbt to 1.3.5 [\#306](https://github.com/47degrees/github4s/pull/306) ([scala-steward](https://github.com/scala-steward))
- 304 migrate unicode arrows [\#305](https://github.com/47degrees/github4s/pull/305) ([duanebester](https://github.com/duanebester))
- Update mockserver-netty to 5.8.0 [\#303](https://github.com/47degrees/github4s/pull/303) ([scala-steward](https://github.com/scala-steward))
- replacing unicode arrows [\#302](https://github.com/47degrees/github4s/pull/302) ([duanebester](https://github.com/duanebester))
- Tagged test as integration [\#301](https://github.com/47degrees/github4s/pull/301) ([duanebester](https://github.com/duanebester))
- Updates [\#298](https://github.com/47degrees/github4s/pull/298) ([BenFradet](https://github.com/BenFradet))
- Specify additional SBT memory settings [\#295](https://github.com/47degrees/github4s/pull/295) ([BenFradet](https://github.com/BenFradet))
- Mockserver 5.7.2 [\#293](https://github.com/47degrees/github4s/pull/293) ([BenFradet](https://github.com/BenFradet))
- Update scalaz-concurrent to 7.2.29 [\#284](https://github.com/47degrees/github4s/pull/284) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.3 [\#283](https://github.com/47degrees/github4s/pull/283) ([scala-steward](https://github.com/scala-steward))
- Update cats-effect to 2.0.0 [\#282](https://github.com/47degrees/github4s/pull/282) ([scala-steward](https://github.com/scala-steward))
- Update mockserver-netty to 5.6.1 [\#281](https://github.com/47degrees/github4s/pull/281) ([BenFradet](https://github.com/BenFradet))
- Update mockserver-netty to 3.12 [\#278](https://github.com/47degrees/github4s/pull/278) ([scala-steward](https://github.com/scala-steward))
- Update cats-effect to 1.4.0 [\#277](https://github.com/47degrees/github4s/pull/277) ([scala-steward](https://github.com/scala-steward))
- Update cats-core, cats-free to 1.6.1 [\#276](https://github.com/47degrees/github4s/pull/276) ([scala-steward](https://github.com/scala-steward))
- Update scalaz-concurrent to 7.2.28 [\#275](https://github.com/47degrees/github4s/pull/275) ([scala-steward](https://github.com/scala-steward))
- Update scalatest to 3.0.8 [\#274](https://github.com/47degrees/github4s/pull/274) ([scala-steward](https://github.com/scala-steward))
- Update scalaj-http to 2.4.2 [\#273](https://github.com/47degrees/github4s/pull/273) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.2 [\#272](https://github.com/47degrees/github4s/pull/272) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalajs, scalajs-compiler to 0.6.29 [\#271](https://github.com/47degrees/github4s/pull/271) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalajs-crossproject to 0.6.1 [\#270](https://github.com/47degrees/github4s/pull/270) ([scala-steward](https://github.com/scala-steward))
- Update mockserver-netty to 3.10.8 [\#269](https://github.com/47degrees/github4s/pull/269) ([scala-steward](https://github.com/scala-steward))
- Update circe-core, circe-generic, ... to 0.11.1 [\#268](https://github.com/47degrees/github4s/pull/268) ([scala-steward](https://github.com/scala-steward))
- Update roshttp to 2.2.4 [\#267](https://github.com/47degrees/github4s/pull/267) ([scala-steward](https://github.com/scala-steward))
- Update simulacrum to 0.19.0 [\#266](https://github.com/47degrees/github4s/pull/266) ([scala-steward](https://github.com/scala-steward))
- Update base64 to 0.2.9 [\#265](https://github.com/47degrees/github4s/pull/265) ([scala-steward](https://github.com/scala-steward))
- Update sbt-buildinfo to 0.9.0 [\#264](https://github.com/47degrees/github4s/pull/264) ([scala-steward](https://github.com/scala-steward))
- Adding merge\_commit\_sha field to PullRequest model [\#260](https://github.com/47degrees/github4s/pull/260) ([jdesiloniz](https://github.com/jdesiloniz))
- Added contributions field [\#258](https://github.com/47degrees/github4s/pull/258) ([oybek](https://github.com/oybek))
- Make the integration tests skippable [\#257](https://github.com/47degrees/github4s/pull/257) ([BenFradet](https://github.com/BenFradet))
- GetFollowing added to the algebra [\#251](https://github.com/47degrees/github4s/pull/251) ([mfirry](https://github.com/mfirry))

## [v0.20.1](https://github.com/47degrees/github4s/tree/v0.20.1) (2019-02-25)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.20.0...v0.20.1)

**Closed issues:**

- Implement the library using freestyle [\#57](https://github.com/47degrees/github4s/issues/57)

**Merged pull requests:**

- Releases 0.20.1 [\#254](https://github.com/47degrees/github4s/pull/254) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Replaces circe-jawn by circe-jackson Parser [\#253](https://github.com/47degrees/github4s/pull/253) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Add cla-bot to the github4s in the wild section [\#252](https://github.com/47degrees/github4s/pull/252) ([BenFradet](https://github.com/BenFradet))
- Adding missing fields to User [\#250](https://github.com/47degrees/github4s/pull/250) ([mfirry](https://github.com/mfirry))
- Update headers and sbt [\#247](https://github.com/47degrees/github4s/pull/247) ([JesusMtnez](https://github.com/JesusMtnez))

## [v0.20.0](https://github.com/47degrees/github4s/tree/v0.20.0) (2018-12-27)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.19.0...v0.20.0)

🚀 **Features**

- Add support for the list branched endpoint \(47deg\#231\) [\#232](https://github.com/47degrees/github4s/pull/232) ([YarekTyshchenko](https://github.com/YarekTyshchenko))

**Closed issues:**

- Implement Edit a gist API [\#238](https://github.com/47degrees/github4s/issues/238)
- Implement Get a single gist API [\#237](https://github.com/47degrees/github4s/issues/237)
- Add docs for list-branches feature [\#234](https://github.com/47degrees/github4s/issues/234)
- Add support for the list user repositories endpoint [\#194](https://github.com/47degrees/github4s/issues/194)

**Merged pull requests:**

- Build upgrade and Release 0.20.0 [\#243](https://github.com/47degrees/github4s/pull/243) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Update Ruby to latest 2.3.8 for Travis CI [\#241](https://github.com/47degrees/github4s/pull/241) ([aleksandr-vin](https://github.com/aleksandr-vin))
- Add Edit gist api [\#240](https://github.com/47degrees/github4s/pull/240) ([aleksandr-vin](https://github.com/aleksandr-vin))
- Fix decoding of issue when body is null [\#236](https://github.com/47degrees/github4s/pull/236) ([YarekTyshchenko](https://github.com/YarekTyshchenko))
- Fix returned type in list branches docs [\#235](https://github.com/47degrees/github4s/pull/235) ([JesusMtnez](https://github.com/JesusMtnez))
- Introduce a way to distinguish different github errors [\#230](https://github.com/47degrees/github4s/pull/230) ([mikegirkin](https://github.com/mikegirkin))
- Missing microsite menu entries + broken link [\#229](https://github.com/47degrees/github4s/pull/229) ([mkobzik](https://github.com/mkobzik))
- Add missing links to issue table of contents [\#228](https://github.com/47degrees/github4s/pull/228) ([mkobzik](https://github.com/mkobzik))
- Fix documentation for the list available assignees endpoint [\#227](https://github.com/47degrees/github4s/pull/227) ([BenFradet](https://github.com/BenFradet))
- Add support for the list available assignees endpoint [\#226](https://github.com/47degrees/github4s/pull/226) ([mkobzik](https://github.com/mkobzik))
- Add support for the list outside collaborators endpoint [\#225](https://github.com/47degrees/github4s/pull/225) ([mkobzik](https://github.com/mkobzik))
- List user repositories api [\#224](https://github.com/47degrees/github4s/pull/224) ([pgabara](https://github.com/pgabara))

## [v0.19.0](https://github.com/47degrees/github4s/tree/v0.19.0) (2018-10-04)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.8...v0.19.0)

**Closed issues:**

- Swap out github API urls? [\#222](https://github.com/47degrees/github4s/issues/222)

**Merged pull requests:**

- Bump sbt-org-policies to 0.9.4 [\#223](https://github.com/47degrees/github4s/pull/223) ([BenFradet](https://github.com/BenFradet))

## [v0.18.8](https://github.com/47degrees/github4s/tree/v0.18.8) (2018-09-04)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.7...v0.18.8)

**Merged pull requests:**

- Release 0.18.8 [\#221](https://github.com/47degrees/github4s/pull/221) ([BenFradet](https://github.com/BenFradet))
- Add cla-bot to the Github4s in the wild section [\#220](https://github.com/47degrees/github4s/pull/220) ([BenFradet](https://github.com/BenFradet))

## [v0.18.7](https://github.com/47degrees/github4s/tree/v0.18.7) (2018-08-20)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.6...v0.18.7)

**Merged pull requests:**

- Release 0.18.7 [\#219](https://github.com/47degrees/github4s/pull/219) ([BenFradet](https://github.com/BenFradet))
- Add listCollaborators method [\#218](https://github.com/47degrees/github4s/pull/218) ([asoltysik](https://github.com/asoltysik))
- add get tree api implementation [\#217](https://github.com/47degrees/github4s/pull/217) ([aberey](https://github.com/aberey))
- Add dashing as an application using github4s [\#216](https://github.com/47degrees/github4s/pull/216) ([BenFradet](https://github.com/BenFradet))

## [v0.18.6](https://github.com/47degrees/github4s/tree/v0.18.6) (2018-07-09)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.5...v0.18.6)

**Closed issues:**

- \[cats-effect\] `Capture` can be derived for anything that has a `Sync` instance [\#213](https://github.com/47degrees/github4s/issues/213)

**Merged pull requests:**

- Release 0.18.6 [\#215](https://github.com/47degrees/github4s/pull/215) ([BenFradet](https://github.com/BenFradet))
- Abstract away from cats-effect IO [\#214](https://github.com/47degrees/github4s/pull/214) ([BenFradet](https://github.com/BenFradet))

## [v0.18.5](https://github.com/47degrees/github4s/tree/v0.18.5) (2018-06-24)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.4...v0.18.5)

**Merged pull requests:**

- Release 0.18.5 [\#212](https://github.com/47degrees/github4s/pull/212) ([BenFradet](https://github.com/BenFradet))
- Add support for the get pull request endpoint [\#211](https://github.com/47degrees/github4s/pull/211) ([BenFradet](https://github.com/BenFradet))
- Add support for the remove label endpoint [\#210](https://github.com/47degrees/github4s/pull/210) ([BenFradet](https://github.com/BenFradet))
- Fix cats-effect JS tests flakiness [\#209](https://github.com/47degrees/github4s/pull/209) ([BenFradet](https://github.com/BenFradet))
- Bump Scala version to 2.12.6 in travis.yml [\#208](https://github.com/47degrees/github4s/pull/208) ([BenFradet](https://github.com/BenFradet))
- Add support for the add labels endpoint [\#207](https://github.com/47degrees/github4s/pull/207) ([BenFradet](https://github.com/BenFradet))
- List labels endpoint [\#206](https://github.com/47degrees/github4s/pull/206) ([BenFradet](https://github.com/BenFradet))

## [v0.18.4](https://github.com/47degrees/github4s/tree/v0.18.4) (2018-04-10)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.3...v0.18.4)

**Closed issues:**

- Fix LabelParam [\#203](https://github.com/47degrees/github4s/issues/203)

**Merged pull requests:**

- Release 0.18.4 [\#205](https://github.com/47degrees/github4s/pull/205) ([BenFradet](https://github.com/BenFradet))
- Change param so we are filtering by label [\#204](https://github.com/47degrees/github4s/pull/204) ([drwlrsn](https://github.com/drwlrsn))
- Keep circe decoders dry [\#202](https://github.com/47degrees/github4s/pull/202) ([BenFradet](https://github.com/BenFradet))

## [v0.18.3](https://github.com/47degrees/github4s/tree/v0.18.3) (2018-03-11)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.2...v0.18.3)

**Merged pull requests:**

- Release 0.18.3 [\#201](https://github.com/47degrees/github4s/pull/201) ([BenFradet](https://github.com/BenFradet))
- \# Make `body: Option\[String\]` [\#200](https://github.com/47degrees/github4s/pull/200) ([lloydmeta](https://github.com/lloydmeta))

## [v0.18.2](https://github.com/47degrees/github4s/tree/v0.18.2) (2018-03-07)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.1...v0.18.2)

**Closed issues:**

- Pagination for ListPullRequests and ListPullRequestFiles [\#197](https://github.com/47degrees/github4s/issues/197)

**Merged pull requests:**

- Releases 0.18.2 [\#199](https://github.com/47degrees/github4s/pull/199) ([fedefernandez](https://github.com/fedefernandez))
- Add pagination support for PullRequest ops [\#198](https://github.com/47degrees/github4s/pull/198) ([lloydmeta](https://github.com/lloydmeta))

## [v0.18.1](https://github.com/47degrees/github4s/tree/v0.18.1) (2018-02-14)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.18.0...v0.18.1)

**Merged pull requests:**

- Release 0.18.1 [\#196](https://github.com/47degrees/github4s/pull/196) ([BenFradet](https://github.com/BenFradet))
- Fix Capture instances for IO and Future [\#195](https://github.com/47degrees/github4s/pull/195) ([pepegar](https://github.com/pepegar))

## [v0.18.0](https://github.com/47degrees/github4s/tree/v0.18.0) (2018-01-23)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.17.0...v0.18.0)

**Closed issues:**

- Unit tests are failing on Travis but not locally [\#189](https://github.com/47degrees/github4s/issues/189)
- Add function to get all comments of an issue [\#186](https://github.com/47degrees/github4s/issues/186)
- Add function to get a single issue [\#185](https://github.com/47degrees/github4s/issues/185)
- Patch should be optional on `PullRequestFile` [\#180](https://github.com/47degrees/github4s/issues/180)
- Issues using the combined status API [\#179](https://github.com/47degrees/github4s/issues/179)

**Merged pull requests:**

- Release version 0.18.0 [\#193](https://github.com/47degrees/github4s/pull/193) ([BenFradet](https://github.com/BenFradet))
- Bump sbt-org-policies to 0.8.22 to benefit from cats 1.0.1 and circe 0.9.1 [\#192](https://github.com/47degrees/github4s/pull/192) ([BenFradet](https://github.com/BenFradet))
- Get a single issue [\#191](https://github.com/47degrees/github4s/pull/191) ([GRBurst](https://github.com/GRBurst))
- Fixes Travis file [\#190](https://github.com/47degrees/github4s/pull/190) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Fetch issue comments [\#188](https://github.com/47degrees/github4s/pull/188) ([GRBurst](https://github.com/GRBurst))
- Make status' id a Long [\#183](https://github.com/47degrees/github4s/pull/183) ([BenFradet](https://github.com/BenFradet))
- Bump ruby version in travis to fix travis build [\#182](https://github.com/47degrees/github4s/pull/182) ([guersam](https://github.com/guersam))
- Make PullRequestFile\#patch an Option\[String\] [\#181](https://github.com/47degrees/github4s/pull/181) ([lloydmeta](https://github.com/lloydmeta))

## [v0.17.0](https://github.com/47degrees/github4s/tree/v0.17.0) (2017-11-08)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.16.0...v0.17.0)

**Closed issues:**

- Documentation not updated [\#167](https://github.com/47degrees/github4s/issues/167)

**Merged pull requests:**

- Bump Travis' Scala version to 2.12.4 [\#177](https://github.com/47degrees/github4s/pull/177) ([BenFradet](https://github.com/BenFradet))
- Bump sbt-org-policies to 0.8.7 to benefit from cats 1.0.0-RC1 & co [\#176](https://github.com/47degrees/github4s/pull/176) ([BenFradet](https://github.com/BenFradet))
- Bumps sbt-org-policies in order to fix docs autopublishing issue [\#168](https://github.com/47degrees/github4s/pull/168) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.16.0](https://github.com/47degrees/github4s/tree/v0.16.0) (2017-09-25)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.15.0...v0.16.0)

**Closed issues:**

- Integration tests create a bunch of gists [\#149](https://github.com/47degrees/github4s/issues/149)

**Merged pull requests:**

- Bump travis 2.12 version to 2.12.3 [\#166](https://github.com/47degrees/github4s/pull/166) ([suhasgaddam](https://github.com/suhasgaddam))
- Release 0.16.0 [\#165](https://github.com/47degrees/github4s/pull/165) ([BenFradet](https://github.com/BenFradet))
- Bump sbt-org-policies to 0.7.4 [\#164](https://github.com/47degrees/github4s/pull/164) ([BenFradet](https://github.com/BenFradet))
- Organization API [\#163](https://github.com/47degrees/github4s/pull/163) ([BenFradet](https://github.com/BenFradet))
- Support for the list organization repositories endpoint [\#162](https://github.com/47degrees/github4s/pull/162) ([BenFradet](https://github.com/BenFradet))
- Support for starring-related operations [\#161](https://github.com/47degrees/github4s/pull/161) ([BenFradet](https://github.com/BenFradet))
- List statuses now gives back a 404 for a non-existing ref instead of an empty list [\#160](https://github.com/47degrees/github4s/pull/160) ([BenFradet](https://github.com/BenFradet))
- cats-effect module [\#155](https://github.com/47degrees/github4s/pull/155) ([BenFradet](https://github.com/BenFradet))
- Make gh4s doc structure reflect gh doc structure [\#152](https://github.com/47degrees/github4s/pull/152) ([BenFradet](https://github.com/BenFradet))
- Remove integration.GHGistsSpec [\#151](https://github.com/47degrees/github4s/pull/151) ([BenFradet](https://github.com/BenFradet))

## [v0.15.0](https://github.com/47degrees/github4s/tree/v0.15.0) (2017-05-23)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.7...v0.15.0)

**Closed issues:**

- Rename the Authentication API to Authorization [\#135](https://github.com/47degrees/github4s/issues/135)
- Support pull request reviews [\#133](https://github.com/47degrees/github4s/issues/133)
- Missing test and docs [\#131](https://github.com/47degrees/github4s/issues/131)
- Refactor algebras [\#129](https://github.com/47degrees/github4s/issues/129)
- Support managing comments for issues [\#106](https://github.com/47degrees/github4s/issues/106)
- Update documentation / microsite [\#42](https://github.com/47degrees/github4s/issues/42)

**Merged pull requests:**

- Releases 0.15.0 [\#150](https://github.com/47degrees/github4s/pull/150) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Rename Authentication to Authorization in the doc [\#148](https://github.com/47degrees/github4s/pull/148) ([BenFradet](https://github.com/BenFradet))
- Fix string interpolation in docs [\#147](https://github.com/47degrees/github4s/pull/147) ([BenFradet](https://github.com/BenFradet))
- Super minor lang edits [\#146](https://github.com/47degrees/github4s/pull/146) ([MaureenElsberry](https://github.com/MaureenElsberry))
- Rename listStatus to listStatuses [\#145](https://github.com/47degrees/github4s/pull/145) ([BenFradet](https://github.com/BenFradet))
- Fixes ghost users associated with pull requests, issues and comments [\#144](https://github.com/47degrees/github4s/pull/144) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Microsite Enhancements [\#143](https://github.com/47degrees/github4s/pull/143) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Replace updateReference's force parameter type from Option\[Boolean\] to Boolean [\#141](https://github.com/47degrees/github4s/pull/141) ([BenFradet](https://github.com/BenFradet))
- Upgrades tut bumping sbt-org-policies version to 0.5.0 [\#140](https://github.com/47degrees/github4s/pull/140) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Add support for the read half of the PR Review API [\#139](https://github.com/47degrees/github4s/pull/139) ([mscharley](https://github.com/mscharley))
- Contributing guide [\#138](https://github.com/47degrees/github4s/pull/138) ([BenFradet](https://github.com/BenFradet))
- Missing unit test [\#137](https://github.com/47degrees/github4s/pull/137) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Missing Test and Docs [\#132](https://github.com/47degrees/github4s/pull/132) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Refactor Algebras [\#130](https://github.com/47degrees/github4s/pull/130) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Remove integration tests creating statuses [\#128](https://github.com/47degrees/github4s/pull/128) ([BenFradet](https://github.com/BenFradet))
- Support Comment API [\#127](https://github.com/47degrees/github4s/pull/127) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Streamlined the getting started [\#126](https://github.com/47degrees/github4s/pull/126) ([BenFradet](https://github.com/BenFradet))

## [v0.14.7](https://github.com/47degrees/github4s/tree/v0.14.7) (2017-05-08)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.6...v0.14.7)

**Closed issues:**

- Put and patch are coerced to post on the JVM [\#119](https://github.com/47degrees/github4s/issues/119)
- Fix documentation for Edit an issue [\#107](https://github.com/47degrees/github4s/issues/107)
- Support the Create Pull Request API [\#105](https://github.com/47degrees/github4s/issues/105)
- Support the Notifications API [\#97](https://github.com/47degrees/github4s/issues/97)
- Unify tests [\#44](https://github.com/47degrees/github4s/issues/44)

**Merged pull requests:**

- Upgrades sbt org policies plugin [\#125](https://github.com/47degrees/github4s/pull/125) ([fedefernandez](https://github.com/fedefernandez))
- Releases 0.14.7 [\#124](https://github.com/47degrees/github4s/pull/124) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Arf 97 support notifications api [\#123](https://github.com/47degrees/github4s/pull/123) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Make sure sbt is executable in travis [\#122](https://github.com/47degrees/github4s/pull/122) ([BenFradet](https://github.com/BenFradet))
- Documentation for the gist API [\#121](https://github.com/47degrees/github4s/pull/121) ([BenFradet](https://github.com/BenFradet))
- Issues API unit tests [\#116](https://github.com/47degrees/github4s/pull/116) ([BenFradet](https://github.com/BenFradet))
- Unify JVM and JS tests [\#115](https://github.com/47degrees/github4s/pull/115) ([fedefernandez](https://github.com/fedefernandez))
- Replace issue id by issue number in the doc [\#114](https://github.com/47degrees/github4s/pull/114) ([BenFradet](https://github.com/BenFradet))
- Made request success check consistent between scala and scala js [\#112](https://github.com/47degrees/github4s/pull/112) ([BenFradet](https://github.com/BenFradet))
- Arf 105 create pull request api [\#109](https://github.com/47degrees/github4s/pull/109) ([AdrianRaFo](https://github.com/AdrianRaFo))
- Removes annoying compiler warnings reported by -Xlint flag [\#104](https://github.com/47degrees/github4s/pull/104) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Publish Microsite automatically when merging in master branch [\#103](https://github.com/47degrees/github4s/pull/103) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Documentation for the PR API [\#102](https://github.com/47degrees/github4s/pull/102) ([BenFradet](https://github.com/BenFradet))
- Replace foldLeft with traverse in Decoders [\#101](https://github.com/47degrees/github4s/pull/101) ([peterneyens](https://github.com/peterneyens))

## [v0.14.6](https://github.com/47degrees/github4s/tree/v0.14.6) (2017-04-25)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.5...v0.14.6)

**Merged pull requests:**

- Fixes head repo decode failure [\#100](https://github.com/47degrees/github4s/pull/100) ([fedefernandez](https://github.com/fedefernandez))
- List pull request files endpoint [\#99](https://github.com/47degrees/github4s/pull/99) ([BenFradet](https://github.com/BenFradet))
- Releases 0.14.5 [\#98](https://github.com/47degrees/github4s/pull/98) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.14.5](https://github.com/47degrees/github4s/tree/v0.14.5) (2017-04-24)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.4...v0.14.5)

🐛 **Bug Fixes**

- Random issues on Travis [\#84](https://github.com/47degrees/github4s/issues/84)

**Closed issues:**

- Github4s [\#96](https://github.com/47degrees/github4s/issues/96)
- Github4s [\#94](https://github.com/47degrees/github4s/issues/94)

**Merged pull requests:**

- Avoids executing create and edit issue operations [\#95](https://github.com/47degrees/github4s/pull/95) ([fedefernandez](https://github.com/fedefernandez))
- Doc for the issue api [\#93](https://github.com/47degrees/github4s/pull/93) ([BenFradet](https://github.com/BenFradet))
- Tries to fix OOM issues. Bumps sbt version [\#92](https://github.com/47degrees/github4s/pull/92) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.14.4](https://github.com/47degrees/github4s/tree/v0.14.4) (2017-04-21)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.3...v0.14.4)

**Closed issues:**

- PullRequest's head missing [\#89](https://github.com/47degrees/github4s/issues/89)

**Merged pull requests:**

- Upgrades Project [\#91](https://github.com/47degrees/github4s/pull/91) ([juanpedromoreno](https://github.com/juanpedromoreno))
- PullRequest's head [\#90](https://github.com/47degrees/github4s/pull/90) ([BenFradet](https://github.com/BenFradet))
- Documentation for the status API [\#88](https://github.com/47degrees/github4s/pull/88) ([BenFradet](https://github.com/BenFradet))
- Remove duplicated circe-parser dependency [\#87](https://github.com/47degrees/github4s/pull/87) ([BenFradet](https://github.com/BenFradet))
- Updated advertised version in the readme to 0.14.3 [\#86](https://github.com/47degrees/github4s/pull/86) ([BenFradet](https://github.com/BenFradet))

## [v0.14.3](https://github.com/47degrees/github4s/tree/v0.14.3) (2017-04-17)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.2...v0.14.3)

**Merged pull requests:**

- Bumps library version [\#85](https://github.com/47degrees/github4s/pull/85) ([fedefernandez](https://github.com/fedefernandez))
- Status API [\#83](https://github.com/47degrees/github4s/pull/83) ([BenFradet](https://github.com/BenFradet))

## [v0.14.2](https://github.com/47degrees/github4s/tree/v0.14.2) (2017-04-10)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.1...v0.14.2)

**Merged pull requests:**

- Upgrades sbt-org-policies [\#81](https://github.com/47degrees/github4s/pull/81) ([fedefernandez](https://github.com/fedefernandez))

## [v0.14.1](https://github.com/47degrees/github4s/tree/v0.14.1) (2017-04-05)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.14.0...v0.14.1)

🚀 **Features**

- Add support for getting contents [\#79](https://github.com/47degrees/github4s/issues/79)

**Merged pull requests:**

- Adds the get contents operation [\#80](https://github.com/47degrees/github4s/pull/80) ([fedefernandez](https://github.com/fedefernandez))
- Upgrades sbt-org-policies plugin [\#78](https://github.com/47degrees/github4s/pull/78) ([fedefernandez](https://github.com/fedefernandez))
- Fixes Github token through env var [\#77](https://github.com/47degrees/github4s/pull/77) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Bumps sbt-org-policies plugin version [\#76](https://github.com/47degrees/github4s/pull/76) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Updates changelog [\#75](https://github.com/47degrees/github4s/pull/75) ([fedefernandez](https://github.com/fedefernandez))

## [v0.14.0](https://github.com/47degrees/github4s/tree/v0.14.0) (2017-04-03)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.13.0...v0.14.0)

**Merged pull requests:**

- Adds the create release operation [\#74](https://github.com/47degrees/github4s/pull/74) ([fedefernandez](https://github.com/fedefernandez))
- Updates the changelog [\#73](https://github.com/47degrees/github4s/pull/73) ([fedefernandez](https://github.com/fedefernandez))

## [v0.13.0](https://github.com/47degrees/github4s/tree/v0.13.0) (2017-03-31)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.12.1...v0.13.0)

**Closed issues:**

- Git list pull requests feature [\#69](https://github.com/47degrees/github4s/issues/69)
- Git Tag Feature [\#68](https://github.com/47degrees/github4s/issues/68)

**Merged pull requests:**

- Updates the headers and formatting [\#72](https://github.com/47degrees/github4s/pull/72) ([fedefernandez](https://github.com/fedefernandez))
- Git tag feature [\#71](https://github.com/47degrees/github4s/pull/71) ([fedefernandez](https://github.com/fedefernandez))
- Pull request list [\#70](https://github.com/47degrees/github4s/pull/70) ([fedefernandez](https://github.com/fedefernandez))
- Update License [\#64](https://github.com/47degrees/github4s/pull/64) ([anamariamv](https://github.com/anamariamv))

## [v0.12.1](https://github.com/47degrees/github4s/tree/v0.12.1) (2017-03-28)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.12.0...v0.12.1)

**Merged pull requests:**

- Adds some git methods [\#67](https://github.com/47degrees/github4s/pull/67) ([fedefernandez](https://github.com/fedefernandez))
- Bumps sbt-org-policies plugin version [\#66](https://github.com/47degrees/github4s/pull/66) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Excludes BuildInfo class from packaging [\#65](https://github.com/47degrees/github4s/pull/65) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.12.0](https://github.com/47degrees/github4s/tree/v0.12.0) (2017-03-22)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.11.1...v0.12.0)

🚀 **Features**

- Publish version for Scala 2.12 [\#40](https://github.com/47degrees/github4s/issues/40)

**Closed issues:**

- Update sbt-catalyst-extras to sbt-org-policies [\#55](https://github.com/47degrees/github4s/issues/55)

**Merged pull requests:**

- Integrates sbt-org-policies plugin and.. [\#63](https://github.com/47degrees/github4s/pull/63) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.11.1](https://github.com/47degrees/github4s/tree/v0.11.1) (2017-03-22)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.11.0...v0.11.1)

**Merged pull requests:**

- Adds rings for publish artifacts [\#62](https://github.com/47degrees/github4s/pull/62) ([fedefernandez](https://github.com/fedefernandez))
- Adds some optional fields to the User model [\#61](https://github.com/47degrees/github4s/pull/61) ([fedefernandez](https://github.com/fedefernandez))
- Update CHANGELOG.md [\#60](https://github.com/47degrees/github4s/pull/60) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Fixes Docs in Travis [\#59](https://github.com/47degrees/github4s/pull/59) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.11.0](https://github.com/47degrees/github4s/tree/v0.11.0) (2017-03-16)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.10.0...v0.11.0)

**Closed issues:**

- Update dependencies [\#56](https://github.com/47degrees/github4s/issues/56)
- Migrate group id com.47deg [\#53](https://github.com/47degrees/github4s/issues/53)

**Merged pull requests:**

- Migrates group ID [\#58](https://github.com/47degrees/github4s/pull/58) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Update dependencies [\#54](https://github.com/47degrees/github4s/pull/54) ([ghost](https://github.com/ghost))
- Update CHANGELOG.md [\#52](https://github.com/47degrees/github4s/pull/52) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.10.0](https://github.com/47degrees/github4s/tree/v0.10.0) (2017-01-09)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.9.0...v0.10.0)

🐛 **Bug Fixes**

- Sample Title [\#48](https://github.com/47degrees/github4s/issues/48)

**Merged pull requests:**

- Makes mandatory some fields when creating issues [\#51](https://github.com/47degrees/github4s/pull/51) ([fedefernandez](https://github.com/fedefernandez))
- Adds a new root project to set Scala version [\#50](https://github.com/47degrees/github4s/pull/50) ([fedefernandez](https://github.com/fedefernandez))
- Adds create and edit methods for issues [\#49](https://github.com/47degrees/github4s/pull/49) ([fedefernandez](https://github.com/fedefernandez))
- Adds list and search methods for issues [\#47](https://github.com/47degrees/github4s/pull/47) ([fedefernandez](https://github.com/fedefernandez))
- Adds crossover for Scala 2.10 and upgrades libraries [\#46](https://github.com/47degrees/github4s/pull/46) ([fedefernandez](https://github.com/fedefernandez))
- Fix forced user agent in js side [\#43](https://github.com/47degrees/github4s/pull/43) ([jdesiloniz](https://github.com/jdesiloniz))
- Updates Footer Descriptions. Bumps new version. [\#41](https://github.com/47degrees/github4s/pull/41) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.9.0](https://github.com/47degrees/github4s/tree/v0.9.0) (2016-11-04)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.8.1...v0.9.0)

**Closed issues:**

- Support Scala.js [\#10](https://github.com/47degrees/github4s/issues/10)

**Merged pull requests:**

- Adding custom user headers at exec time [\#39](https://github.com/47degrees/github4s/pull/39) ([jdesiloniz](https://github.com/jdesiloniz))
- Compatibility with scala-js [\#38](https://github.com/47degrees/github4s/pull/38) ([jdesiloniz](https://github.com/jdesiloniz))

## [v0.8.1](https://github.com/47degrees/github4s/tree/v0.8.1) (2016-10-24)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.7...v0.8.1)

**Merged pull requests:**

- Upgrades sbt catalyst extras plugin [\#37](https://github.com/47degrees/github4s/pull/37) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Integrates sbt-catalysts-extras and sbt-microsites plugins [\#35](https://github.com/47degrees/github4s/pull/35) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Add method to create gists [\#34](https://github.com/47degrees/github4s/pull/34) ([jdesiloniz](https://github.com/jdesiloniz))
- JP - Replaces Xor by Either type [\#33](https://github.com/47degrees/github4s/pull/33) ([juanpedromoreno](https://github.com/juanpedromoreno))

## [v0.7](https://github.com/47degrees/github4s/tree/v0.7) (2016-10-12)

[Full Changelog](https://github.com/47degrees/github4s/compare/v0.1...v0.7)

**Closed issues:**

- Add me to the repository developers [\#27](https://github.com/47degrees/github4s/issues/27)
- Enrich repository model [\#24](https://github.com/47degrees/github4s/issues/24)
- Unit testing  [\#15](https://github.com/47degrees/github4s/issues/15)
- Target monad instances [\#14](https://github.com/47degrees/github4s/issues/14)

**Merged pull requests:**

- JP - Upgrades Libraries [\#32](https://github.com/47degrees/github4s/pull/32) ([juanpedromoreno](https://github.com/juanpedromoreno))
- JP - Fetch Repo Contributors List [\#31](https://github.com/47degrees/github4s/pull/31) ([juanpedromoreno](https://github.com/juanpedromoreno))
- Update cats and circe versions [\#30](https://github.com/47degrees/github4s/pull/30) ([ghost](https://github.com/ghost))
- Bump 0.5 release [\#29](https://github.com/47degrees/github4s/pull/29) ([ghost](https://github.com/ghost))
- Take into account that commit authors may be null [\#28](https://github.com/47degrees/github4s/pull/28) ([ghost](https://github.com/ghost))
- Include JSON body in JsonParsingException [\#26](https://github.com/47degrees/github4s/pull/26) ([ghost](https://github.com/ghost))
- Enrich Repository class [\#25](https://github.com/47degrees/github4s/pull/25) ([rafaparadela](https://github.com/rafaparadela))
- Added the compilation of tut in TravisCI [\#22](https://github.com/47degrees/github4s/pull/22) ([rafaparadela](https://github.com/rafaparadela))
- Added token for codecov.io [\#21](https://github.com/47degrees/github4s/pull/21) ([rafaparadela](https://github.com/rafaparadela))
- Provide interpreters with several monad instances [\#18](https://github.com/47degrees/github4s/pull/18) ([rafaparadela](https://github.com/rafaparadela))
- Rafa 15 unit test [\#17](https://github.com/47degrees/github4s/pull/17) ([rafaparadela](https://github.com/rafaparadela))
- Moved/Renamed package in order to omit organization prefix [\#13](https://github.com/47degrees/github4s/pull/13) ([rafaparadela](https://github.com/rafaparadela))
- Microsite ++ sbt.site ++ sbt-ghpages ++ sbt-tut [\#11](https://github.com/47degrees/github4s/pull/11) ([rafaparadela](https://github.com/rafaparadela))
- ScalaDoc [\#9](https://github.com/47degrees/github4s/pull/9) ([rafaparadela](https://github.com/rafaparadela))
- Add Scalariform plugin and reformat entire project [\#8](https://github.com/47degrees/github4s/pull/8) ([rafaparadela](https://github.com/rafaparadela))
- Test coverage reports [\#7](https://github.com/47degrees/github4s/pull/7) ([rafaparadela](https://github.com/rafaparadela))
- Version 0.2-SNAPSHOT [\#6](https://github.com/47degrees/github4s/pull/6) ([rafaparadela](https://github.com/rafaparadela))

## [v0.1](https://github.com/47degrees/github4s/tree/v0.1) (2016-05-12)

[Full Changelog](https://github.com/47degrees/github4s/compare/d3b048c1f500ee5450e5d7b3d1921ed3e7645891...v0.1)

**Merged pull requests:**

- Fixes test [\#5](https://github.com/47degrees/github4s/pull/5) ([rafaparadela](https://github.com/rafaparadela))
- Remove unused implementation [\#3](https://github.com/47degrees/github4s/pull/3) ([rafaparadela](https://github.com/rafaparadela))
- Add setting for publishing on Sonatype [\#2](https://github.com/47degrees/github4s/pull/2) ([rafaparadela](https://github.com/rafaparadela))
- Adds Travis status image [\#1](https://github.com/47degrees/github4s/pull/1) ([rafaparadela](https://github.com/rafaparadela))



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
