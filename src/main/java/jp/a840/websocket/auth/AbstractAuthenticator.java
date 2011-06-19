/*
 * The MIT License
 * 
 * Copyright (c) 2011 Takahiro Hashimoto
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.a840.websocket.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.a840.websocket.HttpHeader;
import jp.a840.websocket.WebSocket;
import jp.a840.websocket.WebSocketException;
import jp.a840.websocket.util.StringUtil;

/**
 * The Class AbstractAuthenticator.
 *
 * @author Takahiro Hashimoto
 */
abstract public class AbstractAuthenticator implements Authenticator {
	
	/** The websocket. */
	protected WebSocket websocket;
	
	/** The credentials. */
	protected Credentials credentials;

	/**
	 * Instantiates a new abstract authenticator.
	 */
	public AbstractAuthenticator(){
	}
	
	/* (non-Javadoc)
	 * @see jp.a840.websocket.auth.Authenticator#getCredentials(java.lang.String, java.lang.String, jp.a840.websocket.HttpHeader, java.lang.String)
	 */
	public String getCredentials(String method, String requestUri, HttpHeader header, String authenticateHeaderName) throws WebSocketException {
		List<String> proxyAuthenticateList = header
				.getHeaderValues(authenticateHeaderName);
		
		List<Challenge> challengeList = new ArrayList<Challenge>();
		for (String proxyAuthenticateStr : proxyAuthenticateList) {
			// key:   Proxy-Authenticate
			// value: Basic realm="WallyWorld"
			String[] parts = proxyAuthenticateStr.split(" +", 2);
			String authScheme = parts[0];
			String authParams = parts[1];

			Map<String, String> paramMap = StringUtil.parseKeyValues(authParams, ',');

			challengeList.add(new Challenge(method, requestUri, authScheme, paramMap));
		}
		
		return getCredentials(challengeList);
	}

	/* (non-Javadoc)
	 * @see jp.a840.websocket.proxy.ProxyCredentials#getCredentials()
	 */
	abstract public String getCredentials(List<Challenge> challengeList) throws WebSocketException;
	
	/* (non-Javadoc)
	 * @see jp.a840.websocket.auth.Authenticator#init(jp.a840.websocket.WebSocket, jp.a840.websocket.auth.Credentials)
	 */
	public void init(WebSocket websocket, Credentials credentials) {
		this.websocket = websocket;
		this.credentials = credentials;
	}
}