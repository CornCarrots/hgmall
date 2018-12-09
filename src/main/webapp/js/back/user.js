$(
    function () {
        var users = {
            uri:"/admin/users",
            users:[],
            pages:[],
            key:'',
            user:{}
        };
        var userVue = new Vue(
            {
                el:".container",
                data:users,
                mounted:function () {
                    this.list(0);
                },
                filters:{
                    sexFilter:function(value){
                        if(value==0)
                            return '男';
                        if(value==1)
                            return '女';
                    },
                    statusFilter:function (value) {
                        if(value==0)
                            return '已审核';
                        if(value==1)
                            return '已屏蔽';
                    },
                    orderStatusFilter: function (value) {
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
                methods:{
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    userVue.pages = value.data
                                    userVue.users = value.data.content;
                                    $(".back_user_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else
                                {
                                    $(".back_user_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();

                                }

                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,vue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,vue);
                    },
                    getImage: function (id) {
                        if (id == 0||id==undefined)
                            return;
                        else
                            var url= getPath() + "/image/profile_user/" + id + ".jpg";
                        return url;
                    },
                    checkUser:function (id) {
                        var url = getPath() + this.uri + "/check/" + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '标记成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    userVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    shieldUser:function (id) {
                        var url = getPath() + this.uri + "/shield/" + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '屏蔽成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    userVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    getUser:function(id)
                    {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function (value) {
                            userVue.user = value.data;
                            if(userVue.user.recharges.length==0)
                            {
                                $(".rechargeTable").hide();
                                $("#recharge>.notfound_list").show();
                            }
                            else
                            {
                                $(".rechargeTable").show();
                                $("#recharge>.notfound_list").hide();
                            }
                            if(userVue.user.expenses.length==0)
                            {
                                console.log(userVue.user.expenses.length)
                                $(".expenseTable").hide();
                                $("#consume>.notfound_list").show();
                            }
                            else
                            {
                                $(".expenseTable").show();
                                $("#consume>.notfound_list").hide();
                            }
                        });
                        $("#userInfoModel").modal("show");
                    },
                    search:function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var key = this.key;
                        var url = getPath()+"/admin/users/search?key="+key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    userVue.users = value.data;
                                    $(".back_user_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_user_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").hide();
                                }
                            }
                        );
                    }
                }
            }
        )
    }
);