package cn.szkedun.websocket.api;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;

@RestController
public class FileUploadApi {

	@Value("${upload.image.path}")
	String UPLOAD_PATH ;

	String fileName = "";

	@RequestMapping(value = "/uploadfiles")
	public void uploadFiles(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String path = request.getServletContext().getRealPath("/")+UPLOAD_PATH+"/";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setHeaderEncoding("UTF-8");
		sfu.setSizeMax(2 * 1024 * 1024); // 限制文件大小

		 //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
           //获取multiRequest 中所有的文件名
            Iterator<String> iter=multiRequest.getFileNames();
             
            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                     FileUtils.forceMkdir(new File(path));
                     fileName=file.getOriginalFilename();
                     file.transferTo(new File(path+file.getOriginalFilename()));
                }
                 
            }
           
        }
		ServletOutputStream out = response.getOutputStream();
		if (StringUtils.isEmpty(path)) {
			path = request.getServletContext().getRealPath("/images") + "/default.png";
		} else {
			path = request.getContextPath() + "/" + UPLOAD_PATH + "/" + fileName;
		}
		try {
			JSONObject json = new JSONObject();
			json.put("error", 0);
			json.put("url", path);
			out.print(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

}
