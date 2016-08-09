package luke.bamboo.netty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import luke.bamboo.annotation.ExceptionHandler;
import luke.bamboo.annotation.GameService;
import luke.bamboo.annotation.Handler;
import luke.bamboo.annotation.InactiveHandler;
import luke.bamboo.annotation.RequestID;
import luke.bamboo.annotation.UnknownHandler;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.handler.MessageHandlerEngine;
import luke.bamboo.logic.service.GameLogicInterface;

@Component
public class GameApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(GameApplicationListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("*** Initialize register hanler and service for engine ***");
		try {
			ApplicationContext context = event.getApplicationContext();

			Map<Class<?>, Short> msgIdDict = new HashMap<Class<?>, Short>();
			for (String beanName : context.getBeansWithAnnotation(RequestID.class).keySet()) {
				RequestID id = context.findAnnotationOnBean(beanName, RequestID.class);
				MessageHandlerEngine.getInstance().registerRequest(id.value(), context.getBean(beanName).getClass());
				msgIdDict.put(context.getBean(beanName).getClass(), id.value());
			}

			for (String beanName : context.getBeansWithAnnotation(Handler.class).keySet()) {
				Handler handler = context.findAnnotationOnBean(beanName, Handler.class);
				for (Class<?> clazz : handler.value()) {
					Short msgId = msgIdDict.get(clazz);
					if (msgId != null) {
						MessageHandlerEngine.getInstance().registerHandler(msgId.shortValue(),
								(MessageHandler) context.getBean(beanName));
					} else {
						logger.error(String.format("message id[%d] was not found on [%s]: ", msgId, clazz));
					}
				}
			}
			
			Set<String> beanSet = context.getBeansWithAnnotation(InactiveHandler.class).keySet();
			if(beanSet.isEmpty()) {
				logger.info("unregister InactiveHandler");
			}
			else if(beanSet.size() == 1) {
				for (String beanName : beanSet) {	
					MessageHandlerEngine.getInstance().registerInactive((MessageHandler) context.getBean(beanName));
				}
			}
			else {
				throw new Exception(String.format("register duplicate InactiveHandler"));
			}
			
			beanSet = context.getBeansWithAnnotation(UnknownHandler.class).keySet();
			if(beanSet.isEmpty()) {
				logger.info("unregister UnknownHandler");
			}
			else if(beanSet.size() == 1) {
				for (String beanName : beanSet) {	
					MessageHandlerEngine.getInstance().registerUnknow((MessageHandler) context.getBean(beanName));
				}
			}
			else {
				throw new Exception(String.format("register duplicate UnknownHandler"));
			}
			
			beanSet = context.getBeansWithAnnotation(ExceptionHandler.class).keySet();
			if(beanSet.isEmpty()) {
				logger.info("unregister ExceptionHandler");
			}
			else if(beanSet.size() == 1) {
				for (String beanName : beanSet) {	
					MessageHandlerEngine.getInstance().registerException((MessageHandler) context.getBean(beanName));
				}
			}
			else {
				throw new Exception(String.format("register duplicate ExceptionHandler"));
			}

			Map<Integer, GameLogicInterface> gameServiceDict = new HashMap<Integer, GameLogicInterface>();
			for (String beanName : context.getBeansWithAnnotation(GameService.class).keySet()) {
				GameLogicInterface gameServiceBean = (GameLogicInterface) context.getBean(beanName);
				GameService gameService = context.findAnnotationOnBean(beanName, GameService.class);
				if (gameServiceDict.containsKey(gameService.order())) {
					// logger.error(String.format("message id[%d] was not found
					// on [%s]: ", msgId, clazz));
					throw new Exception(String.format("register duplicate order id [%d] to gameservice [%s]",
							gameService.order(), gameServiceBean));
				} else {
					gameServiceDict.put(gameService.order(), gameServiceBean);
				}
			}
			Object[] orderIds = gameServiceDict.keySet().toArray();
			Arrays.sort(orderIds);
			for (Object id : orderIds) {
				GameContext.addGameService(gameServiceDict.get(id));
				logger.trace(String.format("register order id[%d] to gameservice [%s]: ", id ,gameServiceDict.get(id)));
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
