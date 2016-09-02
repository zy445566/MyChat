angular.module('myChat.service', [])
.factory('Chats', function($http, $rootScope, userSetting) {
var DeBug=1;
var baseUrl="";
if (DeBug==1) {
    baseUrl="http://localhost:8080/slyimweb/";
} else {
    baseUrl="http://slyim.com/";
}
    var chats = {};
    var chatDetail={};
    var addressTitle={};
    var addressTitleArray=[];
    var addressUser=[];
    var myAddress={};
    var addressTitle={};
    var user={};
    var dyCircle=[];
    var pushChatDetail=function(data, type){
        var chatDetailOne={};
        if (type=="sendMsg") {
            chatDetailOne.showRight=true;
            chatDetailOne.showLeft=false;
            chatDetailOne.avatar=user.avatar;
        } else if (type=="acceptMsg") {
            chatDetailOne.showRight=false;
            chatDetailOne.showLeft=true;
            chatDetailOne.avatar=myAddress[data.friendId].friendAvatar;
        } else {
            return;
        }
        chatDetailOne.type=data.type;
        chatDetailOne.content=data.content;
        var date=new Date(data.sendTime*1000); 
        var dateStr=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+
            " "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        chatDetailOne.date=dateStr;
        if (chatDetail[data.friendId]==null) {
            chatDetail[data.friendId]=new Array();
        }
        chatDetail[data.friendId].push(chatDetailOne);
        localStorage.setItem("chatDetail", angular.toJson(chatDetail));
    };
    var intoCircle=function(data){
        localStorage.setItem("refreshCircleTime", data.sendTime);
        if (dyCircle.length==0) {
            dyCircle.push(data);
        } else {
            dyCircle.splice(0,0,data);
        }
    };
    return {
        myUser: function() {
            user=userSetting.getUserInfo();
            return user;
        },
        all: function() {
            var chatsStr=localStorage.getItem("chats");
            chats=angular.fromJson(chatsStr);
            if (chatsStr==null) {
                chats={};
            }
            return chats;
        },
        allChatDetail: function() {
            var chatDetailStr=localStorage.getItem("chatDetail");
            chatDetail=angular.fromJson(chatDetailStr);
            if (chatDetailStr==null) {
                chatDetail={};
            }
            return chatDetail;
        },
        allMyAddress: function() {
            var myAddressStr=localStorage.getItem("myAddress");
            myAddress=angular.fromJson(myAddressStr);
            if (myAddressStr==null) {
                myAddress={};
            }   
            return myAddress;
        },
        allCircle: function() {
            var dyCircleStr=localStorage.getItem("dyCircle");
            dyCircle=angular.fromJson(dyCircleStr);
            if (dyCircleStr==null) {
                dyCircle=[];
            }
            return dyCircle;
        },
        getchatDetail: function(friendId) {
//            chatDetail={};
//            localStorage.setItem("chatDetail",angular.toJson(chatDetail));
            console.log(chatDetail);
            if (chatDetail[friendId]==null) {
                chatDetail[friendId]=[];
            }
            return chatDetail[friendId];
        },
        getChatNum: function() {
            var chatNum=0;
            for (friendId in chats) {
                chatNum+=chats[friendId].num;
            }
            return chatNum;
        },
        cleanChatNum: function(friendId) {
            chats[friendId].num=0;
            var chatsStr=angular.toJson(chats);
            localStorage.setItem("chats", chatsStr);
        },
        remove: function(chat) {
            delete chats[chat.friendId];
            var chatsStr=angular.toJson(chats);
            localStorage.setItem("chats", chatsStr);
        },
        get: function(chatId) {
            return myAddress[chatId];
        },
        dealAddressInfo: function() {
        var hash = {};
        for (var objKey in myAddress) {
            var p=/^(.{1}).*?$/;
            var item = myAddress[objKey];
            var res = item.friendName.match(p);
            var key =res[1];
            if (hash[key] !== 1) {
              addressTitle[key]=[];
              addressTitle[key].push(item);
              addressTitleArray.push(key);
              hash[key] = 1;
            } else {
                addressTitle[key].push(item);
            }
        }
        addressTitleArray.sort();
//        console.log(addressTitleArray);
//        console.log(addressTitle);
        },
        getAddressTitleArray: function() {
            return addressTitleArray;
        },
        getAddressTitle: function() {
            return addressTitle;
        },
        sendMsg : function(data, resfun){
                //console.log(data);
                resfun = resfun || function(){};
                var dataStr=angular.toJson(data);
                $rootScope.ws.send(angular.toJson({"sockType":"SendMsg", "jsonData":dataStr}));
                var chatsStr=localStorage.getItem("chats");
                if (chatsStr==null || chats[data.friendId]==null) {
                   var chat = {
                        friendId: data.friendId,
                        name: myAddress[data.friendId].friendName,
                        lastText: data.content,
                        avatar: myAddress[data.friendId].friendAvatar,
                        num: 0
                    };
                } else {
                    var chat = {
                        friendId: data.friendId,
                        name: myAddress[data.friendId].friendName,
                        lastText: data.content,
                        avatar: myAddress[data.friendId].friendAvatar,
                        num: chats[data.friendId].num
                    };
                }
                if (chats==null) {
                    chats={};
                }
                chats[data.friendId]=chat;
                chatsStr=angular.toJson(chats);
                localStorage.setItem("chats", chatsStr);
                pushChatDetail(data,"sendMsg");
                resfun(dataStr);
        },
        acceptMsg : function(dataStr, resfun){
            resfun = resfun || function(){};
            var data=angular.fromJson(dataStr);
            var chatsStr=localStorage.getItem("chats");
            if (chatsStr==null || chats[data.friendId]==null) {
               var chat = {
                    friendId: data.friendId,
                    name: myAddress[data.friendId].friendName,
                    lastText: data.content,
                    avatar: myAddress[data.friendId].friendAvatar,
                    num: 1
                };
            } else {
                var chat = {
                    friendId: data.friendId,
                    name: myAddress[data.friendId].friendName,
                    lastText: data.content,
                    avatar: myAddress[data.friendId].friendAvatar,
                    num: chats[data.friendId].num+1
                };
            }
            if (chats==null) {
                chats={};
            }
            chats[data.friendId]=chat;
            chatsStr=angular.toJson(chats);
            localStorage.setItem("chats", chatsStr);
            pushChatDetail(data,"acceptMsg");
            resfun(chat)
        },
        getCircle: function(resfun) {
            resfun = resfun || function(){};
            var sendTime=localStorage.getItem("refreshCircleTime");
            if (sendTime==null) {
                sendTime=0;
            }
            $http.get(baseUrl+"dynamicCircle/getCircle/",{params: {sendTime:sendTime}})
            .success(function(data, status, headers, config) {
                resfun(data,status);
            })
            .error(function(data, status, headers, config) {
                resfun(data,status);
            });
        },
        sendCircle: function(data,resfun) {
            resfun = resfun || function(){};
            $http.post(baseUrl+"dynamicCircle/sendCircle/",
                {
                    content:data.content,
                    type:data.type,
                    sender:data.sender,
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
        addCircle: function(data) {
            if (data instanceof Array) {
                while(data.length>0) {
                    intoCircle(data.pop());
                }
            } else {
                intoCircle(data);
            }
            localStorage.setItem("dyCircle",angular.toJson(dyCircle));
            //console.log(angular.fromJson(localStorage.getItem("dyCircle")));
        }
    };
});