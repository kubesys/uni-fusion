

function uuid() {
    var s = [];
    var uuidData = "0123456789abcdefghijklmnopqrstuvwxyz";
    var uuidDataLength = uuidData.length;
    for (var i = 0; i < 32; i++) {
        s[i] = uuidData.substr(Math.floor(Math.random() * uuidDataLength), 1);
    }
    var uuid = s.join("");
    return uuid;
}

layui.util.event('lay-active', {
    make_event_id: function(){
        $view.find('[name="eventid"]').val( uuid());
    }
});
