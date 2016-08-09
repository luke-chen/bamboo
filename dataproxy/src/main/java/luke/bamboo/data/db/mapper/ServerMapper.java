package luke.bamboo.data.db.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import luke.bamboo.data.domain.GameServerInfo;

public interface ServerMapper {
	@Select("select * from gameserver_list where type = #{type} and status = 1")
	@MapKey("name")
	public Map<String, GameServerInfo> getGameServer(@Param("type") int type);
}