package pers.zhengxiaojie.application.module.petition;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequestMapping(value = "/petition")
@RestController
public class PetitionController {
  @Autowired
  private PetitionService petitionService;

  @Autowired
  private EmployeeService employeeService;

  /*
  * 分页获取请假条信息
  * */
  @GetMapping(value = "/{current}/{size}")
  public Result getPetitionMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
    IPage<Petition> petition = petitionService.page(new Page<>(current, size));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    List<Petition> records = petition.getRecords();

    List<PetitionVO> newRecords = records.stream().map(item -> {
      if (item.getHandlerAccount() != null) {
        return new PetitionVO(
          item.getContent(),
          item.getReply(),
          item.getType(),
          item.getIsHandled(),
          item.getProposerAccount(),
          item.getHandlerAccount(),
          employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getProposerAccount())).getName(),
          employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getHandlerAccount())).getName(),
          item.getId(),
          item.getIsDeleted(),
          item.getVersion(),
          item.getCreateTime(),
          LocalDateTime.now() ,
          item.getStartDate(),
          item.getEndDate()
        );
      }else {
        return new PetitionVO(
          item.getContent(),
          item.getReply(),
          item.getType(),
          item.getIsHandled(),
          item.getProposerAccount(),
          null,
          employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getProposerAccount())).getName(),
          null,
          item.getId(),
          item.getIsDeleted(),
          item.getVersion(),
          item.getCreateTime(),
          LocalDateTime.now() ,
          item.getStartDate(),
          item.getEndDate()
        );
      }
    }).collect(Collectors.toList());

    data.put("petition", newRecords);
    data.put("petitionPage", petition);

    return ResultUtil.success(data, "获取请假条信息成功");
  }

  /*
   * 单条请假条信息
   * */
  @GetMapping(value = "/{id}")
  public Result getPetitionMultiple(@PathVariable(value = "id") Integer id) {
    Petition item = petitionService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    PetitionVO petitionVO = null;
    if (item.getHandlerAccount() != null) {
      petitionVO = new PetitionVO(
        item.getContent(),
        item.getReply(),
        item.getType(),
        item.getIsHandled(),
        item.getProposerAccount(),
        item.getHandlerAccount(),
        employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getProposerAccount())).getName(),
        employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getHandlerAccount())).getName(),
        item.getId(),
        item.getIsDeleted(),
        item.getVersion(),
        item.getCreateTime(),
        item.getUpdateTime(),
        item.getStartDate(),
        item.getEndDate()
      );
    }else {
      petitionVO =  new PetitionVO(
        item.getContent(),
        item.getReply(),
        item.getType(),
        item.getIsHandled(),
        item.getProposerAccount(),
        null,
        employeeService.getOne(new QueryWrapper<Employee>().eq("username", item.getProposerAccount())).getName(),
        null,
        item.getId(),
        item.getIsDeleted(),
        item.getVersion(),
        item.getCreateTime(),
        item.getUpdateTime(),
        item.getStartDate(),
        item.getEndDate()
      );
    }

    data.put("petition", petitionVO);

    return ResultUtil.success(data, "获取请假条信息成功");
  }

  /*
  *  申请请假条
  * */
  @PostMapping
  public Result postPetitionByProposer(@RequestBody Petition petition) {
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    petitionService.save(petition);

    return ResultUtil.success(null, "已发起请假流程，等待审核");
  }

  /*
  * 修改请假条，与申请请假条区别在于是否有ID（此操作针对于处理者）
  * */
  @PutMapping
  public Result postPetitionByHandler(@RequestBody Petition petition) {
    if (petitionService.getById(petition.getId()).getIsHandled() == 1) {
      return ResultUtil.success(null, "此请假条已由其他管理员审核完毕");
    } else {
      petitionService.updateById(petition);
      return ResultUtil.success(null, "审核完毕");
    }
  }

  /*
  * 撤销请假条, 当处理状态不是1（即已被提交的时候）时候则可以进行撤销
  * */
  @DeleteMapping(value = "/{id}")
  public Result postPetitiond(@PathVariable(value = "id") Integer id) {
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    if (petitionService.getById(id).getIsHandled() == 0) {
      petitionService.removeById(id);
    }

    return ResultUtil.success(data, "已发起请假流程，等待通知");
  }
}
