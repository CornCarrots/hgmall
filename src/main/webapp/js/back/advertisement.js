$(
    function () {
        var advertisements = {
            uri: "/admin/images/advertisements",
            addAdvertisement: {id: 0, type: 'type_home', status: 1},
            editAdvertisement: {id: 0, type: '', status: 1},
            advertisements: [],
            types: [{type: 'type_home', name: '首页'}, {type: 'type_category', name: '分类页面'}, {
                type: 'type_login',
                name: '登录页面'
            }, {type: 'type_loginStore', name: '商家登录页面'}],
            image: null
        };
        var advertisementVue = new Vue(
            {
                el: ".container",
                data: advertisements,
                mounted: function () {
                    this.list();
                },
                filters: {
                    typeFilter: function (value) {
                        if (value == 'type_home')
                            return '首页';
                        if (value == 'type_category')
                            return '分类页面';
                        if (value == 'type_login')
                            return '登陆页面';
                        if (value == 'type_loginStore')
                            return '商家登录页面';
                    }
                },
                methods: {
                    list: function () {
                        var url = getPath() + this.uri;
                        axios.get(url).then(
                            function (value) {
                                advertisementVue.advertisements = value.data;
                                Vue.nextTick(function () {
                                    var html = '';
                                    var first = '';
                                    $(advertisementVue.types).each(
                                        function (i, data) {
                                            if (i == 0) {
                                                first = data.type;
                                            }
                                            html += '<option value=\"' + data.type + '\">' + data.name + '</option>';
                                        }
                                    );
                                    $("#addSelect").html(html);
                                    $("#addSelect").selectpicker('val', first);
                                    $("#addSelect").selectpicker('refresh');
                                    $("#addSelect").selectpicker('render');
                                    $("#addForm").validationEngine({promptPosition: 'centerRight', showArrow: true});
                                    $("#updateForm").validationEngine({promptPosition: 'centerRight', showArrow: true});
                                    checkListener();
                                });
                            }
                        );
                    },
                    getImage: function (id, type) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/advertisement_" + type.substring(type.indexOf("_") + 1) + "/" + id + ".jpg";
                        return url;
                    },
                    addButton: function () {
                        $("#addAdvertisementModel").modal("show");
                    },
                    deleteAllButton: function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除广告',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='advertisementCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + advertisementVue.uri + "/" + input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');

                                                    input.prop("checked", false);
                                                    advertisementVue.list();
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
                    setFile: function (e) {
                        this.image = e.target.files[0];
                    },
                    addAdvertisement_: function () {
                        if (!$("#addForm").validationEngine("validate")) return false;
                        $("#addAdvertisementModel").modal("hide");
                        var url = getPath() + this.uri;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("type", advertisementVue.addAdvertisement.type);
                        formData.append("status", advertisementVue.addAdvertisement.status);
                        axios.post(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加广告成功',
                                        theme: 'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    location.reload();
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    editAdvertisement_: function (id) {
                        var url = getPath() + this.uri + "/" + id;
                        axios.get(url).then(function (value) {
                            advertisementVue.editAdvertisement = value.data;
                            Vue.nextTick(function () {
                                var html = '';
                                var found = '';
                                $(advertisementVue.types).each(
                                    function (i, data) {
                                        if (advertisementVue.editAdvertisement.type == data.type)
                                            found = data.type;
                                        html += '<option value=\"' + data.type + '\">' + data.name + '</option>';
                                    }
                                );
                                $("#editSelect").html(html);
                                $("#editSelect").selectpicker('val', found);
                                $("#editSelect").selectpicker('refresh');
                                $("#editSelect").selectpicker('render');
                            });
                            $("#editAdvertisementModel").modal("show");
                        });
                    },
                    updateAdvertisement: function (id) {
                        if (!$("#updateForm").validationEngine("validate")) return false;
                        var url = getPath() + this.uri + "/" + id;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("status", advertisementVue.editAdvertisement.status);
                        formData.append("type", advertisementVue.editAdvertisement.type);
                        $("#editAdvertisementModel").modal("hide");
                        axios.put(url, formData).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改广告成功',
                                    theme: 'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                location.reload();
                                            }
                                        }
                                    }
                                }
                            );
                        })
                    },
                    deleteAdvertisement: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除广告',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath() + advertisementVue.uri + "/" + id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length)
                                            alert(value.data);
                                        else
                                            $.alert(
                                                {
                                                    title: '恭喜你!',
                                                    content: '修改成功',
                                                    theme: 'modern',
                                                    icon: 'fa fa-smile-o',
                                                    confirm: function () {
                                                        advertisementVue.list();
                                                    }
                                                }
                                            );
                                    })
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });

                    }
                }

            }
        );

        function checkListener() {
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