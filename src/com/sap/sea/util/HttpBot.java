package com.sap.sea.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.AbstractHttpMessage;

public class HttpBot {
	private static final CloseableHttpClient client = buildHttpClient();

	public static HttpResult get(String uri, Map<String, String> headers, Map<String, String> cookies) {
		CloseableHttpResponse response = null;
		StopWatch watch = new StopWatch();

		try {
			// create request
			HttpGet request = new HttpGet(uri);

			// headers
			prepareHeader(request, headers);

			// cookies
			prepareCookie(request, cookies);

			// get response
			watch.start();
			response = client.execute(request);
			watch.stop();

			return new HttpResult(response, watch.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				if (watch.isStarted()) {
					watch.stop();
				}
				return new HttpResult(response, watch.getTime());
			} else {
				return new HttpResult(e.getMessage());
			}
		} finally {

			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static HttpResult post(String uri, String body, ContentType type, Map<String, String> headers,
			Map<String, String> cookies) {
		CloseableHttpResponse response = null;
		StopWatch watch = new StopWatch();

		try {
			// create request
			HttpPost request = new HttpPost(uri);

			// headers
			prepareHeader(request, headers);

			// cookies
			prepareCookie(request, cookies);

			// content-type
			request.setEntity(new StringEntity(body, type));

			// get response
			watch.start();
			response = client.execute(request);
			watch.stop();

			return new HttpResult(response, watch.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				if (watch.isStarted()) {
					watch.stop();
				}
				return new HttpResult(response, watch.getTime());
			} else {
				return new HttpResult(e.getMessage());
			}
		} finally {

			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static HttpResult put(String uri, String body, ContentType type, Map<String, String> headers,
			Map<String, String> cookies) {
		CloseableHttpResponse response = null;
		StopWatch watch = new StopWatch();

		try {
			// create request
			HttpPut request = new HttpPut(uri);

			// headers
			prepareHeader(request, headers);

			// cookies
			prepareCookie(request, cookies);

			// content-type
			request.setEntity(new StringEntity(body, type));

			// get response
			watch.start();
			response = client.execute(request);
			watch.stop();

			return new HttpResult(response, watch.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				if (watch.isStarted()) {
					watch.stop();
				}
				return new HttpResult(response, watch.getTime());
			} else {
				return new HttpResult(e.getMessage());
			}
		} finally {

			if (response != null) {
				try {
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void prepareCookie(AbstractHttpMessage request, Map<String, String> cookies) {
		if (cookies != null && !cookies.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, String> cookie : cookies.entrySet()) {
				sb.append(String.format("%s=%s;", cookie.getKey(), cookie.getValue()));
			}
			sb.delete(sb.length() - 1, sb.length());
			request.addHeader("Cookie", sb.toString());
		}
	}

	private static void prepareHeader(AbstractHttpMessage request, Map<String, String> headers) {
		if (headers != null)
			for (Entry<String, String> header : headers.entrySet()) {
				request.addHeader(header.getKey(), header.getValue());
			}
	}

	private static CloseableHttpClient buildHttpClient() {
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling()
					.setSSLSocketFactory(sslsf).build();
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
