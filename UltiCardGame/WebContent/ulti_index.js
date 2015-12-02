var socket = new WebSocket(getWsUrl());
var onpagename = "loginpage"
//var onpagename = "mainpage"

var dorefresh = false;
var inultiroom = null;

var isadmin = false;
var myname = "";

var mycards = [];
var playedcard = null;
var myturn = false;
var issayingphase = true;
var isayrule = false;
var markedcards = [];
var hastoconfirm = false;
var cardsontable = [];
var redSuit = false;
var left = "";
var right = "";
var partyresult = "";

var notrumpchooserules = [2,6,8,11,14,15,16];

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
				showMessage("Hibás bejelentkezési adatok!");
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
				showMessage("Sikertelen regisztráció " + msg.fault);
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
			handleStartUlti(msg.names);
			break;
		case "someoneleftgame":
			//`{"type":"someoneleftgame", "name":<string>}`
			showMessage("Játék véget ért, " + msg.name + " elhagyta az asztalt");
			standUp(false);
			break;
		case "deal":
			handleDeal(msg);
			break;
		case "playedcard":
			handlePlayedCard(msg);
			break;
		case "playeronturn":
			startTurn(msg.isItMe, msg.name);
			break;
		case "gameselected":
			handleGameSelected(msg.name, msg.isItMe, msg.gameType);
			break;
		case "pickedupcards":
			handleCardPickup(msg.card1, msg.card2);
			break;
		case "hastoconfirm":
			handleHasToConfirm();
			break;
		case "startgame":
			show420andTrump(msg.points, msg.trump);
			sayPhaseOver()
			break;
		case "takecards":
			handleTakeCards(msg.name, msg.isItMe);
			break;
		case "showpartyresult":
		//`{"type":"showpartyresult", "points":[{<string>: <int>}]}`
			partyresult = handlePartyResult(msg.points);
			break;
		case "showresult":
			showResult(msg.points)
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
	var errorp = $('#errormessage');
	errorp.html(errormsg);
	window.location.href = '#openerrormodal';

}

function showMessage (msg) {
	var errorp = $('#showmessage');
	errorp.html(msg);
	window.location.href = '#openmessagemodal';
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
	
	myname = loginData.name;

	namespan.html(myname);
	changeNonGuestFeatures(true);
	changeAdminFeatures(false);
	showRooms();
	
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
		window.location.href = '#close';
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
		showMessage("Nem választottál szobát!");
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

function handleStartUlti (names) {
	var ultiroomspage = $('#ultiroomspage');
	var ultigamepage = $('#ultigamepage');
	var roombuttons = $('.room');
	var gamebuttons = $('.game');

	ultiroomspage.css('display', 'none');
	ultigamepage.css('display', 'block');
	roombuttons.css('display', 'none');
	gamebuttons.css('display', 'block');

	dorefresh = false;

	var myindex = names.indexOf(myname);
	right = names[(myindex + 1) % 3];
	left = names[(myindex + 2) % 3];

	var leftnameplace = $('#leftnameplace');
	var rightnameplace = $('#rightnameplace');

	leftnameplace.html(left);
	rightnameplace.html(right);

	cardsontable = [];
	showCardsOnTable();

}

function standUp (doleaveroom) {
	var ultiroomspage = $('#ultiroomspage');
	var ultigamepage = $('#ultigamepage');
	var roombuttons = $('.room');
	var gamebuttons = $('.game');

	ultigamepage.css('display', 'none');
	ultiroomspage.css('display', 'block');
	gamebuttons.css('display', 'none');
	roombuttons.css('display', 'block');

	if(doleaveroom) {
		leaveUltiRoom();
	}

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

	sortlist.sort(function(a,b) {return b.point - a.point});

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

function sortMyHand () {
	var sortValue = {SEVEN:1, EIGHT:2, NINE:3, TEN:7, UNDER_KNAVE:4, OVER_KNAVE:5, KING:6, ACE:8};
	var sortSuit = {BELL:2, ACORN:3, HEART:4, LEAF:1};

	mycards.sort(function(a,b) {return (sortSuit[a.suit] * 10 + sortValue[a.value]) - (sortSuit[b.suit] * 10 + sortValue[b.value])});
}

function showMyHand () {
	sortMyHand();

	var myhand = $('#myhand');

	var cardnuminhand = mycards.length;

	myhand.html('');
	myhand.css('left', (20 + (3 * (12 - cardnuminhand))) + '%' );

	for (var i = 0; i < cardnuminhand; i++) {
		var card = createCardInHand(mycards[i], i);
		myhand.append(card);
	};
}

function handleDeal (mydeal) {
	var saybar = $('#saying');
	var sayother = $('#saying_other');
	var sayme = $('#saying_me');
	var saytrump = $('#saying_trump');

	mycards = mydeal.cards;
	myturn = mydeal.isStarter;
	isayrule = mydeal.isStarter;
	var startername = mydeal.starterName;
		
	showWhoOnTurn(startername, myturn);

	saybar.css('display', 'block');
	saytrump.css('display', 'none');
	sayother.css('display', 'none');

	if(isayrule) {
		sayme.css('display', 'block');
	} else {
		sayme.css('display', 'none');
	}

	setValidRules(0);
	
	issayingphase = true;
	hastoconfirm = false;
	showMyHand();
}

function handleGameSelected (name, isitme, gametype) {
	if (isitme) {
		isayrule = false;

		if(notrumpchooserules.indexOf(gametype) !== -1) {
			redSuit = true;
		}

		removeMarkedCardsFromHand()
		markedcards = [];
	} else {
		var rule = $('#rule_selector option[value=' + gametype + ']').html();
		showMessage(name + ": " + rule);
	}
	setValidRules(gametype);
}

function showWhoOnTurn(onturnname, isme) {
	var leftnameplace = $('#leftnameplace');
	var rightnameplace = $('#rightnameplace');
	leftnameplace.css('color', 'black');
	rightnameplace.css('color', 'black');
	if(!isme) {
		if(onturnname === left) {
			leftnameplace.css('color', 'blue');
		} else if (onturnname === right) {
			rightnameplace.css('color', 'blue');
		} else {
			alert("öreganyád térgye kalácsa!");
		}
	}
}

function startTurn (ismyturn, onturnname) {
	myturn = ismyturn;

	showWhoOnTurn(onturnname, myturn);

	if(issayingphase) {
		if(myturn) {
			var sayother = $('#saying_other');

			sayother.css('display', 'block');
		} else {
			var sayother = $('#saying_other');
			var sayme = $('#saying_me');

			sayother.css('display', 'none');
			sayme.css('display', 'none');
		}
	} else {
		var cards = $('.card');
		if(myturn) {
			//cards.css('background', 'lightblue');
		} else {
			//cards.css('background', 'red');
		}
	}
}

function handleHasToConfirm () {
	hastoconfirm = true;
	if(!redSuit) {
		var saytrump = $('#saying_trump');
		saytrump.css('display', 'block');
	}
}

function sayPass () {
	if(myturn) {
		if (hastoconfirm) {
			confirmgamemsg = {};
			confirmgamemsg.type = "confirmgame";
			
			var trump;
			
			if(redSuit) {
				trump = "HEART";
			} else {
				trumpselect = $('#trump_selector');
				trump = trumpselect.val();
			}

			if(trump !== "0") {
				confirmgamemsg.suit = trump;
				send(confirmgamemsg);
			} else {
				showMessage("Nem választottál adu színt!");
			}
		} else {
			passmsg = {};
			passmsg.type = "pass";
			send(passmsg);
		}
	}
}

function setValidRules(from) {
	var ruleselect = $('#rule_selector');
	var options = ruleselect.children();
	$.each(options, function (num, option) {
		if((parseInt(option.value) !== 0) && (parseInt(option.value) <= from)) {
			option.disabled = true;
		} else {
			option.disabled = false;
		}
	});
}

function sayRule() {
	if(myturn) {
		if(isayrule) {
			if(markedcards.length < 2) {
				showMessage("Meg kell jelölnöd két kártyát amit a talonba teszel!");
			} else {
				var ruleselect = $('#rule_selector');
				var rule = ruleselect.val();
				ruleselect.val('0');
				if(rule !== "0") {
					gameselectionmsg = {};
					gameselectionmsg.type = "gameselection";
					gameselectionmsg.gameType = rule;
					gameselectionmsg.card1 = markedcards[0];
					gameselectionmsg.card2 = markedcards[1];
					send(gameselectionmsg);
				} else {
					showMessage("Nem választottál szabályt!");
				}
			}
		}
	}
}

function pickUpCards() {
	pickupcardsmsg = {};
	pickupcardsmsg.type = "pickupcards";
	send(pickupcardsmsg);
}

function handleCardPickup (card1, card2) {
	mycards.push(card1, card2);
	isayrule = true;
	redSuit = false;
	
	var sayme = $('#saying_me');
	var sayother = $('#saying_other');
	var saytrump = $('#saying_trump');

	sayme.css('display', 'block');
	sayother.css('display', 'none');
	saytrump.css('display', 'none');

	showMyHand();
}

function show420andTrump (points, trumpsuit) {
	var message = "";
	if(trumpsuit) {
		var hunTrump = toHunSuit(trumpsuit);
		message += "Az adu színe: " + hunTrump + "<br>";
	}
	$.each(points, function(name, point) {
		if(point > 0) {
			message += name + ": " + point + " van a kezemben<br>";
		}
	});
	if(message !== "") {
		showMessage(message);
	}
}

function sayPhaseOver () {
	var sayBar = $('#saying');
	sayBar.css('display', 'none');

	issayingphase = false;
	hastoconfirm = false;
}

function removeMarkedCardsFromHand () {
	for (var i = 0; (i < 2) && (i < markedcards.length); i++) {
		var card = markedcards[i];
		var index = mycards.indexOf(card);

		if(index !== -1) {
			mycards.splice(index, 1);
		}
	}
	showMyHand();
}

function playCard(index) {
	if(myturn) {
		var card = mycards[index];
		if(issayingphase) {
			if(isayrule) {
				var mi = markedcards.indexOf(card)
				var cardView = $('#' + card.suit + '_' + card.value);
				if(mi !== -1) {
					markedcards.splice(mi, 1);

					cardView.css('margin-top', '0');
				} else {
					if(markedcards.length < 2) {
						cardView.css('margin-top', '-1.5%');

						markedcards.push(card);
					}
				}
			}
		} else {
			playedcard = mycards[index];

			var playcardmsg = {};
			playcardmsg.type = "playcard";
			playcardmsg.card = playedcard;
			send(playcardmsg);
		}
	}
}

function showCardsOnTable() {
	var playedcards = $('#playedcards');

	playedcards.html('');

	for (var i = 0; i < cardsontable.length; i++) {
		var card = $('<div>');
		var suit = cardsontable[i].suit;
		var value = cardsontable[i].value;
		
		setCardImg(card, suit, value);
		card.hover(function(){this.style.zIndex=1;}, function(){this.style.zIndex=0;})
		card.addClass('cardontable');
		card.css('top', ((i * 2 % 3)*20) + '%'); 
		card.css('left', (i*20) + '%');

		playedcards.append(card);
	};
}

function handlePlayedCard (data) {
	if(playedcard && data.isItMe) {
		var index = mycards.indexOf(playedcard);
		if(index !== -1) {
			mycards.splice(index, 1);
		}
		showMyHand();
		playedcard = null;
	}
	cardsontable.push(data.card);
	showCardsOnTable();
}

function setCardImg (card, suit, value) {
	card.css('background-image', "url('card_imgs/"+ suit + "_" + value + ".gif')");
}

function toHunSuit (suit) {
	var hunSuit = {BELL:"tök", ACORN:"makk", HEART:"piros", LEAF:"zöld"};
	return hunSuit[suit];
}

function toHunValue (value) {
	var hunValue = {SEVEN:"VII", EIGHT:"VIII", NINE:"IX", TEN:"X", UNDER_KNAVE:"alsó", OVER_KNAVE:"felső", KING:"király", ACE:"ász"};
	return hunValue[value];
}

function toHunCard (suit, value) {
	return {suit:toHunSuit(suit), value:toHunValue(value)};
}

function createCardInHand (cardInfo, num) {
	var card = $('<div>');
	var suit = cardInfo.suit;
	var value = cardInfo.value;

	card.addClass('card');
	setCardImg(card, suit, value);
	card.attr('id', suit + '_' + value);
	card.click(function() {playCard(num)});
	card.hover(function(){this.style.zIndex=1;}, function(){this.style.zIndex=0;})
	if(num === 0) {
		card.css('margin-left', '0');
	}

	return card;
}

function handleTakeCards (name, isitme) {
	var playedcards = $('#playedcards');
	cardsontable = [];
	if(isitme) {
		playedcards.addClass('itakecards');
	} else {
		if(name === left) {
			playedcards.addClass('lefttakecards');
		} else if(name === right) {
			playedcards.addClass('righttakecards');
		} else {
			alert('anyád csipája!!!4');
		}
	}
}

function handlePartyResult (points) {
	var result = "";
	$.each(points, function(name, point) {
		result += name + ": " + point + "pont<br>";
	});

	return result;
}

function showResult (points) {
	var message = "<h3>A kör eredménye</h3><br>"
	if(partyresult !== "") {
		message += "A parti eredménye:<br>" + partyresult + "<br>";
		partyresult = "";
	}
	$.each(points, function(name, point) {
		message += name + ": " + point + "pont<br>";
	});
	showMessage(message);
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
			var info = $('#admin_info');
			info.html("Nem választottál játékost!");
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
	window.location.href = '#close';
	onpage = $('#' + onpagename);
	onpage.css("display", "block");
	var ultiroomrefreshinterval = setInterval(autoGetUltiRooms, 5000);
	var playedcards = $('#playedcards');
	playedcards.bind('animationend', function() {
		$(this).removeClass('itakecards');
		$(this).removeClass('lefttakecards');
		$(this).removeClass('righttakecards');
		showCardsOnTable();
	});
});

function __debug_game__ () {
	showPage("mainpage");
	handleStartUlti();
	for (var i = 0; i < 12; i++) {
		var suit;
		var value;
		switch (i % 4) {
			case 0:
				suit = "LEAF";
				break;
			case 1:
				suit = "ACORN";
				break;
			case 2:
				suit = "BELL";
				break;
			case 3:
				suit = "HEART";
				break;
		}
		if(i < 4) {
			value = "KING";
		} else if (i < 8) {
			value = "TEN";
		} else {
			value = "ACE";
		}
			mycards.push({"suit":suit, "value":value});
	};
	showMyHand();
	myturn = true;
}

function __debug_card__ () {
	return {"suit":"HEART", "value":"ACE"};
}