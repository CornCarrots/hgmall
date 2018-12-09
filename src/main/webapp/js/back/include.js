
//过滤器
Vue.filter("addressFilter",function (value) {
    if (!value)
        return '';
    var reg = new RegExp("-","g");//g,表示全部替换。
    return value.replace(reg,"")
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
var check1 = false;
var check2 = false;
//全选
function checkAll(){
    if(!check1)
    {
        $(".checkOne input").prop("checked",true);
        check1 = true;
    }
    else
    {
        $(".checkOne input").prop("checked",false);
        check1 = false;
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
