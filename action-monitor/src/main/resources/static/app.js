var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (msg) {
            showMessage(JSON.parse(msg.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'senderUsername': $("#name").val(), 'message': $("#message").val(), 'messageTimestamp': Date.now()}));
}

function editMessage(buttonId) {
    let msgId = buttonId.replaceAll('edit-', '')
    stompClient.send("/app/chat", {}, JSON.stringify({'messageId': msgId,'senderUsername': $("#name").val(), 'message': $("#sentmsg-" + msgId).val(), 'messageTimestamp': Date.now()}));
}

function showMessage(message) {
    let editButton = $("#name").val() === message.senderUsername ? "<button id=\"edit-" + message.messageId + "\"" + " class=\"btn btn-default\" style=\"float: right\" onClick=\"editMessage(this.id)\">Edit</button>" : ""
    let editableMessage = $("#name").val() === message.senderUsername ? ": <input type=\"text\" id=\"sentmsg-" + message.messageId + "\" " + "value=\"" + message.message + "\">" : ": <span id=\"sentmsg-" + message.messageId + "\">" + message.message + "</span>"
    // Find existing message if it was edited
    if ($("#sentmsg-" + message.messageId).val() !== undefined) {
        $("#sentmsg-" + message.messageId).val(message.message)
        $("#sentmsg-" + message.messageId).text(message.message)
    } else {
        $("#messages").append("<tr><td>" + "<span id=\"msgId\">" + message.messageId + " </span><span style=\"font-weight: bold\">" + message.senderUsername + "</span>" + editableMessage + editButton + "</td></tr>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#edit").click(() => { editMessage(); })
});

