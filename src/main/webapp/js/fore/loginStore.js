$(
    function () {
        var bean = {
            uri: "/foreLoginStore",
            imgURL:'',
            flag: false,
            manager:{name:'',password:''},
            email: ''
        };
        var loginVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function () {
                    this.get();
                },
                methods:{
                    get:function () {
                        var url = getPath()+"/foreLoginImage";
                        axios.get(url).then(
                            function (value) {
                                var id = value.data.loginStore.id;
                                loginVue.imgURL = getPath()+"/image/advertisement_loginStore/"+id+".jpg";
                                Vue.nextTick(function () {
                                        $("#loginForm").validationEngine(
                                            {
                                                promptPosition: 'topRight',
                                                showArrow: true
                                            }
                                        );
                                    }
                                );
                            }
                        );
                    },
                    cleanName: function () {
                        loginVue.manager.name = '';
                        $("#nameField").val('');
                        $("#nameField").focus();
                    },
                    togglePass: function () {
                        loginVue.flag = !loginVue.flag;
                        if (loginVue.flag) {
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
                    loginStore: function () {
                        if(!$("#loginForm").validationEngine("validate"))
                            return false;
                        var url = getPath() + this.uri;
                        axios.post(url, loginVue.manager).then(function (value) {
                            console.log(value)
                            if (value.data == "success")
                                location.href = getPath() + "/admin";
                            if (value.data == "manager404")
                                $("#nameField").validationEngine("showPrompt", "没有此管理员", "error");
                            if (value.data == "fail")
                                $("#passField").validationEngine("showPrompt", "密码错误", "error");
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
                                            var bean = {random: random, key: key, email: loginVue.email, pass: pass};
                                            var url = getPath() + "/foreManagerPass?timeStamp=" + new Date().getDate();
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
                                    var url = getPath() + "/forgetManager?email=" + loginVue.email + "&timeStamp=" + new Date().getDate();
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