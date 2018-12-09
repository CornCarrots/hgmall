$(
    function () {
        var bean = {
                uri: "/foreConfirmOrder",
                order: {store: {name: ''}, orderItems: [{product: {id: 0, name: '', firstProductImage: {id: 0}}}]},
                password: '',
                email: ''
            }
        ;
        var confirmVue = new Vue(
            {
                el: ".main",
                data: bean,
                filters: {
                    expressFilter: function (value) {
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
                mounted: function () {
                    this.get();
                },
                methods: {
                    get: function () {
                        var id = getUrlParms("oid");
                        var param = "?timeStamp=" + new Date().getTime();
                        var url = getPath() + this.uri + "/" + id + param;
                        axios.get(url).then(
                            function (value) {
                                confirmVue.order = value.data;
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
                    confirm: function () {
                        var url = getPath() + "/foreConfirmOrder?timeStamp=" + new Date().getTime();
                        var bean = {password: confirmVue.password, oid: confirmVue.order.id};
                        axios.post(url, bean).then(function (value) {
                            if (value.data == "no") {
                                $.dialog({
                                    title: '您输入的密码有误!',
                                    content: '请重新输入',
                                    theme: 'modern',
                                    icon: 'fa fa-close'
                                });
                            }
                            else if (value.data == "yes") {
                                $.confirm({
                                    title: '恭喜您!',
                                    content: '收到了您的宝贝',
                                    theme: 'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        '太棒了': function () {
                                        }
                                    },
                                    onDestroy: function () {
                                        // before the modal is hidden.
                                        location.href = getPath() + "/confirmSuccess";
                                    }
                                });
                            }
                            else {
                                $.dialog({
                                    title: '系统出错啦!',
                                    content: '请重试',
                                    theme: 'modern',
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
                                    ok: {
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
                                            var bean = {random: random, key: key, email: confirmVue.email, pass: pass};
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
                                    var url = getPath() + "/forgetUser?email=" + confirmVue.email + "&timeStamp=" + new Date().getDate();
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