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

    @Select("SELECT COALESCE(MAX(c), 0) FROM (SELECT COUNT(*) AS c FROM (SELECT check_date, DATEADD('DAY', -ROW_NUMBER() OVER (ORDER BY check_date), check_date) AS grp FROM t_check_in WHERE role = #{role}) GROUP BY grp)")
    Integer getMaxConsecutiveDays(@Param("role") Integer role);
}
