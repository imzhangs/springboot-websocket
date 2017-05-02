<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="cn.szkedun.websocket.utils.Base64" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<link href="css/main.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/js/connect.js"></script>
<script type="text/javascript" src="/js/kindeditor-4.1.7/kindeditor.js"></script>
<link rel="stylesheet" href="/js/kindeditor-4.1.7/themes/default/default.css" />
<script charset="utf-8" src="/js/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="/js/kindeditor-4.1.7/lang/zh_CN.js"></script>
<title>chat</title>
<script   type="text/javascript">  
  var editor;
  /****/
  KindEditor.ready(function(K) {
    editor = K.create('#txt', {
       resizeType : 1, 
       cssPath :"js/kindeditor-4.1.7/plugins/code/prettify.css",
       uploadJson : '<%=request.getContextPath() %>/uploadfiles',
       width:'755px',
       height:'80px',
	   allowFileManager : true,
	   allowUpload: true,
	   items : [ 'emoticons','|','fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', '|', 'image', '|', 'messageRecord' ],
	   colorTable : [
		  [ '#E53333', '#E56600', '#FF9900', '#64451D', '#DFC5A4', '#FFE500' ],
		  [ '#009900', '#006600', '#99BB00', '#B8D100', '#60D978', '#00D5FF' ],
		  [ '#337FE5', '#003399', '#4C33E5', '#9933E5', '#CC33E5', '#EE33EE' ],
		  [ '#FFFFFF', '#CCCCCC', '#999999', '#666666', '#333333', '#000000' ]
	   ],
	   afterCreate : function() {
		   var self=this; 
		   KindEditor.ctrl(self.edit.doc, 13, function() { 
			   self.sync(); 
			   $("#btnSend").click();
		   }); 
       }
    });
  });
  
  
</script>
</head>
<style>
body{width:100%;}
.wrap{
	mrgin:0 auto;
	width:1080px;
	height:auto;
	font-family:"微软雅黑";
	border:1px solid #CCC;
}
.main_left{
	margin:6px;
	margin: 12px 6px 6px;
	float:left;
	height:auto;
	width:250px;
	border:1px solid #CCC;
}
.main_left_content{
	margin: 3px;
	padding: 6px;
	float: left;
	height: 646px;
	width: 230px;
	/*overflow:scroll;*/
	border:1px solid #CCC;
}
.userList{
	margin-left:-30px;
	font-size:12px;
	
}
.main_right{
	margin:12px;
	padding:6px;
	float:right;
	height:auto;
	width:765px;
	/*overflow:scroll;*/
	border:1px solid #CCC;
}
.content_up{
	height:456px;
	width:100%;
}
.content_show{padding:12px;border:none;height:432px;overflow:scroll;font-size:13px;border:1px solid #CCC;}
.content_down{
	height:auto;
	width:100%;
	margin-top:18px;
}
.content_send{border:none;height:auto;}
.content_send_txt{
	border:1px solid #CCC; border-radius:6px;height:90%;width:98%;
	padding:6px;
}
.content_send_op{
	border:none; 
}
textarea{resize:none;}
</style>
<body onBlur="isFocus=true;" onFocus="isFocus=false;">
<div class="wrap">
<div style="height:28px;width:100% ;border:none;margin-left:30px;">
<p>
	<span>服务器地址:<input type="hidden" value="localhost" id="txtServerIp" size="20"/> </span>
	<span style="margin-left:20px;">昵称:<input type="text" id="txtName" size="10" maxlength="10"></span>
	<span style="margin-left:20px;"><input type="button" value="连接" id="btnConnect"></span>
	<span style="margin-left:20px;" id="connectMsg"></span>
 </p>
</div>
	<div class="main_left">
		<div class="main_left_content">
		<ul id="userList"></ul>
		</div>
	</div>
	<div class="main_right">
		<div class="content_up">
			<div class="content_show" id="showMsg"></div>
		</div>
		<div class="content_down">
			<div class="content_send">
				<textarea class="content_send_txt"  id="txt"></textarea>
			</div>
			<div class="content_send_op" align="right">
				<span style="font-size:13px;float:left;">ctrl+enter 发送    &nbsp;   &nbsp;<font color="red">图片路径不支持中文</font></span>
				<input style="width:200px;margin-right:30px;" class="btn_extends" type="button" id="btnSend" value=" send "/>
			</div>
		</div>
	</div>
</div>
</body>
<script>
var isFocus=false;
var context="<%=request.getContextPath() %>";
var token="<%= Base64.encodeBase64(request.getRemoteAddr()) %>";

	$(function(){
		$("#btnConnect").click(function(){
			var txtServerIp=$("#txtServerIp").val();
			var txtName=$("#txtName").val();
			if(null==txtServerIp || ""==txtServerIp){
				alert("亲，请填写服务器地址");
				return ;
			}
			if(null==txtName || ""==txtName){
				alert("亲，请填昵称");
				return ;
			}
			if(null==txtServerIp || txtServerIp==""){
				txtServerIp="localhost";
			}
			var websocketPort=window.location.port;
			connect(txtServerIp+":"+websocketPort+context,txtName,token);
		});
		
		$("#btnSend").click(function(){
			var txt=editor.html();
			if(null!=txt && ""!=txt){
				ws.send(txt);
				editor.html("");
			}
		});
		
		window.onbeforeunload = function () {  
	        websocket.close();  
	    }  
	});
</script>
</html>
