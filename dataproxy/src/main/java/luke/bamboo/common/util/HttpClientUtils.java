package luke.bamboo.common.util;

import org.apache.commons.codec.Encoder;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP请求工具类
 */
public final class HttpClientUtils {

	private final static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

	private static final CloseableHttpClient HTTP_CLIENT;
	private static final RequestConfig REQUEST_CONFIG;
	/**
	 * 获取连接的最大等待时间(单位毫秒)
	 */
	private final static int WAIT_TIMEOUT = 1000 * 60 * 3;
	/**
	 * 连接超时时间
	 */
	private final static int CONNECT_TIMEOUT = 1000 * 60 * 3;
	/**
	 * 读取超时时间
	 */
	private final static int READ_TIMEOUT = 1000 * 60 * 3;
	/**
	 * 最大连接数
	 */
	private final static int MAX_TOTAL_CONNECTIONS = 256;

	static {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connManager.setDefaultSocketConfig(socketConfig);
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
				.setMaxLineLength(2000).build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_TOTAL_CONNECTIONS);

		REQUEST_CONFIG = RequestConfig.custom().setStaleConnectionCheckEnabled(false).setSocketTimeout(READ_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(WAIT_TIMEOUT).build();

		HTTP_CLIENT = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(REQUEST_CONFIG)
				.disableCookieManagement().disableAutomaticRetries().build();

		final ConnectionMonitorThread closedConnection = new ConnectionMonitorThread(connManager);
		closedConnection.setDaemon(true);
		closedConnection.setContextClassLoader(null);
		closedConnection.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					closedConnection.interrupt();
					closedConnection.shutdown();
					HTTP_CLIENT.close();
				} catch (Exception e) {
					log.error(e.toString());
				}
			}
		});
	}

	private HttpClientUtils() {
	}

	/**
	 * 关闭无效链接
	 */
	private static class ConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public ConnectionMonitorThread(HttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(4 * 1000);
						connMgr.closeExpiredConnections();
						connMgr.closeIdleConnections(8, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				log.error(ex.toString());
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

//	private static byte[] open(HttpRequestBase method) {
//		String url = method.getURI().toString();
//		try {
//			method.setConfig(REQUEST_CONFIG);
//			CloseableHttpResponse response = HTTP_CLIENT.execute(method);
//			final StatusLine statusLine = response.getStatusLine();
//			final HttpEntity entity = response.getEntity();
//			try {
//				if (statusLine.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
//					EntityUtils.consumeQuietly(entity);
//					log.warn(url + " 请求失败，状态值 ：" + statusLine.getStatusCode());
//				} else if (entity != null) {
//					return EntityUtils.toByteArray(entity);
//				}
//			} finally {
//				EntityUtils.consumeQuietly(entity);
//			}
//		} catch (Exception e) {
//			log.error(url + " 请求异常，原因 ：" + ExceptionUtils.getRootCauseMessage(e));
//		}
//		return null;
//	}
	
	private static String open(HttpRequestBase method, RspCode rspCode) throws ParseException, IOException {
		try (CloseableHttpResponse response = HTTP_CLIENT.execute(method) ) {
			if(rspCode != null) {
				rspCode.setCode(response.getStatusLine().getStatusCode());
			}
			String data = EntityUtils.toString(response.getEntity(), "utf-8");
			log.debug(String.format("response code:%d data:%s", response.getStatusLine().getStatusCode(), data));
			return data;
		}
	}
	
	public static String fetchByGet(String url, RspCode rspCode) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		get.setConfig(REQUEST_CONFIG);
		return open(get, rspCode);
	}

	public static <T> T fetchByGet(String url, Class<T> clazz, RspCode rspCode) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		get.setConfig(REQUEST_CONFIG);
		String data = open(get, rspCode);
//		log.debug("response data:" + data);
		if (StringUtil.isNotEmpty(data)) {
			return JsonUtil.toObject(data, clazz);
		} else {
			return null;
		}
	}

	public static <T> T fetchByPost(String url, List<NameValuePair> params, Class<T> clazz, RspCode rspCode)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		String data = open(post, rspCode);
//		log.debug("response data:" + data);
		if (StringUtil.isNotEmpty(data)) {
			return JsonUtil.toObject(data, clazz);
		} else {
			return null;
		}
	}
	
	public static String fetchByPost(String url, List<NameValuePair> params, RspCode rspCode)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		String data = open(post, rspCode);
//		log.debug("response data:" + data);
		if (StringUtil.isNotEmpty(data)) {
			return data;
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
//		HttpClientUtils t = new HttpClientUtils();
//		List<NameValuePair> l = new ArrayList<NameValuePair>();
//		l.add(new BasicNameValuePair("uid", "123"));
//		l.add(new BasicNameValuePair("ac", "wifi"));
//		try {
//			t.fetchByPost("http://54.183.143.105:11000/v1/news/main", l, null);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
