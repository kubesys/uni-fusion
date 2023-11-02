/*!
 * Cropper v3.0.0
 */

layui.define(['jquery','layer','cropper','webframe'],function (exports) {
    var $ = layui.jquery,
        layer = layui.layer;
    var obj = {
        render: function(e){
            var self = this,
                elem = e.elem,
                saveW = e.saveW,
                saveH = e.saveH,
                mark = e.mark,
                area = e.area,
                url = e.url,
                error = e.error,
                done = e.done;

            // 根据传入的ID 每个裁剪组件div都有自己的一套ID 这样能实现一个界面多个裁剪组件
            var elemName = elem;
            if(elem.indexOf("#") >= 0){
                elemName = elem.replace("#","");
            }else if(elem.indexOf(".") >= 0){
                elemName = elem.replace(".","");
            }
            var cropperDivId = elemName + 'Div';
            var uploadId = elemName + 'Upload';
            var imageId = elemName + 'Image';
            var previewId = elemName + 'Preview';
            // 注意更改这里css的路径
            var html = "<div id='cropperimagepanel' class='upload-image-qn'>" +
                "<link rel=\"stylesheet\" href=\"/layui/extends/cropper/cropper.css\">\n" +
                "<div class=\"layui-fluid\" style=\"display: none;padding: 10px 10px;\" id=\""+cropperDivId+"\">\n" +
                "    <div class=\"layui-form-item\">\n" +
                "        <div class=\"layui-input-inline layui-btn-container\" style=\"width: auto;\">\n" +
                "            <label for=\""+uploadId+"\" class=\"layui-btn select-image-qn\">\n" +
                "                <i class=\"layui-icon\">&#xe67c;</i>选择图片\n" +
                "            </label>\n" +
                "            <input class=\"layui-upload-file\" id=\""+uploadId+"\" type=\"file\" value=\"选择图片\" name=\"file\">\n" +
                "        </div>\n" +
                "        <div class=\"layui-form-mid layui-word-aux\" style='color:red !important;'>图片的尺寸限定"+saveW+"px * "+saveH+"px以内</div>\n" +
                "    </div>\n" +
                "    <div class=\"layui-row layui-col-space15\">\n" +
                "        <div class=\"layui-col-xs9\">\n" +
                "            <div class=\"readyimg readyimg-qn\" style=\"height:450px;\">\n" +
                "                <img src=\"\" id='"+imageId+"'>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-xs3\">\n" +
                "            <div class=\"img-preview\" id='"+previewId+"' style=\"width:200px;height:200px;overflow:hidden\">\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"layui-row layui-col-space15\">\n" +
                "        <div class=\"layui-col-xs9\">\n" +
                "            <div class=\"layui-row\">\n" +
                "                <div class=\"layui-col-xs6\">\n" +
                "                    <button type=\"button\" class=\"layui-btn layui-icon layui-icon-left layui-btn-disabled\" cropper-event=\"rotate\" data-option=\"-15\" title=\"Rotate -90 degrees\" disabled> 向左旋转15°</button>\n" +
                "                    <button type=\"button\" class=\"layui-btn layui-icon layui-icon-right layui-btn-disabled\" cropper-event=\"rotate\" data-option=\"15\" title=\"Rotate 90 degrees\" disabled> 向右旋转15°</button>\n" +
                "                </div>\n" +
                "                <div class=\"layui-col-xs5\" style=\"text-align: right;\">\n" +
                "                    <button type=\"button\" class=\"layui-btn layui-icon layui-icon-refresh layui-btn-disabled\" cropper-event=\"reset\" title=\"重置图片\" disabled></button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-xs3\">\n" +
                "            <button class=\"layui-btn layui-btn-fluid layui-btn-disabled\" cropper-event=\"confirmSave\" type=\"button\" disabled> 保存修改</button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "</div></div>";

            var options = {
                aspectRatio: mark, // 裁剪框比例
                preview: '#'+previewId, // 预览div
                viewMode:1,
                dragMode:'crop',
                guides : false, // 去掉裁剪框里面白色虚线
                responsive:false, // 是否在调整窗口大小的时候重新渲染cropper
                restore:false // 是否在调整窗口大小后恢复裁剪的区域
            };
            $(elem).unbind()
            $(elem).on('click',function () {
                $('body').append(html);
                var content = $("#"+cropperDivId)
                    ,image = $("#"+imageId)
                    ,file = $("#"+uploadId);
                var layerindex = layer.open({
                    type: 1
                    , content: content
                    , area: area
                    , success: function () {
                        image.cropper('destroy').attr('src', '').cropper(options);
                        $(".layui-btn").unbind()
                        $(".layui-btn").on('click',function () {
                            var event = $(this).attr("cropper-event");
                            //监听确认保存图像
                            if(event === 'confirmSave'){
                                image.cropper("getCroppedCanvas",{
                                    width: saveW,
                                    height: saveH
                                }).toBlob(function(blob){
                                    var formData=new FormData();
                                    var filename = image.attr('filename') || "cropper.jpg";
                                    formData.append('file',blob,filename);
                                    $.ajax({
                                        method:"post",
                                        url: url, //用于文件上传的服务器端请求地址
                                        data: formData,
                                        processData: false,
                                        contentType: false,
                                        success:function(result){
                                            image.cropper('destroy');
                                            if($.isFunction(done)){
                                                done(result)
                                            }
                                            layer.close(layerindex);
                                        },
                                        error: function (){
                                            if($.isFunction(error)){
                                                error()
                                            }
                                        }
                                    });
                                });
                                //监听旋转
                            }else if(event === 'rotate'){
                                var option = $(this).attr('data-option');
                                image.cropper('rotate', option);
                                //重设图片
                            }else if(event === 'reset'){
                                image.cropper('reset');
                            }
                            //文件选择
                            file.change(function () {
                                var r= new FileReader();
                                var f=this.files[0];
                                var fileType = f.type.substring(Number(f.type.indexOf('/'))+1,f.type.length);
                                var imglist = ['png', 'jpg', 'jpeg', 'bmp', 'gif'];
                                if(imglist.indexOf(fileType)<0){
                                    layui.webframe.alert.msg('文件格式只能为图片格式！',{iconCu:3, anim: 6})
                                    return;
                                }
                                r.readAsDataURL(f);
                                r.onload=function (e) {
                                    image.cropper('destroy').attr('src', this.result).attr('filename',f.name).cropper(options);
                                    $(".layui-btn-disabled").removeAttr("disabled").removeClass("layui-btn-disabled")
                                };
                            });
                        });
                    }
                    , cancel: function (index) {
                        layer.close(index);
                        image.cropper('destroy');
                        setTimeout(function (){
                            $('#cropperimagepanel').remove()
                        },500)
                    }
                });
            });
        }
    };
    exports('croppers', obj);
});
