angular.module('getAccount.controller', [])
.controller('getPhoneCtrl', function($ionicPopup, $rootScope, $scope, $ionicLoading, Account, userSetting){
    $scope.checkCodeTime="发送验证码";
    $scope.isSend=true;
    $scope.notphone=false;
    $scope.notcode=false;
    $scope.errorcode=false;
    $scope.isopencode=true;
    $scope.getphone={};
    if ($rootScope.phone!="" && $rootScope.password!="") {
        $scope.getphone.phonenum=$rootScope.phone;
        $scope.getphone.codenum=$rootScope.password;
    }
    $scope.sendCode=function(){
        if($scope.notphone){
            return;
        }
        $scope.isSend=true;
        var timeId;
        if($scope.checkCodeTime=="发送验证码"){
            $scope.isopencode=false;
            Account.sendRegSms($scope.getphone.phonenum,function(res,stat){
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: '请确认网络有效'
                     });
                }
                if (res.code==0) {
                    $ionicPopup.alert({
                       title: '发送成功',
                       template: '已成功发送短信'
                     });
                } else {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: res.msg
                     });
                }
            });
            $scope.codeTime=60;
            timeId=setInterval(function(){
                $scope.codeTime--;
                //console.log($scope.codeTime);
                if ($scope.codeTime<=0) {
                    window.clearInterval(timeId);
                    $scope.checkCodeTime="发送验证码";
                    if($scope.notphone==false){
                        $scope.isSend=false;
                    }
                    $scope.$digest();
                } else {
                        $scope.checkCodeTime="等待"+$scope.codeTime+"后发送";
                        $scope.$digest();
                }
            },1000);
            $scope.checkCodeTime="等待"+$scope.codeTime+"后发送";
        }
    };
    $scope.checkPhone=function(){
        var p=/^1\d{10}$/;
        //console.log(phonetext.phonenum);
        var res=$scope.getphone.phonenum.match(p);
        //console.log(res);
        if(res==null){
            $scope.notphone=true;
            $scope.isSend=true;
            $scope.isopencode=true;
        }else{
            $scope.notphone=false;
            $scope.isSend=false;
            $scope.isopencode=false;
        }
    };
    $scope.checkCode=function(){
        var p=/^\d{6}$/;
        //console.log(codetext.codenum);
        var phoneres=$scope.getphone.codenum.match(p);
        //console.log(res);
        if(phoneres==null){
            $scope.notcode=true;
//            if($scope.getphone.codenum.length>6){
//                console.log($scope.getphone.codenum.length);
//                $scope.getphone.codenum=$scope.getphone.codenum.substr(1,6);
//            }
        }else{
            $rootScope.phone=$scope.getphone.phonenum;
            $rootScope.password=$scope.getphone.codenum;
            $scope.notcode=false;
            $scope.isopencode=true;
            $ionicLoading.show({
                template: '验证中...'
                
            });
            Account.checkCode($scope.getphone.codenum, function(res,stat){
                $ionicLoading.hide();
                if (stat!=200) {
                    $scope.isopencode=false;
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: '验证超时，请检查网络...'
                     });
                }
                if (res.code==0) {
                    $scope.isopencode=true;
                    $scope.isSend=true;
                    location.href="#accountInfo";
                } else if (res.code==1) {
                    $scope.isopencode=true;
                    $scope.isSend=true;
                    //console.log(res.res);
                    userSetting.setUserInfo(res.res);
                    Account.getAddressList(function(addressRes, addressStat){
                        var myAddress={};
                        for (var i=0;i<addressRes.res.length;i++) {
                            myAddress[addressRes.res[i].friendId]=addressRes.res[i];
                        }
                        console.log(myAddress);
                        localStorage.setItem("myAddress", angular.toJson(myAddress));
                        localStorage.removeItem("chats");
                        localStorage.removeItem("chatDetail");
                        localStorage.removeItem("dyCircle");
                        location.href="myChat.html";
                    });
                } else {
                    $scope.isopencode=false;
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: res.msg
                     });
                }
            });
        }  
    };
})
.controller('changePhoneCtrl', function($scope, $ionicPopup, $ionicLoading, Account, userSetting){
     var myPopup = $ionicPopup.show({
     title: '服务条款',
     subTitle: '注意：如果你现在的新号码已经注册了，旧号码账号的信息将会覆盖到新号码账号上，且不可恢复<br/>如果确认，信息丢失将和我们无关',
     scope: $scope,
     buttons: [
       { 
           text: '确认', 
           type: 'button-positive',
       },
       {
         text: '取消',
         onTap: function(e) {
               location.href="#getPhone";
           }
       },
     ]
   });
    $scope.getoldphone={};
    $scope.getnewphone={};
    $scope.getoldphone.errorphone=false;
    $scope.getoldphone.showcode=false;
    $scope.getoldphone.checkCodeTime="发送验证码";
    $scope.getnewphone.showall=false;
    $scope.getnewphone.errorphone=false;
    $scope.getnewphone.showcode=false;
    $scope.getnewphone.checkCodeTime="发送验证码";
    $scope.getoldphone.sendCode=function(){
        $scope.sendCode($scope.getoldphone);
        Account.getOldPhoneSms($scope.getoldphone.phonenum,function(res,stat){
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: '请确认网络有效'
                     });
                }
                if (res.code==0) {
                    $ionicPopup.alert({
                       title: '发送成功',
                       template: '已成功发送短信'
                     });
                } else {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: res.msg
                     });
                }
            });
    };
    $scope.getnewphone.sendCode=function(){
        $scope.sendCode($scope.getnewphone);
        Account.getNewPhoneSms($scope.getnewphone.phonenum,function(res,stat){
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: '请确认网络有效'
                     });
                }
                if (res.code==0) {
                    $ionicPopup.alert({
                       title: '发送成功',
                       template: '已成功发送短信'
                     });
                } else {
                    $ionicPopup.alert({
                       title: '发送失败',
                       template: res.msg
                     });
                }
            });
    };
    $scope.sendCode=function(thisScope){
        if(thisScope.errorphone){
            return;
        }
        thisScope.isSend=true;
        var timeId;
        if(thisScope.checkCodeTime=="发送验证码"){
            thisScope.isopencode=false;
            thisScope.codeTime=15;
            timeId=setInterval(function(){
                thisScope.codeTime--;
                //console.log($scope.codeTime);
                if (thisScope.codeTime<=0) {
                    window.clearInterval(timeId);
                    thisScope.checkCodeTime="发送验证码";
                    if(thisScope.errorphone==false){
                        thisScope.isSend=false;
                    }
                    $scope.$digest();
                } else {
                        thisScope.checkCodeTime="等待"+thisScope.codeTime+"后发送";
                        $scope.$digest();
                }
            },1000);
            thisScope.checkCodeTime="等待"+thisScope.codeTime+"后发送";
        }
    };
    $scope.getoldphone.checkPhone=function(){
        var p=/^1\d{10}$/;
        var res=$scope.getoldphone.phonenum.match(p);
        //console.log(res);
        if(res==null){
            $scope.getoldphone.isSend=true;
            $scope.getoldphone.errorphone=true;
            $scope.getoldphone.errormsg="您输入的手机号码应为大陆11位号码";
        }else{
            $scope.getoldphone.isSend=false;
            $scope.getoldphone.errorphone=false;
            $ionicLoading.show({
                template: '检测旧手机账号是否注册...'
                
            });
            Account.checkOldPhone($scope.getoldphone.phonenum, function(res,stat){
                //console.log(stat);
                $ionicLoading.hide();
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '不允许继续',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                    
                }
                if (res.code==0) {
                    $scope.getoldphone.showcode=true;
                } else {
                    var myPopup=$ionicPopup.alert({
                       title: '不允许继续',
                       template: res.msg
                     });
                    myPopup.then(function(res){
                        location.href="#getPhone";
                    });
                    return;
                }
        });
            
        }
    };
    
    $scope.getoldphone.checkCode=function(){
        var p=/^\d{6}$/;
        //console.log(codetext.codenum);
        var res=$scope.getoldphone.codenum.match(p);
        //console.log(res);
        if (res==null) {
            $scope.getoldphone.errorcode=true;
            $scope.getoldphone.errorcodemsg="验证码为6位数字";
        } else {
            $scope.getoldphone.errorcode=false;
            $ionicLoading.show({
                template: '检测验证码是否正确...'
                
            });
            Account.checkOldCode($scope.getoldphone.codenum, function(res,stat){
                //console.log(stat);
                $ionicLoading.hide();
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                    
                }
                if (res.code==0) {
                    $scope.getoldphone.opencode=true;
                    $scope.getnewphone.showall=true;
                    $scope.getoldphone.isSend=true;
                } else {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: res.msg
                     });
                    
                    return;
                }
        });
            
        }
    };
    $scope.getnewphone.checkPhone=function(){
        var p=/^1\d{10}$/;
        var res=$scope.getnewphone.phonenum.match(p);
        //console.log(res);
        if(res==null){
            $scope.getnewphone.isSend=true;
            $scope.getnewphone.errorphone=true;
            $scope.getnewphone.errormsg="您输入的手机号码应为大陆11位号码";
        }else{
            $scope.getnewphone.isSend=false;
            $scope.getnewphone.errorphone=false;
            $scope.getnewphone.showcode=true;
        }
    };
    $scope.getnewphone.checkCode=function(){
        var p=/^\d{6}$/;
        //console.log(codetext.codenum);
        var res=$scope.getnewphone.codenum.match(p);
        //console.log(res);
        if (res==null) {
            $scope.getnewphone.errorcode=true;
            $scope.getnewphone.errorcodemsg="验证码为6位数字";
        } else {
            $scope.getnewphone.errorcode=false;
            $ionicLoading.show({
                template: '检测验证码是否正确...'
                
            });
            Account.changePhone($scope.getoldphone.codenum, $scope.getnewphone.codenum, function(res,stat){
                //console.log(stat);
                $ionicLoading.hide();
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                    
                }
                if (res.code==0) {
                    $scope.getnewphone.opencode=true;
                    console.log(res.res);
                    userSetting.setUserInfo(res.res);
                    location.href="myChat.html";
                } else {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: res.msg
                     });
                    
                    return;
                }
        });
        }
    };
})
.controller('accountInfoCtrl', function($ionicLoading, $ionicPopup, $rootScope, $scope, Account, userSetting){
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
    
   Account.checkCode($rootScope.password, function(res,stat){
                //console.log(stat);
                if (stat!=200) {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                    
                }
                if (res.code!=0) {
                    var myPopup=$ionicPopup.alert({
                       title: '验证失败',
                       template: res.msg
                     });
                    myPopup.then(function(res){
                        location.href="#getPhone";
                    });
                    return;
                }
    });
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
    $scope.checkrent=function(){
        if(parseInt($scope.info.rent)==$scope.info.rent && $scope.info.rent>=0 && $scope.info.rent<100000000){
            $scope.errorrent=false;
            $scope.info.rentShow=":￥"+$scope.info.rent+"/小时";
        } else if($scope.info.rent>=100000000) {
            $scope.info.rentShow="";
            $scope.info.rentMsg="超出最大值，无法提交！";
            $scope.errorrent=true;
        } else {
            $scope.info.rentShow="";
            $scope.info.rentMsg="必须输入一个大于或等于0的整数";
            $scope.errorrent=true;
        }
    }
    $scope.sendRegin=function(){
        if ($scope.isselectsex==false) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请选择性别'
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
               template: '请填写昵称或正确填写您的擅长'
             });
            return;
        }
        if($scope.info.rent==null || $scope.info.rent=="" || $scope.errorrent==true) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请填写昵称或正确填写您的定价'
             });
            return;
        }
        $ionicLoading.show({
                template: '注册中...'
                
            });
        $scope.data={};
        $scope.data.phone=$rootScope.phone;
        $scope.data.password=$rootScope.password;
        $scope.data.avatar=$rootScope.myicon;
        $scope.data.sex=$scope.sex;
        $scope.data.name=$scope.info.username;
        $scope.data.strong=$scope.strong;
        $scope.data.rent=$scope.info.rent;
        $scope.data.localtion=$rootScope.localtion;
        Account.sendRegin($scope.data,function(res, stat){
            $ionicLoading.hide();
            //console.log(res);
            //console.log(stat);
            if (stat!=200) {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: '验证超时，请检查网络...'
                     });
                    return;
                    
                }
                if (res.code==0) {
                    userSetting.setUserInfo(res.res);
                    $ionicPopup.alert({
                       title: '验证成功',
                       template: res.msg
                     });
                    location.href="myChat.html";
                } else if (res.code==1) {
                    userSetting.setUserInfo(res.res);
                    var myPopup=$ionicPopup.alert({
                       title: '验证成功',
                       template: res.msg
                     });
                    myPopup.then(function(res){
                        location.href="myChat.html";
                    });
                } else {
                    $ionicPopup.alert({
                       title: '验证失败',
                       template: res.msg
                     });
                }
        });
        
    }
});