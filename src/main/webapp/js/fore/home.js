
$(function () {
    var bean = {
        uri:"/foreHome",
        categories:[{productShow:[{subTitle:'',name:'',firstProductImage:{id:0}}]}],
        carousels:[]

    };
    var homeVue = new Vue(
        {
            el:".home",
            data:bean,
            mounted:function () {
                this.get();
            },
            methods:{
                get:function () {
                    var url = getPath() + this.uri+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(function (value) {
                        console.log(value)
                        homeVue.categories = value.data.categories;
                        homeVue.carousels = value.data.carousels;
                        homeVue.$nextTick(function(){
                            homePageRegisterListeners();
                        })
                    });
                },
                getCarousel:function (id) {
                    var url = getPath()+"/image/carousel_home/"+id+".jpg";
                    return url;
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
                    if(id==null||id==0)
                        return;
                    var url = getPath() + "/image/productSingle_middle/" + id + ".jpg";
                    return url;
                },
            }
        }
    );
    function showProductsAsideCategorys(cid){
        $("div.indexCate_categy div[cid="+cid+"]").css("background-color","#e2e2e3");
        $("div.indexCate_categy div[cid="+cid+"] a").css("color","#fa5555");
        $("div.indexCate_categy div[cid="+cid+"] a:hover").css("color","#fa5555");
        $("div.indexCate_cate[cid="+cid+"]").show();
    };
    function hideProductsAsideCategorys(cid){
        $("div.indexCate_categy div[cid="+cid+"]").css("background-color","white");
        $("div.indexCate_categy div[cid="+cid+"] a").css("color","#000");
        $("div.indexCate_categy div[cid="+cid+"] a:hover").css("color","#fa5555");
        $("div.indexCate_cate[cid="+cid+"]").hide();
    };
    function homePageRegisterListeners() {
        $("div.indexCate_categy div").mouseenter(function(){
            var cid = $(this).attr("cid");
            if(cid==null)
                return;
            showProductsAsideCategorys(cid);
        });
        $("div.indexCate_categy div").mouseleave(function(){
            var cid = $(this).attr("cid");
            if(cid==null)
                return;
            hideProductsAsideCategorys(cid);
        });
        $("div.indexCate_cate").mouseenter(function(){
            var cid = $(this).attr("cid");
            if(cid==null)
                return;
            showProductsAsideCategorys(cid);
        });
        $("div.indexCate_cate").mouseleave(function(){
            var cid = $(this).attr("cid");
            if(cid==null)
                return;
            hideProductsAsideCategorys(cid);
        });
        $("div.indexCate_cate .row a").each(
            function () {
                var v = Math.round(Math.random() *6);
                if(v == 1)
                    $(this).css("color","rgb(135, 206, 250)");
            }
        );
    }


});

