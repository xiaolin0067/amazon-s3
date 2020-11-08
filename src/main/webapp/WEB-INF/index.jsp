<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>demo</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="layui/css/layui.css" media="all">
</head>
<script src="layui/layui.js"></script>
<body>
<div style="width: 258px;margin: 0 auto;margin-top: 60px;">
    <div class="layui-upload-drag" id="uploadDemo">
        <i class="layui-icon"></i>
        <p>点击上传，或将文件拖拽到此处</p>
        <hr>
        当前文件：<span id="uploadResult">未上传文件</span>
    </div>
    <br/>
    <br/>
    <br/>
    <div>
        <a id="down" href="${pageContext.request.contextPath}/s3/download" target="_blank" type="button" class="layui-btn" style="margin-left: 78px;">下载文件</a>
    </div>
</div>

</body>
<script>
    layui.use(['upload'], function(){
        var upload = layui.upload;
        //上传
        upload.render({
            elem: '#uploadDemo'
            ,method: 'POST'
            ,url: '/s3/upload'
            , accept: 'file'
            ,done: function(res) {
                layer.msg('上传成功');
                layui.$('#uploadResult').html(res.message);
            }
        });
    });
</script>
</html>
