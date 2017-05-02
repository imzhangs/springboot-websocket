var ws;
/*
 * serverURI  ip:port/path
 * refers  @ServerEndpoint(value = "/ws/chat/{nickName}")
 */
var connect = function(serverURI, username) {
	var url="ws://" + serverURI + "/webSocketServer?nickName=" + username;
	
	if ('WebSocket' in window) {
		ws = new WebSocket(url);
    } else if ('MozWebSocket' in window) {
    	ws = new MozWebSocket(url);
    }
	ws.onopen = function() {
		//ws.send("connected");
		$("#btnConnect").hide();
	};
	ws.onmessage = function(evt) {
		var json = eval("(" + evt.data + ")");
		//console.log(json);
		switch (json.type) {
		case -1: // disconnection
			showDisConnectMsg();
			break;
		case 0: // connected
			$("#connectMsg").html("<label style='color:green;'>connected</label>");
			break;
		case 1: // friendList
			var userList = "";
			for (var i = 0; i < json.msg.split(",").length; i++) {
				if (null == json.msg.split(",")[i] || "" == json.msg.split(",")[i]) {
					continue;
				}
				userList += "<li>" + json.msg.split(",")[i] + "</li>";
			}
			$("#userList").html(userList);
			break;
		case 2: // chat message body
			showMsgNotification("new message",json.sender+"...");
			if (json.sender.indexOf($("#txtName").val()) >= 0) {
				$("#showMsg").append(
						"<p style='color:green;'>"  +json.date +"    ["+json.sender+"]" 
						+ "</p><p> " + json.msg + "<p>");
			} else {
				$("#showMsg").append(
						"<p style='color:#33b7ff;'>" +json.date +"    ["+ json.sender+"]"
								+ "</p><p> " + json.msg + "<p>");
			}
			$("#showMsg").scrollTop(9999);
			break;
		case 3:
			break;
		}
	};
	ws.onclose = function(evt) {
		//console.log("WebSocketClosed!");
		ws.close();
		$("#btnConnect").show();

	};
	ws.onerror = function(evt) {
		//console.log("WebSocketError!");
		showDisConnectMsg();
	};
}

function showDisConnectMsg() {
	$("#connectMsg").html("<label style='color:red;'>已断开</label>");
}

function showMsgNotification(title, msg) {
	
	if(!isFocus){
		return;
	}
	
	var Notification = window.Notification || window.mozNotification
			|| window.webkitNotification;
	
	var instance ;
	
	if (Notification && Notification.permission === "granted") {
		instance = new Notification(title, {
			body : msg,
			icon : "image_url"
		});

		instance.onclick = function() {
			window.focus();
			instance.onclose();
		};
		instance.onerror = function() {
			// Something to do
		};
		instance.onclose = function() {
			// Something to do
		};
		instance.onshow = function() {
			// Something to do
			// //console.log(instance.close);
			setTimeout("instance.close", 5000); 
		};
		
	} else if (Notification && Notification.permission !== "denied") {
		Notification.requestPermission(function(status) {
			if (Notification.permission !== status) {
				Notification.permission = status;
			}
			// If the user said okay
			if (status === "granted") {
				var instance = new Notification(title, {
					body : msg,
					icon : "image_url"
				});

				instance.onclick = function() {
					window.focus();
					instance.onclose();
				};
				instance.onerror = function() {
					// Something to do
				};

				instance.onclose = function() {
					// Something to do
				};
				instance.onshow = function() {
					// Something to do
//					instance.close();
					setTimeout("instance.close", 5000); 
				};

			} else {
				return false
			}
		});
	} else {
		return false;
	}
}

