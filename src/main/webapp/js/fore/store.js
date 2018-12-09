
$(function () {
    var bean = {
        uri:"/foreStore",
        store:{categories:[{name:'',productShow:[{promotePrice:0,originalPrice:0,name:'',subTitle:'',firstProductImage:{id:0}}]}]},
        sid:0

    };
    var storeVue = new Vue(
        {
            el:".main",
            data:bean,
            mounted:function () {
                this.get();
            },
            methods:{
                get:function () {
                    var sid = getUrlParms("sid");
                    this.sid = sid;
                    var url = getPath() + this.uri+"/"+sid+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(function (value) {
                        console.log(value.data)
                        storeVue.store = value.data;
                    });
                },
                getCategory:function (id) {
                    var param = window.btoa("cid="+id+"&timeStamp="+new Date().getTime());
                    var url = getPath()+"/category?"+param;
                    return url;
                },
                getProduct:function (id) {
                    var param = window.btoa("pid="+id+"&timeStamp="+new Date().getTime());
                    var url = getPath()+"/product?"+param;
                    return url;
                },
                getImage: function (id) {
                    if(id==0)
                        return;
                    var url = getPath() + "/image/productSingle_middle/" + id + ".jpg";
                    return url;
                }
            }
        }
    );
});

