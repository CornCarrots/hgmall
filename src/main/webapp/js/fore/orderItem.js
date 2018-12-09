$(
    function () {
        var bean = {
            uri: "/foreOrderItem",
            stores: [],
            orders: [],
            order: {address: '', post: '', receiver: '', mobile: '', type: '0'},
            numArray: [],
            sumArray: [],
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
        var orderVue = new Vue(
            {
                el: ".main",
                data: bean,
                mounted: function () {
                    this.get();
                },
                computed: {
                    sumArray: function () {
                        return orderVue.sumArray;
                    },
                    sum: function () {
                        var sum = 0;
                        $(this.stores).each(function (i, data1) {
                                $(data1.orderItems).each(function (j, data2) {
                                    sum += data2.product.promotePrice * data2.number;
                                })
                            }
                        );
                        return sum;
                    }

                },
                methods: {
                    get: function () {
                        var ids = getUrlParms("oiid");
                        var url = getPath() + this.uri + "/" + ids;
                        axios.get(url).then(function (value) {
                            orderVue.stores = value.data;
                            $(orderVue.stores).each(function (i, data) {
                                var order = {id:0, orderCode:'', address:'', post:'', receiver:'',
                                    mobile:'', userMessage:'', createDate:null, payDate:null, deliveryDate:null, confirmDate:null,
                                    uid:0, express:'express_zhongtong', status:'', sum:0, quantity:0, payment: "payment_online",
                                    type:'0', sid:0};
                                orderVue.orders.push(order);
                            });
                            $(orderVue.stores).each(
                                function (i, data1) {
                                    var num = 0;
                                    var sum = 0;
                                    $(data1.orderItems).each(
                                        function (j, data2) {
                                            num += data2.number;
                                            sum += data2.product.promotePrice * data2.number;
                                        });
                                    orderVue.numArray.push(num);
                                    orderVue.sumArray.push(sum);
                                }
                            );
                            Vue.nextTick(function () {
                                for (var i = 0; i < orderVue.stores.length; i++) {
                                    $("#select" + i).selectpicker('val', "express_zhongtong");
                                    $("#select" + i).selectpicker('refresh');
                                    $("#select" + i).selectpicker('render');
                                }
                                $("#submitForm").validationEngine(
                                    {
                                        promptPosition: 'centerRight',
                                        showArrow: true
                                    }
                                );
                                $("#distpicker").distpicker({
                                        province: "---- 所在省 ----",
                                        city: "---- 所在市 ----",
                                        district: "---- 所在区 ----"
                                    }
                                );
                            });
                        })
                    },
                    getStore: function (id) {
                        var param = window.btoa("sid=" + id + "&timeStamp=" + new Date().getTime());
                        var url = getPath() + "/store?" + param;
                        return url;
                    },
                    getImage: function (name, id) {
                        if (name == null || id == 0)
                            return;
                        else {
                            var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                            return url;
                        }
                    },
                    getProduct: function (id) {
                        var param = window.btoa("pid=" + id + "&timeStamp=" + new Date().getTime());
                        var url = getPath() + "/product?" + param;
                        return url;
                    },
                    addText: function (e) {
                        $(e.target).parent().parent().css("height", "170px");
                        $(e.target).hide();
                        $(e.target).next().show();
                        $(e.target).next().next().show();
                    },
                    submit: function () {
                        if (!$("#submitForm").validationEngine("validate"))
                            return false;
                        $(orderVue.orders).each(function (i, data1) {
                            var province = $("#province").val();
                            var city = $("#city").val();
                            var district = $("#district").val();
                            data1.sid = orderVue.stores[i].id;
                            data1.address = province+"-"+city+"-"+district+"-"+orderVue.order.address;
                            console.log(data1.address);
                            data1.post = orderVue.order.post;
                            data1.receiver = orderVue.order.receiver;
                            data1.mobile = orderVue.order.mobile;
                            data1.type = orderVue.order.type;
                            data1.sum = orderVue.sumArray[i];
                            data1.quantity = orderVue.numArray[i];
                        });
                        var arr = new Array();
                        $(orderVue.stores).each(function (i, store) {
                            var ar = new Array();
                            $(store.orderItems).each(
                                function (j, data) {
                                    ar.push(data.id);
                                }
                            );
                            arr.push(ar);
                        })
                        var url = getPath() + "/foreOrder?timeStamp=" + new Date().getTime();
                        var bean = {orders:orderVue.orders,ids:arr};
                        axios.post(url,bean ).then(
                            function (value) {
                                if (value.data.result == 'wait')
                                    location.href = getPath()+"/waitSuccess";
                                else if(value.data.result == 'pay' )
                                {
                                    var ids = value.data.ids;
                                    var sum = value.data.sum;
                                    var num = value.data.num;
                                    var param = "sum="+sum+"&num="+num+ids;
                                    var url = window.btoa(param);
                                    location.href = getPath()+"/order_pay?"+url;
                                }
                                else {
                                    $.dialog({
                                        title: '提交失败!',
                                        content: '请重新尝试提交',
                                        theme: 'modern',
                                        icon: 'fa fa-close'
                                    });
                                }
                            }
                        )
                    },
                    sendButton:function(){
                        if(orderVue.message.text.length==0)
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
                            axios.post(url, orderVue.message).then(function (value) {
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
                        orderVue.message.sid = sid;
                        $("#addMessageModel").modal("show");
                    }
                }
            }
        );
    }
);