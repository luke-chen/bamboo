package luke.bamboo.data.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import luke.bamboo.data.db.mapper.ServerMapper;
import luke.bamboo.data.domain.GameServerInfo;

@Service
public class ServerInfoDataService {
	
	public static final int  TYPE_OFFICIAL = 1;
	
	public static final int  TYPE_DEBUG = 0;

	@Autowired
	private ServerMapper serverMapper;

	public Map<String, GameServerInfo> getOfficialServer() {
		return serverMapper.getGameServer(TYPE_OFFICIAL);
	}
	
	public Map<String, GameServerInfo> getDebufServer() {
		return serverMapper.getGameServer(TYPE_DEBUG);
	}
}
