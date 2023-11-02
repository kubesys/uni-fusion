layui.define(['jquery', 'form'], function (E) {
    var $ = layui.jquery, a = "inputSelect", b = 'layui-form', c = $('.' + b + ' .' + a), e = layui.form,
        U = function () {
            this.config = {name: a, author: 'yms', qq: '979719091', version: 'v1.0',}
        };
    U.prototype.render = function () {
        var s = function (I, o) {
            var y = o.find('select'), g = y.attr('lay-data'), z = y.attr('name'), Z = y.attr('lay-verify'),
                t = g != undefined,
                D = t ? eval('(' + g + ')') : {}, s = '无匹配项',
                p = '.layui-anim.layui-anim-upbit', x = 'lay-filter', f = o.attr(x), h = function (v) {
                    t && l(v), k(v)
                }, i = function (r, t) {
                    var d = 'layui-select-none', e = o.find(p);
                    e.find('.' + d).remove(), !r && e.append('<p class="' + d + '">' + t + '</p>')
                }, j = function (v) {
                    var d = 'layui-unselect', e = o.find('input');
                    f = f == undefined ? ('input-select' + I) : f, o.attr(x, f), o.find('.' + d).removeClass(d), e.attr('name', z), y.removeAttr('name'), e.attr('lay-verify', Z), y.removeAttr('lay-verify'), e.removeAttr('readonly'), !t && n(), e.on('input', function () {
                        h($(this).val())
                    }), v != undefined && e.val(v) && e.trigger('click')
                }, k = function (v) {
                    var r = false;
                    o.find('dd').each(function () {
                        var d = $(this), e = d.html();
                        ((e.length > 0) && (e.indexOf(v) >= 0)) ? (d.removeClass(), r = true) : d.addClass('layui-hide')
                    }), i(r, s)
                }, l = function (v) {
                    if (v.length == 0) return;
                    D.method = (D.method.toUpperCase() == 'get') ? 'get' : 'post';
                    if ((D.url == undefined) || (typeof D.url.trim() === '')) throw new Error('url参数缺失');
                    if ((D.field == undefined) || (typeof D.field.trim() === '')) throw new Error('field参数缺失');
                    $.ajax({
                        url: D.url, type: D.method, dataType: 'json', data: {[D.field]: v}, success: function (d) {
                            ((d.status != 200) || (d.data.length == 0)) ? i(false, s) : (m(d.data), j(v), n()), k(v)
                        }
                    })
                }, m = function (d) {
                    var a = o.find('select'), b = '<option value=""></option>';
                    for (var i = 0; i < d.length; i++) {
                        b += '<option value="' + d[i].value + '">' + d[i].name + '</option>'
                    }
                    a.html(b), e.render('select', f)
                }, n = function () {
                    o.find(p).find('dd').on('click', function () {
                        var i = o.find('input'), k = o.find('select');
                        k.attr('name', z), i.removeAttr('name'),k.attr('lay-verify', Z), i.removeAttr('lay-verify');
                    });
                };
            j();
        }

        c.each(function (i, t) {
            s(i, $(t))
        })
    };
    var u = new U();
    u.render(), E(a, u)
});