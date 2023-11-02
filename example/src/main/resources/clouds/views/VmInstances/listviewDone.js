
const options = new Map();
options.set(100, {title: '启动',id: 100,disabled: true});
options.set(101, {title: '重启',id: 101,disabled: true});
options.set(102, {title: '停止',id: 102,disabled: true});
options.set(103, {title: '关闭电源',id: 103,disabled: true});
options.set(104, {type: '-'});
options.set(105, {title: '打开控制台',id: 105,disabled: true});
options.set(106, {title: '创建快照',id: 106,disabled: true});
options.set(107, {type: '-'});
options.set(108, {
    title: '云盘',
    type: 'parent',
    child: [{title: '加载云盘', id: 200},{title: '卸载云盘',id: 201}]
});
options.set(109, {
    title: '镜像与ISO',
    child: [{title: '创建镜像',id: 202},{title: '加载ISO',id: 203},{title: '卸载ISO',id: 204}]
});
options.set(110, {type: '-'});
options.set(111, {title: '删除',id: 111,disabled: true});


res.data.forEach(function (item, index) {
    const maps = new Map();
    for (const [key, value] of options) {
        maps.set(key, JSON.parse(JSON.stringify(value)));
    }
    if (item.state_value == "Running") {
        maps.get(101)['disabled'] = false;
        maps.get(102)['disabled'] = false;
        maps.get(103)['disabled'] = false;
        maps.get(105)['disabled'] = false;
    } else if (item.state_value == "Shutdown") {
        maps.get(100)['disabled'] = false;
        maps.get(111)['disabled'] = false;
    } else {
        maps.get(111)['disabled'] = false;
    }
    var dropdown_options = [...maps.values()];
    // console.log("options",dropdown_options);
    layui.dropdown.render({
        elem: '#tool_more_'+item.id,
        data: dropdown_options,
        click: function(obj){
            // console.log("dropdown",obj);
            // console.log("selectRowData",selectRowData);
            if (obj.id == 100) {
                $.ajax({
                    url: '/VmInstances/startVm/ajax?record='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                        if (result.code == 200) {
                            layui.webframe.alert.msg(result.message,{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg(result.message,{iconCu: 2,anim: 6});
                        }
                        setTimeout(function (){ VmInstancesUpdateListview(); },4000);
                    },
                });
            } else if (obj.id == 101) {
                $.ajax({
                    url: '/VmInstances/restartVm/ajax?record='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                        if (result.code == 200) {
                            layui.webframe.alert.msg(result.message,{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg(result.message,{iconCu: 2,anim: 6});
                        }
                        setTimeout(function (){ VmInstancesUpdateListview(); },4000);
                    },
                });
            } else if (obj.id == 102) {
                $.ajax({
                    url: '/VmInstances/stopVm/ajax?record='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                        if (result.code == 200) {
                            layui.webframe.alert.msg(result.message,{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg(result.message,{iconCu: 2,anim: 6});
                        }
                        setTimeout(function (){ VmInstancesUpdateListview(); },4000);
                    },
                });
            } else if (obj.id == 103) {
                $.ajax({
                    url: '/VmInstances/forceStopVm/ajax?record='+selectRowData.id,
                    contentType: "application/json; charset=UTF-8",
                    dataType:'json',
                    type: "post",
                    async: false,
                    success: function(result) {
                        if (result.code == 200) {
                            layui.webframe.alert.msg(result.message,{iconCu: 1});
                        } else {
                            layui.webframe.alert.msg(result.message,{iconCu: 2,anim: 6});
                        }
                        setTimeout(function (){ VmInstancesUpdateListview(); },4000);
                    },
                });
            } else if (obj.id == 105) {
                var record = selectRowData.id;
                webframe.loadHtml(null,"GET","/VmInstances/viewNoVnc",{record},function (res){
                    layui.layer.open({
                        type: 1,
                        title: "控制台"+"(" + selectRowData.name + ")",
                        resize: false,
                        skin:'layui-layer-fix-select',
                        area: ["98%", "98%"],
                        content: res.html,
                        success: function (layero, index) {
                            form.PopupDialogCallback[index] = function (object) {
                                layui.layer.close(index);
                            }
                        },
                    });
                })
            } else if (obj.id == 111) {
                $.ajax({
                    url: '/VmInstances/deleteAction/ajax?ids='+selectRowData.id,
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
                        setTimeout(function (){ VmInstancesUpdateListview(); },4000);
                    },
                });
            } else if (obj.id == 202) {
                webframe.loadHtml(null,"GET","/VmInstances/viewCreateDiskImage",{record:selectRowData.id},function (res){
                    layui.layer.open({
                        type: 1,
                        title: "创建镜像",
                        resize: false,
                        skin:'layui-layer-fix-select',
                        area: '500px',
                        content: res.html,
                        success: function (layero, index) {
                            form.PopupDialogCallback[index] = function (object) {
                                window.setTimeout(function () {
                                    VmInstancesUpdateListview(table);
                                }, 500);
                            }
                        }
                    });
                })
            }
            return true;
        }
    });
});
