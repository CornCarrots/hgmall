$(
    function () {
        var editor = new Array(8);
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
            uploadJson: getPath() + "/admin/articles/image",
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
        var select = $(".selectpicker");
        var articles = {
            uri: "/admin/articles",
            pages: [],
            articles: [],
            article: {id: 0, title: '', summary: '', cid: 0, status: 0, htmlText: '', createDate: null},
            categories: [],
            key: '',
            index: 0,
            cid: 0
        };
        var articleVue = new Vue(
            {
                el: ".container",
                data: articles,
                mounted: function () {
                    this.list(0);
                },
                filters: {
                    statusFilter: function (value) {
                        if (value == 0)
                            return '发布';
                        if (value == 1)
                            return '草稿';
                    }
                },
                methods: {
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                articleVue.categories = value.data.categories;
                                if (value.data.page.content.length > 0) {
                                    articleVue.pages = value.data.page;
                                    articleVue.articles = value.data.page.content;
                                    $(".back_article_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_article_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    var html = '';
                                    $(articleVue.categories).each(
                                        function (i, data) {
                                            if (i == 0) {
                                                articleVue.cid = data.id;
                                            }
                                            html += '<option value=\"' + data.id + '\">' + data.name + '</option>';
                                        }
                                    );
                                    $("#addSelect").html(html);
                                    $("#addSelect").selectpicker('val', articleVue.cid);
                                    $("#addSelect").selectpicker('refresh');
                                    $("#addSelect").selectpicker('render');
                                    editor[0] = KindEditor.create("#editor_id", options);
                                    $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

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
                        articleVue.article = {
                            id: 0,
                            title: '',
                            summary: '',
                            cid: articleVue.cid,
                            status: 0,
                            htmlText: '',
                            createDate: null
                        };
                        $("#addArticleModel").modal("show");
                    },
                    addArticle_: function () {
                        if(!$("#addForm").validationEngine("validate")) return false;
                        articleVue.article.htmlText = editor[0].html();
                        if(articleVue.article.htmlText.length==0)
                        {
                            $.alert("文章内容不可为空!");
                            return;
                        }
                        var url = getPath() + this.uri;
                        axios.post(url, articleVue.article).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加文章成功',
                                        theme: 'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    articleVue.article = {
                                                        id: 0,
                                                        title: '',
                                                        summary: '',
                                                        cid: articleVue.cid,
                                                        status: 0,
                                                        htmlText: '',
                                                        createDate: null
                                                    };
                                                    editor[0].html('');
                                                    articleVue.list(0);
                                                    $("#addArticleModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    getButton: function (i, id) {
                        articleVue.index = i;
                        var url = getPath() + this.uri + "/" + id;
                        axios.get(url).then(
                            function (value) {
                                articleVue.article = value.data;
                                Vue.nextTick(function () {
                                    var html = '';
                                    var found = '';
                                    $(articleVue.categories).each(
                                        function (i, data) {
                                            if (articleVue.article.cid == data.id)
                                                found = data.id;
                                            html += '<option value=\"' + data.id + '\">' + data.name + '</option>';
                                        }
                                    );
                                    $("#editSelect").html(html);
                                    $("#editSelect").selectpicker('val', found);
                                    $("#editSelect").selectpicker('refresh');
                                    $("#editSelect").selectpicker('render');
                                    KindEditor.remove("#editor_" + articleVue.article.id);
                                    editor[i] = KindEditor.create("#editor_" + articleVue.article.id, options);
                                    editor[i].html(articleVue.article.htmlText);
                                });
                                $("#editArticleModel").modal("show");
                            }
                        );
                    },
                    updateButton: function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        articleVue.article.htmlText = editor[articleVue.index].html();
                        if(articleVue.article.htmlText.length==0)
                        {
                            $.alert("文章内容不可为空!");
                            return;
                        }
                        var url = getPath() + this.uri + "/" + id;
                        axios.put(url, articleVue.article).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '修改文章成功',
                                        theme: 'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    articleVue.article = {
                                                        id: 0,
                                                        title: '',
                                                        summary: '',
                                                        cid: articleVue.cid,
                                                        status: 0,
                                                        htmlText: '',
                                                        createDate: null
                                                    };
                                                    articleVue.list(0);
                                                    $("#editArticleModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteButton: function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除文章',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath() + articleVue.uri + "/" + id;
                                    axios.delete(url).then(function (value) {
                                        if (0 != value.data.length) {
                                            $.alert('系统异常，请重试!');
                                        }
                                        else {
                                            $.alert('成功删除!');
                                            articleVue.list(0);
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
                            content: '您正在删除文章',
                            theme: 'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='articleCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath() + articleVue.uri + "/" + input.val();
                                            axios.delete(url).then(function (value) {
                                                if (0 != value.data.length) {
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else {
                                                    $.alert('成功删除!');
                                                    input.prop("checked", false);
                                                    articleVue.list(0);
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
                    search:

                        function () {
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
                                        articleVue.articles = value.data;
                                        $(".back_article_list_table").show();
                                        $(".notfound_search").hide();
                                    }
                                    else {
                                        $(".back_article_list_table").hide();
                                        $(".notfound_search").show();
                                    }
                                }
                            );
                        }
                }
            }
            )
        ;

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
)
;
