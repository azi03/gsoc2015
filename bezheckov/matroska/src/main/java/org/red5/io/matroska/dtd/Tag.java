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
package org.red5.io.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.VINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all webm tags
 *
 */
public abstract class Tag {
	static Logger log = LoggerFactory.getLogger(Tag.class);
	public enum Type {
		master
		, simple
		, primitive
	}
	
	private String name;
	private VINT id;
	VINT size;

	/**
	 * Constructor, internally calls {@link Tag#Tag(String, VINT, VINT)}
	 * 
	 * @param name - the name of tag to be created
	 * @param id - the id of tag to be created
	 */
	public Tag(String name, VINT id) {
		this(name, id, new VINT(0L, (byte) 0, 0L));
	}

	/**
	 * Constructor
	 * 
	 * @param name - the name of tag to be created
	 * @param id - the id of tag to be created
	 * @param size - the size of tag to be created
	 */
	public Tag(String name, VINT id, VINT size) {
		this.name = name;
		this.id = id;
		this.size = size;
	}

	public abstract void parse(InputStream inputStream) throws IOException, ConverterException;
	
	protected abstract void putValue(ByteBuffer bb) throws IOException;

	/**
	 * getter for type
	 * 
	 * @return type of this {@link Tag}
	 */
	public Type getType() {
		return Type.primitive;
	}
	
	/**
	 * getter for name
	 * 
	 * @return name of this {@link Tag}
	 */
	public String getName() {
		return name;
	}

	/**
	 * getter for id
	 * 
	 * @return id of this {@link Tag} as binary value of correspondent {@link VINT}
	 */
	public long getId() {
		return id.getBinary();
	}

	/**
	 * getter for size
	 * 
	 * @return size of this {@link Tag} as value of correspondent {@link VINT}
	 */
	public long getSize() {
		return size.getValue();
	}
	
	/**
	 * method to get total size of this tag: "header" + "contents"
	 * internally calls {@link Tag#totalSize(false)}
	 * 
	 * @return - total size as int
	 */
	public int totalSize() {
		return totalSize(false);
	}
	
	/**
	 * method to get total size of this tag
	 * 
	 * @param saxMode - if <code>true</code> and type of tag is {@link Type#master} "contents" size will not be added
	 * @return - total size as int
	 */
	public int totalSize(boolean saxMode) {
		return (int)(id.getLength() + size.getLength() + (getType() == Type.master && saxMode ? 0 : size.getValue()));
	}
	
	/**
	 * method to encode {@link Tag} as sequence of bytes, internally call {@link Tag#encode(false)}
	 * 
	 * @return - encoded {@link Tag}
	 * @throws IOException - in case of any IO errors
	 */
	public byte[] encode() throws IOException {
		return encode(false);
	}
	
	/**
	 * method to encode {@link Tag} as sequence of bytes
	 * in "sax" mode "master" tags will not encode their children
	 * 
	 * @param saxMode - "sax" mode if <code>true</code>, "dom" mode otherwise
	 * @return - encoded {@link Tag}
	 * @throws IOException - in case of any IO errors
	 */
	public byte[] encode(boolean saxMode) throws IOException {
		final ByteBuffer buf = ByteBuffer.allocate(totalSize(saxMode));
		log.debug("Tag: " + this);
		buf.put(id.encode());
		buf.put(size.encode());
		if (getType() != Type.master || saxMode) {
			putValue(buf);
		}
		buf.flip();
		return buf.array();
	}
	
	/**
	 * method to get "pretty" represented {@link VINT}
	 */
	@Override
	public String toString() {
		return String.format("%s %s [id: %s, size: %s]", name, getType(), id, size);
	}
}
