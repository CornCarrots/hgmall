var checkArray = new Array();
$(
    function () {
        var bean = {
            uri: "/foreOrderItem",
            number: 0,
            sum: 0.00,
            stores: [],
            temp: 0,
            message: {
                id: 0,
                uid: 0,
                createDate: null,
                text: '',
                reply: null,
                replyDate: null,
                status: 1,
                type: 'type_message',
                sid:0
            }
        };
        var cartVue = new Vue(
            {
                el: ".main",
                data: bean,
                mounted: function () {
                    this.list();
                },
                methods: {
                    list: function () {
                        var url = getPath() + this.uri+"?timeStamp="+new Date().getTime();
                        axios.get(url).then(
                            function (value) {
                                if(value.data.length>0)
                                {
                                    cartVue.stores = value.data;
                                    $(cartVue.stores.length).each(function (i) {
                                        checkArray.push(false);
                                    })
                                    $(".shoppingCar").show();
                                    $(".notfound_list").hide();
                                }
                                else
                                {
                                    $(".shoppingCar").hide();
                                    $(".notfound_list").show();
                                }

                            }
                        );
                    },
                    getImage: function (name, id) {
                        if (name == null || id == 0)
                            return;
                        else {
                            var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                            return url;
                        }
                    },
                    getStore: function (id) {
                        var parm = window.btoa("sid=" + id+"&timeStamp="+new Date().getTime());
                        var url = getPath() + "/store?"+parm;
                        return url;
                    },
                    getProduct: function (id) {
                        var parm = window.btoa("pid=" + id+"&timeStamp="+new Date().getTime());
                        var url = getPath() + "/product?"+parm;
                        return url;
                    },
                    updateItem: function (id, number) {
                        var url = getPath() + "/foreOrderItem/" + id+"&timeStamp="+new Date().getTime();
                        var bean = {number: number};
                        axios.put(url, bean).then(function (value) {
                            cartVue.list();
                            if ($("input#" + id).prop("checked")) {
                                cartVue.number += value.data.number;
                                cartVue.sum += value.data.product.promotePrice * value.data.number;
                            }
                        });
                    },
                    up: function (id, i,j) {
                        var item = cartVue.stores[j].orderItems[i];
                        if ($("input#" + id).prop("checked")) {
                            cartVue.number -= item.number;
                            cartVue.sum -= item.product.promotePrice * item.number;
                        }
                        item.number++;
                        if (item.number >= item.product.stock)
                            item.number = cartVue.orderItems[i].product.stock;
                        cartVue.updateItem(id, item.number);
                    },
                    down:
                        function (id, i,j) {
                            var item = cartVue.stores[j].orderItems[i];
                            if ($("input#" + id).prop("checked")) {
                                cartVue.number -= item.number;
                                cartVue.sum -= item.product.promotePrice * item.number;
                            }
                            item.number--;
                            if (item.number < 1)
                                item.number = 1;
                            cartVue.updateItem(id, item.number);
                        },
                    setNumber: function (e, id, i,j) {
                        var item = cartVue.stores[j].orderItems[i];
                        if ($("input#" + id).prop("checked")) {
                            cartVue.number -= item.number;
                            cartVue.sum -= item.product.promotePrice * item.number;
                        }
                        item.number = $(e.target).val();
                        if ($(e.target).val() < 1)
                            item.number = 1;
                        if ($(e.target).val() >= item.product.stock)
                            item.number = item.product.stock;
                        cartVue.updateItem(id, item.number);
                    },
                    deleteItem: function (id) {
                        $.confirm({
                            title: '确定删除',
                            content: '您确定不买这个商品了吗？',
                            buttons: {
                                确定: function () {
                                    var url = getPath() + "/foreOrderItem/" + id+"?timeStamp="+new Date().getTime();
                                    axios.delete(url).then(function (value) {
                                        console.log(value)
                                        if (value.data.length == 0) {
                                            cartVue.list();
                                            $.alert('成功删除!');
                                        }
                                        else {
                                            $.alert('删除失败，请重试!');
                                        }

                                    });
                                },
                                取消: function () {
                                    $.alert('成功取消!');
                                }
                            }
                        });
                    },
                    select: function (e, i,j) {
                        var item = cartVue.stores[j].orderItems[i];
                        var check = $(e.target).prop("checked");
                        if (check) {
                            cartVue.sum += item.product.promotePrice * item.number;
                            cartVue.number += item.number;
                            $(".myShoppingButton").css("background-color", "#C40000");
                            $(".myShoppingButton").removeAttr("disabled");
                        }
                        else {
                            cartVue.sum -= item.product.promotePrice * item.number;
                            cartVue.number -= item.number;
                            if (cartVue.number < 0)
                                cartVue.number = 0;
                            if (cartVue.number == 0) {
                                $(".myShoppingButton").css("background-color", "#aaaaaa");
                                $(".myShoppingButton").attr("disabled", "disabled");
                            }
                        }
                        _checkOne_(j);
                        _checkOne();
                    },
                    selectAll: function (e) {
                        var check = $(e.target).prop("checked");
                        if (check) {
                            $(cartVue.stores).each(
                                function (i, data) {
                                    $(data.orderItems).each(
                                        function (j, item) {
                                            cartVue.sum += item.product.promotePrice * item.number;
                                            cartVue.number += item.number;
                                        }
                                    )
                                }
                            );
                            $(".myShoppingButton").css("background-color", "#C40000");
                            $(".myShoppingButton").removeAttr("disabled");
                        }
                        else {
                            cartVue.sum = 0.00;
                            cartVue.number = 0;
                            $(".myShoppingButton").css("background-color", "#aaaaaa");
                            $(".myShoppingButton").attr("disabled", "disabled");
                        }
                        _checkAll();
                    },
                    selectAll_: function (e,j) {
                        var check = $(e.target).prop("checked");
                        if (check) {
                            var arr = new Array();
                            $("input[name=checkbox"+j+"]").each(function (i,data) {
                                if ($(data).prop("checked")== false)
                                {
                                    arr.push($(data).attr("id"));
                                }
                            });
                            $(cartVue.stores[j].orderItems).each(
                                function (i, item) {
                                    $(arr).each(function (k,id) {
                                        if(id==item.id)
                                        {
                                            cartVue.sum += item.product.promotePrice * item.number;
                                            cartVue.number += item.number;
                                        }
                                    })
                                }
                            );
                            $(".myShoppingButton").css("background-color", "#C40000");
                            $(".myShoppingButton").removeAttr("disabled");
                        }
                        else {
                            $(cartVue.stores[j].orderItems).each(
                                function (i, item) {
                                    cartVue.sum -= item.product.promotePrice * item.number;
                                    cartVue.number -= item.number;
                                }
                            );
                            if(cartVue.number==0)
                            {
                                $(".myShoppingButton").css("background-color", "#aaaaaa");
                                $(".myShoppingButton").attr("disabled", "disabled");
                            }
                        }
                        _checkAll_(j);
                        _checkOne();
                    },
                    submit: function () {
                        var uri = "";
                        $(".checkOne input").each(function (i,data) {
                            var id = $(data).attr("id");
                            if($(data).prop("checked")==true)
                            {
                                if(i==0)
                                    uri += "oiid="+ id;
                                else
                                    uri += "&oiid=" + id;
                            }
                        });
                        var param = window.btoa(uri+"&timeStamp="+new Date().getTime());
                        var url = getPath() + "/orderItem?" + param;
                        location.href = url;
                    },
                    sendButton:function(){
                        if(cartVue.message.text.length==0)
                        {
                            $.alert({
                                title: '内容不能为空!',
                                content: '请检查您的留言内容并尝试重新提交',
                                theme:'modern',
                                icon: 'fa fa-close'
                            });
                            return;
                        }
                        else
                        {
                            var url = getPath() + "/foreMessage?timeStamp="+new Date().getTime();
                            axios.post(url, cartVue.message).then(function (value) {
                                if (value.data == 'ok')
                                {
                                    $.alert({
                                        title: '留言成功!',
                                        content: '请留意您的邮箱，等待我们的工作人员答复',
                                        theme:'modern',
                                        icon: 'fa fa-check'
                                    });
                                    $("#addMessageModel").modal("hide");
                                }
                                else
                                {
                                    $.alert({
                                        title: '留言失败!',
                                        content: '请重新留言',
                                        theme:'modern',
                                        icon: 'fa fa-close'
                                    });
                                }

                            });
                        }
                    },
                    sendMessage:function (sid) {
                        cartVue.message.sid = sid;
                        $("#addMessageModel").modal("show");
                    }
                }
            }
        );
        var check1 = false;
        var check2 = false;
        var check4 = false;
//全选
        function _checkAll(){
            if(!check1)
            {
                $(".checkOne input").prop("checked",true);
                $(".checkAllTH_ input").prop("checked",true);
                $("#checkAllTH_1 input,#checkAllTH_2 input").prop("checked",true);
                check1 = true;
            }
            else
            {
                $(".checkOne input").prop("checked",false);
                $(".checkAllTH_ input").prop("checked",false);
                $("#checkAllTH_1 input,#checkAllTH_2 input").prop("checked",false);
                check1 = false;
            }
        }
        function _checkAll_(j){
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
        function _checkOne()
        {
            $(".checkOne input").each(function (i,data) {
                check2 = $(data).prop("checked");
                // console.log(check2);
                if(check2==false)
                    return false;
            });
            if(check2==true)
            {
                $("#checkAllTH_1 input,#checkAllTH_2 input").prop("checked",true);
                check2 = false;
                check1 = true;
            }
            else
            {
                $("#checkAllTH_1 input,#checkAllTH_2 input").prop("checked",false);
                check1 = false;
            }
        }
//选择一个
        function _checkOne_(j)
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
    }
);