$(function () {
    var bean = {
        uri:"/foreRegister",
        user:{id:0,name:'',nickName:'',password:'',salt:'',account:0,sex:0,registerDate:null,openDate:null,address:'',
            mobile:'',email:'',mid:0,score:0,status:0}
    };
    var registerVue = new Vue(
        {
            el:".main",
            data:bean,
            mounted:function(){
              Vue.nextTick(function () {
                  $("#registerForm").validationEngine(
                      {
                          promptPosition:'centerRight',
                          showArrow:true
                      }
                  );
                  valControls( $("#registerForm")) ;
              });
            },
            methods:{
                register:function () {
                    if(controlId.length > 0) {
                        alertinfo(0) ;
                        return false ;
                    }
                    if(!$("#registerForm").validationEngine("validate")) return false;  //验证没有使用ajax验证的控件(不是ajax验证的字段是可以正常验证的, 不通过就返回)
                    var url = getPath()+this.uri;
                    axios.post(url,registerVue.user).then(function (value) {
                            location.href=getPath()+"/registerSuccess"+"?timeStamp="+new Date().getTime();
                    });
                    return false;
                },
                reset:function () {
                    registerVue.user = {id:0,name:'',nickName:'',password:'',salt:'',account:0,sex:0,registerDate:null,openDate:null,address:'',
                        mobile:'',email:'',mid:0,score:0,status:0}
                },
                getUrl:function (key) {
                    return getPath()+key;
                }
            }
        }
    );

});