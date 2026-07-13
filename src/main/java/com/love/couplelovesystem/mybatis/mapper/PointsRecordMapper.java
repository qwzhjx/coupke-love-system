package com.love.couplelovesystem.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.love.couplelovesystem.entity.PointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointsRecordMapper extends BaseMapper<PointsRecord> {

    @Select("SELECT COALESCE(SUM(CASE WHEN type = 1 THEN amount ELSE -amount END), 0) FROM t_points_record WHERE role = #{role}")
    Integer getBalance(@Param("role") Integer role);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_points_record WHERE role = #{role} AND type = 1 AND DATE(create_time) = CURDATE()")
    Integer getTodayGain(@Param("role") Integer role);
}
