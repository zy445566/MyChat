angular.module('myIntro.service', [])
.factory('myIntro', function($http) {
var DeBug=1;
var baseUrl="";
if (DeBug==1) {
    baseUrl="http://localhost:8080/slyimweb/";
} else {
    baseUrl="http://slyim.com/";
}

 return {
     getServerTime: function(resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"user/getServerTime/")
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getUserById: function(id,resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"user/getUserById/",{params: {id:id}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    upUserInfo: function(data,resfun) {
        resfun = resfun || function(){};
        $http.post(baseUrl+"user/upUserInfo/",
            {
                id:data.id,
                avatar:data.avatar,
                sign:data.sign,
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
    }
 }
 
});