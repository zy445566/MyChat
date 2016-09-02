angular.module('userSetting.service', [])
.factory('userSetting', function($http) {
    var DeBug=1;
    var baseUrl="";
    if (DeBug==1) {
        baseUrl="http://localhost:8080/slyimweb/";
    } else {
        baseUrl="http://slyim.com/";
    }
    var isLocalData=function(){
                var userData={};
                userData.id=localStorage.getItem('id');
                userData.phone=localStorage.getItem('phone');
                userData.password=localStorage.getItem('password');
                if (
                    userData.id==null || userData.id=="" || 
                    userData.phone==null || userData.phone=="" || 
                    userData.password==null || userData.password=="" 
                ) 
                {
                    return false;
                } else {
                    return true;
                }
            };
    return {
        setUserInfo: function(userData) {
        localStorage.setItem('id', userData.id);
        localStorage.setItem('phone', userData.phone);
        localStorage.setItem('password', userData.password);
        localStorage.setItem('sign', userData.sign);
        localStorage.setItem('avatar', userData.avatar);
        localStorage.setItem('sex', userData.sex);
        localStorage.setItem('name', userData.name);
        localStorage.setItem('strong', userData.strong);
        localStorage.setItem('rent', userData.rent);
        localStorage.setItem('localtion', userData.localtion);
        localStorage.setItem('modifyTime', userData.modifyTime);
        localStorage.setItem('vipScore', userData.vipScore);
        localStorage.setItem('vipDeadLine', userData.vipDeadLine);
        localStorage.setItem('vipRefreshTime', userData.vipRefreshTime);
        },
        getUserInfo: function() {
            var userData={};
            userData.id=localStorage.getItem('id');
            userData.phone=localStorage.getItem('phone');
            userData.password=localStorage.getItem('password');
            userData.sign=localStorage.getItem('sign');
            userData.avatar=localStorage.getItem('avatar');
            userData.sex=localStorage.getItem('sex');
            userData.name=localStorage.getItem('name');
            userData.strong=localStorage.getItem('strong');
            userData.rent=localStorage.getItem('rent');
            userData.localtion=localStorage.getItem('localtion');
            userData.modifyTime=localStorage.getItem('modifyTime');
            userData.vipScore=localStorage.getItem('vipScore');
            userData.vipDeadLine=localStorage.getItem('vipDeadLine');
            userData.vipRefreshTime=localStorage.getItem('vipRefreshTime');
            var timestamp = parseInt(new Date().getTime()/1000);
            if (timestamp<userData.vipDeadLine) {
                while (timestamp-userData.vipRefreshTime>=24*3600) {
                    userData.vipRefreshTime+=24*3600;
                    userData.vipScore+=10;
                }
            } else {
                while (userData.vipDeadLine-userData.vipRefreshTime>=24*3600) {
                    userData.vipRefreshTime+=24*3600;
                    userData.vipScore+=10;
                }
                if (userData.vipScore>0) {
                    while (timestamp-userData.vipRefreshTime>=24*3600) {
                        userData.vipRefreshTime+=24*3600;
                        if (userData.vipScore-5>0) {
                            userData.vipScore-=5;
                        } else {
                            userData.vipScore=0;
                        }
                    }
                }
                
            }
            localStorage.setItem('vipRefreshTime', userData.vipRefreshTime);
            localStorage.setItem('vipScore', userData.vipScore);
            return userData;
        },
        isWelcome: function() {
            var welcome=localStorage.getItem('welcome');
            if (welcome==1) {
                location.href="getAccount.html";
            } else {
                location.href="index.html#welcome";
            }
        },
        isExistLocal: function() {
            return isLocalData();
        },
        initUserData: function(initfun) {
            initfun = initfun || function(){};
            var getIsLogin=function(resfun){
                resfun = resfun || function(){};
                $http.get(baseUrl+"user/isLogin/")
                .success(function(data, status, headers, config) {
                    resfun(data,status);
                })
                .error(function(data, status, headers, config) {
                    resfun(data,status);
                });
            };
            if (isLocalData()) {
                getIsLogin(function(data,status){
                    if (status!=200) {
                        initfun(false);
                        return;
                    }
                    if (data.code==0) {
                        initfun(true);
                        return;
                    } else {
                        initfun(false);
                        return;
                    }
                });
            } else {
                initfun(false);
                return;
            }
        }

    };
});