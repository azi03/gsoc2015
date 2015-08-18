/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
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
 */
package org.red5.io.webm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static Logger log = LoggerFactory.getLogger(WebmWriter.class);

	public static void main(String[] args) {

		if (args.length < 1) {
			log.error("usage: java -jar webm-1.0.0-SNAPSHOT.jar path/to/your/file.webm");
			return;
		}

		if ("".equals(args[0])) {
			log.error("invalid arguments");
			return;
		}

		try {
			writeRecord(args[0]);
		} catch (FileNotFoundException e) {
			log.error("File not found", e);
		} catch (IOException e) {
			log.error("IO exception", e);
		}
	}

	public static void writeRecord(String path) throws IOException, FileNotFoundException {
		File outputFile = getRecordFile(path);
		WebmWriter writer = new WebmWriter(outputFile, false);
		writer.writeHeader();
	}

	public static File getRecordFile(String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
		return file;
	}
}