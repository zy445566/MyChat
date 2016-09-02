angular.module('myChat',['ionic', 'myChat.controller', 'myChat.service', 'userSetting.service', 'mySocket.service', 'findFriend.service'])

.run(function($ionicPlatform) {
    $ionicPlatform.ready(function(){
        if(window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            cordova.plugins.Keyboard.disableScroll(true);
        }
        if(window.StatusBar) {
            StatusBar.styleDefault();
        }
    });
})
.directive('hideTabs', function($rootScope) {
    return {
        restrict: 'A',
        link: function(scope, element, attributes) {
            scope.$on('$ionicView.beforeEnter', function() {
                scope.$watch(attributes.hideTabs, function(value){
                    $rootScope.hideTabs = value;
                });
            });

            scope.$on('$ionicView.beforeLeave', function() {
                $rootScope.hideTabs = false;
            });
        }
    };
})
.config(function($stateProvider, $urlRouterProvider, $ionicConfigProvider){
    $ionicConfigProvider.platform.ios.tabs.style('standard'); 
    $ionicConfigProvider.platform.ios.tabs.position('bottom');
    $ionicConfigProvider.platform.android.tabs.style('standard');
    $ionicConfigProvider.platform.android.tabs.position('bottom');
    $ionicConfigProvider.platform.ios.navBar.alignTitle('center'); 
    $ionicConfigProvider.platform.android.navBar.alignTitle('center');
    $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
    $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');        
    $ionicConfigProvider.platform.ios.views.transition('ios'); 
    $ionicConfigProvider.platform.android.views.transition('android');

    //如果要加路由，直接加state配置即可
    $stateProvider
    .state('tab',{
        url:'/tab',
        abstract: true,
        templateUrl:'templates/myChat/tab.html', 
        controller: 'TabCtrl'
    })
    .state('tab.chat', {
    url: '/chat',
        views: {
          'tab-item': {
            templateUrl: 'templates/myChat/chat.html',
            controller: 'ChatCtrl'
          }
        }
    })
    .state('tab.chatdetail', {
    url: '/chatdetail/:chatId',
        views: {
          'tab-item': {
            templateUrl: 'templates/myChat/chatdetail.html',
            controller: 'ChatDetailCtrl'
          }
        }
    })
    .state('tab.address', {
    url: '/address',
        views: {
          'tab-item': {
            templateUrl: 'templates/myChat/address.html',
            controller: 'AddressCtrl'
          }
        }
    })
    .state('tab.near', {
    url: '/near',
        views: {
          'tab-item': {
            templateUrl: 'templates/myChat/near.html',
            controller: 'NearCtrl'
          }
        }
    });
    $urlRouterProvider.otherwise('/tab/chat');
});