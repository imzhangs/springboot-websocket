package cn.szkedun.websocket.api;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageOperationApi {
	

	@RequestMapping(value="/showimage")
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		 Object path = request.getAttribute("picturePath");
	        ServletOutputStream out = response.getOutputStream();
	        if (null == path) {
	            path=request.getServletContext().getRealPath("/images")+"/default.png";
	        }
	        try {
	            File file = new File(path.toString());

	            byte[] b = FileUtils.readFileToByteArray(file);

	            out.write(b);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            out.flush();
	            out.close();
	        }
	}
}
