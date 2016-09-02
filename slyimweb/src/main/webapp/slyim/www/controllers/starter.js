angular.module('starter.controller', [])
.controller('welcomeCtrl', function($scope) {
    $scope.screenBody={};
    $scope.screenBody.X=document.body.scrollWidth;
    $scope.screenBody.Y=document.body.scrollHeight;
    console.log("X:"+$scope.screenBody.X);
    console.log("Y:"+$scope.screenBody.Y);
    $scope.screenBody.slideStyle = {
                "background-color" : "#E87877"
            }
    if($scope.screenBody.X>$scope.screenBody.Y && $scope.screenBody.X!=0 && $scope.screenBody.Y!=0){
        $scope.welcome1="mypic/welcome1.jpg";
        $scope.welcome2="mypic/welcome2.jpg";
        $scope.welcome3="mypic/welcome3.jpg";
        $scope.welcome4="mypic/welcome4.jpg";       
    }else{
        $scope.welcome1="mypic/pwelcome1.jpg";
        $scope.welcome2="mypic/pwelcome2.jpg";
        $scope.welcome3="mypic/pwelcome3.jpg";
        $scope.welcome4="mypic/pwelcome4.jpg";
    }
    $scope.slidestep=function(index){
        step1="#E87877";
        step2="#E35D78";
        step3="#E3556B";
        step4="#DC214C";
        stepEsle="white";
        if (index ==0){
            $scope.screenBody.slideStyle = {
                "background-color" : step1
            }
        } else if (index ==1) {
            $scope.screenBody.slideStyle = {
                "background-color" : step2
            }
        } else if (index ==2) {
            $scope.screenBody.slideStyle = {
                "background-color" : step3
            }
        } else if (index ==3) {
            $scope.screenBody.slideStyle = {
                "background-color" : step4
            }
        } else {
            $scope.screenBody.slideStyle = {
                "background-color" : step1
            }
        }
    }
    $scope.nextstep=function(){
        localStorage.setItem('welcome', 1);
        location.href="getAccount.html";     
    }
})
.controller('startCtrl', function($scope, $ionicLoading, userSetting) {
//    $ionicLoading.show({
//                template: '载入系统中...'
//                
//    });
    if (userSetting.isExistLocal()) {
        location.href="myChat.html";
    } else {
        userSetting.isWelcome();
    }
//    userSetting.initUserData(function(isLogin){
//        $ionicLoading.hide();
//        if (isLogin) {
//            location.href="myChat.html";
//        } else {
//            userSetting.isWelcome();
//        }
//    });
  
});