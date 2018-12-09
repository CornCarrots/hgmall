$(
    function () {
        var managers = {
            uri: "/admin/managers",
            pages: [],
            key: '',
            managers: [],
            manager: {id:0,sid:0,name:'',password:'',salt:'',nickName:'',sex:0,mobile:'',email:'',createDate:null,status:1},
            manager_role: {},
            password: '',
            rids: [],
            allRoles: []
        };
        var managerVue = new Vue(
            {
                el: ".container",
                data: managers,
                mounted: function () {
                    this.list(0);
                },
                filters: {
                    statusFilter: function (value) {
                        if (value == 0)
                            return '已禁用';
                        if (value == 1)
                            return '已启用';
                    }
                },
                methods: {
                    list: function (start) {
                        // $("#addSelect").fancySelect();
                        // $("#editSelect").fancySelect();

                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                $("#addSelect").html('');
                                managerVue.allRoles = value.data.roles;
                                if (value.data.page.content.length > 0) {
                                    managerVue.pages = value.data.page;
                                    managerVue.managers = value.data.page.content;
                                    $(".back_manager_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else {
                                    $(".back_manager_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    var html = '';
                                    var first;
                                    $(managerVue.allRoles).each(
                                        function (i, data) {
                                            if (i == 0)
                                                first = data.id;
                                            html += '<option value=\"' + data.id + '\">' + data.desc + '</option>';
                                        }
                                    );
                                    $("#addSelect").html(html);
                                    $("#addSelect").selectpicker('val', first);
                                    $("#addSelect").selectpicker('refresh');
                                    $("#addSelect").selectpicker('render');
                                    checkListener();
                                });
                            }
                        )
                    },
                    jump: function (page) {
                        jump(page, managerVue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, managerVue);
                    },
                    addButton: function () {
                        managerVue.manager = {id:0,sid:0,name:'',password:'',salt:'',nickName:'',sex:0,mobile:'',email:'',createDate:null,status:1};
                        managerVue.password = '';
                        $("#addManagerModel").modal("show");
                    },
                    deleteAllButton: function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除管理员',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='moduleCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + managerVue.uri + "/" + input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');
                                                    input.prop("checked", false);
                                                    managerVue.list(0);
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
                    saveButton: function () {
                        if (managerVue.manager.password != managerVue.password) {
                            $.alert("输入的两次密码不一致！");
                            return;
                        }
                        var uri = getPath() + this.uri;
                        var bean = {manager: this.manager, rids: this.rids};
                        axios.post(uri, bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加管理员成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    managerVue.manager = {id:0,sid:0,name:'',password:'',salt:'',nickName:'',sex:0,mobile:'',email:'',createDate:null,status:1};
                                                    managerVue.password = '';
                                                    managerVue.list(0);
                                                    $("#addManagerModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    editButton: function (id) {
                        var url = getPath() + this.uri + "/" + id;
                        axios.get(url).then(function (value) {
                            $("#editSelect").html('');
                            managerVue.manager = value.data;
                            Vue.nextTick(function () {
                                var html = '';
                                var arr = new Array();

                                $(managerVue.allRoles).each(
                                    function (i, data1) {
                                        $(managerVue.manager.roles).each(
                                            function (i, data2) {
                                                if (data1.id == data2.id)
                                                    arr.push(data2.id);
                                            }
                                        );
                                        html += '<option value=\"' + data1.id + '\">' + data1.desc + '</option>';
                                    }
                                );
                                $("#editSelect").html(html);
                                $("#editSelect").selectpicker('val', arr);
                                $("#editSelect").selectpicker('refresh');
                                $("#editSelect").selectpicker('render');
                            });
                        });
                        $("#editManagerModel").modal("show");
                    },
                    updateButton: function (id) {
                        var url = getPath() + managerVue.uri + "/" + id;
                        var bean = {manager: this.manager, rids: this.rids};
                        axios.put(url, bean).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改管理员成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                managerVue.manager = {id:0,sid:0,name:'',password:'',salt:'',nickName:'',sex:0,mobile:'',email:'',createDate:null,status:1};
                                                managerVue.password = '';
                                                managerVue.list(0);
                                                $("#editManagerModel").modal("hide");
                                            }
                                        }
                                    }
                                }
                            );

                        });
                    },
                    deleteButton: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除管理员',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath() + managerVue.uri + "/" + id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length) {
                                            $.alert('系统异常，请重试!');
                                        }
                                        else {
                                            $.alert('成功删除!');
                                            managerVue.list(0);
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
                    search: function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var url = getPath() + managerVue.uri + "/search?key=" + this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    managerVue.managers = value.data;
                                    $(".back_manager_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_manager_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
                    }
                }
            }
        );
        function checkListener(){
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