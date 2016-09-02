angular.module('findFriend.service', [])
.factory('findFriend', function($http, $rootScope) {
var DeBug=1;
var baseUrl="";
if (DeBug==1) {
    baseUrl="http://localhost:8080/slyimweb/";
} else {
    baseUrl="http://slyim.com/";
}
    var friends = [];
    var delFriendMsg=function(data) {
        var friendReqListStr=localStorage.getItem("friendReqList");
        var friendReqList=angular.fromJson(friendReqListStr); 
        if (friendReqList==null || friendReqList=="") {
            return;
        }
        if (friendReqList[data.friendId]!=null) {
            delete friendReqList[data.friendId];
        }
        friendReqListStr=angular.toJson(friendReqList);
        localStorage.setItem("friendReqList", friendReqListStr);
    };
    var addFriendToAddress=function(data){
        var myAddressStr=localStorage.getItem("myAddress");
        if (myAddressStr==null || myAddressStr=="") {
            myAddressStr="{}";
        }
        var myAddress=angular.fromJson(myAddressStr);
        myAddress[data.friendId]=data;
        myAddressStr=angular.toJson(myAddress);
        localStorage.setItem("myAddress", myAddressStr);
    }
 return {
    getFriendByPhone: function(phone,resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"user/searchFriendByPhone/",{params: {phone:phone}})
        .success(function(data, status, headers, config) {
            friends=data.res;
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getFriendByName: function(name,resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"user/searchFriendByName/",{params: {name:name}})
        .success(function(data, status, headers, config) {
            friends=data.res;
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    getFriends: function(){
        return friends;
    },
    addFriendInfo: function(data,resfun) {
        resfun = resfun || function(){};
        $http.post(baseUrl+"user/addFriendInfo/",
            {
                phone:data.phone,
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
    },
    addFriend: function(friendId,commit,resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"address/addFriend/",{params: {friendId:friendId,commit:commit}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
     blackFriendToData:function(data){
        //console.log("Into addFriendMsg");
        var blackFriendStr=localStorage.getItem("blackFriendList");
        if (blackFriendStr==null || blackFriendStr=="") {
            blackFriendStr="{}";
        }
        var blackFriendList=angular.fromJson(blackFriendStr);
        for (var i=0;i<blackFriendList.length;i++) {
            if (data.friendId==blackFriendList[i].friendId) {
                return;
            }
        }
         blackFriendList.push(data);
         localStorage.setItem("blackFriendList",angular.toJson(blackFriendList));
        //console.log(friendReqList);
    },
     addFriendMsg:function(data){
        //console.log("Into addFriendMsg");
        var friendReqListStr=localStorage.getItem("friendReqList");
        var friendReqList=angular.fromJson(friendReqListStr);
        var addressData=angular.fromJson(data);
        if (addressData.state==0) {
            if (friendReqListStr==null || friendReqListStr=="") {
                friendReqList={};
                friendReqList[addressData.friendId]=addressData;
            } else {
                friendReqList[addressData.friendId]=addressData;
            }
            friendReqListStr=angular.toJson(friendReqList);
            localStorage.setItem("friendReqList", friendReqListStr); 
        }
        
        //console.log(friendReqList);
    },
    receiveFriendReq : function(data, resfun){
            //console.log(data);
            resfun = resfun || function(){};
            data=angular.toJson(data);
            $rootScope.ws.send(angular.toJson({"sockType":"AddFriend", "jsonData":data}));
            resfun(data);
    },
    blackFriend: function(friendId, resfun) {
        resfun = resfun || function(){};
        $http.get(baseUrl+"address/blackFriend/",{params: {friendId:friendId}})
        .success(function(data, status, headers, config) {
            resfun(data,status);
        })
        .error(function(data, status, headers, config) {
            resfun(data,status);
        });
    },
    dealFriendMsg : function(data) {
            if (data.state==1) {
                delFriendMsg(data);
                addFriendToAddress(data);
            } else if (data.state==2) {
                delFriendMsg(data);
            }
    }
 };
});