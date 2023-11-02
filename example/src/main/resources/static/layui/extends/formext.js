layui.define(['jquery', 'webframe', 'laytpl', 'layer','transfer','Base64'], function (exports) {
    var $ = layui.jquery,
        webframe = layui.webframe,
        base64 = layui.Base64,
        device = layui.device(),
        MOD_NAME = 'form',
        ELEM = '.layui-form',
        t = {},
        $dom = $(document), $win = $(window),
        formVerify = {
            required: function (e){
                if (!e || !/[\S]+/.test(e.toString())){
                    return "必填项不能为空"
                }
            },
            mobile: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^1[3-9]\d{9}$/.test(e.toString())) {
                    return "请输入正确的手机号"
                }
            },
            tel: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6}))$/.test(e.toString())){
                    return "座机号码格式不正确"
                }
            },
            phone: function (e){
                if(/[\S]+/.test(e.toString()) && !/(^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6})))$|(^1[3-9]\d{9}$)/.test(e.toString())){
                    return "座机或手机号格式不正确"
                }
            },
            email: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^[\w\+\-]+(\.[\w\+\-]+)*@[a-z\d\-]+(\.[a-z\d\-]+)*\.([a-z]{2,4})$/i.test(e.toString())) {
                    return "邮箱格式不正确"
                }
            },
            qq: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^[1-9]\d{4,}$/.test(e.toString())) {
                    return "QQ号格式不正确"
                }
            },
            url: function (e) {
                if (/[\S]+/.test(e.toString()) && !/(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/.test(e.toString())) {
                    return "链接格式不正确"
                }
            },
            ip: function (e) {
                if (/^(([1-9]?\d|1\d{2}|2[0-4]\d|25[0-5])\.){3}([1-9]?\d|1\d{2}|2[0-4]\d|25[0-5])$/.test(e.toString())) {
                    return "IP地址格式不正确"
                }
            },
            number: function (e) {
                if (/[\S]+/.test(e.toString()) && (!e || isNaN(e))) return "请输入有效的数字"
            },
            digits: function (e){
                if(/[\S]+/.test(e.toString()) && !/^\d+$/.test(e)){
                    return "请输入整数"
                }
            },
            normalString: function (e){
                if(/[\S]+/.test(e.toString()) && !/^[A-Za-z0-9\u4e00-\u9fa5\_\-\.]+$/.test(e)){
                    return "您的输入包含了特殊字符"
                }
            },
            letter: function (e){
                if(/[\S]+/.test(e.toString()) && !/^[a-zA-Z]+$/.test(e)){
                    return "请输入英文字母 "
                }
            },
            letterDigits: function (e){
                if(/[\S]+/.test(e.toString()) && !/^[a-zA-Z0-9]+$/.test(e)){
                    return "请输入英文字母或数字 "
                }
            },
            money: function (e){
                if(/[\S]+/.test(e.toString()) && !/^[\-\+]?(?:0|[0-9]\d*)(?:\.\d{1,2})?$/.test(e)){
                    return "请填写有效的货币数据"
                }
            },
            date: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(e.toString())) {
                    return "请输入正确的日期"
                }
            },
            noyeardate: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^(0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/.test(e.toString())) {
                    return "请输入正确的日期"
                }
            },
            nodaydate: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^\d{4}[\/\-](0?[1-9]|1[012])$/.test(e.toString())) {
                    return "请输入正确的年和月"
                }
            },
            time: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^(2[0123]|(1|0?)[0-9]){1}:([0-5][0-9]){1}:([0-5][0-9]){1}$/.test(e.toString())) {
                    return "请输入正确的时间"
                }
            },
            datetime: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(2[0123]|(1|0?)[0-9]){1}:([0-5][0-9]){1}:([0-5][0-9]){1}$/.test(e.toString())) {
                    return "请输入正确的日期时间"
                }
            },
            simpledatetime: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(2[0123]|(1|0?)[0-9]){1}:([0-5][0-9]){1}$/.test(e.toString())) {
                    return "请输入正确的日期时间"
                }
            },
            identity: function (e) {
                if (/[\S]+/.test(e.toString()) && !/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/.test(e.toString())) {
                    return "请输入正确的身份证号"
                }
            },
            postcode: function (e){
                if (/[\S]+/.test(e.toString()) && !/^[1-9]\d{5}$/.test(e.toString())) {
                    return "邮政编码格式不正确"
                }
            },
            chinese: function (e){
                if (/[\S]+/.test(e.toString()) && !/^[\u0391-\uFFE5]+$/.test(e)) {
                    return "请输入中文"
                }
            },
            nochinese: function (e){
                if (/[\S]+/.test(e.toString()) && /[^\x00-\xff]|[\u4e00-\u9fa5]+/g.test(e)) {
                    return "不能包含中文"
                }
            },
            charnumber: function (e) {
                if(/[\S]+/.test(e.toString()) && !/(^[A-Za-z])([0-9a-zA-Z_-]*$)/g.test(e)) {
                    return "首字符必需是字符,且只能是包含字符、下划线和数字"
                }
            },
            htmlwh: function (e) {
                if(/[\S]+/.test(e.toString()) && !/(^[1-9])([0-9]*)$/g.test(e) && !/(^[1-9])([0-9]*)(px|%)$/g.test(e)) {
                    return "像素单位无效"
                }
            },
            keyword: function (e) {
                var kw = ["abstract","assert","boolean","break","byte","case","catch","char","class","continue",
                    "default","do","double","else","enum","extends","final","finally","float","for","if",
                    "implements","import","int","interface","instanceof","long","native","new","package","private",
                    "protected","public","return","short","static","strictfp","super","switch","synchronized",
                    "this","throw","throws","transient","try","void","volatile","while","Object","String","List",
                    "Map","HashMap","ArrayList","Arrays","<script>","</script>"];
                if(/[\S]+/.test(e.toString()) && $.inArray(e,kw) > -1) {
                    return "不能是系统关键字"
                }
            },
            password: function (e){
                if (/[\S]+/.test(e.toString()) && !/^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,12})$/.test(e.toString())) {
                    return "密码由6-12位数字、字母组成"
                }
            },
            range: function (e, item){
                if (/[\S]+/.test(e.toString())){
                    var range = $(item).attr('lay-range')
                    if(range) {
                        var sp = range.split("~"), l = sp[0], u = sp[1], c = +e === +e
                        if (2 === sp.length) {
                            if (l && u) {
                                if (!(c && e >= +l && +u >= e))
                                return "请输入"+l+"到"+u+"的数"
                            } else if (l && !u) {
                                if (!(c && e >= +l))
                                return "请输入大于或等于"+l+"的数"
                            } else if (!l && u) {
                                if (!(c && +u >= e))
                                return "请输入小于或等于"+u+"的数"
                            }
                        }
                    }
                }
            },
            relation: function (e, item) {
                if (/[\S]+/.test(e.toString())){
                    var result = ""
                    var relation = $(item).attr('lay-relation');
                    if(relation) {
                        var form = $(item).parents('.layui-form');
                        if(form){
                            $(form).find('input[name="'+relation+'"]').each(function (i,field){
                                if($(field).val() !== e){
                                    result = "两次输入不一至，请新输入";
                                    $(field).focus();
                                }
                            });
                        }
                    }
                    if(result !== "") return result;
                }
            }
        },
        resizeObject = {};

    //为layui弹窗添加回调方法
    t.PopupDialogCallback = {}

    t.extRender = function (elem, fVerify) {
        if(fVerify) formVerify = fVerify;

        layui.form.verify(formVerify);
        layui.form.render(elem);
        $(".layui-input.required,.layui-textarea.required").each(function () {
            if(!t.hasAttr(this,"required")) {
                $(this).attr("required", true)
            }
        })
        $("input[lay-verify*='required'],textarea[lay-verify*='required'],select[lay-verify*='required']").each(function (){
            if(!$(this).hasClass("required")){
                if($(this).parent().parent().find('.layui-form-label').length > 0) {
                    $(this).parent().parent().find('.layui-form-label').each(function () {
                        if (this.tagName === "LABEL" && !$(this).hasClass("web-form-item-require")) {
                            $(this).addClass("web-form-item-require")
                        }
                    });
                }else if($(this).parents('.layui-block').find('.layui-form-label').length > 0) {
                    $(this).parents('.layui-block').find('.layui-form-label').each(function () {
                        if (this.tagName === "LABEL" && !$(this).hasClass("web-form-item-require")) {
                            $(this).addClass("web-form-item-require")
                        }
                    });
                }else {
                    $(this).parent().parent().parent().find('.layui-form-label').each(function () {
                        if (this.tagName === "LABEL" && !$(this).hasClass("web-form-item-require")) {
                            $(this).addClass("web-form-item-require")
                        }
                    })
                }
            }
        })

        $('.layui-form-popup > .layui-popup-icon, .layui-form-popup > .layui-popup-textarea-icon').each(function () {
            $(this).off('click').on('click', function () {
                if(!t.hasAttr($(this).parent().find('.layui-popup-input'),"disabled")) {
                    OutsideLinks_Popup_Event($(this).parent().find('.layui-popup-input'));
                }
            })
        })
        $('.layui-form-popup > .layui-clear-icon,.layui-form-department > .layui-clear-icon, .layui-form-popup > .layui-clear-textarea-icon').each(function () {
            $(this).off('click').on('click', function () {
                $(this).parent().find('.layui-input, .layui-textarea').each(function () {
                    $(this).val("");
                    var callback = $(this).attr("lay-callback");
                    if(callback) {
                        layui.event('callback', callback, {elem: this, id: "", value: ""});
                    }
                })
            })
        })

        $('.layui-form-popup > .layui-input, .layui-form-popup > .layui-textarea').each(function () {
            if (!t.hasAttr(this, "disabled") && t.hasAttr(this,"readonly")) {
                $(this).off('click').on('click', function () {
                    OutsideLinks_Popup_Event(this);
                })
            }
        })

        function OutsideLinks_Popup_Event(obj) {
            var that = obj,
                url = $(that).attr("lay-url"),
                multiselect = t.hasAttr(that, "lay-multiselect");
            if (url && url !== "") {
                var selectid = $(that).prev().val();
                if(typeof selectid === "undefined")
                    selectid = $(that).val();
                var dialogtitle = $(that).attr("lay_title") || "请选择"
                var digwidth = $(that).attr("lay-digwidth") || "800px"
                var digheight = $(that).attr("lay-digheight") || "auto"
                var filter = $(that).attr("lay-filter") || null
                var exclude = $(that).attr("lay-exclude") || ""
                var linkage = $(that).attr("lay-linkage") || ""
                var remodule = $(that).attr("lay-remodule") || ""
                var record = $(that).attr("lay-record") || ""
                var callback = $(that).attr("lay-callback") || ""
                webframe.waitbar.show()
                var base64_selectid = base64.encode(selectid);
                base64_selectid =  base64_selectid.replaceAll("+", "%2B");
                webframe.loadHtml(null, 'post', url, {
                    "select.id": selectid,
                    "base64": base64_selectid,
                    "exclude.id": exclude,
                    multiselect: multiselect,
                    digwidth: digwidth,
                    digheight: digheight,
                    remodule: remodule,
                    linkage: linkage,
                    record: record
                }, function (obj) {
                    webframe.waitbar.close()
                    layui.layer.open({
                        type: 1,
                        title: dialogtitle,
                        resize: false,
                        area: [digwidth, digheight],
                        content: obj.html,
                        skin:'layui-layer-fix-select',
                        success: function (layero, index) {
                            if (typeof t.PopupDialogCallback === "object") {
                                t.PopupDialogCallback[index] = function (obj) {
                                    if (obj && obj.type === "submit" && obj.data) {
                                        $(that).val(obj.data.name)
                                        if (filter) {
                                            layui.event('input', filter, obj.data)
                                        }
                                        if (callback) {
                                            var pamn = {elem: that};
                                            layui.each(obj.data,function (key,value){
                                                if(obj.data.hasOwnProperty(key)) pamn[key] = value;
                                            });
                                            layui.event('callback',callback,pamn)
                                        }
                                        $(that).prev().val(obj.data.id)
                                        $(that).removeClass("layui-form-danger")
                                    }
                                    layui.layer.close(index)
                                    t.PopupDialogCallback[index] = null
                                }
                            }
                        },
                    });
                })
            }
        }
    }

    //获取当前url的前缀
    t.gePreUrl = function gePreUrl(url) {
        var urlRgx=/\/\w+\/.*/;
        if(urlRgx.test(url)){
            return url.replace(/(\/\w+\/).*/ig,"$1");
        }
        return url;
    }

    t.getEventPreFix = function getEventPreFix(url) {
        var route = Object.assign([], layui.webframe.route.path)
        route.pop()
        var defaulturl = ""
        if (route.length > 0) {
            defaulturl = "/" + route.join("/") + "/"
        }
        var hash = url || defaulturl
        if (!hash.endsWith("/")) {
            hash += "/"
        }
        return hash;
    }
    //扩展用于检查Form中的字段是否被修改过
    t.isUpdateWithForm = function (checkform,isHit) {
        isHit = typeof isHit === "undefined";
        var isUpdate = false
        $(checkform).find('*[verify_value]').each(function () {
            var verify_value = $(this).attr("verify_value");
            if(typeof verify_value !== "undefined") {
                if ($(this).attr("type") === "radio" || $(this).attr("type") === "checkbox") {
                    if ($(this).is(':checked')) {
                        if ($(this).attr("verify_value") === "off") {
                            isUpdate = true;
                        }
                    } else {
                        if ($(this).attr("verify_value") === "on") {
                            isUpdate = true;
                        }
                    }
                } else {
                    if ($(this).attr("data-toggle") === "tinymce") {
                        var id = $(this).attr("id"), editer = layui.tinymce.get("#" + id);
                        if (typeof editer !== "undefined" && !editer.readonly &&  t.encodeHtml(verify_value) !== t.encodeHtml(editer.getContent({format: 'raw'}))) {
                            isUpdate = true;
                        }
                    }else if(t.hasAttr(this,"multiple")){
                        var value = $(this).val();
                        if(value){
                            if(verify_value !== value.toString()) {
                                isUpdate = true;
                            }
                        }else if(verify_value !== ""){
                            isUpdate = true;
                        }
                    } else if (verify_value !== $(this).val().toString()) {
                        isUpdate = true;
                    }
                }
            }
        })
        if (isHit && !isUpdate) {
            webframe.alert.msg('表单没有更新,无需保存!',{iconCu: 3,anim: 6});
        }
        return isUpdate
    }
    //扩展用于最大化指定的容器
    t.resizeToFull = function (obj, target, fix) {
        if (target && !resizeObject.hasOwnProperty(target)) {
            resizeObject[target] = fix || 0;
        }
        if(typeof obj === "string"){
            obj = $(obj);
        }
        for (var selector in resizeObject) {
            $(selector).find('.layui-full-container').each(function () {
                var impairment = 12;
                if($(window).width()<=1440){ impairment = 12; }
                if(resizeObject[selector] && parseInt(resizeObject[selector], 10) > 0){
                    impairment += parseInt(resizeObject[selector], 10);
                }
                $(this).css({height: obj.height() - impairment + "px"});
            })
        }
    }
    //扩展用于判断元素是否有某个属性
    t.hasAttr = function (obj, attr) {
        return typeof $(obj).attr(attr) !== "undefined";
    }
    t.sleep = function (time) {
        var startTime = new Date().getTime() + parseInt(time, 10);
        while (new Date().getTime() < startTime) {
        }
    }

    //扩展用于编辑页面关闭事件
    t.closeEdit = function (url,res) {
        var closeFunction = function (purl) {
            var parenturl = purl || t.getEventPreFix() + "listview"
            if (parenturl === "") {
                console.error("关闭参数为空，处理失败！")
                return
            }
            if (webframe.viewTabs === true) {
                var url = $('.web-tabs-active').attr('lay-url')
                if (webframe.tab.has(parenturl)) {
                    webframe.tab.del(parenturl)
                }
                webframe.navigate(parenturl);
                var close = function () {
                    setTimeout(function () {
                        if (!webframe.tab.has(parenturl)) {
                            close();
                        } else {
                            webframe.tab.del(url)
                            webframe.navigate(parenturl);
                            if (res != null && res.msg != null && res.msg !== "") {
                                if (res.code !== 0 && res.code !== 200) {
                                    layui.webframe.alert.confirm('温馨提示',res.msg,{iconCu:4,showSure:true});
                                } else {
                                    layui.webframe.alert.msg(res.msg,{iconCu:1});
                                }
                            }
                        }
                    }, 10)
                }
                close();
            } else {
                webframe.navigate(parenturl);
                if (res != null && res.msg != null && res.msg !== "") {
                    if (res.code !== 0 && res.code !== 200) {
                        layui.webframe.alert.confirm('温馨提示',res.msg,{iconCu:4,showSure:true});
                    } else {
                        layui.webframe.alert.msg(res.msg,{iconCu:1});
                    }
                }
            }
        }

        closeFunction(url)
    }

    //扩展用于编辑页面刷新事件
    t.RefreshEdit = function (module, url, res) {
        // if(typeof module !== "undefined")
            // layui.webframe.refresh(module);
        setTimeout(function () {
            layui.webframe.navigate(url);
            if (res != null && res.msg != null && res.msg !== "") {
                if (res.code !== 0 && res.code !== 200) {
                    layui.webframe.alert.confirm('温馨提示',res.msg,{iconCu:4,showSure:true});
                } else {
                    layui.webframe.alert.msg(res.msg,{iconCu:1});
                }
            }
        }, 10);
    }

    t.getCurrentIndex = function (e) {
        var index = 0
        $('.layui-layer-page').each(function () {
            if ($(this).find(e).length > 0) {
                index = $(this).attr("times")
            }
        })
        return index
    }
    t.getCurrentLayerZIndex = function (e) {
        var index = 0
        $('.layui-layer-page').each(function () {
            if ($(this).find(e).length > 0) {
                index = $(this).css("zIndex");
            }
        })
        return index
    }

    t.doVerify = function (z) {
        var e = null,
            n = layui.device(),
            a = formVerify,
            s = "layui-form-danger",
            c = "object" == typeof z ? z : $(z),
            d = t.hasAttr(c,"lay-verify") ? c : c.find("*[lay-verify]");
        layui.each(d, function (l, r) {
            var o = $(this),
                c = o.attr("lay-verify").split("|"),
                u = o.attr("lay-verType"),
                ty = o.attr("type"),
                d = o.val();
            if (o.removeClass(s), layui.each(c, function (t, l) {
                var c, f = "", v = "function" == typeof a[l];
                if (a[l]) {
                    var c = v ? f = a[l](d, r) : !a[l][0].test(d);
                    if (f = f || a[l][1], "required" === l && (f = o.attr("lay-reqText") || f), c) return "tips" === u ? layui.layer.tips(f, function () {
                        return "string" == typeof o.attr("lay-ignore") || "select" !== r.tagName.toLowerCase() && !/^checkbox|radio$/.test(r.type) && "hidden" !== ty ? o : o.next()
                    }(), {tips: 1}) : "alert" === u ? layui.webframe.alert.confirm('温馨提示',f, {iconCu:2,showSure:true}) : layui.webframe.alert.msg(f, {iconCu:2}), n.android || n.ios || setTimeout(function () {
                        r.focus()
                    }, 7), o.addClass(s), e = !0
                }
            }), e) return e
        });
        return !e;
    }

    t.VerifyStr = function (Str,verify) {
        var e = null,a = formVerify,c = verify.split("|");
        if (layui.each(c, function (t, l) {
            var c, f = "", v = "function" == typeof a[l];
            if (a[l]) {
                var c = v ? f = a[l](Str, null) : !a[l][0].test(Str);
                if (f = f || a[l][1], "required" === l && f, c)
                    e = c
            }
        }), e) return e
        return e;
    }

    t.encodeHtml = function (Str) {
        if(typeof Str === "undefined") return "";
        return Str.replaceAll("&", "&#38;").replaceAll("<", "&#60;").replaceAll(">", "&#62;").replaceAll('"', "&#34;").replaceAll("'","&#39;");
    }
    t.decodeHtml = function (Str) {
        if(typeof Str === "undefined") return "";
        var ENTITY_PATTERN = /&#(\d+);|&#x([0-9a-f]+);|&(\w+);/ig;
        var ENTITY_MAPPING = {
            'amp': '&',
            'apos': '\'',
            'gt': '>',
            'lt': '<',
            'quot': '"'
        };
        Object.keys(ENTITY_MAPPING).forEach(function(k) {
            ENTITY_MAPPING[k.toUpperCase()] = ENTITY_MAPPING[k];
        });

        function replaceEntities(_, d, x, z) {
            if (z) {
                if (String.prototype.hasOwnProperty.call(ENTITY_MAPPING, z)) {
                    return ENTITY_MAPPING[z];
                } else {
                    return '&' + z + ';';
                }
            }
            if (d) {
                return String.fromCharCode(d);
            }
            return String.fromCharCode(parseInt(x, 16));
        }

        if (Str.length > 3 && Str.indexOf('&') !== -1) {
            return Str.replace(ENTITY_PATTERN, replaceEntities);
        }
        return Str;
    }

    t.decodeJson = function (Str) {
        if(typeof Str === "undefined") return "";
        return t.decodeHtml(Str).replaceAll("\\","\\\\").replaceAll('"','\\"').replaceAll("'","\\'");
    }

    t.getValue = function(filter, itemForm){
        itemForm = itemForm || $(ELEM + '[lay-filter="' + filter +'"]').eq(0);

        var nameIndex = {} //数组 name 索引
            ,field = {}
            ,fieldElem = itemForm.find('input,select,textarea') //获取所有表单域

        layui.each(fieldElem, function(_, item){
            var othis = $(this)
                ,init_name; //初始 name

            item.name = (item.name || '').replace(/^\s*|\s*&/, '');
            if(!item.name) return;
            //用于支持数组 name
            if(/^.*\[\]$/.test(item.name)){
                var key = item.name.match(/^(.*)\[\]$/g)[0];
                nameIndex[key] = nameIndex[key] | 0;
                init_name = item.name.replace(/^(.*)\[\]$/, '$1['+ (nameIndex[key]++) +']');
            }

            if(/^checkbox|radio$/.test(item.type) && !item.checked) return;  //复选框和单选框未选中，不记录字段
            if(othis.attr("data-toggle") === "tinymce") { //编辑器内容
                var id = $(this).attr("id"), editer = layui.tinymce.get("#" + id);
                if (typeof editer !== "undefined") {
                    field[init_name || item.name] = editer.getContent({format: 'raw'});
                }
            }else if(t.hasAttr(othis,"multiple")){
                var value = othis.val();
                if(value) {
                    field[init_name || item.name] = value.toString();
                }
            }else {
                field[init_name || item.name] = item.value;
            }
        });
        return field;
    };
    //表单提交校验
    t.submit = function(){
        var stop = null //验证不通过状态
            ,verify = formVerify //验证规则
            ,DANGER = 'layui-form-danger' //警示样式
            ,field = {}  //字段集合
            ,button = $(this) //当前触发的按钮
            ,elem = button.parents(ELEM) //当前所在表单域
            ,verifyElem = elem.find('*[lay-verify]') //获取需要校验的元素
            ,formElem = button.parents('form')[0] //获取当前所在的 form 元素，如果存在的话
            ,filter = button.attr('lay-filter'); //获取过滤器


        //开始校验
        layui.each(verifyElem, function(_, item){
            var othis = $(this)
                ,vers = othis.attr('lay-verify').split('|')
                ,verType = othis.attr('lay-verType') //提示方式
                ,value = othis.val();

            // 判断是否存在tab切换，如存在tab切换，则切换到“o”对象的tab
            if(othis.parents('.layui-tab-content').find('.layui-tab-item').length>1){
                othis.parents('.layui-tab-item').siblings().removeClass('layui-show')
                othis.parents('.layui-tab-item').addClass('layui-show')
                var idx = othis.parents('.layui-tab-content').find('.layui-show').index()
                othis.parents('.layui-tab-content').prev('.layui-tab-title').find('li').eq(idx).addClass('layui-this')
                othis.parents('.layui-tab-content').prev('.layui-tab-title').find('li').eq(idx).siblings().removeClass('layui-this')
            }

            othis.removeClass(DANGER); //移除警示样式

            //遍历元素绑定的验证规则
            layui.each(vers, function(_, thisVer){
                var isTrue //是否命中校验
                    ,errorText = '' //错误提示文本
                    ,isFn = typeof verify[thisVer] === 'function';

                //匹配验证规则
                if(verify[thisVer]){
                    isTrue = isFn ? errorText = verify[thisVer](value, item) : !verify[thisVer][0].test(value);
                    errorText = errorText || verify[thisVer][1];

                    if(thisVer === 'required'){
                        errorText = othis.attr('lay-reqText') || errorText;
                        if(/^checkbox|radio$/.test(item.type)){
                            var name = item.name;
                            if(name){
                                var ischecked = false;
                                layui.each(elem.find('input[name="'+name+'"]'),function (_,box){
                                    if(box.checked) ischecked = true;
                                });
                                if(!ischecked){
                                    if(/^radio$/.test(item.type))
                                        errorText = "必需选择一项";
                                    else
                                        errorText = "必需至少选择一项";
                                }
                                isTrue = !ischecked;
                            }
                        }else if(!isTrue && othis.attr("data-toggle") === "rate") {
                            isTrue = parseInt(item.value,10) <= 0
                        }else if(othis.attr("data-toggle") === "tinymce") {
                            var id = othis.attr("id"), editer = layui.tinymce.get("#" + id);
                            if (typeof editer !== "undefined") {
                                var evalue = editer.getContent({format: 'raw'});
                                isTrue = evalue === '<p><br data-mce-bogus="1"></p>' || evalue === "";
                            }
                        }
                    }

                    //如果是必填项或者非空命中校验，则阻止提交，弹出提示
                    if(isTrue){
                        //非移动设备自动定位焦点
                        if(!device.android && !device.ios){
                            setTimeout(function(){
                                item.focus();
                                tips();
                            }, 7);
                        }else{
                            tips();
                        }

                        function tips(){
                            //提示层风格
                            if(verType === 'tips'){
                                layer.tips(errorText, function(){
                                    if(typeof othis.attr('lay-ignore') !== 'string'){
                                        if(item.tagName.toLowerCase() === 'select' || item.style.display === "none" || item.type === 'hidden' || /^checkbox|radio$/.test(item.type)){
                                            return othis.next();
                                        }
                                    }
                                    return othis;
                                }(), {tips: 1});
                            } else if(verType === 'alert') {
                                layui.webframe.alert.confirm('温馨提示',errorText, {iconCu:2,showSure:true})
                                // layer.alert(errorText, {title: '提示', shadeClose: true});
                            }
                            //如果返回的为字符或数字，则自动弹出默认提示框；否则由 verify 方法中处理提示
                            else if(/\bstring|number\b/.test(typeof errorText)){
                                layui.webframe.alert.msg(errorText, {iconCu:2})
                                // layer.msg(errorText, {icon: 5, shift: 6});
                            }
                        }

                        othis.addClass(DANGER);
                        return stop = true;
                    }
                }
            });

            if(stop) return stop;
        });

        if(stop) return false;

        //获取当前表单值
        field = t.getValue(null, elem);

        //返回字段
        return layui.event.call(this, MOD_NAME, 'submit('+ filter +')', {
            elem: this
            ,form: formElem
            ,field: field
        });
    };
    //表单提交事件
    $dom.off("submit",ELEM).off("click","*[lay-submit]").on('submit', ELEM, t.submit)
        .on('click', '*[lay-submit]', t.submit);

    layui.form = $.extend(layui.form, t);

    exports('formext', {});
});
