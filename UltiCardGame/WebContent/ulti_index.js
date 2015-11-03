var socket = new WebSocket(getWsUrl());
var onpagename = "loginpage"

socket.onopen = onOpen;
socket.onclose = onClose;
socket.onmessage = onMessage;
socket.onerror = onError;


function onOpen() {
	console.log("Socket Opened.")

}
function onClose() {
	console.log("Socket Closed.")

}
function onMessage(msg) {
	console.log("Message: " + msg.data	)
}

function onError (error) {
	console.log("ERROR: " + error);
}

function send(data) {
	var msg = JSON.stringify(data);
	alert(msg);
	//socket.send(msg);
}

function login() {
	var nametb = $('#login_name');
	var passwordtb = $('#login_password');
	var name = nametb.val();
	var password = passwordtb.val();
	nametb.val('');
	passwordtb.val('');
	login(name, password);
	var loginmsg = {};
	loginmsg.type = "login";
	loginmsg.name = name;
	loginmsg.password = pw;
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

function getWsUrl() {
	if (window.location.protocol == 'http:') {
		return 'ws://' + window.location.host + '/UltiCardGame/websocket/try';
	} else {
		return 'wss://' + window.location.host + '/UltiCardGame/websocket/try';
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
	}, 1700);
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
	
});