var socket = new WebSocket(getWsUrl());
var onpagename = "loginpage"

var chat = {};

chat.messages = [];
chat.newLine = (function(msg, sender) {
	this.messages.push(sender + ": " + msg);
	this.refreshHistory();

});
chat.refreshHistory = (function() {
	var chatbox = $('#chat_history');
	var scrollContainer = $('#chat_container');
	chatbox.html("");
	for (var i = 0; i < this.messages.length; i++) {
		chatbox.html(chatbox.html() + "<br>" + this.messages[i]);
	}
	scrollContainer.scrollTop(function () {return this.scrollHeight;});
});

socket.onopen = onOpen;
socket.onclose = onClose;
socket.onmessage = onMessage;
socket.onerror = onError;

function log(msg) {
	console.log(msg);
}

function onOpen() {
	log("Socket Opened.")

}
function onClose() {
	log("Socket Closed.")

}
function onMessage(msg) {
	log("Message: " + msg.data);
	handleMessage(JSON.parse(msg.data));
}

function onError (error) {
	log("ERROR: " + error);
}

function handleMessage (msg) {
	switch (msg.type) {
		case "login":
			if(msg.success) {
				showPage("mainpage");
				log("sikeres bejelentkezés");
			} else {
				alert("Hibás bejelentkezési adatok!");
				log("sikertelen bejelentkezés");
			}
			break;
		case "register":
			if(msg.success) {
				showPage("loginpage");
				alert("Sikeres regisztráció, most már beléphetsz!");
				log("sikeres regisztráció");
			} else {
				alert("Sikertelen regisztráció " + msg.fault);
				log("sikertelen regisztráció");
			}
			break;
		case "chat":
			chat.newLine(msg.message, msg.from);
			break;
		case "error":
			log("Error: " + msg.message);
		default:
			log("Unkonwn message!");
	}
}

function send(data) {
	var msg = JSON.stringify(data);
	//alert(msg);
	socket.send(msg);
}

function login() {
	var nametb = $('#login_name');
	var passwordtb = $('#login_password');
	var name = nametb.val();
	var password = passwordtb.val();
	nametb.val('');
	passwordtb.val('');
	var loginmsg = {};
	loginmsg.type = "login";
	loginmsg.name = name;
	loginmsg.password = password;
	send(loginmsg);
}

function loginGuest() {
	var guestmsg = {};
	guestmsg.type = "guestlogin";
	send(guestmsg);
}

function register() {
	var nametb = $('#register_name');
	var emailtb = $('#email');
	var passwordtb = $('#register_password');
	var confirmtb = $("#confirmpw");
	var name = nametb.val();
	var email = emailtb.val();
	var password = passwordtb.val();
	nametb.val('');
	emailtb.val('');
	passwordtb.val('');
	confirmtb.val('');
	var registermsg = {};
	registermsg.type = "register";
	registermsg.name = name;
	registermsg.email = email;
	registermsg.password = password;
	send(registermsg);
}

function sendChat() {
	var msgtb = $('#chat_msg');
	var msg = msgtb.val();
	msgtb.val('');
	msgtb.focus();
	var chatmsg = {};
	chatmsg.type = "chat";
	chatmsg.message = msg;
	send(chatmsg);
}

function getWsUrl() {
	if (window.location.protocol == 'http:') {
		return 'ws://' + window.location.host + '/UltiCardGame/websocket/ulti';
	} else {
		return 'wss://' + window.location.host + '/UltiCardGame/websocket/ulti';
	}
};

function showPage(nextpagename) {
	onpage = $("#" + onpagename);
	nextpage = $("#" + nextpagename);
	onpage.removeClass("pagecomein").addClass("pagegoaway");
	nextpage.removeClass("pagegoaway").addClass("pagecomein");
	setTimeout(function() {
		onpage.css("display", "none")
		onpagename = nextpagename;
	}, 2000);
	nextpage.css("display", "block");
}

function validatePw() {
	var pw = $('#register_password');
	var confirmpw = $('#confirmpw');
	var pattern = pw.val().replace(/\.|\\|\+|\*|\?|\[|\^|\]|\$|\(|\)|\{|\}|\=|\!|\<|\>|\||\:|\-/g, function(x) {return "\\" + x;});
	//alert(pattern);
	confirmpw.attr("pattern", pattern);
}

$(document).ready(function () {
	onpage = $('#' + onpagename);
	onpage.css("display", "block");
});