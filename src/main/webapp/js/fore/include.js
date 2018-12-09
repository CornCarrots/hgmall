$(
    function () {

        $("#keyword").keypress(function (e) {
            if (e.which == 13) {
                search();
            }
        });

        $("#search").click(
            function () {
                search();
            }
        );

        $.goup({
            trigger: 100,
            bottomOffset: 10,
            locationOffset: 30,
            // title: 'Top',
            titleAsText: true
        });

        $("#categorySelect").selectpicker('refresh');
        $("#categorySelect").selectpicker('render');

        $(".closeButton").click(
            function(){
                $(this).hide();
                $(".top_adv").hide(1500);
            }
        );

        $(".indexNav_mycate").mouseenter(
            function(){
                $(".indexNav_mycate").removeClass("active");
                $(this).addClass("active");
            }
        );
        // $(".indexNav_mycate").click(
        //     function(){
        //         var href = $(this).attr("href");
        //         $(".indexNav_mycate").removeClass("active");
        //         $(this).addClass("active");
        //         return true;
        //     }
        // );
    }
);
//过滤器
Vue.filter("addressFilter",function (value) {
    if (!value)
        return '';
    var reg = new RegExp("-","g");//g,表示全部替换。
    return value.replace(reg,"")
})
Vue.filter("splitFilter",function (value) {
    if(!value)
        return '';
    var str = value.replace(/[\ |\~|\`|\!|\@|\#|\$|\%|\^|\&|\*|\(|\)|\-|\_|\+|\=|\||\\|\[|\]|\{|\}|\;|\:|\"|\'|\,|\<|\.|\>|\/|\?]/g," ");
    str.replace(/[：|— |——|?|【|】|“|”|！|，|。|？|、|~|@|#|￥|%|…|……|&|*|（|）]/g, " ");
    return str.split(" ")[0];
})
Vue.filter("subStringFilter", function(value, start, end){
    if (!value)
        return '';
    return value.substring(start,end);
});

Vue.filter("formatMoneyFilter", function(value){
    return formatMoney(value);
});

Vue.filter('formatDateFilter', function (value, formatString) {
    if(null==value)
        return "";
    formatString = formatString || 'YYYY-MM-DD HH:mm:ss';
    return moment(value).format(formatString);
});
//搜索
function search() {
    var key =$("#keyword").val();
    key = encodeURI(key);
    var param = "key="+key+"&timeStamp="+new Date().getTime();
    var url = window.btoa(param);
    location.href = getPath()+"/search?"+url;
}
//格式化
function formatMoney(num){
    num = new String(num);
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
        cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        num = num.substring(0,num.length-(4*i+3))+','+
            num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + num + '.' + cents);
}
//判断是否为空
function checkEmpty(value,text){

    if(null==value || value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    return true;
}
function getPath() {
    var path = location.pathname;
    path = path.substring(0,path.substring(1).indexOf("/")+1);
    return path
}
//获取地址栏参数的函数
function getUrlParms(para){
    var search=location.search; //页面URL的查询部分字符串
    var arrPara=new Array(); //参数数组。数组单项为包含参数名和参数值的字符串，如“para=value”
    var arrVal=new Array(); //参数值数组。用于存储查找到的参数值

    if(search!=""){
        var index=0;
        search=search.substr(1); //去除开头的“?”
        search = window.atob(search);
        arrPara=search.split("&");

        for(i in arrPara){
            var paraPre=para+"="; //参数前缀。即参数名+“=”，如“para=”
            if(arrPara[i].indexOf(paraPre)==0&& paraPre.length<arrPara[i].length){
                arrVal[index]=decodeURI(arrPara[i].substr(paraPre.length)); //顺带URI解码避免出现乱码
                index++;
            }
        }
    }

    if(arrVal.length==1){
        return arrVal[0];
    }else if(arrVal.length==0){
        return null;
    }else{
        return arrVal;
    }
}

//判断是否数字 (小数和整数)
function checkNumber(value, text){

    if(value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    if(isNaN(value)){
        alert(text+ "必须是数字");
        return false;
    }
    return true;
}
//判断是否整数
function checkInt(value, text){

    if(value.length==0){
        alert(text+ "不能为空");
        return false;
    }
    if(parseInt(value)!=value){
        alert(text+ "必须是整数");
        return false;
    }
    return true;
}

//确实是否要删除
function checkDeleteLink(){
    var confirmDelete = confirm("确认要删除");
    if(confirmDelete)
        return true;
    return false;
}
//分页跳转函数，向前跳或者向后跳，或者跳转到第一页或者最后一页。
function jump(page,vue){
    if('first'== page && !vue.pages.first)
        vue.list(0);

    else if('pre'== page &&	vue.pages.hasPrevious )
        vue.list(vue.pages.number-1);

    else if('next'== page && vue.pages.hasNext)
        vue.list(vue.pages.number+1);

    else if('last'== page && !vue.pages.last)
        vue.list(vue.pages.totalPages-1);
}
//分页跳转函数，跳转到指定页
function jumpByNumber(start,vue){
    if(start!=vue.pages.number)
        vue.list(start);
}
var controlId = new Array();  //保存验证不通过的控件ID
var errors = new Array() ;  //保存验证不通过的提示信息
var successes = new Array() ;  //保存验证不通过的提示信息
//表单需要验证的控件
function valControls(ajaxForm2Controls) {
    //获取需要使用ajax验证的控件
    var controlsStr = ajaxForm2Controls.attr("control") ;
    //属性未定义时返回
    if(typeof(controlsStr) === "undefined" || controlsStr.length <= 0) return ;
    //分隔获取控件ID
    var controls = controlsStr.split(/,/g) ;
    for(var i in controls) {
        //添加焦点离开事件
        $("#" + controls[i]).blur(function() {
            if($(this).val().length <= 0) return false;
            //重新设置数组
            controlId.length = 0;
            errors.length = 0 ;
            successes.length = 0 ;
            //错误信息
            var error = $(this).attr("error") ;
            var success = $(this).attr("success") ;
            $.ajax({
                type: "GET",
                url: $(this).attr("url"),
                data: $(this).serialize(),
                dataType: "text",
                success: function(data){
                    if(data=="true") {
                        //验证不通过将错误信息放入数组中
                        controlId.push(controls[i]);
                        errors.push(error) ;
                        //提示信息
                        alertinfo(1) ;
                    }
                    if(data=="false"){
                        successes.push(success) ;
                        //提示信息
                        alertSuccess() ;
                    }
                }
            });
        }) ;
    }
}
function alertinfo(j) {
    if(controlId.length > 0) {
        for(var i in controlId) {
            //validationEngine方法,为指定ID弹出提示.
            //用法:<span style="">$("#id").validationEngine("showPrompt","提示内容","load");
            //<span style="">在该元素上创建一个提示，3 种状态："pass", "error", "load"</span></span>
            $("#" + controlId[i]).validationEngine("showPrompt", errors[i], "error");
            if(j==0)
                $("#" + controlId[i]).focus();
        }
    }
}
function alertSuccess() {
    if(controlId.length <= 0) {
        for(var i in controlId) {
            //validationEngine方法,为指定ID弹出提示.
            //用法:<span style="">$("#id").validationEngine("showPrompt","提示内容","load");
            //<span style="">在该元素上创建一个提示，3 种状态："pass", "error", "load"</span></span>
            $("#" + controlId[i]).validationEngine("showPrompt", successes[i], "pass");
        }
    }
}
var check1 = false;
var check2 = false;
var check4 = false;
//全选
function checkAll(){
    if(!check1)
    {
        $(".checkOne input").prop("checked",true);
        $(".checkAllTH_ input").prop("checked",true);
        check1 = true;
    }
    else
    {
        $(".checkOne input").prop("checked",false);
        $(".checkAllTH_ input").prop("checked",false);
        check1 = false;
    }
}
function checkAll_(j){
    if(!checkArray[j])
    {
        $("input[name=checkbox"+j+"]").prop("checked",true);
        checkArray[j] = true;
    }
    else
    {
        $("input[name=checkbox"+j+"]").prop("checked",false);
        checkArray[j] = false;
    }
}
//选择一个
function checkOne()
{
    $(".checkOne input").each(function (i,data) {
        check2 = $(data).prop("checked");
        // console.log(check2);
        if(check2==false)
            return false;
    });
    if(check2==true)
    {
        $("#checkAllTH input").prop("checked",true);
        check2 = false;
        check1 = true;
    }
    else
    {
        $("#checkAllTH input").prop("checked",false);
        check1 = false;
    }
}
//选择一个
function checkOne_(j)
{
    $("input[name=checkbox"+j+"]").each(function (i,data) {
        check4 = $(data).prop("checked");
        // console.log(check2);
        if(check4==false)
            return false;
    });
    if(check4==true)
    {
        $("#checkAllTH"+j+" input").prop("checked",true);
        check4 = false;
        check3 = true;
    }
    else
    {
        $("#checkAllTH"+j+" input").prop("checked",false);
        check3 = false;
    }
}