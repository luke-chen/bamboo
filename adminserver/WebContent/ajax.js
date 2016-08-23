/**
 * XMLHttpRequest
 * @param url AJAX只能在本域名访问
 * @param callbackFunction 处理数据的回调函数
 */
function XMLHttpRequest(url, callbackFunction) {
    var xmlHttp = null;

    if (window.XMLHttpRequest) {
        //非IE内核浏览器
        xmlHttp = new XMLHttpRequest();
        // 有些版本的Mozilla浏览器处理服务器返回的未包含XML mime-type头部信息的内容时会出错.
        // 因此,要确保返回的内容包含text/xml信息.
        if (xmlHttp.overrideMimeType) {
            xmlHttp.overrideMimeType("text/xml");
        }
        document.write("创建XMLHttpRequest对象实例!<p>");
    } else if (window.ActiveXObject) { //IE内核浏览器
        try { //IE6.0
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            document.write("创建Microsoft.XMLHTTP对象实例!<p>");
        } catch (e1) {
            try {
                xmlHttp = new ActiveXObject("MSXML2.XMLHTTP");
                document.write("创建MSXML2.XMLHTTP对象实例!<p>");
            } catch (e2) {
                try {
                    xmlHttp = new ActiveXObject("MSXML3.XMLHTTP");
                    document.write("创建MSXML3.XMLHTTP对象实例!<p>");
                } catch (e3) {
                    document.write("创建Ajax失败：" + e3 + "<p>");
                }
            }
        }
    } else { //未知浏览器
        alert("未能识别的浏览器");
    }

    // 指定响应处理函数
    xmlHttp.onreadystatechange = function() {
        try {
            if (xmlHttp.readyState == 0) {
                document.write("对象未初始化<p>");
            } else if (xmlHttp.readyState == 1) {
                document.write("正在加载连接对象<p>");
            } else if (xmlHttp.readyState == 2) {
                document.write("连接对象加载完毕<p>");
            } else if (xmlHttp.readyState == 3) {
                document.write("数据交互中<p>");
            } else if (xmlHttp.readyState == 4) {
                document.write("数据交互完成<p>");
                document.write("HTTP Status Code:" + xmlHttp.status + "<p>");
                if (typeof(callbackFunction) == "function") {
                    callbackFunction(xmlHttp.responseText);
                } else {}
            }
        } catch (e) {
            document.write("回调处理错误:" + e);
        }
    }

    xmlHttp.open("GET", url, false);
    //xmlHttp.setRequestHeader("cache-control","no-cache");
    //xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xmlHttp.send(null);
    document.write("发送成功<p>" + url);
}

//自定义回调函数
function function1(responseText) {
    document.write("xmlHttp.responseText:" + responseText);
}

//定义URL
var url = "http://localhost:8080/TTUserCenter/images/but_bj1.gif";
//开始请求
XMLHttpRequest(url, function1);
