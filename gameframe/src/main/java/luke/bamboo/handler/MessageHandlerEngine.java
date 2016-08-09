package luke.bamboo.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a container for message handler
 * 
 * @author luke
 */
public class MessageHandlerEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageHandlerEngine.class);
	
	private static MessageHandlerEngine engine = new MessageHandlerEngine();

	private HashMap<Short, MessageHandler> messageHandlerMap = new HashMap<Short, MessageHandler>();
	
	private MessageHandler unKnowHandler;
	
	private MessageHandler inactionHandler;
	
	private MessageHandler exceptionHandler;
	
	private HashMap<Short, Class<?>> reqClazz = new HashMap<Short, Class<?>>();
	
	public static MessageHandlerEngine getInstance() {
		return engine;
	}

	private MessageHandlerEngine() {
	}

	public Class<?> getClazz(short msgId) {
		return reqClazz.get(msgId);
	}

	public MessageHandlerEngine register(short cid, MessageHandler handler,
			Class<?> requestClazz) throws Exception {
		if (messageHandlerMap.containsKey(cid)) {
			throw new Exception("register duplicate cid for message handler");
		} else {
			if (handler != null) {
				messageHandlerMap.put(cid, handler);
				reqClazz.put(cid, requestClazz);
			} else {
				throw new Exception("handler is null");
			}
		}
		return this;
	}
	
	public MessageHandlerEngine registerHandler(short msgId, MessageHandler handler) throws Exception {
		if (messageHandlerMap.containsKey(msgId)) {
			throw new Exception(String.format("register duplicate message id [%d] to handler [%s]", msgId, handler));
		} else {
			logger.trace(String.format("register message id [%d] to handler [%s]", msgId, handler));
			if (handler != null) {
				messageHandlerMap.put(msgId, handler);
			} else {
				throw new Exception("handler is null");
			}
		}
		return this;
	}
	
	public MessageHandlerEngine registerRequest(short msgId, Class<?> requestClazz) throws Exception {
		if (reqClazz.containsKey(msgId)) {
			throw new Exception(String.format("register duplicate message id [%d] to request [%s]", msgId, requestClazz));
		} else {
			logger.trace(String.format("register message id [%d] to request [%s]", msgId, requestClazz));
			if (requestClazz != null) {
				reqClazz.put(msgId, requestClazz);
			} else {
				throw new Exception("request is null");
			}
		}
		return this;
	}

	public MessageHandlerEngine registerUnknow(MessageHandler handler) {
		unKnowHandler = handler;
		return this;
	}

	public MessageHandlerEngine registerException(MessageHandler handler) {
		exceptionHandler = handler;
		return this;
	}

	public MessageHandlerEngine registerInactive(MessageHandler handler) {
		inactionHandler = handler;
		return this;
	}

//	public MessageHandlerEngine register(final short cid,
//			final Class<?> handlerClazz, final Class<?> requestClazz)
//			throws Exception {
//		if (messageHandlerMap.containsKey(cid)) {
//			throw new Exception("register duplicate cid for message handler");
//		} else {
//			MessageHandler handler = (MessageHandler) GameContext
//					.getBean(handlerClazz);
//			if (handler != null) {
//				messageHandlerMap.put(cid, handler);
//				reqClazz.put(cid, requestClazz);
//			} else {
//				throw new Exception("handler is null");
//			}
//		}
//		return this;
//	}

	public MessageHandler getMessageHandler(final short id) {
		MessageHandler msgHandler = messageHandlerMap.get(id);
		return null != msgHandler ? msgHandler : unKnowHandler;
	}

	public MessageHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public MessageHandler getInactiveHandler() {
		return inactionHandler;
	}
}
