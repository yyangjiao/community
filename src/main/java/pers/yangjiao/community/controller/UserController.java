package pers.yangjiao.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import pers.yangjiao.community.LoginRequired;
import pers.yangjiao.community.entity.User;
import pers.yangjiao.community.service.UserService;
import pers.yangjiao.community.util.CommunityUtil;
import pers.yangjiao.community.util.HostHolder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload-path}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "未选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式错误");
            return "/site/setting";
        }
        // 生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放路径
        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传头像失败: " + e.getMessage());
            throw new RuntimeException("上传头像失败", e);
        }

        // 更新用户头像路径（web访问路径）
        // http://localhost/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(fileName);
             OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败:+" + e.getMessage());
        }
    }

    @RequestMapping(path = "/modify", method = RequestMethod.POST)
    public String modifyPassword(String oldPassword, String newPassword, Model model) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            model.addAttribute("error", "参数不可为空");
            return "/site/setting";
        }
        // 获取当前用户
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if (map.isEmpty()) {
            model.addAttribute("msg", "密码修改成功！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/setting";
        }
    }

}
