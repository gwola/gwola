package io.gwola.boot.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.gwola.boot.entity.Role;
import io.gwola.boot.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sunyu1984
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Role> findByUserId(@Param("userId") String userId);
}
