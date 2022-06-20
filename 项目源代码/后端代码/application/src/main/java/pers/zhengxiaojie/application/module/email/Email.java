package pers.zhengxiaojie.application.module.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
    private String receiver; // 邮件接收方

    private String subject; // 邮件主题

    private String content; // 邮件内容

    private List<String> attachmentUrls; // 邮件附件

    private String templateName; // 模板名称

    private Map<String, Object> data;
}
