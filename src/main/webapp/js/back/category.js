$(
    function () {

        var categories = {
            uri: '/admin/categories',
            pages: {},
            beans: [],
            addBean: {id: 0, name: '', sid: 0, type: 'type_product', icon: ''},
            editBean: {id: 0, name: '', sid: 0, type: 'type_product', icon: '', store: {}},
            key: '',
            image: null
        };

        var vue = new Vue(
            {
                el: ".container",
                data: categories,
                mounted: function () {
                    this.list(0);

                },
                methods: {
                    list: function (start) {
                        var uri = getPath() + this.uri + "?start=" + start;
                        axios.get(uri).then(
                            function (value) {
                                console.log(value.data)
                                if (value.data.content.length > 0) {
                                    vue.pages = value.data
                                    vue.beans = value.data.content;
                                    $(".back_category_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else {
                                    $(".back_category_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();

                                }
                                Vue.nextTick(
                                    function () {
                                        $("#addForm").validationEngine({
                                            promptPosition: 'centerRight',
                                            showArrow: true
                                        });
                                        $("#updateForm").validationEngine({
                                            promptPosition: 'centerRight',
                                            showArrow: true
                                        });
                                        checkListener();
                                    }
                                );
                            }
                        )
                    },
                    jump: function (page) {
                        jump(page, vue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, vue);
                    },
                    addCategoryButton: function () {
                        $("#addCategoryModel").modal("show");
                    },
                    deleteAllButton: function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除分类',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    $("input[name='categoryCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + vue.uri + "/" + input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');
                                                    input.prop("checked", false);
                                                    vue.list(0);
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
                    search: function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var url = getPath() + "/admin/category/search?key=" + this.key;
                        location.href = url;
                    },
                    setFile: function (e) {
                        this.image = e.target.files[0];
                    },
                    getImage: function (id) {
                        if (id != 0 || id != undefined)
                            return getPath() + "/image/store/" + id + ".jpg";
                    },
                    addCategory: function () {
                        if (!$("#addForm").validationEngine("validate")) return false;
                        var url = getPath() + this.uri;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("name", this.addBean.name);
                        formData.append("sid", this.addBean.sid);
                        formData.append("type", this.addBean.type);
                        axios.post(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加分类成功',
                                        theme: 'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    vue.image = null;
                                                    vue.addBean = {id: 0, name: '', sid: 0, type: 'type_product'};
                                                    $("#addCategoryModel").modal("hide");
                                                    location.reload();
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteCategory: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除分类',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath() + vue.uri + "/" + id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length) {
                                            $.alert('系统异常，请重试!');
                                        }
                                        else {
                                            $.alert('成功删除!');
                                            vue.list(0);
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
                    editCategory: function (id) {
                        var url = getPath() + this.uri + "/" + id;
                        axios.get(url).then(function (value) {
                            vue.editBean = value.data;
                        });
                        $("#editCategoryModel").modal("show");
                    },
                    updateCategory: function (id) {
                        if (!$("#updateForm").validationEngine("validate")) return false;
                        var url = getPath() + this.uri + "/" + id;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("name", this.editBean.name);
                        formData.append("sid", this.editBean.sid);
                        formData.append("type", this.editBean.type);
                        axios.put(url, formData).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改分类成功',
                                    theme: 'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                vue.editBean = {id: 0, name: '', sid: 0, type: 'type_product'};
                                                $("#editCategoryModel").modal("hide");
                                                location.reload();
                                            }
                                        }
                                    }
                                }
                            );


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