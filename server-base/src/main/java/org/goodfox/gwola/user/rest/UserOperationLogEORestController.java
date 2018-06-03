package org.goodfox.gwola.user.rest;

import io.swagger.annotations.ApiOperation;

import org.goodfox.gwola.user.service.UserOperationLogEOService;
import org.goodfox.gwola.user.entity.UserOperationLogEO;
import org.goodfox.gwola.util.http.PageInfo;
import org.goodfox.gwola.util.http.ResponseMessage;
import org.goodfox.gwola.util.http.Result;
import io.swagger.annotations.Api;
import org.goodfox.gwola.user.entity.UserOperationLogEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/${restPath}/user/operationLog")
@Api(description = "|UserOperationLogEO|")
public class UserOperationLogEORestController {

    @Autowired
    private UserOperationLogEOService userOperationLogEOService;

    @ApiOperation(value = "|UserOperationLogEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<UserOperationLogEO> getById(@PathVariable("id") String id) {
        return Result.success(userOperationLogEOService.get(id));
    }

    @ApiOperation(value = "|UserOperationLogEO|列表")
    @GetMapping("")
    public ResponseMessage<List<UserOperationLogEO>> list(Integer pageNo, Integer pageSize) {
        PageInfo pageInfo = new PageInfo(pageNo, pageSize);
        return Result.success(userOperationLogEOService.findAll());
    }

    @ApiOperation(value = "新增|UserOperationLogEO|")
    @PostMapping("")
    public ResponseMessage<UserOperationLogEO> save(@RequestBody UserOperationLogEO sysMenuEO) {
        return Result.success(userOperationLogEOService.save(sysMenuEO));
    }

    @ApiOperation(value = "修改|UserOperationLogEO|")
    @PutMapping("")
    public ResponseMessage<UserOperationLogEO> update(@RequestBody UserOperationLogEO sysMenuEO) {
        return Result.success(userOperationLogEOService.update(sysMenuEO));
    }

    @ApiOperation(value = "删除|UserOperationLogEO|")
    @DeleteMapping("/{id}")
    public ResponseMessage deleteById(@PathVariable("id") String id) {
        userOperationLogEOService.delete(id);
        return Result.success();
    }

}
