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
package webmTags;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;

import webm2flv.matroska.ParserUtils;
import webm2flv.matroska.VINT;


public class StringTag extends Tag {
	
	private String value;

	public StringTag(String name, VINT id) {
		super(name, id);
	}
	
	public StringTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	public void setValue(String content) throws IOException {
		value = content;
		this.size = new VINT(content.length(), (byte)(4), content.length());
	}
	
	
	public String getValue() {
		return value;
	}
	
	
	protected byte[] dataToByteArray() throws UnsupportedEncodingException {
		return value.getBytes("UTF-8");
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseString(inputStream, (int) getSize());
	}
	
	public void setDefaultValue(String newValue) {
		value = newValue;
	}
	
	public String toString() {
		return (getName() + " = " + value);
	}

}
