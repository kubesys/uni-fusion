var dropdown_options = [{
    title: '删除',
    id: 101
}];





res.data.forEach(function (item, index) {
    var options = dropdown_options;
    layui.dropdown.render({
        elem: '#tool_more_'+item.id,
        data: options,
        click: function(obj){
            // console.log("dropdown",obj);
            // console.log("selectRowData",selectRowData);
            if (obj.id == 101) {
                $.ajax({
                    url: '/VmDisks/deleteAction/ajax?ids='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                        if (result.code == 0) {
                            layui.webframe.alert.msg('删除成功',{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg('删除失败！',{iconCu: 2,anim: 6});
                        }
                        VmDisksUpdateListview();
                    },
                });
            }


            return true;
        }
    });
});

