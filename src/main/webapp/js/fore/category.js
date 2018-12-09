function compare(num1, num2) {
    var low = parseFloat(num1);
    var high = parseFloat(num2);
    // console.log(low + " " + high);
    var product = $(".item_price1");
    if (!isNaN(low) && !isNaN(high) && low <= high) {

        $(product.parent().parent().parent()).hide();
        // for (let i = 0; i < product.length; i++) {
        //     $(product[i]).parent().parent().css("display","none");
        // }
        $(product).each(function () {
            var child = parseFloat($(this).text().substring(1));
            if (child < high && child > low)
                $(this).parent().parent().parent().show();
        });
    }
    else
        $(product.parent().parent()).show();
};
$(function () {

    var count = true;
    var bean = {
        uri: "/foreCategory",
        categories: [],
        hot: [],
        advertisement: {},
        category: {products:[{store:{name:0,id:0},promotePrice:0,originalPrice:0,name:'',subTitle:'',firstProductImage:{id:0}}]}
    };
    var categoryVue = new Vue(
        {
            el: ".main",
            data: bean,
            mounted: function () {
                this.get('all');
            },
            methods: {
                get: function (order) {
                    var cid = getUrlParms("cid");
                    var url = getPath() + this.uri + "/" + cid + "?orderItem=" + order + "&sort=" + count+"&timeStamp="+new Date().getTime();
                    count = !count;
                    axios.get(url).then(function (value) {
                        console.log(value.data)
                        categoryVue.categories = value.data.categories;
                        categoryVue.category = value.data.category;
                        categoryVue.hot = value.data.hot;
                        categoryVue.advertisement = value.data.advertisement;
                        Vue.nextTick(function () {
                            $(".category_price").keyup(function () {
                                var num1 = $("#lowPrice").val();
                                var num2 = $("#highPrice").val();
                                compare(num1, num2);
                            });
                        })
                    });
                },
                getCategory: function (id) {
                    var param = window.btoa("cid="+id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/category?"+param;
                    return url;
                },
                getProduct: function (id) {
                    var param = window.btoa("pid=" + id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/product?"+param;
                    return url;
                },
                getImage: function (name, id) {
                    if (name == undefined || id == 0 ||id ==undefined)
                        return;
                    else {
                        var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                        return url;
                    }

                },
                getAdvertisement: function (id) {
                    if (id == undefined)
                        return;
                    var url = getPath() + "/image/advertisement_category/" + id + ".jpg";
                    return url;
                },
                getStore: function (id) {
                    var param = window.btoa("sid=" + id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/store?"+param;
                    return url;
                },
                sort: function (order, e) {
                    $(".category_sort").removeClass("active");
                    $(e.target).parent().addClass("active");
                    categoryVue.get(order);
                }
            }
        }
    );


});
