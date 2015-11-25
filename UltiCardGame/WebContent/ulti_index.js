var socket = new WebSocket(getWsUrl());
var onpagename = "loginpage"
//var onpagename = "mainpage"

var dorefresh = false;
var inultiroom = null;

var isadmin = false;
var mycards = [];

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
	showError("Megszakadt a kapcsolat a szerverrel. Próbáld újratölteni az oldalt.");
	dorefresh = false;
}

function onMessage(msg) {
	log("Got: " + msg.data);
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
			doLogout("Kijelentkeztél");
			break;
		case "register":
			if(msg.success) {
				showPage("loginpage");
				showMessage("Sikeres regisztráció, most már beléphetsz!");
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
		case "activeplayerlist":
			admin_refreshPlayerNames(msg.nameList);
			break;
		case "kick":
			admin_showKickResult(msg.success);
			break;
		case "toplist":
			showToplist(msg.topList);
			break;
		case "kickplayer":
			doLogout("Egy admin kirúgott!");
			break;
		case "startulti":
			startUlti();
			break;
		case "deal":
			mycards = msg.cards;
			showCards();
			break;
		case "error":
			showError(msg.message);
			log("Error: " + msg.message);
			break;
		default:
			log("Unkonwn message!");
			break;
	}
}

function showError (errormsg) {
	alert(errormsg);
}

function showMessage (msg) {
	alert(msg);
}

function send(data) {
	var msg = JSON.stringify(data);
	log("Send: "+ msg);
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

function doLogout (logoutmessage) {
	showPage("loginpage");
	chat.flush();
	inultiroom = null;
	dorefresh = false;
	isadmin = false;
	showMessage(logoutmessage);
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
			isadmin = true;
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

	var leaveultimessage = {};
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
	var roomname = $('<h4>');
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

function startUlti () {
	var ultiroomspage = $('#ultiroomspage');
	var ultigamepage = $('#ultigamepage');
	var roombuttons = $('.room');
	var gamebuttons = $('.game');

	ultiroomspage.css('display', 'none');
	ultigamepage.css('display', 'block');
	roombuttons.css('display', 'none');
	gamebuttons.css('display', 'block');

	dorefresh = false;

}

function standUp () {
	var ultiroomspage = $('#ultiroomspage');
	var ultigamepage = $('#ultigamepage');
	var roombuttons = $('.room');
	var gamebuttons = $('.game');

	ultigamepage.css('display', 'none');
	ultiroomspage.css('display', 'block');
	gamebuttons.css('display', 'none');
	roombuttons.css('display', 'block');

	dorefresh = true;
}

function showStatistics () {
	var gettoplistmsg = {};
	gettoplistmsg.type = "gettoplist";
	send(gettoplistmsg);
}

function showToplist(toplist) {
	var ultiroomspage = $('#ultiroomspage');
	var statspage = $('#statpage');
	var roombuttons = $('.room');
	var statbuttons = $('.stat');

	ultiroomspage.css('display', 'none');
	statspage.css('display', 'block');
	roombuttons.css('display', 'none');
	statbuttons.css('display', 'block');

	var table = $('#staticticstable');
	table.html('');
	
	var headerrow = $('<tr>');
	var header = $('<th>');
	header.html('Név');
	headerrow.append(header);
	
	header = $('<th>');
	header.html('Pontszám');
	headerrow.append(header);
	
	table.append(headerrow);

	var sortlist = [];

	$.each(toplist, function(key, value) {
		sortlist.push({name:key, point:value});
	});

	sortlist.sort(function(a,b) {return a.point - b.point});

	$.each(sortlist, function(index,value) { 
		var row = $('<tr>');
		var data = $('<td>');
		data.html(value.name);
		row.append(data);

		data = $('<td>');
		data.html(value.point);
		row.append(data);

		table.append(row);
	});
}

function showRooms() {
	var ultiroomspage = $('#ultiroomspage');
	var statspage = $('#statpage');
	var roombuttons = $('.room');
	var statbuttons = $('.stat');

	statspage.css('display', 'none');
	ultiroomspage.css('display', 'block');
	statbuttons.css('display', 'none');
	roombuttons.css('display', 'block');
}

function showCards () {
	var myhand = $('#myhand');

	myhand.html('');

	for (var i = 0; i < mycards.length; i++) {
		var card = createCard(mycards[i], i);
		myhand.append(card);
	};
}

function playCard(index) {
	card = mycards.splice(index, 1)[0];
	showCards();
	
	var playcardmsg = {};
	playcardmsg.type = "playcard";
	playcardmsg.card = card;
	send(playcardmsg);
}

function createCard (cardInfo, num) {
	var card = $('<div>');
	var suit = cardInfo.suit;
	var value = cardInfo.value;

	card.addClass('card');
	card.attr('id', suit + ' ' + value);
	card.click(function() {playCard(num)});
	card.hover(function(){this.style.zIndex=1;}, function(){this.style.zIndex=0;})
	if(num === 0) {
		card.css('margin-left', '0');
	}
	
	var cardtext = $('<p>');
	
	cardtext.html(suit + "<br>" + value);
	cardtext.addClass('font');
	card.append(cardtext);

	return card;
}

function admin_getAllPlayers () {
	if(admin_checkAdmin()) {
		listactiveplayersmsg = {};
		listactiveplayersmsg.type = "listactiveplayers";
		send(listactiveplayersmsg);
	}
}

function admin_kickPlayer (msg) {
	if(admin_checkAdmin()) {
		var playerselector = $('#admin_playerselector');
		var player = playerselector.val();

		if(player != 0) {
			var kickplayermsg = {};
			kickplayermsg.type = "kick";
			kickplayermsg.name = player;
			send(kickplayermsg);
			admin_getAllPlayers();
		} else {
			showError("Nem választottál játékost!");
		}
	}
}

function admin_checkAdmin() {
	if(isadmin) {
		return true;
	}
	window.location.href = "#close";
	showError('Nem vagy admin!');
	return false;
}

function admin_refreshPlayerNames (names) {
	var playerselector = $('#admin_playerselector');
	playerselector.html("");
	var newoption = $('<option>');
	newoption.val(0);
	newoption.html("Összes aktív játékos");
	playerselector.append(newoption);
	for (var i = 0; i < names.length; i++) {
		newoption = $('<option>');
		newoption.val(names[i]);
		newoption.html(names[i]);
		playerselector.append(newoption);
	}
}

function admin_showKickResult (success) {
	var admininfo = $('#admin_info');
	if(success) {
		admin_info = "Kirúgás sikerült!"
	} else {
		admin_info = "Kirúgás nem sikerült!"
	}
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

function __debug_game__ () {
	showPage("mainpage");
	startUlti();
	for (var i = 0; i < 12; i++) {
		mycards.push({"suit":"kaka", "value":i});
	};
	showCards();
}