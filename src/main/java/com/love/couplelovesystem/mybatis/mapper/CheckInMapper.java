package com.love.couplelovesystem.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.couplelovesystem.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface CheckInMapper extends BaseMapper<CheckIn> {

    @Select("SELECT check_date FROM t_check_in WHERE role = #{role} AND check_date >= #{startDate} AND check_date <= #{endDate} ORDER BY check_date")
    List<Date> getCheckInDates(@Param("role") Integer role, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT COUNT(*) FROM (SELECT check_date, DATE_SUB(check_date, INTERVAL ROW_NUMBER() OVER (ORDER BY check_date) DAY) AS grp FROM t_check_in WHERE role = #{role}) t GROUP BY grp ORDER BY COUNT(*) DESC LIMIT 1")
    Integer getMaxConsecutiveDays(@Param("role") Integer role);
}
