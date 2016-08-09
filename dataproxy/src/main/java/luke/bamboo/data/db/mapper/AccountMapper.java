package luke.bamboo.data.db.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import luke.bamboo.data.domain.Account;

public interface AccountMapper {
	@Select("select * from account where username = #{username} and password = #{password}")
	public Account getUser(@Param("username") String username, @Param("password") String password);

	@Insert("insert into account (username, password, enable) values (#{username}, #{password}, 1)")
	public void addUser(@Param("username") String username, @Param("password") String password);
}