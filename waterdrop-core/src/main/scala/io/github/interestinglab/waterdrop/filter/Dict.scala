/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package io.github.interestinglab.waterdrop.filter

import io.github.interestinglab.waterdrop.config.{Config, ConfigFactory}
import io.github.interestinglab.waterdrop.apis.BaseFilter
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._

class Dict extends BaseFilter {

  var conf: Config = ConfigFactory.empty()

  /**
   * Set Config.
   * */
  override def setConfig(config: Config): Unit = {
    this.conf = config
  }

  /**
   * Get Config.
   * */
  override def getConfig(): Config = {
    this.conf
  }

  // parameters:
  // file_url, support protocol: hdfs, file, http
  // delimiter, column delimiter in row
  // headers
  // source_field
  // dict_field
  // join method: self implemented by broadcast variable, 2 dataframe join(make sure it is broadcast join)
  override def checkConfig(): (Boolean, String) = {
    conf.hasPath("file_url") && conf.hasPath("headers") && conf.hasPath("source_field") && conf.hasPath("dict_field") match {
      case true => {
        val fileURL = conf.getString("file_url")
        val allowedProtocol = List("hdfs://", "file://", "http://")
        var unsupportedProtocol = true
        allowedProtocol.foreach(p => {
          if (fileURL.startsWith(p)) {
            unsupportedProtocol = false
          }
        })

        if (unsupportedProtocol) {
          (false, "unsupported protocol in [file_url], please choose one of " + allowedProtocol.mkString(", "))
        } else {
          (true, "")
        }
      }
      case false => (false, "please specify [file_url], [headers], [source_field], [dict_field]")
    }
  }

  override def prepare(spark: SparkSession): Unit = {
    super.prepare(spark)
    val defaultConfig = ConfigFactory.parseMap(
      Map(
        "delimiter" -> ","
      )
    )

    conf = conf.withFallback(defaultConfig)
  }

  override def process(spark: SparkSession, df: Dataset[Row]): Dataset[Row] = {
    // TODO
    df
  }
}