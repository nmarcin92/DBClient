
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}


function getConnectionFromCookie($scope){
    cookie = getCookie("hostName");
    if(cookie == "" || cookie == undefined) return;
    $scope.hostName = cookie;

    cookie = getCookie("port");
    if(cookie == "" || cookie == undefined) return;
    $scope.port = cookie;

    cookie = getCookie("databaseName");
    if(cookie == "" || cookie == undefined) return;
    $scope.databaseName = cookie;

    cookie = getCookie("userName");
    if(cookie == "" || cookie == undefined) return;
    $scope.userName = cookie;

    cookie = getCookie("password");
    if(cookie == "" || cookie == undefined) return;
    $scope.password = cookie;

    cookie = getCookie("databaseType");
    if(cookie == "" || cookie == undefined) return;
    $scope.databaseType = cookie;

    $scope.connected = true;
}

function setConnection($scope){
    var fields = [$scope.hostName, $scope.port, $scope.databaseName, $scope.userName, $scope.password, $scope.databaseType];
    var cookies = ["hostName", "port", "databaseName", "userName", "password", "databaseType"];

    //connect
    if(!$scope.connected){
        for(var i = 0; i < fields.length; i++){
            if(fields[i] == "" || fields[i] == undefined) {
                alert("Please fill all the fields");
                return;
            }

            setCookie(cookies[i], fields[i], 7);
        }

        $scope.connected = true;
    }

    else {
        for(var i = 0; i < cookies.length; i++){
            document.cookie = cookies[i] + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        }
        $scope.connected = false;

    }
}