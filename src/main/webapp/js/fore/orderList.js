$(
    function () {
        var editor;
        var options = {
            cssPath: getPath() + '/js/include/kindeditor/plugins/code/prettify.css',
            // autoHeightMode: true,
            width: "100%",
            // minheight: 30,
            height: "250%",
            resizeType: 0,
            filterMode: true,
            allowImageUpload: true,//允许上传图片
            items: [
                'source', 'fullscreen', '|', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'image', 'link'
            ],
            filePostName: "image",
            uploadJson: getPath() + "/admin/articles/image",
            dir: "image",
            afterUpload: function (value) {//图片上传后，将上传内容同步到textarea中
                this.sync();
            },
            afterFocus: function () {      //得到焦点事件
                // self.edit = edit = this; var strIndex = self.edit.html().indexOf("@我也说一句"); if (strIndex != -1) { self.edit.html(self.edit.html().replace("@我也说一句", "")); }
            },
            afterBlur: function () {
                this.sync();//失去焦点时，将上传内容同步到textarea中
            },
            afterCreate: function () {
                var self = this;
                self.html("请输入：");
                KindEditor.ctrl(document, 13, function () {
                    articleVue.addArticle_();
                });
                KindEditor.ctrl(self.edit.doc, 13, function () {
                    self.sync();
                    articleVue.addArticle_();
                });
            }
        };
        var bean = {
            uri:"/foreOrderList",
            orders:[],
            type:'all',
            oid:0,
            status:'',
            content:'',
            logistics:[],
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
        var orderVue = new Vue({
            el:".main",
            data:bean,
            mounted:function () {
                this.list('all');
            },
            methods:{
                list:function (type) {
                    this.type = type;
                    var url = getPath()+this.uri+"?type="+type+"&timeStamp="+new Date().getTime();
                    axios.get(url).then(
                        function (value) {
                            $(orderVue.orders).each(function (i, data) {
                                data={store:{name:'',authentication_credit:0,authentication_id:0,authentication_phone:0},orderItems:[]};
                                $(data.orderItems).each(function (j,data2) {
                                    data2={product:{firstProductImage:{id:0}}};
                                })
                            });
                            if(value.data.length == 0 )
                            {
                                $(".order404").show();
                                $(".orderObject_myobject").hide();
                            }
                            else
                            {
                                orderVue.orders=value.data;
                                $(".order404").hide();
                                $(".orderObject_myobject").show();
                            }
                            Vue.nextTick(function () {
                                editor = KindEditor.create("#editor_id", options);
                            });
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
                getProduct: function (id) {
                    var param = window.btoa("pid=" + id + "&timeStamp=" + new Date().getTime());
                    var url = getPath() + "/product?" + param;
                    return url;
                },
                deleteOrder:function (id) {
                    $.confirm({
                        title: '确定吗？',
                        content: '您正在删除您的订单',
                        theme:'modern',
                        icon: 'fa fa-question',
                        buttons: {
                            '确认': function () {
                                var url = getPath()  + "/foreOrder/" +id+"?timeStamp="+new Date().getTime();
                                axios.delete(url).then(function (value) {
                                    if (0 != value.data.length) {
                                        $.alert('系统异常，请重试!');
                                    }
                                    else {
                                        $.alert('成功删除!');
                                        orderVue.list('all');
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
                pay:function(id){
                    var url = getPath()  + "/forePayOrder/" +id+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(function (value) {
                        var ids = value.data.id;
                        var sum = value.data.sum;
                        var num = value.data.num;
                        var param = "sum="+sum+"&num="+num+ids;
                        var url = window.btoa(param);
                        location.href = getPath()+"/order_pay?"+url;
                    })
                },
                delivery:function (e,id) {
                    $.dialog({
                        title: '催货成功!',
                        content: '卖家正在加紧发货中',
                        theme:'modern',
                        icon: 'fa fa-check',
                        onDestroy: function () {
                            $(e.target).text('催货成功');
                            $(e.target).attr("disabled","");
                        }
                    });

                },
                confirm:function (id) {
                    var param = "oid="+id+"&timeStamp="+new Date().getTime();
                    var url = window.btoa(param);
                    location.href = getPath()+"/order_confirm?"+url;
                },
                review:function (id) {
                    var param = "oid="+id+"&timeStamp="+new Date().getTime();
                    var url = window.btoa(param);
                    location.href = getPath()+"/order_review?"+url;
                },
                exchange:function (id,i) {
                    if(orderVue.orders[i].status=='type_waitExchange'||orderVue.orders[i].status=='type_waitRejected'||orderVue.orders[i].status=='type_waitRefund')
                    {
                        $.alert({
                            title: '正在处理中，请等待!',
                            content: '卖家已经收到了您的请求',
                            theme:'modern',
                            icon: 'fa fa-refresh'
                        });
                        return;
                    }
                    orderVue.oid=id;
                    $("#exchangeModel").modal("show");
                },
                exchangeButton:function () {
                    if(orderVue.status.length==0 )
                    {
                        $.alert('请先选择一种类型!');
                        return;
                    }
                    orderVue.content = editor.html();
                    if(orderVue.content.length==0 )
                    {
                        $.alert('请填写您的描述内容!');
                        return;
                    }
                    var url = getPath()  + "/foreExchangeOrder/" +orderVue.oid+"?timeStamp="+new Date().getTime();
                    var bean = {status:orderVue.status, describe:orderVue.content};
                    axios.post(url,bean).then(
                        function (value) {
                            if (value.data == "ok")
                            {
                                $.alert({
                                    title: '正在处理中，请等待!',
                                    content: '卖家已经收到了您的请求',
                                    theme:'modern',
                                    icon: 'fa fa-refresh'
                                });
                                orderVue.list('all');
                                $("#exchangeModel").modal("hide");

                            }
                            else
                            {
                                $.alert('系统异常，请重试!');
                            }
                        }
                    );
                },
                logistics_:function (id) {
                    orderVue.oid=id;
                    var url = getPath()+"/foreLogistics/"+id+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(
                        function (value) {
                            orderVue.logistics = value.data;
                            if(orderVue.logistics.length==0)
                                $("#notLogistics").show();
                            else
                                $("#notLogistics").hide();
                            $("#logisticsModal").modal("show");
                        }
                    );

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
        });
    }
);