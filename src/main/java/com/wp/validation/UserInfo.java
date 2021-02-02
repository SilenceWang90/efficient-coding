package com.wp.validation;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @Classname UserInfo
 * @Description 验证类实体
 * @Date 2021/2/2 18:31
 * @Created by wangpeng116
 */
@Data
public class UserInfo {

    @NotNull(message = "用户id不能为空")
    private String userId;
    @NotEmpty(message = "用户名称不能为空")
    private String userName;
    @NotBlank(message = "用户密码不能为空")
    @Length(min = 6, max = 20, message = "密码不能少于6位，多于20位")
    private String password;
    @Email(message = "必须是有效邮箱")
    private String email;
    private String phone;
    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 60, message = "年龄不能大于60岁")
    private Integer age;
    @Past(message = "生日不能为未来时间点")
    private Date birthday;
    @Size(min = 1, message = "集合不能少于一个好友")
    private List<@Valid UserInfo> friends;
}
