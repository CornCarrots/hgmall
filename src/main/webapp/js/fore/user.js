$(function () {
    var bean = {
        uri: "/foreUser",
        user: {
            id: 0,
            name: '',
            nickName: '',
            password: '',
            salt: '',
            account: 0,
            sex: 0,
            registerDate: null,
            openDate: null,
            address: '',
            mobile: '',
            email: '',
            mid: 0,
            score: 0,
            status: 0
        },
        type: 'account',
        changeType: '',
        image: null,
        money: 0.0,
        password: ''
    };
    var userVue = new Vue(
        {
            el: ".main",
            data: bean,
            mounted: function () {
                this.get();
            },
            methods: {
                get: function () {
                    var type = getUrlParms('type');
                    if (type != undefined)
                        this.type = type;
                    var url = getPath() + this.uri + "?timeStamp=" + new Date().getDate();
                    axios.get(url).then(
                        function (value) {
                            userVue.user = value.data;
                        }
                    );
                    Vue.nextTick(function () {
                        $("#modifyForm").validationEngine(
                            {
                                promptPosition: 'centerRight',
                                showArrow: true
                            }
                        );
                        $("#rechargeForm").validationEngine(
                            {
                                promptPosition: 'centerRight',
                                showArrow: true
                            }
                        );
                        valControls($("#modifyForm"));
                    });
                },
                getImage: function (name, id) {
                    if (name == undefined || id == 0)
                        return;
                    else {
                        var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                        return url;
                    }

                },
                getUser: function () {
                    var param = "type=user&timeStamp=" + new Date().getDate();
                    var url = window.btoa(param);
                    return getPath() + "/user?" + url;
                },
                getUrl: function (key) {
                    return getPath() + key;
                },
                modify: function () {
                    if (controlId.length > 0) {
                        alertinfo(0);
                        return false;
                    }
                    if (!$("#registerForm").validationEngine("validate"))
                        return false;  //验证没有使用ajax验证的控件(不是ajax验证的字段是可以正常验证的, 不通过就返回)
                    var url = getPath() + this.uri + "/" + userVue.user.id + "?timeStamp=" + new Date().getDate();
                    axios.put(url, userVue.user).then(function (value) {
                        if (value.data == 'ok') {
                            $.dialog({
                                title: '恭喜您!',
                                content: '修改成功啦',
                                theme: 'modern',
                                icon: 'fa fa-smile-o'
                            });
                        }
                        else {
                            $.alert("系统错误，请重试");
                        }
                    });
                },
                reset: function () {
                    this.get();
                },
                upload: function (e) {
                    this.image = e.target.files[0];
                    if (this.image == null)
                        return;
                    var formData = new FormData();
                    formData.append("image", this.image);
                    var url = getPath() + this.uri + "?timeStamp=" + new Date().getDate();
                    axios.post(url, formData).then(
                        function (value) {
                            if (value.data == 'ok') {
                                $.dialog({
                                    title: '恭喜您!',
                                    content: '修改成功啦',
                                    theme: 'modern',
                                    icon: 'fa fa-smile-o',
                                    onDestroy: function () {
                                        // before the modal is hidden.
                                        location.reload();
                                    }
                                });
                            }
                            else {
                                $.alert("系统错误，请重试");
                            }
                        }
                    );
                },
                change: function (type) {
                    var s = '';
                    if (type == 'mobile')
                        s = '手机号码';
                    if (type == 'email')
                        s = '邮箱';
                    if (type == 'password')
                        s = '密码';

                    $.confirm({
                        title: '修改信息',
                        content: '' +
                            '<div class="form-group">' +
                            '<label>您正在修改您的' + s + '</label>' +
                            '<input type="text" placeholder="请输入您要修改的' + s + '" class="key form-control"/>' +
                            '</div>',
                        theme: 'modern',
                        buttons: {
                            formSubmit: {
                                text: '确定',
                                btnClass: 'btn-blue',
                                action: function () {
                                    var key = this.$content.find('.key').val();
                                    if (!key) {
                                        $.alert('请输入一个有效的值');
                                        return false;
                                    }
                                    var bean = {type: type, key: key};
                                    var url = getPath() + "/foreUser?timeStamp=" + new Date().getDate();
                                    axios.put(url, bean).then(
                                        function (value) {
                                            if (value.data == 'ok') {
                                                $.dialog({
                                                    title: '恭喜您!',
                                                    content: '修改成功啦',
                                                    theme: 'modern',
                                                    icon: 'fa fa-smile-o',
                                                    onDestroy: function () {
                                                        location.reload();
                                                    }
                                                });
                                            }
                                            else {
                                                $.alert("系统错误，请重试");
                                                return false;

                                            }
                                        }
                                    );
                                }
                            },
                            取消: function () {
                                text: '取消'
                            }
                        }
                    });
                },
                open: function () {
                    var random;
                    $.confirm({
                        title: '请稍后',
                        content: '<div class="ball"></div>\n' +
                            '<div class="ball1"></div>',
                        buttons: {
                            formSubmit: {
                                text: '确定',
                                btnClass: 'btn-blue',
                                action: function () {
                                    var key = this.$content.find('.key').val();
                                    if (!key) {
                                        $.alert('请输入一个有效的值');
                                        return false;
                                    }
                                    var bean = {random: random, key: key};
                                    var url = getPath() + "/foreOpenAccount?timeStamp=" + new Date().getDate();
                                    axios.post(url, bean).then(
                                        function (value) {
                                            if (value.data == 'ok') {
                                                $.dialog({
                                                    title: '恭喜您!',
                                                    content: '成功开通账户啦',
                                                    theme: 'modern',
                                                    icon: 'fa fa-smile-o',
                                                    onDestroy: function () {
                                                        location.reload();
                                                    }
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
                            self.buttons['取消'].hide();
                        },
                        onContentReady: function () {
                            var self = this;
                            var url = getPath() + "/foreOpenAccount?timeStamp=" + new Date().getDate();
                            return axios.get(url).then(
                                function (value) {
                                    if (value.data.result == 'ok') {
                                        random = value.data.random;
                                        self.setContent('<div class="form-group">' +
                                            '<label>验证码已发送到您的邮箱，请查看</label>' +
                                            '<input type="text" placeholder="请输入验证码" class="key form-control"/>' +
                                            '</div>');
                                        self.setTitle('开通账户');
                                        self.setTheme('modern');
                                        self.buttons['formSubmit'].show();
                                        self.buttons['取消'].show();
                                    }
                                    else {
                                        self.setContent("系统错误，请重试");
                                        return false;

                                    }
                                }
                            );
                        }
                    });
                },
                recharge: function () {
                    $("#rechargeModel").modal("show");
                },
                rechargeButton: function () {
                    if (!$("#rechargeForm").validationEngine("validate")) return false;  //验证没有使用ajax验证的控件(不是ajax验证的字段是可以正常验证的, 不通过就返回)
                    var url = getPath() + "/foreRecharge?timeStamp=" + new Date().getDate();
                    var bean = {password: userVue.password, money: userVue.money};
                    axios.post(url, bean).then(function (value) {
                        if (value.data == 'no') {
                            $.dialog({
                                title: '您输入的密码有误!',
                                content: '请重新输入',
                                theme: 'modern',
                                icon: 'fa fa-close'
                            });
                            return false;

                        }
                        else if (value.data == 'ok') {
                            $.dialog({
                                title: '恭喜你，充值成功!',
                                content: '<div align="center" class="payDIV">\n' +
                                    '    <h5>服务器以及网站维护需要一点费用，如果可以的话，扫描一下二维码支持一下站长，帮我加个鸡腿吧！</h5>\n' +
                                    '    <img src="image/site/pay1.jpg" alt="alipay" style="width: 20%">\n' +
                                    '    <img src="image/site/pay2.jpg" alt="weixin" style="width: 20%">\n' +
                                    '</div>',
                                icon: 'fa fa-check',
                                theme: 'modern',
                                columnClass: 'col-md-12',
                                onDestroy: function () {
                                    location.reload();
                                }
                            });
                        }
                        else {
                            $.alert("系统错误，请重试");
                            return false;

                        }
                    });
                }
            }
        }
    );

});