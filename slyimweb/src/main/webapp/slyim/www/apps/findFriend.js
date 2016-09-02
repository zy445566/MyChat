angular.module('findFriend', ['ionic', 'findFriend.controller', 'findFriend.service', 'userSetting.service', 'mySocket.service'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
.directive('fileUpData', function($rootScope) {
    return {
        restrict: 'E',
        template: '<input type="file" id="inputFileUp" style="display:none;" accept="image/*">',
        replace: true,
        link: function(scope, element, attr) {
            element.bind("change",function(changeEvent){
                var readerUrl = new FileReader();
//                var readerBinary = new FileReader();
                var compressImg = function(imgData,onCompress){
                    if(!imgData)return;
                    onCompress = onCompress || function(){};
                    var canvas = document.createElement('canvas');
                    var img = new Image();
                    img.onload = function(){ 
                    img.width = 100; 
                    img.height = 100;
                    canvas.width=100;
                    canvas.height = 100; 
                    var ctx = canvas.getContext("2d"); 
                    ctx.clearRect(0, 0, canvas.width, canvas.height); // canvas清屏
                    //重置canvans宽高 canvas.width = img.width; canvas.height = img.height;
                    ctx.drawImage(img, 0, 0, img.width, img.height); // 将图像绘制到canvas上 
                    onCompress(canvas.toDataURL());//必须等压缩完才读取canvas值，否则canvas内容是黑帆布
                    };
                    // 记住必须先绑定事件，才能设置src属性，否则img没内容可以画到canvas
                    img.src = imgData;
                };
                readerUrl.onload = function(loadEvent) {
                    compressImg(loadEvent.target.result,function(data){
                        scope.$apply(function() {
                            $rootScope.myicon=data;
                            $rootScope.selectAvatar=true;
//                            var b64 = data.substring(22);
//                            $rootScope.iconBinary=b64;
                            //$rootScope.iconBinary=data;
                            //console.log($rootScope.iconBinary);
                        });
                    });
                };
//                readerBinary.onload = function(loadEvent) {
//                    scope.$apply(function() {
//                        $rootScope.iconBinary=loadEvent.target.result;
//                        //console.log($rootScope.iconBinary);
//                    });
//                };
                if (typeof(changeEvent.target.files[0]) === 'object') {
                    //读取为Url
                    readerUrl.readAsDataURL(changeEvent.target.files[0]);
                    //读取为二进制编码
//                    readerBinary.readAsBinaryString(changeEvent.target.files[0]);
                    //读取为文本
                   //reader.readAsText(file, "utf-8");
                };
            });
        }
    };
})
.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('search', {
        url: '/search',
        templateUrl: 'templates/findFriend/search.html',
        controller: 'searchCtrl'
    })
  .state('addFriend', {
        url: '/addFriend',
        templateUrl: 'templates/findFriend/addFriend.html',
        controller: 'addFriendCtrl'
    });
  $urlRouterProvider.otherwise('/search');
});