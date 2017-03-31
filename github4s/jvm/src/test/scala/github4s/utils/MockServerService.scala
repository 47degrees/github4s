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

import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.integration.ClientAndServer._
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

trait MockServerService extends FlatSpec with BeforeAndAfterAll {

  val mockServerPort = 9999

  val mockServer = startClientAndServer(mockServerPort)

  override def beforeAll = ConfigurationProperties.overrideLogLevel("OFF")

  override def afterAll = {
    mockServer.stop
    ()
  }

}
