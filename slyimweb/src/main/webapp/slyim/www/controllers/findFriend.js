angular.module('findFriend.controller', [])
.controller('searchCtrl', function($scope, $rootScope, $ionicPopup, $ionicLoading, userSetting, findFriend, mySocket){
    $scope.friendInfo={};
    $scope.friendInfo.friends=[];
    $scope.searchKeyDown=function(keyEvent){
        var keycode = window.event?keyEvent.keyCode:keyEvent.which;
        if (keycode==13) {
            $scope.findFriend();
        }
    };
    $scope.updataReqList=function(){
        var friendReqListStr=localStorage.getItem("friendReqList");
        if (friendReqListStr==null || friendReqListStr=="") {
            $scope.friendInfo.friendReqList={};
        } else {
            $scope.friendInfo.friendReqList=angular.fromJson(friendReqListStr);
        }
        
    };
    $scope.updataReqList();
    //console.log(localStorage.getItem("friendReqList"));
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
                $scope.updataReqList();
            });
        }
    };
    $scope.findFriend=function() {
        if ($scope.friendInfo.searchText==null || $scope.friendInfo.searchText=="") {
            $ionicPopup.alert({
               title: '信息未填写',
               template: '请填写你要搜索的手机号码或昵称'
            });
            return;
        }
        if ($scope.friendInfo.searchText.length<=6) {
            $ionicLoading.show({
                template: '已智能检测为用户名正在搜索...'
                    
            });
                findFriend.getFriendByName($scope.friendInfo.searchText,function(data,status){
                    $ionicLoading.hide();
                    if (status!=200) {
                        $ionicPopup.alert({
                           title: '网络异常',
                           template: '请检查你的网络，或稍后再试'
                        });
                        return;
                    }
                    if (data.code==0) {
                        $scope.friendInfo.friends=findFriend.getFriends();
                        //console.log(findFriend.getFriends());
                    } else {
                        $ionicPopup.alert({
                           title: '错误',
                           template: data.msg
                        });
                    }
                });
            return;
        }
        var p=/^1\d{10}$/;
        var res=$scope.friendInfo.searchText.match(p);
        //console.log(res);
        if(res!=null){
            $ionicLoading.show({
                template: '已智能检测为手机号正在搜索...'
                
            });
            findFriend.getFriendByPhone($scope.friendInfo.searchText,function(data,status){
                    $ionicLoading.hide();
                    if (status!=200) {
                        $ionicPopup.alert({
                           title: '网络异常',
                           template: '请检查你的网络，或稍后再试'
                        });
                        return;
                    }
                    if (data.code==0) {
                        $scope.friendInfo.friends=findFriend.getFriends();
                        //console.log(findFriend.getFriends());
                    } else if (data.code==-1) {
                        var myPopup = $ionicPopup.show({
                         title: '帮助好友',
                         subTitle: '当前好友不存在，是否帮助好友补全信息？',
                         scope: $scope,
                         buttons: [
                           { 
                               text: '嘿嘿,让我来', 
                               type: 'button-positive',
                               onTap: function(e) {
                                   localStorage.setItem('addFriendPhone', $scope.friendInfo.searchText);
                                   location.href="#addFriend";
                               }
                           },
                           {
                             text: '不了'
                           },
                         ]
                       });
                    } else {
                        $ionicPopup.alert({
                           title: '错误',
                           template: data.msg
                        });
                    }
                });
            return;
        }
        $ionicPopup.alert({
           title: '信息错误',
           template: '您输入的既不是手机号码，也不是好友昵称！请核对！'
        });
    }
    $scope.addFriend=function(friendId) {
         var myPopup = $ionicPopup.show({
         template: '<input type="text" ng-model="friendInfo.commit" placeholder="你的身份信息" >',
         title: '输入证明你身份的信息',
         scope: $scope,
         buttons: [
           { text: '取消' },
           {
             text: '<b>添加</b>',
             type: 'button-positive',
             onTap: function(e) {
               if (!$scope.friendInfo.commit) {
                 e.preventDefault();
               } else {
                 return $scope.friendInfo.commit;
               }
             }
           },
         ]
       });
        myPopup.then(function(res) {
            //console.log('Tapped!', res);
            if (!res) {
                return;
            }
            $ionicLoading.show({
                template: '正在添加好友...'         
            });

            findFriend.addFriend(friendId, $scope.friendInfo.commit, function(res, stat){
                $ionicLoading.hide();
                //console.log(res);
                //console.log(stat);
                if (stat!=200) {
                        $ionicPopup.alert({
                           title: '添加失败',
                           template: '验证超时，请检查网络...'
                         });
                        return;
                    }
                    if (res.code==0) {
                        findFriend.receiveFriendReq(res.res, function() {
                            //console.log(res.res);
                            findFriend.dealFriendMsg(res.res);
                        });   
                        var myPopup=$ionicPopup.alert({
                           title: '添加成功',
                           template: res.msg
                         });
                        myPopup.then(function(){
                            location.href="myChat.html#/tab/address";
                        });
                    } else if (res.code=-1) {
                        var myPopup=$ionicPopup.alert({
                           title: '添加失败',
                           template: res.msg
                         });
                        myPopup.then(function(res){
                            location.href="index.html";
                        });
                    } else {
                        var myPopup=$ionicPopup.alert({
                           title: '添加失败',
                           template: res.msg
                         });
                    }
            });
        });
        //console.log(id);

    }
    $scope.blackFriend=function(friend) {
        $ionicLoading.show({
                template: '正在拉黑好友...'         
            });
        findFriend.blackFriend(friend.friendId, function(res, stat){
            $ionicLoading.hide();
                if (stat!=200) {
                        $ionicPopup.alert({
                           title: '添加失败',
                           template: '验证超时，请检查网络...'
                         });
                        return;
                    }
                    if (res.code==0) {
                        findFriend.dealFriendMsg(res.res);
                         var myPopup=$ionicPopup.alert({
                           title: '拉黑成功',
                           template: res.msg
                         });
                        myPopup.then(function(){
                            findFriend.blackFriendToData(friend);
                            location.href="myChat.html#/tab/address";
                        });
                    } else if (res.code=-1) {
                        var myPopup=$ionicPopup.alert({
                           title: '拉黑失败',
                           template: res.msg
                         });
                        myPopup.then(function(res){
                            location.href="index.html";
                        });
                    } else {
                        var myPopup=$ionicPopup.alert({
                           title: '拉黑失败',
                           template: res.msg
                         });
                    }
        });
    }
    $scope.seeFriend=function(id) {
        location.href="myIntro.html#/myInfo/"+id;
    }
    
})
.controller('addFriendCtrl', function($ionicLoading, $ionicPopup, $rootScope, $scope, findFriend){
    $rootScope.myicon="myico/man.png";
    $scope.sex=1;
    $rootScope.selectAvatar=false;
    $scope.isselectsex=false;
    $scope.errorusername=false;
    $scope.errorrent=false;
    $scope.errorstrong=false;
    $scope.info={};
    $scope.info.strongShow=" 每个技能请空格隔开";
    $rootScope.localtion={};
    $scope.showPosition =function(position){
        $rootScope.localtion.longitude=position.coords.longitude;
        $rootScope.localtion.latitude=position.coords.latitude;
        console.info($rootScope.localtion);
    };
    $scope.info.friendPhone=localStorage.getItem('addFriendPhone');
    if ($scope.info.friendPhone==null ||$scope.info.friendPhone=="") {
        var myPopup=$ionicPopup.alert({
               title: '请求中断',
               template: '请先搜索好友，好友不在再来添加！'
            });
        myPopup.then(function(res){
            location.href="#search";
        });
        return;
    }
    var p=/^1\d{10}$/;
    var res=$scope.info.friendPhone.match(p);
    if (res==null) {
        var myPopup=$ionicPopup.alert({
               title: '请求中断',
               template: '请先搜索好友，好友不在再来添加！'
            });
        myPopup.then(function(res){
            location.href="#search";
        });
        return;
    }
//   $scope.toDataUrl=function(getImg){
//    var canvas = document.createElement('canvas');
//    var img = new Image();
//    getImg = getImg || function(){};
//    img.onload = function(){ 
//        img.width = 100; 
//        img.height = 100;
//        canvas.width=100;
//        canvas.height = 100; 
//        var ctx = canvas.getContext("2d"); 
//        ctx.clearRect(0, 0, canvas.width, canvas.height); // canvas清屏
//        //重置canvans宽高 canvas.width = img.width; canvas.height = img.height;
//        ctx.drawImage(img, 0, 0, img.width, img.height); // 将图像绘制到canvas上 
//        getImg(canvas.toDataURL());//必须等压缩完才读取canvas值，否则canvas内容是黑帆布
//    };
//    img.src=$rootScope.myicon;
//   }
//   $scope.toDataUrl(function(dataUrl){
//        $rootScope.myicon=dataUrl;
//    });
    $scope.checksign=function(){
        if ($scope.info.sign.length > 75) {
            $scope.info.signMsg="您输入的字符超过75个字了！";
            $scope.errorsign=true;
        }else{
            $scope.errorsign=false;
        }
    }
    if (navigator.geolocation)
    {
        navigator.geolocation.getCurrentPosition($scope.showPosition);
    } else {
        $rootScope.localtion.longitude=1000.00;
        $rootScope.localtion.latitude=1000.00;
        console.info($scope.localtion);
    }
    $scope.selectm=function(){
        $scope.sex=1;
        if ($rootScope.selectAvatar==false) {
            $rootScope.myicon="myico/man.png";
//            $scope.toDataUrl(function(dataUrl){
//                $rootScope.myicon=dataUrl;
//            });
        }
        $scope.isselectsex=true;
    }
    $scope.selectf=function(){
        $scope.sex=2;
        if ($rootScope.selectAvatar==false) {
            $rootScope.myicon="myico/female.png";
//            $scope.toDataUrl(function(dataUrl){
//                $rootScope.myicon=dataUrl;
//            });
        }
        $scope.isselectsex=true;
    }
    $scope.takePic=function(){
        var fileUp=document.getElementById("inputFileUp");
        fileUp.click();
    }
    $scope.randomname=function(){
        //console.log($scope.myinput.username);
        //$scope.info.username="randomname";
        var charUs = 'ABCDEFGHJKMNPQRSTWXYZ';
        var charLs = 'abcdefhijkmnprstwxyz';
        var maxUPos=  charUs.length;
        var maxLPos=  charLs.length;
        var randomname="";
        for (i=0;i<5;i++) {
            if (i==0) {
                randomname+=charUs.charAt(Math.floor(Math.random() * maxUPos));
            } else {
                randomname+=charLs.charAt(Math.floor(Math.random() * maxLPos));
            }
            
        }
        $scope.info.username=randomname;
    }
    $scope.limitlen=function(){
        if ($scope.info.username.length > 6) {
            $scope.errorusername=true;
        }else{
            $scope.errorusername=false;
        }
    }
    $scope.strong =new Array();
    $scope.checkstrong=function(){
        $scope.strong = $scope.info.strong.split(/\s+/i);
        console.log($scope.strong);
        
        if($scope.strong instanceof Array){
            $scope.errorstrong=false;
            if ($scope.strong.length>5) {
                $scope.info.strongMsg="最大不可以超过5个擅长！";
                $scope.errorstrong=true;
                return;
            }
            $scope.info.strongShow=" :";
            for(var i=0;i<$scope.strong.length;i++){
                for(var j=0;j<$scope.strong.length;j++) {
                    if(i==j){
                        continue;
                    }
                    if ($scope.strong[i]==$scope.strong[j]) {
                        $scope.info.strongShow=" ";
                        $scope.info.strongMsg="不能存在相同的擅长！";
                        $scope.errorstrong=true;
                        return;
                    }
                }
                if ($scope.strong[i]=="") {
                    $scope.info.strongShow=" ";
                    $scope.info.strongMsg="擅长不可为空或空格！";
                    $scope.errorstrong=true;
                    return;
                }
                if ($scope.strong[i].match(/\S{5,}/i)) {
                    $scope.info.strongShow=" ";
                    $scope.info.strongMsg="每个擅长不可超过4个字，多个擅长，请用逗号隔开！";
                    $scope.errorstrong=true;
                    return;
                }
                $scope.info.strongShow+="["+$scope.strong[i]+"]";
            }
        }
    }
    $scope.addFriendInfo=function(){
        if ($scope.isselectsex==false) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请选择性别'
             });
            return;
        }
        if($scope.errorsign==true) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请正确填写签名'
             });
            return;
        }
        if($scope.info.username==null || $scope.info.username=="" || $scope.errorusername==true) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请填写昵称或正确填写昵称'
             });
            return;
        }
        if($scope.info.strong==null || $scope.info.strong=="" || $scope.errorstrong==true) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请填写昵称或正确填写擅长'
             });
            return;
        }
        $ionicLoading.show({
                template: '申请助人为乐中...'
                
            });
        $scope.data={};
        $scope.data.phone=$scope.info.friendPhone;
        $scope.data.avatar=$rootScope.myicon;
        $scope.data.sex=$scope.sex;
        $scope.data.name=$scope.info.username;
        $scope.data.sign=$scope.info.sign;
        $scope.data.strong=$scope.strong;
        $scope.data.rent=0;
        $scope.data.localtion=$rootScope.localtion;
        findFriend.addFriendInfo($scope.data,function(res, stat){
            $ionicLoading.hide();
            //console.log(res);
            console.log(stat);
            if (stat!=200) {
                    $ionicPopup.alert({
                       title: '申请失败',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                }
                if (res.code==0) {
                    var myPopup=$ionicPopup.alert({
                       title: '申请成功',
                       template: res.msg
                     });
                    myPopup.then(function(){
                        localStorage.setItem('addFriendPhone', null);
                        location.href="myChat.html";
                    });
                } else {
                    var myPopup=$ionicPopup.alert({
                       title: '申请失败',
                       template: res.msg
                     });
                    myPopup.then(function(){
                        location.href="myChat.html";
                    });
                }
        });
        
    }
});