var dropdown_options = [{
    title: '重连',
    id: 100
},{
    type: '-'
},{
    title: '删除',
    id: 101
}];


var disabled_dropdown_options = [{
    title: '重连',
    disabled: true,
    id: 100
},{
    type: '-'
},{
    title: '删除',
    id: 101
}];


res.data.forEach(function (item, index) {
    var options = dropdown_options;
    if (item.state == "已就绪") {
        options = disabled_dropdown_options;
    }
    layui.dropdown.render({
        elem: '#tool_more_'+item.id,
        data: options,
        click: function(obj){
            console.log("dropdown",obj);
            console.log("selectRowData",selectRowData);
            if (obj.id == 100) {
                $.ajax({
                    url: '/VmPools/startLink?record='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                       // console.log("startLink",result);
                        if (result.code == 200) {
                            layui.webframe.alert.msg('重连成功',{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg('重连失败！',{iconCu: 2,anim: 6});
                        }
                        VmPoolsUpdateListview();
                    },
                });
            } else if (obj.id == 101) {
                $.ajax({
                    url: '/VmPools/deleteAction/ajax?ids='+selectRowData.id,
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
                        VmPoolsUpdateListview();
                    },
                });
            }


            return true;
        }
    });
});

