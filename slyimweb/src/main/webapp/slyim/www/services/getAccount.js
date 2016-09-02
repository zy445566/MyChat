angular.module('getAccount.service', [])
.factory('Account', function($http) {
var DeBug=1;
var baseUrl="";
if (DeBug==1) {
    baseUrl="http://localhost:8080/slyimweb/";
} else {
    baseUrl="http://slyim.com/";
}

  return {
    getCordova: function() {
        var isOpenCordova=false;
        if(window.cordova && window.cordova.plugins){
            isOpenCordova=true;
        }
        return isOpenCordova;
    },
    sendRegSms: function(phonenum,resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"user/getRegSms/",{params: {phone:phonenum}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getAddressList: function(resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"address/getAddressList/")
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    sendRegin: function(data, resfun) {
        resfun = resfun || function(){};
        $http.post(baseUrl+"user/regin/",
            {
                phone:data.phone,
                password:data.password,
                avatar:data.avatar,
                sex:data.sex,
                name:data.name,
                strong:data.strong,
                rent:data.rent,
                localtion:data.localtion
            }
        )
        .success(function(data, status, headers, config) {
              resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    checkCode: function(password, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/reginBefore/",{params: {password:password}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    checkOldPhone: function(oldphone, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/checkOldPhone/",{params: {oldphone:oldphone}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getOldPhoneSms: function(oldphone, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/getOldPhoneSms/",{params: {oldphone:oldphone}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    checkOldCode: function(oldpassword, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/checkOldCode/",{params: {oldpassword:oldpassword}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getNewPhoneSms: function(newphone, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/getNewPhoneSms/",{params: {newphone:newphone}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    changePhone: function(oldpassword,newpassword, resfun) {
        resfun = resfun || function(){};
        //$rootScope.password=password;
        $http.get(baseUrl+"user/changePhone/",{params: {newpassword:newpassword,oldpassword:oldpassword}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
            //$rootScope.backcode=response;
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    }
  };
});