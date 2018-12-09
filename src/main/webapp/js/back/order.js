$(
    function () {
        var orders = {
            uri: "/admin/orders",
            pages: [],
            orders: [],
            key: '',
            order:{}
        };
        var orderVue = new Vue(
            {
                el: ".container",
                data: orders,
                mounted: function () {
                    this.list(0);
                },
                filters: {
                    statusFilter: function (value) {
                        if (value == 'type_waitPay')
                            return '待付款';
                        if (value == 'type_waitDelivery')
                            return '待发货';
                        if (value == 'type_waitConfirm')
                            return '待收货';
                        if (value == 'type_waitReview')
                            return '待评价';
                        if (value == 'type_success')
                            return '交易成功';
                        if (value == 'type_waitExchange')
                            return '待换货';
                        if (value == 'type_waitRefund')
                            return '待退款';
                        if (value == 'type_waitRejected')
                            return '待退货';
                        if (value == 'type_fail')
                            return '交易失败';
                    }
                },
                methods: {
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if (value.data.content.length > 0) {
                                    orderVue.pages = value.data
                                    orderVue.orders = value.data.content;
                                    $(".back_order_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else {
                                    $(".back_order_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    checkListener();
                                })
                            }
                        )
                    },
                    jump: function (page) {
                        jump(page, orderVue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, orderVue);
                    },
                    delivery: function (id) {
                        var url = getPath() + orderVue.uri + "/" + id;
                        axios.put(url).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '发货成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                orderVue.list(0);
                                            }
                                        }
                                    }
                                }
                            );
                        })
                    },
                    seeOrder: function (id) {
                        var url = getPath() + "/admin/sale/orderItem?id="+ id;
                        location.href=url;
                    },
                    deleteOrder: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除订单',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    var url = getPath() + orderVue.uri + "/" +id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length) {
                                            $.alert('系统异常，请重试!');
                                        }
                                        else {
                                            $.alert('成功删除!');
                                            orderVue.list(0);
                                        }
                                    });
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });
                    },
                    deleteAllButton: function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除订单',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    $("input[name='orderCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + orderVue.uri + "/" +input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');
                                                    input.prop("checked", false);
                                                    orderVue.list(0);
                                                }
                                            });
                                        }
                                    );
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });
                    },
                    search: function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var key = this.key;
                        var url = getPath() + orderVue.uri + "/search?key=" +key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    orderVue.orders = value.data;
                                    $(".back_order_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_order_list_table").hide();
                                    $(".notfound_search").show();
                                }
                            }
                        );
                    }
                }
            }
        );
        function checkListener(){
            $("#checkAllTH input").click(
                function () {
                    checkAll();
                }
            );
            $(".checkOne input").click(
                function () {
                    checkOne();
                }
            );
        }
    }
);