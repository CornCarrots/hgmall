$(
    function () {

        var editor = new Array(8);
        var mid = 0;
        var options = {
            cssPath: getPath() + '/js/include/kindeditor/plugins/code/prettify.css',
            // autoHeightMode: true,
            width: "150%",
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
            uploadJson: getPath() + "/admin/scores/" + mid + "/powers/image",
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
        var powers = {
            uri: "/admin/scores/",
            pages: [],
            powers: [],
            power: {id: 0, mid: 0, title: '', text: '', score: 0, exchange: 0},
            index: 0,
            key: '',
            image: null
        };
        var powerVue = new Vue(
            {
                el: ".container",
                data: powers,
                mounted: function () {
                    var mid = getUrlParms("mid");
                    mid = mid;
                    this.uri = this.uri + mid + "/powers";
                    this.list(0);
                },
                methods: {
                    subString: function (text) {
                        return text.substring(0, 25);
                    },
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if (value.data.content.length > 0) {
                                    powerVue.pages = value.data;
                                    powerVue.powers = value.data.content;
                                    $(".back_power_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_power_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

                                    editor[0] = KindEditor.create("#editor_id", options);
                                    checkListener();
                                });
                            }
                        )
                    },
                    jump: function (page) {
                        jump(page, articleVue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, articleVue);
                    },
                    addButton: function () {
                        powerVue.power = {id: 0, mid: 0, title: '', text: '', score: 0, exchange: 0}
                        powerVue.image = null;
                        $("#addPowerModel").modal("show");
                    },
                    setFile: function (e) {
                        this.image = e.target.files[0];
                    },
                    sendButton: function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        powerVue.power.text = editor[0].html();
                        if(powerVue.power.text.length==0)
                        {
                            $.alert("文章内容不可为空!");
                            return;
                        }
                        var url = getPath() + this.uri;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("title", powerVue.power.title);
                        formData.append("text", powerVue.power.text);
                        formData.append("score", powerVue.power.score);
                        axios.post(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加会员权限成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    powerVue.image = null;
                                                    powerVue.power = {id: 0, mid: 0, title: '', text: '', score: 0, exchange: 0};
                                                    editor[0].html('');
                                                    powerVue.list(0);
                                                    $("#addPowerModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    search: function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var key = this.key;
                        var url = getPath() + this.uri + "/search?key=" + this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    powerVue.powers = value.data;
                                    $(".back_power_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_power_list_table").hide();
                                    $(".notfound_search").show();
                                }
                            }
                        );
                    },
                    deleteButton: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除会员权限',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    var url = getPath() + powerVue.uri + "/" + id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length) {
                                            $.alert('系统异常，请重试!');
                                        }
                                        else {
                                            $.alert('成功删除!');
                                            powerVue.list(0);
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
                    deleteAllButton: function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除会员权限',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='powerCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + powerVue.uri + "/" + input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');
                                                    input.prop("checked", false);
                                                    powerVue.list(0);
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
                    getButton: function (i, id) {
                        powerVue.index = i;
                        var url = getPath() + this.uri + "/" + id;
                        axios.get(url).then(
                            function (value) {
                                powerVue.power = value.data;
                                Vue.nextTick(function () {
                                    KindEditor.remove("#editor_" + powerVue.power.id);
                                    editor[i] = KindEditor.create("#editor_" + powerVue.power.id, options);
                                    editor[i].html(powerVue.power.text);
                                });
                                $("#editPowerModel").modal("show");
                            }
                        );
                    },
                    updateButton: function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        powerVue.power.text = editor[powerVue.index].html();
                        if(powerVue.power.text.length==0)
                        {
                            $.alert("文章内容不可为空!");
                            return;
                        }
                        var url = getPath() + this.uri + "/" + id;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("title", powerVue.power.title);
                        formData.append("text", powerVue.power.text);
                        formData.append("score", powerVue.power.score);
                        axios.put(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '修改会员权限成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    powerVue.image = null;
                                                    powerVue.power = {id: 0, mid: 0, title: '', text: '', score: 0, exchange: 0};
                                                    powerVue.list(0);
                                                    $("#editPowerModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

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