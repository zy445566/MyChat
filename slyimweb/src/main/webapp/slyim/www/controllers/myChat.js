angular.module('myChat.controller', [])
.controller('TabCtrl', function($scope, $rootScope, $ionicPlatform, $ionicLoading, $ionicScrollDelegate, userSetting, Chats, mySocket, findFriend){
    $scope.actionList={};
    $scope.footBadge={};
    $scope.myChat={};
    $scope.footBadge.chatNum=0;
    $scope.footBadge.addressNum=0;
    $scope.footBadge.nearNum=0;
    $scope.actionList.addFriendBageShow=false;
    $ionicLoading.show({
                template: '载入系统中...'
                
    });
    userSetting.initUserData(function(isLogin){
        $ionicLoading.hide();
        if (!isLogin) {
            userSetting.isWelcome();
        }
    });
    $ionicPlatform.onHardwareBackButton(function(){
        var myPopup = $ionicPopup.show({
         template: '确认要退出吗？',
         title: '是否推出',
         scope: $scope,
         buttons: [
           { text: '取消' },
           {
             text: '<b>确认</b>',
             type: 'button-positive',
             onTap: function(e) {
                navigator.app.exitApp();
             }
           },
         ]
       });
    });
    mySocket.makeWS();
    $rootScope.ws.onmessage = function (event) {
        //console.log(event.data);
        var socketObj=angular.fromJson(event.data);
        if (socketObj.sockType==null || socketObj.sockType=="") {
            return;
        }
        if (socketObj.sockType=="AddFriend") {
            findFriend.addFriendMsg(socketObj.jsonData);
            $scope.$apply(function() {
                $scope.addFriendBage();
            });
        } else if (socketObj.sockType=="SendMsg") {
            Chats.acceptMsg(socketObj.jsonData, function(chat){
                $scope.myChat.chats = Chats.all();
                $scope.footBadge.chatNum = Chats.getChatNum();
                $scope.$digest();
                $ionicScrollDelegate.$getByHandle('myChatDetailList').scrollBottom();
            });
        }
    };
    Chats.myUser();
    $scope.myChat.chats = Chats.all();
    Chats.allChatDetail();
    Chats.allMyAddress();
    $scope.myChat.dyCircle = Chats.allCircle();
    $scope.footBadge.chatNum = Chats.getChatNum();
    $scope.addFriendBage=function(){
        var friendReqListStr=localStorage.getItem("friendReqList");
        if (friendReqListStr==null || friendReqListStr=="") {
            var friendReqList={};
        } else {
            var friendReqList=angular.fromJson(friendReqListStr);
        }
        var friendReqLen=0;
        for (var property in friendReqList) {
            friendReqLen++;
        }
        if (friendReqLen>0) {
            $scope.footBadge.addressNum+=friendReqLen;
            $scope.actionList.addFriendBageNum=friendReqLen;
            $scope.actionList.addFriendBageShow=true;
        }
    };
    $scope.addFriendBage();
    $scope.openMyInfo=function(){
        var id=localStorage.getItem('id');
        location.href="myIntro.html#/myInfo/"+id;
    }
    $scope.myAddress={};
    //---------------Address----------------
    Chats.dealAddressInfo();
    $scope.myAddress.addressTitleArray=Chats.getAddressTitleArray();
    $scope.myAddress.addressTitle=Chats.getAddressTitle();
    //-----------------Circle-----------------------
    Chats.getCircle(function(res, stat){
            if (res.code==0) {
                Chats.addCircle(res.res);
            } 
    });
})
.controller('ChatCtrl', function($scope, Chats){
    $scope.remove = function(chat) {
        Chats.remove(chat);
        $scope.myChat.chats = Chats.all();
        $scope.footBadge.chatNum = Chats.getChatNum();
    };
    $scope.intoChat =function(friendId){
        Chats.cleanChatNum(friendId);
        $scope.myChat.chats = Chats.all();
        $scope.footBadge.chatNum = Chats.getChatNum();
//        $scope.$digest();
        location.href="#/tab/chatdetail/"+friendId;
    }
})
.controller('ChatDetailCtrl', function($scope, $stateParams, $ionicScrollDelegate, $timeout,  Chats){
    $scope.mysay={};
    $scope.chat = Chats.get($stateParams.chatId);
    $scope.myChat.chatList=Chats.getchatDetail($scope.chat.friendId);
    $timeout(function(){
        $ionicScrollDelegate.$getByHandle('myChatDetailList').scrollBottom();
    },100);
//    console.log($scope.chat);
    $scope.sendMsg = function(){
        var chatData={};
        chatData.type="text";
        chatData.content=$scope.mysay.data;
        chatData.friendId=$scope.chat.friendId;
        chatData.sendTime=parseInt((new Date().getTime())/1000);
        //console.log(chatData);
        Chats.sendMsg(chatData, function(data) {
            $ionicScrollDelegate.$getByHandle('myChatDetailList').scrollBottom();
            $scope.mysay.data="";
        });
    }
    

})
.controller('AddressCtrl', function($scope, $ionicPopup, $location, $ionicScrollDelegate, Chats){
    $scope.openGoToTitle = function() {
        var myPopup = $ionicPopup.show({
         template: '<button class="button button-positive" ng-repeat="addressTitle in myAddress.addressTitleArray" ng-click="gotoAddressTitle(addressTitle)"><span ng-bind="addressTitle"></span></button>',
         title: '选择跳转位置',
         subTitle: '选择对应字符可跳转到对应位置',
         scope: $scope,
         buttons: [
            {
             text: '返回顶端',
             type: 'button-positive',
             onTap: function(e) {
                 $location.hash("");
                 $anchorScroll();
             }
           },
           { text: '取消' },
         ]
       });
    }
    $scope.gotoAddressTitle=function(locationName) {
        var toLocalName="addressDivider"+locationName;
        //console.log(toLocalName);
        $location.hash(toLocalName);
//        $anchorScroll();
        $ionicScrollDelegate.anchorScroll();
    }
})
.controller('NearCtrl', function($scope, $rootScope, $ionicPopup, Chats, userSetting){
    $scope.doRefresh=function(){
        Chats.getCircle(function(res, stat){
            if (stat!=200) {
                $ionicPopup.alert({
                   title: '更新失败',
                   template: '无法连接服务器...'
                 });
            }
            if (res.code==0) {
                //console.log(res.res);
                Chats.addCircle(res.res);
            } else {
                $ionicPopup.alert({
                   title: '更新失败',
                   template: '已是最新，无需更新...'
                 });
            }
            $scope.$broadcast('scroll.refreshComplete');
        })
    };
    if ($rootScope.localtion==null) {
        $rootScope.localtion={};
    }
    $scope.showPosition =function(position){
        $rootScope.localtion.longitude=position.coords.longitude;
        $rootScope.localtion.latitude=position.coords.latitude;
        console.info($rootScope.localtion);
    };
    if (navigator.geolocation)
        {
            navigator.geolocation.getCurrentPosition($scope.showPosition);
        } else {
            $rootScope.localtion.longitude=1000.00;
            $rootScope.localtion.latitude=1000.00;
            console.info($scope.localtion);
        }
    $scope.sendCircle=function(){
        var circleData={};
        var user=userSetting.getUserInfo();
        var sender={};
        var date=new Date();
        var timeStamp=parseInt(date.getTime()/1000);
        sender.id=user.id;
        sender.name=user.name;
        sender.avatar=user.avatar;
        circleData.content=$scope.sendCircle.content;
        $scope.sendCircle.content="";
        circleData.type="text";
        circleData.sender=sender;
        circleData.localtion=$rootScope.localtion;
        circleData.sendTime=timeStamp;
        //console.log(circleData);
        Chats.sendCircle(circleData,function(res, stat){
            if (stat!=200) {
                $ionicPopup.alert({
                   title: '发送失败',
                   template: '发送圈子失败...'
                 });
                return;
            }
            if (res.code==0) {
                Chats.addCircle(res.res);
            } else {
                $ionicPopup.alert({
                   title: '发送失败',
                   template: res.msg
                 });
                return;
            }
        });
    };
});