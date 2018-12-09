function smallImg(imgID) {
    var id = imgID;
    // console.log(id);
    $(".productImg_big").children().attr("src", getPath() + "/image/productSingle/" + id + ".jpg");
};

function productPageRegister() {
    var imgs = $(".productImg_small").children();
    imgs.each(
        function () {
            $(this).mouseenter(
                function () {
                    smallImg($(this).attr("imgID"));
                    // console.log($(this).attr("imgID"))
                }
            );

        });
    var big = $(".productImg_big").children();
    big.on("onload",
        function () {
            // console.log("1");
            imgs.each(
                function () {
                    var id = $(this).attr("imgID");
                    var url = getPath() + "/image/productSingle/" + id + ".jpg";
                    // console.log(".." + url);
                    // console.log($(this).attr("imgID"))
                    var image = new Image();
                    image.attr("src", url);
                    var div = $("#loadImg");
                    image.onload = function () {
                        // console.log(url);
                        div.appendChild($(image));
                    };

                }
            );
        }
    );
    var up = $("#numberUp");
    var down = $("#numberDown");
    var number = $("#number").children();
    var maxNumber = parseInt($("#maxNumber").text());
    var numberValue = parseInt(number.val());


    var title = $(".product_nav").children();
    title.click(
        function () {
            var className = $(this).attr("class").split(" ");
            //
            if ("product_navTitle" == className[0]) {
                $(".product_access").hide();
                $(".product_detail").show();
            }
            else {
                $(".product_detail").hide();
                $(".product_access").show();
            }

        }
    );
};
$(function () {

    var bean = {
        uri: "/foreProduct",
        product: {store:{name:'',id:0,cid:0},category:{id:0}},
        properties: [],
        propertyValues: [],
        pages: [],
        products: [],
        user: {name: '', password: ''},
        flag: false,
        number: 1,
        email: ''
    };
    var productVue = new Vue(
        {
            el: ".main",
            data: bean,
            mounted: function () {
                this.get(0);
            },
            filters:{
                anonyNameFilter:function (name) {
                    if(null==name)
                        return null;

                    if(name.length<=1)
                        return "*";

                    if(name.length==2)
                        return name.substring(0,1) +"*";
                    var arr = name.split('');
                    for (var i = 1; i < arr.length-1; i++) {
                        arr[i] = '*';
                    }
                    return arr.join('');
                }
            },
            computed: {
                subTitle: function () {
                    if (this.product.subTitle == undefined)
                        return;
                    var text = this.product.subTitle;
                    return text.split(" ");
                },
                payDate: function () {
                    var oldDate = new Date();
                    var newDate = new Date();
                    newDate.setDate(Math.round(Math.random() * 7) + oldDate.getDate());
                    newDate.setHours(Math.round(Math.random() * 24));
                    newDate.setMinutes(Math.round(Math.random() * 60));
                    newDate.setSeconds(Math.round(Math.random() * 60));
                    newDate = newDate - oldDate;
                    var formatString = 'DD天 HH时mm分ss秒';
                    return moment(newDate).format(formatString);
                    return formatDateFilter
                }
            },
            methods: {
                get: function (start) {
                    var pid = getUrlParms("pid");
                    var url = getPath() + this.uri + "/" + pid + "?start=" + start+"&timeStamp="+new Date().getTime();
                    axios.get(url).then(
                        function (value) {
                            productVue.product = value.data.product;
                            console.log(productVue.product)
                            var arr = [];
                            $.each(value.data.properties, function (i, data) {
                                arr.push(data);
                                if ((i + 1) % 3 == 0 || i == value.data.properties.length - 1) {
                                    productVue.properties.push(arr);
                                    arr = new Array();
                                }
                            });
                            productVue.propertyValues = value.data.propertyValues;
                            productVue.pages = value.data.page;
                            productVue.products = value.data.page.content;
                            productVue.$nextTick(function () {
                                $("#loginForm").validationEngine(
                                    {
                                        promptPosition: 'topRight',
                                        showArrow: true
                                    }
                                );
                                if (productVue.product.reviews == 0) {
                                    $(".product_access_comment").hide();
                                    $(".product_access_comment404").show();
                                }
                                else {
                                    $(".product_access_comment404").hide();
                                    $(".product_access_comment").show();
                                }
                                var h = $(".product_detail_info").css("height");
                                h = h.substring(0, h.indexOf("px") + 1);
                                h = parseInt(h);
                                h += productVue.properties.length * 35;
                                $(".product_detail_info").css("height", h);
                                productPageRegister();
                            })
                        }
                    );
                },
                getCategory: function (id) {
                    var param = window.btoa("cid="+id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/category?"+param;
                    return url;
                },
                jump: function (page) {
                    jump(page, productVue);
                },
                getImage: function (name, id) {
                    if (name == undefined || id == 0)
                        return;
                    else {
                        var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                        return url;
                    }

                },
                up: function () {
                    // console.log(numberValue);
                    productVue.number++;
                    if (productVue.number >= productVue.product.stock)
                        productVue.number = productVue.product.stock;
                },
                down:
                    function () {
                        productVue.number--;
                        if (productVue.number < 1)
                            productVue.number = 1;
                    },
                setNumber: function () {
                    if(productVue.number<1)
                        productVue.number = 1;
                    if (productVue.number >= productVue.product.stock)
                        productVue.number = productVue.product.stock;
                },
                getProduct: function (id) {
                    var param = window.btoa("pid=" + id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/product?"+param;
                    return url;
                },
                getStore: function (id) {
                    var param = window.btoa("sid=" + id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/store?"+param;
                    return url;
                },
                buyProduct: function () {
                    var url = getPath() + "/foreLoginCheck"+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(function (value) {
                        if (value.data == "fail")
                            $("#loginModel").modal("show");
                        else {
                            url = getPath() + "/foreAccountCheck" + "?timeStamp=" + new Date().getTime();
                            axios.get(url).then(function (value) {
                                if (value.data == "fail")
                                    $.dialog({
                                        title: '您还没有开通个人账户!',
                                        content: '先去个人中心开通账户吧',
                                        theme:'modern',
                                        icon: 'fa fa-close'
                                    });
                                else {
                                    var bean = {
                                        pid: productVue.product.id,
                                        number: productVue.number,
                                        sid: productVue.product.store.id
                                    };
                                    var url = getPath() + "/foreBuyProduct";
                                    axios.post(url, bean).then(
                                        function (value1) {
                                            var param = "oiid=" + value1.data + "&timeStamp=" + new Date().getTime();
                                            var url = window.btoa(param);
                                            location.href = getPath() + "/orderItem?" + url;
                                        }
                                    )
                                }

                            });
                        }
                    });
                },
                addCart: function (e) {
                    var url = getPath() + "/foreLoginCheck"+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(function (value) {
                        if (value.data == "fail")
                            $("#loginModel").modal("show");
                        else {
                            var bean = {pid: productVue.product.id, number: productVue.number,sid:productVue.product.store.id};
                            var url = getPath() + "/foreBuyProduct"+"?timeStamp="+new Date().getTime();
                            axios.post(url, bean).then(
                                function (value1) {
                                    if(value.data =="max"){
                                        $.dialog({
                                            title: '添加失败!',
                                            content: '您的购物车太多宝贝啦',
                                            theme:'modern',
                                            icon: 'fa fa-close'
                                        });
                                    }
                                    if(value.data =="success" )
                                    {
                                        $.dialog({
                                            title: '添加成功!',
                                            content: '您的宝贝正在购物车等你呢',
                                            theme:'modern',
                                            icon: 'fa fa-check'
                                        });
                                        var button = $(e.target);
                                        if(!button.hasClass("product_Info_shop"))
                                            button = button.parent();
                                        button.html("已加入购物车");
                                        button.attr("disabled","disabled");
                                        button.css("background-color","lightgray");
                                        button.css("border-color","lightgray");
                                        button.css("color","black");
                                    }
                                }
                            )
                        }
                    });
                },
                loginUser: function () {
                    if (!$("#loginForm").validationEngine("validate"))
                        return false;
                    var url = getPath() + "/foreLoginUser"+"?timeStamp="+new Date().getTime();
                    axios.post(url, productVue.user).then(function (value) {
                        if (value.data == "success")
                            location.reload();
                        if (value.data == "user404")
                            $("#nameField").validationEngine("showPrompt", "没有此用户", "error");
                        if (value.data == "fail")
                            $("#passField").validationEngine("showPrompt", "密码错误", "error");
                    })
                },
                cleanName: function () {
                    productVue.user.name = '';
                    $("#nameField").val('');
                    $("#nameField").focus();
                },
                togglePass: function () {
                    productVue.flag = !productVue.flag;
                    if (productVue.flag) {
                        $("#passField").attr("type", "text");
                        $("#seePass").toggleClass("fa-eye");
                        $("#seePass").toggleClass("fa-eye-slash");
                    }
                    else {
                        $("#passField").attr("type", "password");
                        $("#seePass").toggleClass("fa-eye-slash");
                        $("#seePass").toggleClass("fa-eye");
                    }
                },
                forgetPass: function () {
                    $("#loginModel").modal("hide");
                    $("#forgetModel").modal("show");
                },
                forgetButton: function () {
                    var random;
                    $.confirm({
                            title: '请稍后',
                            content: '<div class="ball"></div>\n' +
                                '<div class="ball1"></div>',
                            buttons: {
                                ok:{
                                    text: '确定',
                                    btnClass: 'btn-blue',
                                },
                                formSubmit: {
                                    text: '确定',
                                    btnClass: 'btn-blue',
                                    action: function () {
                                        var key = this.$content.find('.key').val();
                                        var pass = this.$content.find('.pass').val();
                                        if (!key || !pass) {
                                            $.alert('请输入一个有效的值');
                                            return false;
                                        }
                                        var bean = {random: random, key: key, email: productVue.email, pass: pass};
                                        var url = getPath() + "/foreUserPass?timeStamp=" + new Date().getDate();
                                        axios.post(url, bean).then(
                                            function (value) {
                                                if (value.data == 'ok') {
                                                    $.dialog({
                                                        title: '恭喜您,密码修改成功!',
                                                        content: '快去登录吧',
                                                        theme: 'modern',
                                                        icon: 'fa fa-smile-o'
                                                    });
                                                }
                                                else {
                                                    $.alert({
                                                        title: '抱歉!',
                                                        content: '验证码错误',
                                                        theme: 'modern',
                                                        icon: 'fa fa-close'
                                                    });
                                                    return false;
                                                }
                                            }
                                        );
                                    }
                                },
                                '取消': function () {
                                }
                            },
                            onOpenBefore: function () {
                                var self = this;
                                self.buttons['formSubmit'].hide();
                                self.buttons['ok'].hide();
                                self.buttons['取消'].hide();
                            },
                            onContentReady: function () {
                                var self = this;
                                var url = getPath() + "/forgetUser?email=" + productVue.email + "&timeStamp=" + new Date().getDate();
                                return axios.get(url).then(
                                    function (value) {
                                        if (value.data.result == 'yes') {
                                            $("#forgetModel").modal("hide");
                                            random = value.data.random;
                                            self.setContent(
                                                '<div class="form-group">' +
                                                '<label>验证码已发送到您的邮箱，请查看</label>' +
                                                '<input type="text" placeholder="请输入验证码" class="key form-control"/>' +
                                                '</div>' +
                                                '<div class="form-group">' +
                                                '<label>请输入您要修改的密码</label>' +
                                                '<input type="text" placeholder="请输入密码" class="pass form-control"/>' +
                                                '</div>'
                                            );
                                            self.setTitle("修改密码");
                                            self.buttons['formSubmit'].show();
                                            self.buttons['取消'].show();
                                        }
                                        else {
                                            self.setContent(
                                                '<div>你输入的邮箱有误, 请重新输入' +
                                                '</div>'
                                            );
                                            self.buttons['ok'].show();
                                            self.buttons['取消'].show();
                                        }
                                    });
                            }
                        }
                    );
                }
            }
        }
    );
});