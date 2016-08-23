package luke.bamboo.monitor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Monitor {
	private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
	
	@Value("${zk.connect}")
	private String zkStr;
	
	@Value("${game.ip}")
	private String host;

	@Value("${game.port}")
	private int port;
	
	private Timer timer;
	
	@PostConstruct
	public void start() {
		timer = new Timer();
//		timer.schedule(new Reporter(host+":"+port, zkStr), 3000, 3000);
		logger.info("Monitor startup: "+zkStr);
	}
}
