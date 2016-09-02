angular.module('cookieHandle.service', [])
.factory('CookieHandle', function($http) {
    return {
        getCookie: function(name) {
            var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr=document.cookie.match(reg)){
                return unescape(arr[2]);
            } else {
               return null; 
            }
        },
        setCookie: function(name, value, expire) {
            var exp = new Date();
            exp.setTime(exp.getTime() + expire);
            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toUTCString();
        },
        delCookie: function(name) {
            var exp = new Date();
            exp.setTime(exp.getTime() - 1);
            if(cval!=null){
                document.cookie= name + "="+0+";expires="+exp.toUTCString();
            }
        },
    }
});