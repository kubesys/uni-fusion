layui.define(['jquery', 'element', 'loadBar', 'dropdown', 'laytpl', 'layer'], function (exports) {
    var $ = layui.jquery,
        element = layui.element,
        loadBar = layui.loadBar,
        laytpl = layui.laytpl,
        layer = layui.layer,
        waitindex = "",
        webframe = {
            route: layui.router(),
            containerBody: null,
            container: null,
            currrentUrl: null,
            shrinkCls: 'web-sidebar-shrink',
            viewTabs: true,
            config: {
                entry: '/base/index',
                engine: '',
                views: '',
                containerElem: '#web',
                containerElemBody: '#web-body',
                sidemenuElem: '#app-sidebar',
                indPage: [],
                style: {},
                single_page_nav: '.web-single-page',
                debug: true,
                currentUser: {},
                eventName: 'web-event',
            },
            modal: {
                getIcon: function(iconId){
                    var iconText = '';
                    switch (iconId)
                    {
                        case 1:iconText='&#xe6af;';
                            break;
                        case 2:iconText="&#xe691;";
                            break;
                        case 3:iconText="&#xe699;";
                            break;
                        case 4:iconText="&#xe713;";
                            break;
                        case 5:iconText="&#xe737;";
                            break;
                        default: iconText="&#xe699;";
                    }
                    return iconText;
                }
            },
            alert: {
                //iconindex: 1:成功图标；2：错误图标；3：警示图标；4：信息图标；5：等待图标
                confirm : function (title, msg, params, yes, no) {
                    params = params || {};
                    params.titleIco = 'exclaimination';
                    params.titleIcoColor = '#ffc107';
                    params.titleValue = title;
                    params.shadeClose = false;
                    params.shade = '0';
                    params.resize = false;
                    params.iconCu = params.iconCu || '3';

                    // params.offset = '180px';
                    var titleCon = '<div class="cu-alert-title"><i class="lcdp-icon lcdp-icon-color-cu icon-color-cu'+ params.iconCu +'">'+ webframe.modal.getIcon(params.iconCu) +'</i><span class="icon-color-span">'+ title +'</span></div>';
                    var content = '<span class="cu-close lcdp-icon layui-layer-close">&#xe691;</span><div class="cu-alert-con"><span class="cu-alert-con-text">'+ msg +'</span></div>';
                    var noTitlecontent = '<span class="cu-close lcdp-icon layui-layer-close">&#xe691;</span><div class="cu-alert-con"><i class="lcdp-icon lcdp-icon-color-cu icon-color-cu'+ params.iconCu +'">'+ webframe.modal.getIcon(params.iconCu) +'</i><span class="cu-alert-con-text">'+ msg +'</span></div>';
                    var allContent = content;
                    if(title){
                        allContent = titleCon + content;
                    }else{
                        allContent = noTitlecontent;
                    }
                    params.title = '';
                    params.content = allContent;

                    var btn = [];
                    if(params.showSure){
                        btn.push('确定');
                    }
                    if(params.showCancel){
                        btn.push('取消');
                    }
                    params = $.extend({
                        skin: 'cu-alert',
                        area: [$(window).width() <= 750 ? '60%' : 'auto'], closeBtn:0, maxWidth: '640px',
                        shadeClose: true,
                        btn: btn
                        , yes: function (index, layero) {
                            yes && (yes)();
                            layer.close(index);
                            return false;
                        }
                        , btn2: function (index, layero) {
                            no && (no)();
                            layer.close(index);
                        }
                    }, params);
                    layer.alert(msg, params);
                    // this.base(msg, params);
                },
                msg : function (msg, params, callback) {
                    params = params || {};
                    params.offset = params.offset || '140px';
                    params.time = params.time || '2000';
                    params.iconCu = params.iconCu || '3';
                    // params.anim = params.anim || 6;
                    layui.layer.msg('<i class="lcdp-icon lcdp-icon-color-cu icon-color-cu'+ params.iconCu +'">'+ webframe.modal.getIcon(params.iconCu) +'</i><span class="icon-color-span">'+ msg +'</span>',params,function (){
                        if($.isFunction(callback)) callback();
                    });
                }
            },
            waitbar: {
                show: function (config) {
                    var borderColor = "#1892ff",
                        flowColor = "#1892ff",
                        title = "加载中";
                    if(config){
                        borderColor = config.bdcolor || borderColor;
                        flowColor = config.flowcolor || flowColor;
                        title = config.title || title;
                    }
                    if(waitindex <= 0){
                        var html =
                            '    <div class="loading-div-wrap">\n' +
                            '        <span class="loading-div-iconfont icon-color-cu5 lcdp-icon">&#xe737;</span><span class="loading-div-text">'+title+'<span class="loading-div-dian"><i>.</i><i>.</i><i>.</i></span></span>\n' +
                            '    </div>';
                        layer.open({
                            type: 1,
                            title: false,
                            resize: false,
                            closeBtn: 0,
                            skin:'layui-layer-waitbar',
                            content: html,
                            success: function (layero, index) {
                                waitindex = index;
                            },
                            end: function (){
                                waitindex = -1;
                            }
                        })
                    }
                },
                close: function () {
                    layer.close(waitindex)
                }
            },
            ie8: navigator.appName === 'Microsoft Internet Explorer' && navigator.appVersion.split(';')[1].replace(/[ ]/g, '') === 'MSIE8.0'
        };

    webframe.init = function (config) {
        this.config = $.extend(this.config, config)
        this.container = $(this.config.containerElem)
        this.viewTabs = (typeof this.config.currentUser.profilesettings.istab === "undefined") ? true : this.config.currentUser.profilesettings.istab === '1'
        layui.each(this.config.style, function (index, url) {
            layui.link(url + '?v=' + new Date().getTime())
        });

        this.initView();

        $(window).on('resize', function (e) {
            webframe.event("resize", e)
        });


        $(document).on('click', '[lay-href]', function (e) {
            var href = $(this).attr('lay-href');
            var target = $(this).attr('target');
            if (href === '') return;
            if (href.startsWith('http')) {
                window.open(href)
            }
            if (webframe.isUrl(href)) {
                next()
            }

            function next() {
                target === '__blank' ? window.open(href) : webframe.navigate(href);

            }

            if ($.isFunction(webframe.routeLeaveFunc)) {
                webframe.routeLeaveFunc(webframe.route + "asdfasdf", href, next)
            } else {
                next()
            }

            return false
        });
        $(document).on('mouseenter', '[lay-tips]', function (e) {
            var title = $(this).attr('lay-tips');
            var dire = $(this).attr('lay-dire') || 3;
            if (title) {
                layer.tips(title, $(this), {
                    tips: [dire, '#263147']
                })
            }
        });
        $(document).on('mouseleave', '[lay-tips]', function (e) {
            layer.closeAll('tips')
        });

        $(document).on('click', '*[' + webframe.config.eventName + ']', function (e) {
            webframe.event($(this).attr(webframe.config.eventName), $(this))
        });

        var shrinkSidebarBtn = '.' + webframe.shrinkCls + ' #app-sidebar .layui-nav-item a';
        $(document).on('click', shrinkSidebarBtn, function (e) {
            var chileLength = $(this)
                .parent()
                .find('.layui-nav-child').length;
            if (chileLength > 0) {
                webframe.flexible(true);
                layer.closeAll('tips')
            }
        });
        $(document).on('mouseenter', shrinkSidebarBtn, function (e) {
            var title = $(this).attr('title');
            if (title) {
                layer.tips(title, $(this).find('.layui-icon'), {
                    tips: [2, '#263147']
                })
            }
        });

        $(document).on('mouseleave', shrinkSidebarBtn, function (e) {
            layer.closeAll('tips')
        });

        webframe.on('flexible', function (init) {
            var status = webframe.container.hasClass(webframe.shrinkCls);
            webframe.flexible(status);
        });
    }

    webframe.render = function (elem){
        if (typeof elem == 'string') elem = $(elem);
        if (elem.length > 0) {
            var action = elem.get(0).tagName === 'SCRIPT' ? 'next' : 'find';
            elem[action]('[is-template]').remove();
            this.parse(elem)
        }
    }

    webframe.on = function (name, callback) {
        return layui.onevent(webframe.config.eventName, 'system(' + name + ')', callback)
    };
    webframe.event = function (name, params) {
        layui.event(webframe.config.eventName, 'system(' + name + ')', params)
    };

    webframe.flexible = function (open) {
        if (open === true) {
            webframe.container.removeClass(webframe.shrinkCls)
        } else {
            webframe.container.addClass(webframe.shrinkCls)
        }
    };

    webframe.isUrl = function (str) {
        return /^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/.test(
            str
        )
    }

    webframe.fillHtml = function (url, htmlElem, modeName) {
        var fluid = htmlElem.find('.layui-fluid[lay-title]');
        var title = '',module = '';
        if (fluid.length > 0) {
            title = fluid.attr('lay-title');
            module = fluid.attr('lay-module');
        }

        var container = webframe.containerBody || webframe.container;
        container[modeName](htmlElem.html());
        if (modeName === 'prepend') {
            webframe.parse(container.children('[lay-url="' + url + '"]'))
        } else {
            webframe.parse(container)
        }
        return {title: title,module: module, url: url, htmlElem: htmlElem}
    };

    //初始化视图区域
    webframe.initView = function () {
        if (!webframe.route || !webframe.route.href || webframe.route.href === '/') {
            webframe.route = layui.router('#' + webframe.config.entry);
        }
        webframe.route.fileurl = '/' + webframe.route.path.join('/');
        if ($.inArray(webframe.route.fileurl, webframe.config.indPage) === -1) {
            var loadRenderPage = function () {
                if (webframe.viewTabs === true) {
                    webframe.renderTabs(webframe.route);
                    $(webframe.config.single_page_nav).addClass('layui-hide');
                } else {
                    webframe.renderHtml(webframe.route.fileurl, webframe.route.search,function (p){
                        if (p.title !== "") {
                            webframe.updateBreadcrumb(p.title);
                        }
                    })
                    $(webframe.config.single_page_nav).addClass('layui-show');
                }
            };

            if (this.containerBody == null) {
                //加载layout文件
                this.renderLayout(function () {
                    //重新渲染导航
                    element.render('nav', 'web-sidebar');
                    //加载视图文件
                    loadRenderPage()
                })
            } else {
                //layout文件已加载，加载视图文件
                loadRenderPage()
            }
        } else {
            //加载单页面
            this.renderIndPage(webframe.route.fileurl, webframe.route.search, function () {
                if (webframe.viewTabs === true) webframe.tab.clear();
            })
        }
    };

    //解析普通文件
    webframe.renderHtml = function (fileurl, querys, callback) {
        webframe.getHtml('GET', fileurl, querys || {}, function (res) {
            var htmlElem = $('<div>' + res.html + '</div>');
            var params = webframe.fillHtml(res.url, htmlElem, 'html');
            if ($.isFunction(callback)) callback(params)
        })
    };

    //加载 tab
    webframe.renderTabs = function (route, callback) {
        webframe.tab.change(route, callback);
    };

    //加载layout文件
    webframe.renderLayout = function (callback, url, querys) {
        if (url === undefined) url = 'layout';
        this.containerBody = null;

        webframe.renderHtml(url, querys, function (res) {
            webframe.containerBody = $(webframe.config.containerElemBody);
            if (webframe.viewTabs === true) {
                webframe.containerBody.addClass('web-tabs-body')
            } else {
                webframe.containerBody.addClass('web-single-body')
            }
            if ($.isFunction(callback)) callback()
        })
    };

    //加载单页面
    webframe.renderIndPage = function (fileurl, querys, callback) {
        webframe.renderLayout(function () {
            webframe.containerBody = null;
            if ($.isFunction(callback)) callback()
        }, fileurl, querys)
    };

    //扩展自定义的Ajax调用
    webframe.ajax = function(type, url, pram, callback, error, isTips) {
        webframe.asyncAjax(type,url,true,pram,callback,error,isTips)
    }

    webframe.asyncAjax = function(type, url, async, pram, callback, error, isTips) {
        if(typeof isTips === "undefined") isTips = true;
        if (url && url.startsWith("/")) {
            url = url.substr(1)
        }
        webframe.waitbar.show()
        layui.each(pram,function (i,v) {
            if(v !== null) {
                pram[i] = encodeURIComponent(v);
            }
        })
        $.ajax({
            type: type,
            url: this.config.views + url,
            data: pram,
            async: async,
            success: function (res) {
                webframe.waitbar.close()
                if(res.msg === "no login" || res.code === 401) {
                    webframe.alert.confirm('登录失效', '登录已失效，请重新登录',{iconCu:2,showSure:true}, function () {
                        window.location.reload();
                        window.location.hash = '';
                    });
                    return;
                }
                if (res.msg != null && res.msg !== "" && isTips) {
                    if (res.code !== 0 && res.code !== 200) {
                        webframe.alert.confirm('温馨提示',res.msg,{iconCu:4,showSure:true},function (){
                            if ($.isFunction(callback)) callback(res);
                        });
                    } else {
                        webframe.alert.msg(res.msg,{iconCu:1});
                        if ($.isFunction(callback)) callback(res);
                    }
                }else{
                    if (res.code !== 0 && res.code !== 200 && isTips) {
                        webframe.alert.confirm('温馨提示',"内部错误！code["+res.code+"]",{iconCu:2,showSure:true},function (){
                            if ($.isFunction(callback)) callback(res);
                        });
                    }else{
                        if ($.isFunction(callback)) callback(res);
                    }
                }
            },
            error: function (request, err, other) {
                webframe.waitbar.close()
                if(isTips) {
                    webframe.alert.confirm('温馨提示', '操作失败或网络故障，请稍候再试。。。', {iconCu: 2, showSure: true},function (){
                        if ($.isFunction(error)) error(request, err, other);
                    });
                }else{
                    if ($.isFunction(error)) error(request, err, other);
                }
            }
        });
    }

    webframe.waitJavaVM = function (callback) {
        webframe.waitbar.show({title:"等待重启"});
        var readStatus = setInterval(function () {
            $.ajax({
                type: 'get',
                url: '/base/waitjavavm',
                data: {t: new Date().getTime()},
                timeout: -1,
                success: function (res) {
                    if(res.code === 0 || res.code === 200) {
                        clearInterval(readStatus);
                        // setTimeout(function () {
                            webframe.waitbar.close();
                            if($.isFunction(callback)) callback();
                        // },2000)
                    }
                },
                error: function (request, err, other) {}
            });
        }, 1000)
    }

    //扩展Ajax下载文件
    webframe.download = function (url,pram,success,error){
        webframe.waitbar.show()
        var savefile = function (data,filename,type){
            var name = filename || new Date().toLocaleDateString();
            var typeStr = type || 'application/vnd.ms-excel';
            if(window.navigator.msSaveOrOpenBlob){
                var blob = new Blob([data],{type:typeStr});
                window.navigator.msSaveBlob(blob,name);
            }else{
                var blob = new Blob([data],{type:typeStr});
                var oa = document.createElement('a');
                oa.href = URL.createObjectURL(blob);
                oa.download = name;
                $("body").append(oa);
                oa.click();
                $(oa).remove();
            }
        };
        var xhr;
        if(window.XMLHttpRequest){
            xhr = new XMLHttpRequest();
        }else{
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xhr.responseType = "blob";
        xhr.open("post",url,true);
        xhr.onload = function (){
            if(this.readyState === 4 && this.status === 200){
                var blob = this.response;
                var reader = new FileReader();
                reader.onloadend = function (e) {
                    if(blob.type.indexOf('json') >= 0){
                        try{
                            var jsonData = JSON.parse(e.target.result);
                            if (jsonData.msg != null && jsonData.msg !== "") {
                                if (jsonData.code !== 0 && jsonData.code !== 200) {
                                    webframe.alert.confirm('温馨提示',jsonData.msg,{iconCu:2,showSure:true})
                                } else {
                                    webframe.alert.confirm('温馨提示',jsonData.msg,{iconCu:1,showSure:true})
                                }
                            }else{
                                if (jsonData.code !== 0 && jsonData.code !== 200) {
                                    webframe.alert.confirm('温馨提示',"内部错误！code["+jsonData.code+"]",{iconCu:2,showSure:true})
                                }
                            }
                            if($.isFunction(success)) success(jsonData)

                        }catch (err){
                            if($.isFunction(error)) error(err)
                        }
                    }else if(blob.type.indexOf('text') >= 0){
                        if($.isFunction(success)) success(e.target.result)
                    }
                }
                if(blob.type.indexOf('json') >= 0 || blob.type.indexOf('text') >= 0){
                    reader.readAsText(blob);
                }else{
                    var filename = xhr.getResponseHeader("Content-Disposition").split("=")[1];
                    filename = unescape(decodeURIComponent(filename))
                    savefile(blob,filename,blob.type)
                }
                webframe.waitbar.close()
            }
        }

        xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        var data = [];
        for(var key in pram){
            if(pram.hasOwnProperty(key)){
                data.push(key + "=" + pram[key]);
            }
        }
        xhr.send(data.join("&"));
    }

    webframe.loadHtml = function (elem, type, url, query, callback, errorback) {
        webframe.getHtml(type, url, query || {}, function (obj) {
            if (elem != null && elem !== "") {
                $(elem).html(obj.html)
            }
            if($.isFunction(callback)) callback(obj)
        },errorback,true)
    };

    webframe.getHtml = function (type, url, qurys, callback, errorback, isAjax) {
        function getErrorHtml() {
            return '<div class="layui-fluid layui-anim web-anim" id="web-error-500" lay-title="服务器错误" lay-module="error">\n' +
                '    <div class="layui-row layui-col-space10" style="margin-top:100px">\n' +
                '        <div class="layui-col-xs3 layui-col-xs-offset2">\n' +
                '            <img src="/images/500.svg" alt="" width="85%"/>\n' +
                '        </div>\n' +
                '        <div class="layui-col-xs7">\n' +
                '            <h1 style="font-size: 60px">500</h1>\n' +
                '            <h2 class="web-mar-b20 web-c-gray">抱歉，服务器出错了</h2>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>\n';
        }
        webframe.currrentUrl = url;
        url = url || webframe.config.entry;
        if (url.startsWith("/")) {
            url = url.substr(1);
        }
        loadBar.start();
        var queryIndex = url.indexOf('?');
        if (queryIndex !== -1) url = url.slice(0, queryIndex);
        qurys['invalid_ie_cache'] = new Date().getTime();
        webframe.waitbar.show()
        $.ajax({
            url:
                (url.indexOf(webframe.config.views) === 0 ? '' : webframe.config.views) +
                url +
                webframe.config.engine,
            type: type || 'get',
            data: qurys,
            dataType: 'html',
            success: function (r) {
                webframe.waitbar.close()
                var result;
                var isJson = false;
                try {
                    result = JSON.parse(r);
                    isJson = true;
                } catch (e) {
                    result = {'code': 'err'};
                }
                if (result.code === 401) {
                    webframe.alert.confirm('登录失效', '登录已失效，请重新登录',{iconCu:2,showSure:true}, function () {
                        window.location.reload();
                        window.location.hash = '';
                    });
                    return;
                }
                loadBar.finish();
                if(!isAjax) {
                    if (result.code === 403) {
                        if(webframe.viewTabs)
                            webframe.tab.change('/base/403');
                        else
                            webframe.navigate('/base/403')
                        return;
                    }
                    if (result.code === 404) {
                        if(webframe.viewTabs)
                            webframe.tab.change('/base/404');
                        else
                            webframe.navigate('/base/404')
                        return;
                    }
                    if (result.code === 500) {
                        if(webframe.viewTabs)
                            webframe.tab.change('/base/500');
                        else
                            webframe.navigate('/base/500')
                        return;
                    }
                    if (result.code !== 0 && result.code !== 200 && result.code !== "err") {
                        if ($.isFunction(callback)) {
                            callback({html: '请求视图文件异常<br>文件路径：' + url + '<br>状态：' + result.code, url: url});
                        }
                        return;
                    }
                }
                if (isJson && result.code === 600) {
                    if (result.message) {
                        var title = result.title || '温馨提示';
                        webframe.alert.confirm(title,result.message,{iconCu:2,showSure:true},function (){
                            if($.isFunction(errorback)) {
                                errorback(result);
                            }
                        })
                    }
                    return;
                }
                if($.isFunction(callback)) {
                    if(isJson){
                        callback({html: result, url: url});
                    }else {
                        callback({html: r, url: url});
                    }
                }
            },
            error: function (res) {
                webframe.waitbar.close()
                loadBar.error();
                if(!isAjax) {
                    if (res.status === 404) {
                        if(webframe.viewTabs)
                            webframe.tab.change('/base/404');
                        else
                            webframe.navigate('/base/404')
                        return;
                    }
                    if (res.status === 403) {
                        if(webframe.viewTabs)
                            webframe.tab.change('/base/403');
                        else
                            webframe.navigate('/base/403')
                        return;
                    }
                    if (res.status === 500) {
                        if ($.isFunction(callback)) {
                            callback({html: getErrorHtml(), url: url});
                        }
                        return;
                    }
                    if (res.code === 600) {
                        if (res.message) {
                            var title = res.title || '温馨提示';
                            webframe.alert.confirm(title,res.message,{iconCu:2,showSure:true},function (){
                                if($.isFunction(errorback)) {
                                    errorback(res);
                                }
                            })
                        }
                        return;
                    }
                }
                if($.isFunction(callback)) {
                    callback({html: '请求视图文件异常<br>文件路径：' + url + '<br>状态：' + res.status, url: url})
                }else {
                    webframe.log('请求视图文件异常\n文件路径：' + url + '\n状态：' + res.status);
                }
            }
        })
    };

    webframe.log = function (msg) {
        if (webframe.config.debug === false) return;
    };

    //解译内容
    webframe.parse = function (container) {
        if (container === undefined) container = webframe.containerBody;
        var template = container.get(0).tagName === 'SCRIPT' ? container : container.find('[template]');
        layui.each(template, function (index, t) {
            var tem = $(t),
                url = tem.attr('lay-url') || '',
                type = tem.attr('lay-type') || 'get',
                data = new Function('return ' + tem.attr('lay-data'))(),
                done = tem.attr('lay-done') || '';

            if (url) {
                if (url.indexOf("{profileid}")) {
                    if (webframe.config.currentUser && webframe.config.currentUser.id) {
                        url = url.replace("{profileid}", webframe.config.currentUser.id)
                        if (url.indexOf("{params}")) {
                            if (webframe.params) {
                                url = url.replace("{params}", webframe.params)
                            } else {
                                url = url.replace("{params}", "")
                            }
                        }
                        webframe.loadTemplate(tem, url, type, data, function (html) {
                            tem.after(html);
                            if (done) new Function(done)()
                        })
                    } else {
                        webframe.log("登录信息失效")
                    }
                } else {
                    webframe.loadTemplate(tem, url, type, data, function (html) {
                        tem.after(html);
                        if (done) new Function(done)()
                    })
                }
            } else {
                webframe.renderTemplate(
                    tem.html(),
                    data || {},
                    webframe.ie8
                        ? function (elem) {
                            if (elem[0] && elem[0].tagName !== 'LINK') return;
                            container.hide();
                            elem.load(function () {
                                container.show()
                            })
                        }
                        : function (html) {
                            tem.after(html)
                        }
                );
                if (done) new Function(done)()
            }
        })
    };

    webframe.updateBreadcrumb = function (title) {
        if (!webframe.viewTabs) {
            var elem = $(webframe.config.sidemenuElem).find('.layui-this').eq(0);
            if (elem.length > 0) {
                // 生成面包屑
                var bread = $(webframe.config.single_page_nav).find('.layui-breadcrumb')[0];
                if (typeof bread === "undefined") return;
                $(bread).empty();
                var breadHtml = '';
                elem.parents('dl').prev('a').each(function (k, v) {
                    if (v.innerText !== "") {
                        if($(v).attr('lay-href') !== ""){
                            // breadHtml += '<a href="#'+ $(v).attr('lay-href') + '">' + $(v).attr('title') + '</a>';
                            breadHtml = '<a class="single-page-a" urlHref="#'+ $(v).attr('lay-href') + '">' + $(v).attr('title') + '</a>' + breadHtml;
                        }else {
                            breadHtml = '<a style="cursor: default;">' + $(v).attr('title') + '</a>' + breadHtml;
                        }
                    }
                });
                elem.find('a').each(function (k,v){
                    if (v.innerText !== "") {
                        var url = $(v).attr('lay-href')
                        // if (url !== webframe.route.fileurl) {
                        if (url.indexOf(webframe.route.fileurl) < 0) {
                            // breadHtml += '<a href="#' + url + '">' + v.innerText + '</a>';
                            breadHtml += '<a class="single-page-a" urlHref="#' + url + '">' + v.innerText + '</a>';
                        }
                    }
                })
                if (typeof title != "undefined" && title !== "") {
                    if(title=='首页' || title=='系统首页' || title=='个人中心'){
                        breadHtml = '';
                    }
                    breadHtml += '<a style="cursor: default;">' + title + '</a>';
                }
                $(bread).append(breadHtml);
                element.render('breadcrumb');

                $(document).off('click', '.single-page-a');
                $(document).on('click', '.single-page-a', function (e) {
                    webframe.route = layui.router($(this).attr('urlHref'));
                    webframe.initView();

                })
            } else {
                var bread = $(webframe.config.single_page_nav).find('.layui-breadcrumb');
                if (typeof bread === "undefined") return;
                bread.css("visibility","visible");
            }
        }
    };

    webframe.tab = {
        isInit: false,
        data: [],
        tabMenuTplId: 'TPL-app-tabsmenu',
        minLeft: null,
        maxLeft: null,
        wrap: '.web-tabs-wrap',
        menu: '.web-tabs-menu',
        next: '.web-tabs-next',
        prev: '.web-tabs-prev',
        step: 200,
        init: function () {
            var tab = this;
            var btnCls = tab.wrap + ' .web-tabs-btn';
            //顶部tab点击
            $(document).on('click', btnCls, function (e) {
                var url = $(this).attr('lay-url');
                if ($(e.target).hasClass('web-tabs-close')) {
                    tab.del(url)
                } else {
                    var type = $(this).attr('data-type');
                    if (type === 'page') {
                        var i = null;
                        webframe.tab.data.forEach(function (cur,idx,arr){
                            if(cur.fileurl==url){
                                i = idx;
                            }
                        })
                        webframe.route = webframe.tab.data[i];
                        tab.change(tab.has(url));
                        webframe.event(webframe.route.href);
                    } else if (type === 'prev' || type === 'next') {
                        tab.menuElem = $(tab.menu);
                        var menu = tab.menuElem;
                        tab.minLeft = tab.minLeft || parseInt(menu.css('left'));
                        tab.maxLeft = tab.maxLeft || $(tab.next).offset().left;

                        var left = 0;
                        if (type === 'prev') {
                            left = parseInt(menu.css('left')) + tab.step;
                            if (left >= tab.minLeft) left = tab.minLeft
                        } else {
                            left = parseInt(menu.css('left')) - tab.step;
                            var last = menu.find('li:last');
                            if (last.offset().left + last.width() < tab.maxLeft) return
                        }
                        menu.css('left', left)
                    }
                }
            });
            $('.web-tabs-hidden').addClass('layui-show');
            this.isInit = true
        },
        //通过url获取this.data上的对象
        has: function (url) {
            var exists = false;
            layui.each(this.data, function (i, data) {
                if (data.fileurl === url) return (exists = data)
            });
            return exists;
        },
        delAll: function (type) {
            var tab = this,
                menuBtnClas = tab.menu + ' .web-tabs-btn';
            $(menuBtnClas).each(function () {
                var url = $(this).attr('lay-url');
                if (url === webframe.config.entry) return true;
                tab.del(url)
            })
        },
        delOther: function () {
            var tab = this,
                menuBtnClas = tab.menu + ' .web-tabs-btn';
            $(menuBtnClas + '.web-tabs-active')
                .siblings()
                .each(function () {
                    if ($(this).attr('lay-url') === webframe.config.entry) return true;
                    tab.del($(this).attr('lay-url'))
                })
        },
        //点击右边删除按钮
        del: function (url, backgroundDel) {
            var tab = this;
            if (tab.data.length <= 1 && backgroundDel === undefined) return;
            layui.each(tab.data, function (i, data) {
                if (data.fileurl === url) {
                    tab.data.splice(i, 1);
                    return true
                }
            });
            var lay = '[lay-url="' + url + '"]',
                thisBody = $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay),
                thisMenu = $(this.menu).find(lay);
            thisMenu.remove();
            thisBody.remove();

            if (backgroundDel === undefined) {
                if (thisMenu.hasClass('web-tabs-active')) {
                    $(this.menu + ' li:last').click();
                    // webframe.navigate($(this.menu + ' li:last').attr('lay-url'));
                }
            }
        },
        delEdit: function () {
            var tab = this;
            if (tab.data.length <= 1 ) return;
            layui.each(tab.data, function (i, data) {
                if(data.module === "editview"){
                    tab.data.splice(i, 1);
                    var lay = '[lay-url="' + data.fileurl + '"]',
                        thisBody = $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay),
                        thisMenu = $(tab.menu).find(lay);
                    thisMenu.remove();
                    thisBody.remove();
                    return true
                }
            });
        },
        refresh: function (url) {
            url = url || webframe.route.fileurl;
            if (this.has(url)) {
                this.del(url, true);
                webframe.renderTabs(url)
            }
        },
        clear: function () {
            this.data = [];
            this.isInit = false;
            $(document).off('click', this.wrap + ' .web-tabs-btn')
        },
        change: function (route, callback) {
            if (typeof route == 'string') {
                route = layui.router('#' + route);
                route.fileurl = '/' + route.path.join('/')
            }
            var fileurl = route.fileurl,
                tab = this;
            if (tab.isInit === false) tab.init();
            var changeView = function (lay) {
                    $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay)
                        .show()
                        .siblings()
                        .hide()
                },
                lay = '[lay-url="' + fileurl + '"]',
                activeCls = 'web-tabs-active',
                existsTab = tab.has(fileurl);
            //判断是否存在
            if (existsTab) {
                var menu = $(this.menu);
                var currentMenu = menu.find(lay);
                if (existsTab.href !== route.href) {
                    tab.del(existsTab.fileurl, true);
                    tab.change(route);
                    return false;
                }
                currentMenu
                    .addClass(activeCls)
                    .siblings()
                    .removeClass(activeCls);

                changeView(lay);

                this.minLeft = this.minLeft || parseInt(menu.css('left'));

                var offsetLeft = currentMenu.offset().left;
                if (offsetLeft - this.minLeft - $(this.next).width() < 0) {
                    $(this.prev).click();
                } else if (offsetLeft - this.minLeft > menu.width() * 0.5) {
                    $(this.next).click();
                }
                $(document).scrollTop(-100);
                // webframe.navigate(route.href)
            } else {
                // route.search.record = '';
                webframe.getHtml('GET', fileurl, route.search || {}, function (res) {
                    var lay = '[lay-url="' + fileurl + '"]';
                    $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay).remove();
                    var htmlElem = $(
                        "<div><div class='web-tabs-item' lay-url='" +
                        fileurl +
                        "' >" +
                        res.html +
                        '</div></div>'
                    );
                    var params = webframe.fillHtml(fileurl, htmlElem, 'prepend');
                    route.title = params.title;
                    route.module = params.module;

                    //如果打开的是编辑页面，那么删除原有的编辑页面
                    if(route.module=='editview'){
                        webframe.tab.data.forEach(function (cur,idx,arr){
                            if(cur.module=='editview'){
                                tab.del(cur.fileurl);
                            }
                        })
                    }

                    if(route.module=='moduledesign'){
                        var delUrl;
                        webframe.tab.data.forEach(function (cur,idx,arr){
                            if(cur.module=='moduledesign'){
                                delUrl = cur.fileurl;
                            }
                        })
                        if(delUrl) tab.del(delUrl, true);
                    }

                    tab.data.push(route);
                    webframe.render('#' + tab.tabMenuTplId);
                    webframe.adjustLocotion();
                    layui.dropdown.render({
                        elem: '.web-tabs-down',
                        data: [
                                { name: 'current', title: '关闭当前选项卡'},
                                { name: 'other', title: '关闭其他选项卡'},
                                { name: 'all', title: '关闭所有选项卡'},
                                { name: 'refresh',title: '刷新当前选项卡'}
                            ],
                        click: function(data, othis){
                            var name = data.name;
                            if (name === 'all') { tab.delAll(); }
                            if (name === 'other') { tab.delOther(); }
                            if (name === 'current') { tab.del(webframe.route.fileurl); }
                            if (name === 'refresh') { tab.refresh(); }
                        }
                    })
                    var currentMenu = $(tab.menu + ' ' + lay);
                    currentMenu.addClass(activeCls);
                    changeView(lay);
                    if ($.isFunction(callback)) callback(params);
                })
            }
            if(route.fileurl === '/base/index'){
                webframe.sidebarFocus(route.href);
            }
            return false;
        },
        onChange: function () {
        },
    };

    webframe.navigate = function (url,editClick) {
        if (url === webframe.config.entry) url = '/';
        webframe.urlChange(url,editClick);
    };

    webframe.urlChange = function (url,editClick) {
        webframe.route = layui.router('#'+ url);
        webframe.route.fileurl = '/' + webframe.route.path.join('/');
        if(editClick){
            webframe.tab.delEdit();
        }
        layer.closeAll();
        webframe.initView();
        if (webframe.viewTabs === true) {
            webframe.event(webframe.route.href)
        }
    }

    //是否刷新页面
    // if(editClick){
    //     webframe.tab.del(fileurl,true);
    // }
    // webframe.refreshGetHtml = function (url, rClick){
    //     var i = null;
    //     webframe.tab.data.forEach(function (cur,idx,arr){
    //         if(cur.href==url){
    //             i = idx;
    //         }
    //     })
    //     if(i!=null && webframe.tab.data[i].ifRrfresh ){
    //         webframe.getHtml('GET', url, {}, function (res) {
    //             var lay = '[lay-url="' + url + '"]';
    //             $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay).remove();
    //             $(webframe.config.containerElemBody + ' > .web-tabs-item' + lay).find('.layui-table-tool-self').empty();
    //             var htmlElem = $(
    //                 "<div><div class='web-tabs-item' lay-url='" +
    //                 url +
    //                 "' >" +
    //                 res.html +
    //                 '</div></div>'
    //             );
    //             webframe.fillHtml(url, htmlElem, 'prepend');
    //         })
    //         if(i!=null && webframe.tab.data[i].ifRrfresh){ webframe.tab.data[i].ifRrfresh = false; }
    //     }
    // };

    // 需要刷新的页面保存状态   module为逗号隔开的字符串数组
    // webframe.refresh = function (module) {
    //     if(!module) return;
    //     var moduleArr = module.split(',');
    //     webframe.tab.data.map(function (cur,idx,arr){
    //         if(moduleArr.indexOf(cur.module)>=0){
    //             var ifRrfresh = true;
    //         }
    //         return cur.ifRrfresh = ifRrfresh;
    //     })
    // }

    //根据当前加载的 URL高亮左侧导航
    webframe.sidebarFocus = function (url) {
        // url = url || webframe.route.href;
        // var elem = $(webframe.config.sidemenuElem)
        //     .find('[lay-href="' + url + '"]')
        //     .eq(0);
        // if (elem.length > 0) {
        //     elem.parents('.layui-nav-item').addClass('layui-nav-itemed')
        //         .siblings().removeClass('layui-nav-itemed');
        //     // elem.click();
        //     elem.parents('dd').addClass('layui-this')
        //         .siblings().removeClass('layui-this');
        // }
        // $('#app-sidebar_tpl ul.layui-nav').find('li.layui-nav-item').eq(0).addClass('layui-nav-itemed');
    }

    webframe.renderTemplate = function (template, data, callback) {
        var checkHtml = function (htmlStr) {
            var reg = /<[^>]+>/g;
            return reg.test(htmlStr)
        };
        laytpl(template).render(data, function (html) {
            try {
                html = $(
                    checkHtml(html) ? html : '<span>' + html + '</span>'
                )
            } catch (err) {
                html = $('<span>' + html + '</span>')
            }
            html.attr('is-template', true);
            if ($.isFunction(callback)) callback(html)
        })
    }

    webframe.renderScript = function (template, data, callback) {
        this.renderTemplate(template,data,function (res) {
            var html = "";
            if(typeof res === "object"){
                for(var item in res){
                    if(res.hasOwnProperty(item)){
                        if(res[item].outerHTML) {
                            html += res[item].outerHTML;
                        }else if(res[item].textContent){
                            html += res[item].textContent;
                        }
                    }
                }
            }else if(typeof res === "string"){
                html = res;
            }
            if ($.isFunction(callback)) callback(html)
        });
    }
    webframe.loadTemplate = function (elem, url, ajaxtype, query, callback) {
        if (!url) return;
        ajaxtype = ajaxtype || 'GET';
        $.ajax({
            type: ajaxtype,
            url: url,
            data: query,
            success: function (res) {
                if (res.data) {
                    webframe.renderTemplate($(elem).html(), res.data, callback)
                } else {
                    webframe.log("加载模板失败，返回数据格式错误：" + url)
                }
            },
            error: function (request, err, other) {
                webframe.log("加载模板失败：" + url)
            }
        });
    }

    //判断是深色主题
    webframe.judgeTheme = function (){
        var i = '';
        if($('body').attr('id')&&$('body').attr('id')=='lyi-dark-body'){
            i = true;
        }else{
            i = false;
        }
        return i;
    }

    //左边菜单导航宽度随鼠标拖动
    webframe.wrapResize = function (resizeWrap,moveFun){
        $(resizeWrap).css('transition','none 0s ease 0s');
        var resizeDom = $('<div style="position: absolute;width: 10px;height: 100%;right: 0;top: 0;cursor: w-resize;" class="resize-line"></div>');
        $(resizeWrap).append(resizeDom);
        var ifMove = false;
        resizeDom.mousedown(function(e1){
            ifMove = true;
            $(document).mousemove(function(e2){
                if(ifMove){
                    var moveDis = e2.clientX;
                    if(Number(moveDis)<400&&Number(moveDis)>190){
                        $('body').css('cursor','w-resize');
                        $('body').css('user-select','none');
                        moveFun(moveDis);
                    }
                }
            });
        });
        $(document).mouseup(function(){
            ifMove = false;
            $('body').css('cursor','auto');
            $('body').css('user-select','auto');
        });
    }

    // 调整左边菜单宽度后，点击左边导航，调整tab切换按钮位置
    webframe.adjustLocotion = function (){
        var w = $('#app-sidebar').width();
        $('.web-tabs-wrap .web-tabs-prev').css({'left': w+'px'});
        $('.web-tabs-wrap .web-tabs-menu').css({'left': w+'px'});
    }

    webframe.loadScriptString = function (code,e){
        var script = document.createElement("script");
        script.type = "text/javascript";
        try {
            script.appendChild(document.createTextNode(code));
        } catch (ex) {
            script.text = code;
        }
        e.appendChild(script);
    }

    webframe.replaceMarks = function (){
        $('.replaceMarkInput').blur(function (){
            var v = $(this).val();
            $(this).val(v.replaceAll('"',''));
        })
    }

    // 打开左边第一项
    webframe.openFirstOne = function (url) {
        $('#app-sidebar_tpl ul.layui-nav').find('li.layui-nav-item').eq(0).addClass('layui-nav-itemed');
    }

    exports('webframe', webframe)
});
