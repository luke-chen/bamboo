package luke.bamboo.startup;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import luke.bamboo.netty.GameServer;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("game/applicationContext.xml").getBean(GameServer.class).start();
	}
}
