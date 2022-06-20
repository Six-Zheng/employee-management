package pers.zhengxiaojie.application.module.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.zhengxiaojie.application.exception.PasswordErrorException;
import pers.zhengxiaojie.application.exception.VerificationCodeTimeOutException;
import pers.zhengxiaojie.application.module.department.Department;
import pers.zhengxiaojie.application.module.department.DepartmentService;
import pers.zhengxiaojie.application.module.post.Post;
import pers.zhengxiaojie.application.module.post.PostService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.module.role.Role;
import pers.zhengxiaojie.application.module.role.RoleService;
import pers.zhengxiaojie.application.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private PostService postService;

  @Value("${spring.security.token.signature}")
  private String signature;

  @Value(value = "${spring.image.save-path}")
  private String imageSavePath;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private FTPUtil ftpUtil;

  @PostMapping(value = "/avatar")
  public Result postEmployeeAvatar(@RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile, HttpServletRequest httpServletRequest) throws IOException {
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    String a = httpServletRequest.getParameter("fileName");
    System.out.println(a);
    OSSUtil.uploadImage(multipartFile, a);
    String avatar = "https://eduteacher-pic.oss-cn-guangzhou.aliyuncs.com/" + a + ".jpg";
    data.put("avatar",avatar);

    return ResultUtil.success(data, "上传图片成功");
  }

  @PostMapping
  public Result postEmployee(@RequestBody Employee employee) {
    employeeService.save(employee);

    return ResultUtil.success(null, "添加员工信息成功");
  }

  @DeleteMapping(value = "/{id}")
  public Result deleteEmployeeSingle(@PathVariable(value = "id") Integer id) {
    employeeService.removeById(id);

    return ResultUtil.success(null, "删除员工信息成功");
  }

  @DeleteMapping
  public Result deleteEmployeeMultiple(@RequestBody List<Integer> ids) {
    employeeService.removeBatchByIds(ids);

    return ResultUtil.success(null, "删除员工信息成功");
  }

  @GetMapping(value = "/token")
  public Result getEmployeeMultiple(@RequestHeader(value = "token") String token) throws UnsupportedEncodingException, JsonProcessingException {
    ConcurrentHashMap<String, Object> payloadData = TokenUtil.getTokenPayloadData(token, signature);
    String username = (String) payloadData.get("username");
    Employee employee = employeeService.getEmployeeByUsername(username);

    String roleName = roleService.getById(employee.getRoleId()).getName();
    String departmentName = departmentService.getById(employee.getDepartmentId()).getName();
    String postName = postService.getById(employee.getPostId()).getName();

    ConcurrentHashMap<String, Object>  data = new ConcurrentHashMap<>();

    data.put("employee", employee);
    data.put("postName", postName);
    data.put("departmentName", departmentName);
    data.put("roleName", roleName);

    return ResultUtil.success(data, "获取员工信息成功");
  }

  @GetMapping(value = "/{id}")
  public Result getEmployeeMultiple(@PathVariable(value = "id") Integer id) {
    Employee employee = employeeService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("employee", employee);

    return ResultUtil.success(data, "获取员工信息成功");
  }



  @GetMapping(value = "/{current}/{size}")
  public Result getEmployeeMultiple(HttpServletRequest httpServletRequest,@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) throws UnsupportedEncodingException, JsonProcessingException {
    String token = httpServletRequest.getHeader("token");
    ConcurrentHashMap<String, Object> tokenPayloadData = TokenUtil.getTokenPayloadData(token, signature);
    IPage<Employee> employee = employeeService.page(new Page<>(current, size), new QueryWrapper<Employee>().ne("username", tokenPayloadData.get("username")));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("employeePage", employee);

    return ResultUtil.success(data, "获取员工信息成功");
  }

  @GetMapping(value = "/{current}/{size}/{name}")
  public Result getEmployeeMultiple(HttpServletRequest httpServletRequest,@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size,  @PathVariable(value = "name") String name) throws UnsupportedEncodingException, JsonProcessingException {
    String token = httpServletRequest.getHeader("token");
    ConcurrentHashMap<String, Object> tokenPayloadData = TokenUtil.getTokenPayloadData(token, signature);
    QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
    employeeQueryWrapper.like("name", name);
    employeeQueryWrapper.ne("username", tokenPayloadData.get("username"));
    IPage<Employee> employee = employeeService.page(new Page<>(current, size), employeeQueryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("employeePage", employee);
    System.out.println(employee);

    return ResultUtil.success(data, "获取员工信息成功");
  }

  @GetMapping
  public Result getEmployee(@RequestHeader(value = "token") String token) throws UnsupportedEncodingException, JsonProcessingException {
    ConcurrentHashMap<String, Object> tokenPayloadData = TokenUtil.getTokenPayloadData(token, signature);

    List<Employee> list = employeeService.list(new QueryWrapper<Employee>().ne("username", tokenPayloadData.get("username")));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    data.put("employees", list);
    return ResultUtil.success(data, "获取员工信息成功");
  }

  @PutMapping(value = "/reset_password/{username}")
//  注意，0为验证码，1为密码
  public Result resetEmployeePassword(@PathVariable(value = "username") String username, @RequestBody String[] verificationCodeAndPassword) {
    String redisVerificationCode = (String) redisUtil.getValue("reset_password-" + username);

//    若验证码匹配成功，则OK
    if (verificationCodeAndPassword[0].equals(redisVerificationCode)) {
      Employee employee = employeeService.getEmployeeByUsername(username);
      String salt = MD5Util.getRandomString(16);
      String password = MD5Util.getMD5(verificationCodeAndPassword[1], salt, 1024);

      employee.setSalt(salt);
      employee.setPassword(password);
      employeeService.updateById(employee);
    }

    return ResultUtil.success(null, "重置密码成功，请重新登录");
  }

  @PutMapping
  public Result putEmployee(@RequestBody Employee employee) {
    employeeService.updateById(employee);

    return ResultUtil.success(null, "修改员工信息成功");
  }


  @PutMapping(value = "/reset_password/{username}/{verification_code}/{password}")
  public Result resetEmployeePassword(@PathVariable(value = "username") String username, @PathVariable(value = "verification_code") String verification_code,  @PathVariable(value = "password") String password) {
    Employee employee = employeeService.getEmployeeByUsername(username);
    String salt = MD5Util.getRandomString(16);
    String newPassword = MD5Util.getMD5(password, salt, 1024);
    String redisVerificationCode = (String) redisUtil.getValue("reset_password-" + username);

    if (redisVerificationCode.equals(verification_code)){
      employee.setPassword(newPassword);
      employee.setSalt(salt);
      employeeService.updateById(employee);
    } else {
//      TODO 扔出验证码过期或超时问题
      throw new VerificationCodeTimeOutException("验证码已过期");
    }

    return ResultUtil.success(null, "重置密码成功");
  }

  @PutMapping(value = "/reset_password_self/{username}/{old_password}/{new_password}")
  public Result resetEmployeePasswordSelf(@PathVariable(value = "username") String username, @PathVariable(value = "old_password") String oldPassword,  @PathVariable(value = "new_password") String newPassword) {
    Employee employee = employeeService.getEmployeeByUsername(username);

    //校验旧密码是否准确
    boolean isRight = MD5Util.verifyMD5IsValid(
      employee.getPassword(),
      oldPassword,
      employee.getSalt(),
      1024
    );

    if (isRight) {
      String salt = MD5Util.getRandomString(16);
      String newPasswordBySalt = MD5Util.getMD5(newPassword, salt, 1024);
      employee.setPassword(newPasswordBySalt);
      employee.setSalt(salt);
      employeeService.updateById(employee);

    } else {
      throw new PasswordErrorException("密码错误");
    }

    return ResultUtil.success(null, "重置密码成功");
  }

  @GetMapping(value = "/{username}/fix")
  public Result getEmployeeMultipleFix(@PathVariable(value = "username") String username)  {
    Employee employee = employeeService.getEmployeeByUsername(username);

    String roleName = roleService.getById(employee.getRoleId()).getName();
    String departmentName = departmentService.getById(employee.getDepartmentId()).getName();
    String postName = postService.getById(employee.getPostId()).getName();

    ConcurrentHashMap<String, Object>  data = new ConcurrentHashMap<>();

    data.put("employee", employee);
    data.put("postName", postName);
    data.put("departmentName", departmentName);
    data.put("roleName", roleName);

    return ResultUtil.success(data, "获取员工信息成功");
  }

  @GetMapping(value = "/fixed/bug")
  public Result getFixedProblem(){
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
    List<Employee> employees = employeeService.list();

    List<EmployeeDTO> employeeDTOS = employees.stream().map(employee -> {
      String sex = employee.getSex() == 1 ? "男" : "女";
      Department department = departmentService.getById(employee.getDepartmentId());
      Post post = postService.getById(employee.getPostId());
      Role role = roleService.getById(employee.getRoleId());

      return new EmployeeDTO(
        employee.getName(),
        employee.getUsername(),
        employee.getEmail(),
        employee.getPhone(),
        sex,
        department.getName(),
        post.getName(),
        role.getName()
      );
    }).collect(Collectors.toList());

    data.put("employees", employeeDTOS);
    return ResultUtil.success(data, "获取员工信息成功");
  }

  public static void main(String[] args) {
    String salt = MD5Util.getRandomString(16);
    String password = MD5Util.getMD5("123456", salt, 1024);

    System.out.println(salt);
    System.out.println(password);
  }
}
