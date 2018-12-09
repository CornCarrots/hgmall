$(
    function () {
        var bean = {
            style:0,
            sum:0,
            num:0,
            oids:[],
            password:'',
            mid:-1,
            email: ''
        };
        var orderVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function () {
                    this.get();
                },
                computed:{
                    discord:function () {
                      return this.sum - this.sum*0.09;
                  }
                },
                methods:{
                    get:function () {
                        this.sum = getUrlParms("sum");
                        this.num = getUrlParms("num");
                        this.oids = getUrlParms("oid");
                        var url = getPath()+"/checkUser?timeStamp="+new Date().getTime();
                        axios.get(url).then(function (value) {
                            Vue.nextTick(function () {
                                if(value.data.result1=="yes")
                                {
                                    $("#inlineRadio1").removeAttr("disabled");
                                }
                                if(value.data.result2=="yes")
                                {
                                    $("#inlineRadio2").removeAttr("disabled");
                                }
                            });
                        })
                    },
                    payOrder:function () {
                        if(orderVue.style == 0 )
                        {
                            $.dialog({
                                title: '系统错误!',
                                content: '请先选择一种付款方式',
                                theme:'modern',
                                icon: 'fa fa-close'
                            });
                            return;
                        }
                        var url = getPath()+"/forePayOrder?timeStamp="+new Date().getTime();
                        if(typeof orderVue.oids=="string")
                        {
                            var temp = orderVue.oids;
                            orderVue.oids = new Array();
                            orderVue.oids.push(temp);
                        }
                        var bean = {password:orderVue.password,style:orderVue.style,oids:orderVue.oids};
                        axios.post(url,bean).then(function (value) {
                            if(value.data == "no" )
                            {
                                $.dialog({
                                    title: '您输入的密码有误!',
                                    content: '请重新输入',
                                    theme:'modern',
                                    icon: 'fa fa-close'
                                });
                            }
                            else if(value.data == "notMember" )
                            {
                                $.dialog({
                                    title: '抱歉，您还不是会员!',
                                    content: '请先升级为会员，或者使用账户余额支付',
                                    theme:'modern',
                                    icon: 'fa fa-close'
                                });
                            }
                            else if(value.data == "lessAccount" )
                            {
                                $.dialog({
                                    title: '您的账户余额不足!',
                                    content: '请先充值',
                                    theme:'modern',
                                    icon: 'fa fa-close'
                                });
                            }
                            else if (value.data == "yes")
                            {
                                $.confirm({
                                    title: '恭喜您!',
                                    content: '付款成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        '太棒了': function () {
                                        }
                                    },
                                    onDestroy: function () {
                                        // before the modal is hidden.
                                        location.href=getPath()+"/paySuccess";
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

                        })
                    },
                    forgetPass: function () {
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
                                            var bean = {random: random, key: key, email: orderVue.email, pass: pass};
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
                                    var url = getPath() + "/forgetUser?email=" + orderVue.email + "&timeStamp=" + new Date().getDate();
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
    }
);