angular.module('myIntro.controller', [])
.controller('myInfoCtrl', function($ionicLoading, $ionicPopup, $stateParams, $interval, $rootScope, $scope, myIntro, userSetting){
    $scope.userId=$stateParams.userId;
    $scope.info={};
    $scope.info.unlockIco="ion-android-lock";
    $scope.info.unlockCol="col col-75 col-offset-25";
    $scope.info.unlockShow=false;
    $rootScope.myicon="myico/man.png";
    $scope.sex=1;
    $rootScope.selectAvatar=false;
    $scope.isdisabled=true;
    $scope.isdisMyself=true;
    $scope.errorusername=false;
    $scope.errorrent=false;
    $scope.errorstrong=false;
    $rootScope.selectAvatar=true;
    $scope.info.strongShow=" 每个技能请空格隔开";
    $rootScope.localtion={};
    $ionicLoading.show({
        template: '载入用户中...'          
    });
    myIntro.getUserById($scope.userId, function(data,status){
        $ionicLoading.hide();
        if (status!=200) {
            $ionicPopup.alert({
               title: '网络异常',
               template: '请检查你的网络，或稍后再试'
            });
            return;
        }
        if (data.code==0) {
            console.log(data.res);
            $rootScope.myicon=data.res.avatar;
            if (data.res.sex==1) {
                $scope.info.man=true;
                $scope.selectm();
            } else {
                $scope.info.female=true;
                $scope.selectf();
            }
            $scope.info.username=data.res.name;
            $scope.info.sign=data.res.sign;
            $scope.info.modifyTime=data.res.modifyTime;
            $scope.info.nowTime=parseInt(data.res.password);
            $scope.info.unlockTime=$scope.info.modifyTime+14*24*3600-$scope.info.nowTime;
            $scope.info.unlockShow=true;
            $scope.info.isUnlock=false;
            $scope.toDataText=function(){
                var dataText="";
                $scope.info.unlockTempTime=$scope.info.unlockTime;
                if ($scope.info.unlockTempTime>0) {
                    $scope.info.unlockDay=0;
                    if ($scope.info.unlockTempTime>=3600*24){
                        $scope.info.unlockDay=parseInt($scope.info.unlockTempTime/(3600*24));
                        $scope.info.unlockTempTime=$scope.info.unlockTempTime-($scope.info.unlockDay*3600*24);
                    }
                    if ($scope.info.unlockDay>0) {
                        dataText+=$scope.info.unlockDay+"天";
                    }
                    $scope.info.unlockHour=0;
                    if ($scope.info.unlockTempTime>=3600){
                        $scope.info.unlockHour=parseInt($scope.info.unlockTempTime/(3600));
                        $scope.info.unlockTempTime=$scope.info.unlockTempTime-($scope.info.unlockHour*3600);
                    }
                    if ($scope.info.unlockHour>0) {
                        dataText+=$scope.info.unlockHour+"时";
                    }
                    $scope.info.unlockMin=0;
                    if ($scope.info.unlockTempTime>=60){
                        $scope.info.unlockMin=parseInt($scope.info.unlockTempTime/(60));
                        $scope.info.unlockTempTime=$scope.info.unlockTempTime-($scope.info.unlockMin*60);
                    }
                    if ($scope.info.unlockMin>0) {
                        dataText+=$scope.info.unlockMin+"分";
                    }
                    if ($scope.info.unlockTempTime>0) {
                        dataText+=$scope.info.unlockTempTime+"秒";
                    }
                    return dataText;
                }
            }
            
            if ($scope.info.unlockTime>0) {
                $scope.info.unlockText="还有"+$scope.toDataText()+"解锁陌生人编辑";
            } else {
                $scope.info.unlockIco="ion-android-unlock";
                $scope.info.unlockCol="col col-33 col-offset-33";
                $scope.info.unlockText="已解锁编辑";
                $scope.isdisabled=false;
                $scope.info.isUnlock=true;
            }
            var timer=$interval(function(){
                if ($scope.info.unlockTime>0) {
                    $scope.info.unlockTime--;
                    $scope.info.unlockText="还有"+$scope.toDataText()+"解锁陌生人编辑";
                } else {
                    $scope.info.unlockIco="ion-android-unlock";
                    $scope.info.unlockCol="col col-33 col-offset-33";
                    $scope.info.unlockText="已解锁编辑";
                    $scope.isdisabled=false;
                    $scope.info.isUnlock=true;
                    $interval.cancel(timer);
                }
            },1000);
            $scope.strong=data.res.strong;
            var strongText="";
            for(var i=0;i<$scope.strong.length;i++){
                strongText+=$scope.strong[i]+" ";
            }
            $scope.info.strong=strongText;
            $scope.info.rent=data.res.rent;
        } else {
            var myPopup=$ionicPopup.alert({
               title: '信息错误',
               template: data.msg
            });
            myPopup.then(function(res) {
                location.href="findFriend.html";
            });
        }
    });
    $scope.data={};
    $scope.data=userSetting.getUserInfo();
    if ($scope.data.id==$scope.userId) {
        $scope.isdisabled=false;
        $scope.isdisMyself=false;
    }
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
    
    $scope.checksign=function(){
        if ($scope.info.sign.length > 75) {
            $scope.info.signMsg="您输入的字符超过75个字了！";
            $scope.errorsign=true;
        }else{
            $scope.errorsign=false;
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

    $scope.upUserInfo=function(){
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
        if($scope.errorsign==true) {
            $ionicPopup.alert({
               title: '信息填写有误',
               template: '请正确填写您的签名'
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
            template: '保存资料中...'          
        });
        $scope.data.sign=$scope.info.sign;
        $scope.data.avatar=$rootScope.myicon;
        $scope.data.sex=$scope.sex;
        $scope.data.name=$scope.info.username;
        $scope.data.strong=$scope.strong;
        $scope.data.rent=$scope.info.rent;
        $scope.data.localtion=$rootScope.localtion;
        myIntro.upUserInfo($scope.data, function(data,status){
            $ionicLoading.hide();
            if (status!=200) {
                $ionicPopup.alert({
                   title: '网络异常',
                   template: '请检查你的网络，或稍后再试'
                });
                return;
            }
            if (data.code==0) {
                var myPopup=$ionicPopup.alert({
                   title: '成功',
                   template: '保存成功'
                });
                myPopup.then(function(res){
                    history.go(0);
                });
            } else {
                var myPopup=$ionicPopup.alert({
                   title: '失败',
                   template: data.msg
                });
                myPopup.then(function(res){
                    history.go(0);
                });
            }
        });
    }
        
});