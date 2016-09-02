angular.module('mySocket.service', [])
.factory('mySocket', function($http, $rootScope) {
    var DeBug=1;
    var baseUrl="";
    if (DeBug==1) {
        baseUrl="ws://localhost:8080/slyimweb/";
    } else {
        baseUrl="ws://slyim.com/";
    }
    var errorCount = 0;
    var openWebSocket = function() {
        $rootScope.ws = new WebSocket(baseUrl+"websocket");
        $rootScope.ws.onopen = function (event) {
            errorCount=0;
            console.log("WebSocket Open");
        };
    };
    return {
        makeWS : function(){
            if (!("WebSocket" in window)) {
                console.log("WebSocket Not In");
                return;
            }
            openWebSocket();
            $rootScope.ws.onerror = function (event) {
                if (errorCount<3){
                    errorCount++;
                    openWebSocket(); 
                }
            }
            $rootScope.ws.close = function (event) {
                console.log("WebSocket Close");
                
            };
        }
        
    };
});