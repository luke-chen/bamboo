package luke.bamboo.data.db.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import luke.bamboo.data.domain.Cost;
import luke.bamboo.data.domain.Player;

public interface PlayerMapper {
	@Insert("insert into player (accountId,name,phyle,vip,level,gold,wood,stone,iron,diamonds,crystal) values (#{accountId},#{name}, #{phyle}, #{vip}, #{level}, #{gold}, #{wood}, #{stone}, #{iron}, #{diamonds}, #{crystal})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
	public void addPlayer(Player player);

	@Select("select * from player where id = #{playerId} limit 1")
	public  Player getPlayer(long playerId);
	
	@Select("select * from player where accountId = #{accountId} limit 1")
	public  Player getPlayerByAccount(@Param("accountId")int accountId);

	@Select("select name from player where name = #{name} limit 1")
	public String nameIsExist(@Param("name") String name);
	
	@Update("update player set vip=#{vip}, level=#{level}, gold=#{gold}, wood=#{wood}, stone=#{stone}, iron=#{iron}, diamonds=#{diamonds}, crystal=#{crystal} where id=#{id}")
	public void updatePlayer(Player player);
	
	@Select("select unix_timestamp() % (select count(id) from player where score<=#{score}+50 and score>=#{score}-50) as seq")
	public long getEnemyBySocre(Player player);
	
	@Select("select * from player order by id limit #{n}, 1")
	public Player getPlayerBylimit(@Param("n") long n);
	
	@Update("update player set level = level + 1 where id = #{id}")
	public void upgradePlayer(@Param("id")long playerId);
	
	@Select("select gold, wood, stone, iron, diamonds, crystal from player where id = #{id}")
	public Cost getResource(@Param("id")long playerId);
}