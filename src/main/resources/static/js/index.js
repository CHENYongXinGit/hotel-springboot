
layui.use(['layer','colorpicker','element','jquery'], function(){
    var layer = layui.layer
        ,$ = layui.jquery
        ,colorpicker = layui.colorpicker
        ,element = layui.element//导航的hover效果、二级菜单等功能，需要依赖element模块
        ,setIframe = function(){
            var height = $(window).height() - 107;
            $('#demoAdmin').height(height);
        };

    setIframe();
    $(window).on('resize', setIframe);

    $("#fu").click(function () {
        layer.open({
            type: 1
            ,shade:0.1
            ,shadeClose:true
            ,title: false
            ,anim: 1
            ,closeBtn: false
            ,offset: ['55px', '82%']
            ,content:"<div><input type=\"hidden\" name=\"color\" value=\"\" id=\"test-all-input\">\n" +
                "  <div id=\"te\"></div></div>"
        });
        //开启全功能
        colorpicker.render({
            elem: '#te'
            ,color: 'rgba(0, 0, 0, 1)'
            ,format: 'rgb'
            ,predefine: true
            ,alpha: true
            ,done: function(color){
                $('#test-all-input').val(color); //向隐藏域赋值
                layer.tips('给指定隐藏域设置了颜色值：'+ color, this.elem);

                color || this.change(color); //清空时执行 change
            }
            ,change: function(color){
                //给当前页面头部和左侧设置主题色
                $('.header-demo,.layui-side .layui-nav').css('background-color', color);
                //$('.layui-side').removeClass('layui-bg-black');
                $('.bg-black').css('background-color', color);
            }
        });
    });

    //锁屏
    $("#lockScreen").on('click',function () {
        layer.open({
            type: 2,
            id:'edit',
            area: ['348px', '208px'],
            resize:false,
            title: false,
            closeBtn: false,
            shade: 0.9,
            content: ['/Safety/lockScreen', 'no']
        });
    });

    //时钟
    function changeTitle () {
        var myDate = new Date();
        var YY = myDate.getFullYear();
        var MM = myDate.getMonth()+1;
        var dd = myDate.getDate();
        var hour = myDate.getHours();
        var minutes = myDate.getMinutes();
        var seconds = myDate.getSeconds();
        if(hour<10)
            hour = "0"+hour;
        if(minutes<10)
            minutes = "0"+minutes;
        if(seconds<10)
            seconds = "0"+seconds;
        $("#time").html(YY+"年"+MM+"月"+dd+"日&nbsp;"+hour+":"+minutes+":"+seconds);
        setTimeout(changeTitle,1000);
    }
    changeTitle ();

    //隐藏菜单
    var i=0;
    $('#navSorH').click(function(){
        if(i==0){
            $(".layui-side").animate({width:'0px'});
            $(".layui-body").animate({left:'0px'});
            $(".layui-footer").animate({left:'0px'});
            $(".shrink i").addClass("layui-icon-spread-left");
            $(".shrink i").removeClass("layui-icon-shrink-right");
            i++;
        }else{
            $(".layui-side").animate({width:'200px'});
            $(".layui-body").animate({left:'200px'});
            $(".layui-footer").animate({left:'200px'});
            $(".shrink i").addClass("layui-icon-shrink-right");
            $(".shrink i").removeClass("layui-icon-spread-left");
            i--;
        }
    });

    // 全屏代码
    $("body").on("click", "#fullScreen", function () {
        if ($(this).children("i").hasClass("layui-icon-screen-restore")) {
            screenFun(2);
            $("#fullScreen i").addClass("layui-icon-screen-full");
            $("#fullScreen i").removeClass("layui-icon-screen-restore");
        } else {
            screenFun(1);
            $("#fullScreen i").addClass("layui-icon-screen-restore");
            $("#fullScreen i").removeClass("layui-icon-screen-full");
        }
    });
    function screenFun(num) {
        num = num * 1;
        var docElm = document.documentElement;

        switch (num) {
            case 1:
                if (docElm.requestFullscreen) {
                    docElm.requestFullscreen();
                } else if (docElm.mozRequestFullScreen) {
                    docElm.mozRequestFullScreen();
                } else if (docElm.webkitRequestFullScreen) {
                    docElm.webkitRequestFullScreen();
                } else if (docElm.msRequestFullscreen) {
                    docElm.msRequestFullscreen();
                }
                break;
            case 2:
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
                break;
        }
    }

    //系统公告
    $(document).on("click","#notice",function () {
        noticeFun();
    });

    function noticeFun() {
        layer.open({
            type: 0,
            title: "系统公告",
            btn: "我知道啦",
            btnAlign: 'c',
            content: "糖果酒店管理系统3.0上线啦(^し^)<br />" +
                "在此郑重承诺该系统<span style='color:#5cb85c'>永久免费</span>为大家提供" +
                "<br />谢谢大家支持！！！",
            yes: function(index, layero){
                layer.tips('公告跑这里啦！', '#notice', {
                    tips: [1, '#000'],
                    time: 2000
                });
                layer.close(index);
            },
            cancel: function(index, layero){
                layer.tips('公告跑这里啦！', '#notice', {
                    tips: [1, '#000'],
                    time: 2000
                });
            }
        });
    }

    //刷新权限
    $(document).on("click","#refresh",function () {
        var layIndex = layer.load(2, {
            shade: [0.1, '#393D49']
        });
        $.ajax({
            url:"/refresh",
            type:'get',//method请求方式，get或者post
            dataType:"json",
            success:function(res){//res为相应体,function为回调函数
                if(res){
                    setTimeout(function() {
                        layer.close(layIndex);
                        layer.msg("刷新完成");
                    }, 500);
                }
            },
            error:function(){
                layer.close(layIndex);
                layer.alert('操作失败！！！',{icon:5});
            }
        });
    });

});

