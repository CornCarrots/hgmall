$(
    function () {
        var orderItem = {
            order:{},
            orderItems:[],
            uri : "/admin/orders/exchange"
        };
        var orderItemVue = new Vue(
            {
                el:".container",
                data:orderItem,
                mounted:function () {
                    this.get();
                },
                filters:{
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
                    },
                    paymentFilter:function (value) {
                        if (value == 'payment_online')
                            return '在线支付';
                        if (value == 'payment_delivery')
                            return '发货后付款';
                        if (value == 'payment_receiving')
                            return '货到付款';
                    },
                    expressFilter:function (value) {
                        if (value == 'express_zhongtong')
                            return '中通快递';
                        if (value == 'express_yuantong')
                            return '圆通快递';
                        if (value == 'express_shunfeng')
                            return '顺丰快递';
                        if (value == 'express_yunda')
                            return '韵达快递';
                        if (value == 'express_tiantian')
                            return '天天快递';
                    }
                },
                methods:{
                    get:function () {
                        var id = getUrlParms("id");
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(
                            function (value) {
                                orderItemVue.order = value.data.order;
                                orderItemVue.orderItems = value.data.orderItems;
                            }
                        );
                    },
                    getImage: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/productSingle_small/" + id + ".jpg";
                        return url;
                    }
                }
            }
        );
    }
);