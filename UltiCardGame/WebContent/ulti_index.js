var socket = new WebSocket(getWsUrl());
var onpagename = "loginpage"
//var onpagename = "mainpage"

var dorefresh = false;
var inultiroom = null;

var chat = {};

chat.messages = [];
chat.newLine = (function(msg, sender) {
	this.messages.push(sender + ": " + msg);
	this.refreshHistory();

});
chat.refreshHistory = (function() {
	var chatbox = $('#chat_history');
	var scrollContainer = $('#chat_historycontainer');
	chatbox.html("");
	for (var i = 0; i < this.messages.length; i++) {
		chatbox.html(chatbox.html() + this.messages[i] + "<br>");
	}
	scrollContainer.scrollTop(function () {return this.scrollHeight;});
});
chat.flush = (function () {
	this.messages = [];
});

socket.onopen = onOpen;
socket.onclose = onClose;
socket.onmessage = onMessage;
socket.onerror = onError;

function log(msg) {
	console.log(msg);
}

function onOpen() {
	log("Socket Opened.");
}

function onClose() {
	log("Socket Closed.");
	dorefresh = false;
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
				loginSuccess(msg);
			} else {
				showError("Hibás bejelentkezési adatok!");
			}
			break;
		case "logout":
			showPage("loginpage");
			chat.flush();
			inultiroom = null;
			dorefresh = false;
			alert("Kijelentkeztél.");
			break;
		case "register":
			if(msg.success) {
				showPage("loginpage");
				alert("Sikeres regisztráció, most már beléphetsz!");
			} else {
				showError("Sikertelen regisztráció " + msg.fault);
			}
			break;
		case "chat":
			chat.newLine(msg.message, msg.from);
			break;
		case "allchat":
			refreshChatRoomNames(msg.rooms);
			break;
		case "newchat":
			newChat(msg.success);
			break;
		case "tochat":
			chat.newLine("Mostantól a " + msg.message + " szobában vagy!", "Szerver üzenet");
			break;
		case "allulti":
			refreshUltiRooms(msg.playersInUltiRoom);
			break;
		case "newulti":
			newUlti(msg.success)
			break;
		case "toulti":
			if(msg.success) {
				inultiroom = msg.message;
				getAllUltiRoom();
			}
			break;
		case "error":
			showError(msg.message);
			log("Error: " + msg.message);
			break;
		default:
			log("Unkonwn message!");
	}
}

function showError (errormsg) {
	alert(errormsg);
}

function send(data) {
	var msg = JSON.stringify(data);
	log(msg);
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
	changeNonGuestFeatures(false);

	var guestmsg = {};
	guestmsg.type = "guestlogin";
	send(guestmsg);
}

function logout () {
	var logoutmessage = {};
	logoutmessage.type = "logout";
	send(logoutmessage);
}

function loginSuccess (loginData) {
	
	var playertype = loginData.playerType;
	var namespan = $('#name');
	
	namespan.html(loginData.name);
	changeNonGuestFeatures(true);
	changeAdminFeatures(false);
	
	if(playertype === "guest") {
		changeNonGuestFeatures(false);
	} else {
		if(playertype === "admin") {
			changeAdminFeatures(true);
		}
	}

	showPage("mainpage");
	getAllUltiRoom();
	dorefresh = true;
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
	if(msg != "") {
		var chatmsg = {};
		chatmsg.type = "chat";
		chatmsg.message = msg;
		send(chatmsg);
	}
}

function changeNonGuestFeatures(toEnable) {
	var nonguestelements = $(".nonguest");
	if(toEnable) {
		nonguestelements.prop('disabled', false);
		nonguestelements.removeClass('disabled');
	} else {
		nonguestelements.prop('disabled', true);
		nonguestelements.addClass('disabled');
	}
}

function changeAdminFeatures (toEnable) {
	var adminelements = $(".admin");
	if(toEnable) {
		adminelements.css('display', 'block');
	} else {
		adminelements.css('display', 'none');
	}
}

function newChatRoom () {	
	var roomnametb = $('#chat_roomname');
	var playernumbertb = $('#chat_playernumber');
	var infiniteplayercb = $('#chat_infiniteplayer');
	
	var roomname = roomnametb.val();
	var playernumber = playernumbertb.val();

	
	if(infiniteplayercb.prop('checked')) {
		playernumber = -1;
	}

	var newchatroommsg = {};
	newchatroommsg.type = "newchat";
	newchatroommsg.name = roomname;
	newchatroommsg.maxmembers = playernumber;
	send(newchatroommsg);
}

function newChat(isDone) {
	var roomnametb = $('#chat_roomname');
	var playernumbertb = $('#chat_playernumber');
	var infiniteplayercb = $('#chat_infiniteplayer');
	var newroomerror = $('#chat_newroomerror');
	var playernumbertb = $('#chat_playernumber');
	
	if(isDone) {
		window.location.href = '#close'
		newroomerror.html('');
		roomnametb.val('');
		playernumbertb.val('');
		infiniteplayercb.prop('checked', false);
		playernumbertb.prop('disabled', false);
	} else {
		newroomerror.html('Ez a szobanév már foglalt!');
	}
}

function getAllChatRoom () {
	var getallchatmsg = {};
	getallchatmsg.type = "getallchat";
	send(getallchatmsg);
}

function refreshChatRoomNames (names) {

	var roomselector = $('#chat_roomselector');
	roomselector.html("");
	var newoption = $('<option>');
	newoption.val(0);
	newoption.html("Válassz szobát");
	roomselector.append(newoption);
	for (var i = 0; i < names.length; i++) {
		newoption = $('<option>');
		newoption.val(names[i]);
		newoption.html(names[i]);
		roomselector.append(newoption);
	}
}

function joinChatRoom () {

	var roomselector = $('#chat_roomselector');
	var selectedRoom = roomselector.val();

	if(selectedRoom != 0) {
		window.location.href = '#close';
		var joinchatroommsg = {};
		joinchatroommsg.type = "tochat";
		joinchatroommsg.name = selectedRoom;
		send(joinchatroommsg);
	} else {
		showError("Nem választottál szobát!");
	}
}

function leaveChatRoom () {
	var leavechatroommsg = {};
	leavechatroommsg.type = "leavechat";
	send(leavechatroommsg);
	getAllUltiRoom();
}

function newUltiRoom () {
	var roomnametb = $('#ulti_roomname');

	var roomname = roomnametb.val();

	var newultiroommsg = {};
	newultiroommsg.type = "newulti";
	newultiroommsg.name = roomname;
	newultiroommsg.maxmembers = 3;
	send(newultiroommsg);
}

function joinUltiRoom (roomname) {
	var joinultimessage = {};
	joinultimessage.type = "toulti";
	joinultimessage.name = roomname;
	send(joinultimessage);
}

function leaveUltiRoom () {
	inultiroom = null;

	var leaveultimessage = {}
	leaveultimessage.type = "leaveulti";
	send(leaveultimessage);
}

function newUlti (isDone) {
	var roomnametb = $('#ulti_roomname');
	var newroomerror = $('#ulti_newroomerror');
	if(isDone) {
		window.location.href = '#close';
		roomnametb.val('');
	} else {
		newroomerror.html('Ez a szobanév már foglalt!');
	}
}

function getAllUltiRoom () {
	var getallultimsg = {};
	getallultimsg.type = "getallulti";
	send(getallultimsg);
}

function refreshUltiRooms(rooms) {
	var rowcontainer = $('#ultiroomrows');
	rowcontainer.html('');
	var roominrow = 0;
	var maxroominrow = 5;
	var row;
	for(var i = 0; i < rooms.length; i++) {
		if(roominrow === 0) {
			row = $('<div>');
			row.addClass('ultiroomrow');
			rowcontainer.append(row);
		}
		var room = createUltiRoom(rooms[i]);
		row.append(room);
		roominrow++;
		if(roominrow == maxroominrow) {
			roominrow = 0;
		}
	}
}

function createUltiRoom (roomData) {
	var name = roomData.roomName;
	var room = $('<div>');
	room.addClass('ultiroom');
	var roomname = $('<p>');
	roomname.html(name);
	room.append(roomname);
	var playernames = $('<div>');
	playernames.addClass('ulti_playernames');
	for(var i = 0; i < 3; i++) {
		var player = $('<div>');
		player.addClass('ulti_playername');
		player.html(roomData.names[i]);
		playernames.append(player);
	}
	room.append(playernames);
	var buttoncontainer = $('<div>');
	buttoncontainer.addClass('ulti_joinbuttoncontainer');
	buttoncontainer.attr("id", "ultiroom_" + name);
	var button = $('<button>');
	button.addClass('btn');
	if(inultiroom && inultiroom === name) {
		button.addClass('btn-danger');
		button.click(function() { leaveUltiRoom(); });
		button.html('Elhagyás');
	} else {
		button.addClass('btn-primary');
		button.click(function() { joinUltiRoom(name); });
		button.html('Csatlakozás');
	}
	buttoncontainer.append(button);
	room.append(buttoncontainer);

	return room;
}

function autoGetUltiRooms () {
	if(dorefresh) {
		getAllUltiRoom();
	}
}

function togglechatplayernumber(to) {
	var playernumbertb = $('#chat_playernumber');
	playernumbertb.prop('disabled', to);
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
	confirmpw.attr("pattern", pattern);
}

$(document).ready(function () {
	onpage = $('#' + onpagename);
	onpage.css("display", "block");
	var ultiroomrefreshinterval = setInterval(autoGetUltiRooms, 5000);
});