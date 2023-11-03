layui.define(function(exports){
    var Base64 = {
        _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

        isbase64 : function (str) {
            var notBase64 = /[^A-Z0-9+\/=]/i;
            var len = str.length;
            if (!len || len % 4 !== 0 || notBase64.test(str)) {
                return false;
            }
            var firstPaddingChar = str.indexOf('=');
            return firstPaddingChar === -1 ||
                firstPaddingChar === len - 1 ||
                (firstPaddingChar === len - 2 && str[len - 1] === '=');
        },

        encode : function (input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = this._utf8_encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output + Base64._keyStr.charAt(enc1) +
                    Base64._keyStr.charAt(enc2) +
                    Base64._keyStr.charAt(enc3) +
                    Base64._keyStr.charAt(enc4);
            }
            return output;
        },

        decode : function (input) {
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;
            input = input.replace(/[^A-Za-z0-9+/=]/g, "");
            while (i < input.length) {
                enc1 = Base64._keyStr.indexOf(input.charAt(i++));
                enc2 = Base64._keyStr.indexOf(input.charAt(i++));
                enc3 = Base64._keyStr.indexOf(input.charAt(i++));
                enc4 = Base64._keyStr.indexOf(input.charAt(i++));
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
                output = output + String.fromCharCode(chr1);
                if (enc3 !== 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 !== 64) {
                    output = output + String.fromCharCode(chr3);
                }
            }
            output = this._utf8_decode(output);
            return output;
        },

        _utf8_encode : function (str) {
            str = str.replace(/\r\n/g, "\n");
            var utfText = "";
            for (var n = 0; n < str.length; n++) {
                var c = str.charCodeAt(n);
                if (c < 128) {
                    utfText += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utfText += String.fromCharCode((c >> 6) | 192);
                    utfText += String.fromCharCode((c & 63) | 128);
                } else {
                    utfText += String.fromCharCode((c >> 12) | 224);
                    utfText += String.fromCharCode(((c >> 6) & 63) | 128);
                    utfText += String.fromCharCode((c & 63) | 128);
                }
            }
            return utfText;
        },

        _utf8_decode : function (utfText) {
            var str = "";
            var i = 0;
            var c = 0;
            var c2 = 0;
            var c3 = 0;
            while (i < utfText.length) {
                c = utfText.charCodeAt(i);
                if (c < 128) {
                    str += String.fromCharCode(c);
                    i++;
                } else if ((c > 191) && (c < 224)) {
                    c2 = utfText.charCodeAt(i + 1);
                    str += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                } else {
                    c2 = utfText.charCodeAt(i + 1);
                    c3 = utfText.charCodeAt(i + 2);
                    str += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }
            }
            return str;
        }
    }

    exports('Base64',Base64);

})

