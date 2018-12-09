$(
    function () {
        var bean = {
            uri: "/foreMessage",
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
            },
            user: {name: '', password: ''},
            flag: false,
            email: ''
        };
        var messageVue = new Vue(
            {
                el: ".main",
                data: bean,
                mounted: function () {
                    Vue.nextTick(function () {
                        $("#loginForm").validationEngine(
                            {
                                promptPosition: 'topRight',
                                showArrow: true
                            }
                        );
                    });
                },
                methods: {
                    addButton: function () {
                        var url = getPath() + "/foreLoginCheck"+"?timeStamp="+new Date().getTime();
                        axios.get(url).then(function (value) {
                            if (value.data == "fail")
                                $("#loginModel").modal("show");
                            else {
                                $("#addMessageModel").modal("show");
                            }
                        });

                    },
                    sendButton: function () {
                        if(messageVue.message.text.length==0)
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
                            var url = getPath() + messageVue.uri+"?timeStamp="+new Date().getTime();
                            axios.post(url, messageVue.message).then(function (value) {
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
                    loginUser: function () {
                        if (!$("#loginForm").validationEngine("validate"))
                            return false;
                        var url = getPath() + "/foreLoginUser"+"?timeStamp="+new Date().getTime();
                        axios.post(url, messageVue.user).then(function (value) {
                            if (value.data == "success")
                                location.reload();
                            if (value.data == "user404")
                                $("#nameField").validationEngine("showPrompt", "没有此用户", "error");
                            if (value.data == "fail")
                                $("#passField").validationEngine("showPrompt", "密码错误", "error");
                        })
                    },
                    cleanName: function () {
                        messageVue.user.name = '';
                        $("#nameField").val('');
                        $("#nameField").focus();
                    },
                    togglePass: function () {
                        messageVue.flag = !messageVue.flag;
                        if (messageVue.flag) {
                            $("#passField").attr("type", "text");
                            $("#seePass").toggleClass("fa-eye");
                            $("#seePass").toggleClass("fa-eye-slash");
                        }
                        else {
                            $("#passField").attr("type", "password");
                            $("#seePass").toggleClass("fa-eye-slash");
                            $("#seePass").toggleClass("fa-eye");
                        }
                    },
                    forgetPass: function () {
                        $("#loginModel").modal("hide");
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
                                            var bean = {random: random, key: key, email: messageVue.email, pass: pass};
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
                                    var url = getPath() + "/forgetUser?email=" + messageVue.email + "&timeStamp=" + new Date().getDate();
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