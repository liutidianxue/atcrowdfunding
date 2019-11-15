package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.AdvertService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * @author hewei
 * @date 2019/10/5 - 13:30
 */
@Controller
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    @RequestMapping("index")
    public String index(){
        return "advert/index";
    }

    @RequestMapping("add")
    public String add(){
        return "advert/add";
    }

    @ResponseBody
    @RequestMapping("showAdverts")
    public AjaxResult showAdverts(@RequestParam(value="page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize,
                                String queryText){
        AjaxResult result = new AjaxResult();
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("page",page);
            map.put("pagesize", pagesize);
            map.put("queryText", queryText);



            PageInfo pageInfo = advertService.queryPage(map);
            result.setPageInfo(pageInfo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("数据查询错误");
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView();
        Advertisement advert = advertService.queryById(id);

        mv.addObject("advert", advert);

        mv.setViewName("advert/update");
        return mv;
    }


    @ResponseBody
    @RequestMapping("/doUpdate")
    public AjaxResult doUpdate(Advertisement advertisement){
        AjaxResult result = new AjaxResult();
        try {
            advertService.updateAdvert(advertisement);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult doDelete(Integer id){
        AjaxResult result = new AjaxResult();
        try {
            advertService.deleteAdvert(id);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    /*//同步请求处理.
	@RequestMapping("/doAdd")
	public String doAdd(HttpServletRequest request, Advertisement advert , MultipartFile advpic) {
        try {
            // 校验文件类型
            List<String> content_types = Arrays.asList("image/gif", "image/jpeg");
            String contentType = advpic.getContentType();
            if (!content_types.contains(contentType)) {
                throw new RuntimeException("文件类型不合法");
            }
            //校验文件内容
                BufferedImage bufferedImage = ImageIO.read(advpic.getInputStream());
                if (bufferedImage == null) {
                    throw new RuntimeException("文件内容不合法");

                }


            // 先获取到要上传的文件目录
            String path = request.getSession().getServletContext().getRealPath("/uploads");
            // 创建File对象，一会向该路径下上传文件
            File file = new File(path);
            // 判断路径是否存在，如果不存在，创建该路径
            if(!file.exists()) {
                file.mkdirs();
            }


            // 获取到上传文件的名称
            String filename = advpic.getOriginalFilename();
            // 把文件的名称唯一化
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            filename = uuid+"_"+filename;

            advpic.transferTo(new File(path,filename)); //文件上传.


			User user = (User)request.getSession().getAttribute("user");
			advert.setUserid(user.getId());
			advert.setStatus("1");
			advert.setIconpath(filename);

			advertService.insertAdvert(advert);

            return "redirect:/advert/index.htm";
            } catch ( Exception e ) {
                e.printStackTrace();

            }

		return null;
	}*/

    //异步请求处理.
    @ResponseBody
	@RequestMapping("/doAdd")
	public AjaxResult doAdd(HttpServletRequest request, Advertisement advert , MultipartFile advpic) {
        AjaxResult result = new AjaxResult();
        try {
            // 校验文件类型
            List<String> content_types = Arrays.asList("image/gif", "image/jpeg");
            String contentType = advpic.getContentType();
            if (!content_types.contains(contentType)) {
                throw new RuntimeException("文件类型不合法");
            }
            //校验文件内容
                BufferedImage bufferedImage = ImageIO.read(advpic.getInputStream());
                if (bufferedImage == null) {
                    throw new RuntimeException("文件内容不合法");

                }


            // 先获取到要上传的文件目录
            String path = request.getSession().getServletContext().getRealPath("/uploads");
            // 创建File对象，一会向该路径下上传文件
            File file = new File(path);
            // 判断路径是否存在，如果不存在，创建该路径
            if(!file.exists()) {
                file.mkdirs();
            }


            // 获取到上传文件的名称
            String filename = advpic.getOriginalFilename();
            // 把文件的名称唯一化
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            filename = uuid+"_"+filename;

            advpic.transferTo(new File(path,filename)); //文件上传.


			User user = (User)request.getSession().getAttribute("user");
			advert.setUserid(user.getId());
			advert.setStatus("1");
			advert.setIconpath(filename);

			advertService.insertAdvert(advert);

            result.setSuccess(true);
            return result;
            } catch ( Exception e ) {
                e.printStackTrace();

            }

		return result;
	}


}
