
layui.use(['layer','form','jquery'], function() {
    var form = layui.form,
        $ = layui.jquery;

    $('#forget').on('click', function() {
        location.href = '/Safety/changePwd';
    });
    $('#forLogin').on('click', function() {
        location.href = '/Login/login';
    });

    form.verify({
        username: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                return '用户名不能有特殊字符';
            }
            if(/(^\_)|(\__)|(\_+$)/.test(value)){
                return '用户名首尾不能出现下划线\'_\'';
            }
            if(/^\d+\d+\d$/.test(value)){
                return '用户名不能全为数字';
            }
        }
        ,pass: [
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ]
        ,rePass: function (value, item) {
            if ($("#password").val() != value) {
                return '两处密码不一致';
            }
        }
        ,yzm: function (value, item) {
            if ($("#yzm").val() == "") {
                return '验证码不能为空';
            }
            if ($("#yzm").val().toUpperCase() != code) {
                createCode();
                $("#yzm").val("");
                return '验证码错误';
            }
        }
        ,jym: function (value, item) {
            if ($("#jym").val() == "") {
                return '验证码不能为空';
            }
        }
    });

    //发送校验码
    var $phone = "手机号不能为空";
    $("#phoneJym").click(function CheckPhone() {
        var phonereg = /^1\d{10}$/;
        var phonestring = $(this).val();
        if ($("#phone").val() == "") {
            layer.tips($phone, '#phone',{tips:[2,'red'],time:0});
            return false;
        }
        if (phonereg.test(phonestring)) {
            $.ajax({
                type:"GET",
                url:"/Admin/checkByPhone",
                data:{phone:phonestring},
                dataType:"json",
                success:function(res){
                    if(res){//账号可用，正确提示
                        $phone = "验证码已发送";
                        layer.tips($phone, '#phone');
                        return true;
                    }else{//账号不可用，错误提示
                        $phone = "手机号不存在";
                        layer.tips($phone, '#phone',{tips:[2,'red'],time:0});
                        return false;
                    }
                },
                error:function(data){//当访问时候，404，500 等非200的错误状态码
                    layer.msg('请求错误！', {icon: 5,anim: 6});
                    return false;
                }
            });
        }else{
            $phone = "请输入正确的手机号";
            layer.tips($phone, '#phone',{tips:[2,'red'],time:0});
            return false;
        }
    });

    //检查手机号
    var $checkPhone = "手机号不能为空";
    $("#checkPhone").blur(function CheckPhone() {
        var phonereg = /^1\d{10}$/;
        var phonestring = $(this).val();
        if ($("#phone").val() == "") {
            layer.tips($checkPhone, '#checkPhone',{tips:[2,'red'],time:0});
            return false;
        }
        if (phonereg.test(phonestring)) {
            $.ajax({
                type:"GET",
                url:"/Admin/checkByPhone",
                data:{phone:phonestring},
                dataType:"json",
                success:function(res){
                    if(res.code == 200){//账号可用，正确提示
                        $checkPhone = "手机号可用";
                        layer.tips($checkPhone, '#checkPhone');
                        return true;
                    }else{//账号不可用，错误提示
                        $checkPhone = res.message;
                        layer.tips($checkPhone, '#checkPhone',{tips:[2,'red'],time:0});
                        return false;
                    }
                },
                error:function(data){//当访问时候，404，500 等非200的错误状态码
                    layer.msg('请求错误！', {icon: 5,anim: 6});
                    return false;
                }
            });
        }else{
            $checkPhone = "请输入正确的手机号";
            layer.tips($checkPhone, '#checkPhone',{tips:[2,'red'],time:0});
            return false;
        }
    });

    //验证码
    var code;
    function createCode(){
        code = '';//首先默认code为空字符串
        var codeLength = 4;//设置长度，这里看需求，我这里设置了4
        var codeV = $("#yan");
        //设置随机字符
        var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R', 'S','T','U','V','W','X','Y','Z');
        for(var i = 0; i < codeLength; i++){ //循环codeLength 我设置的4就是循环4次
            var index = Math.floor(Math.random()*36); //设置随机数范围,这设置为0 ~ 36
            code += random[index]; //字符串拼接 将每次随机的字符 进行拼接
        }
        codeV.text(code);//将拼接好的字符串赋值给展示的Value
    }
    //页面开始加载验证码
    createCode();
    //验证码Div加载点击事件
    $("#yan").bind('click',function() {
        createCode();
    });

    //登录监听提交
    form.on('submit(login_hash)', function(data) {
        var param=data.field;//定义临时变量获取表单提交过来的数据，非json格式
        console.log(JSON.stringify(param));//测试是否获取到表单数据，调试模式下在页面控制台查看
        var layIndex = layer.load(2, {
            shade: [0.1, '#393D49']
        });
        $.ajax({
            url:"/Login/login",
            type:'post',//method请求方式，get或者post
            dataType:'json',//预期服务器返回的数据类型
            data:JSON.stringify(param),//表格数据序列化
            contentType: "application/json; charset=utf-8",
            success:function(res){//res为相应体,function为回调函数
                console.log(res);
                if(res.code == 200){
                    window.open("/", "_parent");
                } else if (res.code == 2004 || res.code == 2005 || res.code == 2001){
                    layer.close(layIndex);
                    layer.msg(res.message, {icon: 5,anim: 6});
                    return false;
                } else {
                    layer.close(layIndex);
                    createCode();
                    $("#yzm").val("");
                    if (res.data.flag == true) {
                        var result = "因 <em style='color: #009688'>"+res.data.name+"</em> 用户超过了限制登录次数，已被禁止登录。还剩："
                            +"<em style='color: red'>"+res.data.loginLockTime+"</em>";
                        layer.msg(result, {icon: 5,anim: 6});
                    } else {
                        $("#password").val("");
                        var result = "密码错误,在 "+"<em style='color: red'>"+res.data.loginInvalidTime+"</em>"+" 内还允许输入错误 "
                            +"<em style='color: red'>"+res.data.allowNum+"</em>"+" 次";
                        layer.msg(result, {icon: 5,anim: 6});
                    }
                    return false;
                }
            },
            error:function(){
                layer.close(layIndex);
                layer.alert('操作失败！！！',{icon:5});
                return false;
            }
        });
        return false;
    });

    //修改密码监听提交
    form.on('submit(changePwd)', function(data) {
        var param=data.field;//定义临时变量获取表单提交过来的数据，非json格式
        console.log(JSON.stringify(param));//测试是否获取到表单数据，调试模式下在页面控制台查看
        var layIndex = layer.load(2, {
            shade: [0.1, '#393D49']
        });
        $.ajax({
            url:"/Safety/changePwd",
            type:'post',//method请求方式，get或者post
            dataType:'json',//预期服务器返回的数据类型
            data:JSON.stringify(param),//表格数据序列化
            contentType: "application/json; charset=utf-8",
            success:function(res){//res为相应体,function为回调函数
                console.log(res);
                if(res){
                    layer.msg('修改成功');
                    window.open("/Login/login", "_parent");
                }else {
                    layer.close(layIndex);

                    return false;
                }
            },
            error:function(){
                layer.close(layIndex);
                layer.alert('操作失败！！！',{icon:5});
                return false;
            }
        });
        return false;
    });

    //注册
    form.on('submit(register)', function(data) {
        if ($checkPhone != "手机号可用") {
            $("#checkPhone").trigger("blur");
            return false;
        }
        var param=data.field;//定义临时变量获取表单提交过来的数据，非json格式
        console.log(JSON.stringify(param));//测试是否获取到表单数据，调试模式下在页面控制台查看
        var layIndex = layer.load(2, {
            shade: [0.1, '#393D49']
        });
        $.ajax({
            url:"/Admin/register",
            type:'post',//method请求方式，get或者post
            dataType:'json',//预期服务器返回的数据类型
            data:JSON.stringify(param),//表格数据序列化
            contentType: "application/json; charset=utf-8",
            success:function(res){//res为相应体,function为回调函数
                console.log(res);
                if(res.code == 200){
                    layer.close(layIndex);
                    layer.msg("注册成功");
                    return false;
                } else {
                    layer.close(layIndex);
                    createCode();
                    $("#yzm").val("");
                    layer.msg(res.message, {icon: 5,anim: 6});
                    return false;
                }
            },
            error:function(){
                layer.close(layIndex);
                layer.alert('操作失败！！！',{icon:5});
                return false;
            }
        });
        return false;
    });
});