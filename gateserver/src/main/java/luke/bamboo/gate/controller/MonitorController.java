package luke.bamboo.gate.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import luke.bamboo.gate.service.GateService;
import luke.bamboo.gate.service.ResourceService;

@Controller
public class MonitorController {
	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

	@Autowired
	GateService gateService;
	
	@Autowired
	ResourceService resourceService;
	
	@PostConstruct
	public void init() {
		// 加载excel 资源到redis
		resourceService.loadGameResources();
	}

	@PreDestroy
	public void destory() {
	}
	
	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public String monitor(ModelMap model) {
		return "monitor";
	}
}