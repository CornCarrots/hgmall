$(
    function () {
        var bean = {
            uri:"/foreReview",
            item:{order:{createDate:new Date()},product:{firstProductImage:{id:0}}},
            review:{id:0,content:'',uid:0,pid:0,createDate:new Date()},
            oid:0
        };
        var reviewVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function () {
                    this.get();
                },
                methods:{
                    get:function () {
                        var id = getUrlParms("oid");
                        this.oid = id;
                        var param = "?timeStamp=" + new Date().getTime();
                        var url = getPath() + this.uri + "/" + id + param;
                        axios.get(url).then(
                            function (value) {
                                reviewVue.item = value.data;
                                reviewVue.review.pid = reviewVue.item.product.id;
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
                    reviewButton:function () {
                        if(reviewVue.review.content.length==0)
                        {
                            $.alert({
                                title: '内容不能为空!',
                                content: '请检查您的评价内容并尝试重新提交',
                                theme:'modern',
                                icon: 'fa fa-close'
                            });
                            return;
                        }
                        var param = "?timeStamp=" + new Date().getTime();
                        var url = getPath() + this.uri +"/"+ reviewVue.oid+ param;
                        axios.post(url,reviewVue.review).then(function (value) {
                            if(value.data == 'ok' )
                            {
                                $.confirm({
                                    title: '恭喜您!',
                                    content: '评价成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        '太棒了': function () {
                                        }
                                    },
                                    onDestroy: function () {
                                        // before the modal is hidden.
                                        var param = "oid="+reviewVue.oid+"&timeStamp="+new Date().getDate();
                                        var url = window.btoa(param);
                                        location.href=getPath()+"/reviewSuccess?"+url;
                                    }
                                });
                            }
                            else
                            {
                                $.dialog({
                                    title: '系统出错啦!',
                                    content: '请重试',
                                    theme:'modern',
                                    icon: 'fa fa-close'
                                });
                            }
                        });
                    }
                }
            }
        );
    }
);