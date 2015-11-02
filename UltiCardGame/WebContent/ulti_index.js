var socket = new WebSocket(getWsUrl());
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

function send() {
	socket.send("dafaq");
}

function getWsUrl() {
	if (window.location.protocol == 'http:') {
		return 'ws://' + window.location.host + '/UltiCardGame/websocket/try';
	} else {
		return 'wss://' + window.location.host + '/UltiCardGame/websocket/try';
	}
};

function showPage(pagename) {
	$(".pagecontent").css("display", "none");
	$("#" + pagename).css("display", "block");
}

$(document).ready(function () {
	
});