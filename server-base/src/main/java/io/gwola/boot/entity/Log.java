package io.gwola.boot.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.gwola.boot.base.entity.GwolaBaseEntity;
import io.gwola.boot.common.utils.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 * @author sunyu1984
 */
@Data
@Entity
@Table(name = "t_log")
@TableName("t_log")
public class Log extends GwolaBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "方法操作名称")
    private String name;

    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    @ApiModelProperty(value = "请求类型")
    private String requestType;

    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    @ApiModelProperty(value = "请求用户")
    private String username;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "ip信息")
    private String ipInfo;

    @ApiModelProperty(value = "花费时间")
    private Integer costTime;

    /**
     * 转换请求参数为Json
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {

        this.requestParam = ObjectUtil.mapToString(paramMap);
    }

}
