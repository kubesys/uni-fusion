

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

function doMediatype(mediatype) {
    // console.log("doAlarmType",alarmtypes);
    if (mediatype == "iso") {
        $view.find('[name="source"]').parents('.layui-block').css("display","none");
        $view.find('[name="source"]').parents('.layui-form-item').css("display","none");
        $view.find('[name="source"]').removeAttr('lay-verify');
        $view.find('[name="source"]').parents('.layui-block').find('.layui-form-label').removeClass('web-form-item-require');
        $view.find('[name="imageserver.id"]').parents('.layui-block').css("display","");
        $view.find('[name="imageserver.id"]').parents('.layui-form-item').css("display","");
        $view.find('[lay-filter="imageserver"]').attr('lay-verify','required');
        $view.find('[name="imageserver.id"]').attr('lay-verify','required');
        $view.find('[name="imageserver.id"]').parents('.layui-block').find('.layui-form-label').addClass('web-form-item-require');
        $view.find('[name="imagefile"]').parents('.layui-block').css("display","");
        $view.find('[name="imagefile"]').parents('.layui-form-item').css("display","");
        $view.find('[name="imagefile"]').attr('lay-verify','required');
        $view.find('[name="imagefile"]').parents('.layui-block').find('.layui-form-label').addClass('web-form-item-require');
    } else {
        $view.find('[name="source"]').parents('.layui-block').css("display","");
        $view.find('[name="source"]').parents('.layui-form-item').css("display","");
        $view.find('[name="source"]').attr('lay-verify','required');
        $view.find('[name="source"]').parents('.layui-block').find('.layui-form-label').addClass('web-form-item-require');
        $view.find('[name="imageserver.id"]').parents('.layui-block').css("display","none");
        $view.find('[name="imageserver.id"]').parents('.layui-form-item').css("display","none");
        $view.find('[name="imageserver.id"]').removeAttr('lay-verify');
        $view.find('[lay-filter="imageserver"]').removeAttr('lay-verify');
        $view.find('[name="imageserver.id"]').parents('.layui-block').find('.layui-form-label').removeClass('web-form-item-require');
        $view.find('[name="imagefile"]').parents('.layui-block').css("display","none");
        $view.find('[name="imagefile"]').parents('.layui-form-item').css("display","none");
        $view.find('[name="imagefile"]').removeAttr('lay-verify');
        $view.find('[name="imagefile"]').parents('.layui-block').find('.layui-form-label').removeClass('web-form-item-require');
    }
}
$(function(){
    var mediatype = "";
    $view.find('[name="mediatype"]:checked').each(function(index, obj){
        mediatype = $(this).val();
    });
    doMediatype(mediatype);
});
//此处即为 radio 的监听事件
form.on('radio(mediatype)', function(data){
    var mediatype = "";
    $view.find('[name="mediatype"]:checked').each(function(index, obj){
        mediatype = $(this).val();
    });
    doMediatype(mediatype);
});


// $(function() {
//     //输入框的值改变时触发
//     const productid = $view.find('input[name="productid.id"]').val();
//     if (productid != "") {
//         $view.find('.chanpinbaojia_edit_factorid_name').attr('lay-linkage',productid);
//     }
// });


layui.onevent('input','imageserver',function(data){
    console.log(data);
    $.ajax({
        url: '/VmImages/getIsoFileLists?record='+data.id,
        contentType: "application/json; charset=UTF-8",
        dataType:'json',
        type: "get",
        async: false,
        success: function(result) {
            if (result.code == 200) {
                $view.find('[name="imagefile"]').html("");
                $view.find('[name="imagefile"]').append("<option value=''>请选择</option>");
                $.each(result.data, function (index, item) {
                    $view.find('[name="imagefile"]').append("<option value="+item.file+">"+item.file+"</option>");
                });
                layui.form.render('select');
            }
        },
    });
});
