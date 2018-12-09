$(
    function () {

        var categories = {
            uri: '/admin/articles/categories',
            pages: {},
            categories: [],
            category:{id:0,name:'',sid:0,type:'type_article'},
            key:''
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
                                if(value.data.content.length>0)
                                {
                                    vue.pages = value.data
                                    vue.categories = value.data.content;
                                    $(".back_art_category_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else
                                {
                                    $(".back_art_category_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(
                                    function () {
                                        $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                        $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

                                        checkListener();
                                    }
                                );
                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,vue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,vue);
                    },
                    addCategoryButton:function()
                    {
                        $("#addCategoryModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除文章分类',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='categoryCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+vue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');                                                }
                                                else{
                                                      $.alert('成功删除!');
                                                    input.prop("checked",false);
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
                    search:function()
                    {
                        if(!checkEmpty(this.key,'关键词'))
                        {
                            return;
                        }
                        if(this.key.length>=10)
                        {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var key = this.key;
                        var url = getPath()+vue.uri+"/search/?key="+key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if(value.data.length>0)
                                {
                                    vue.categories=value.data;
                                    $(".back_art_category_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                else
                                {
                                    $(".back_art_category_list_table").hide();
                                    $(".notfound_search").show();
                                }
                            }
                        );
                    },
                    addCategory:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        var uri = getPath() + this.uri;
                        var bean = this.category;
                        axios.post(uri,bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加文章分类',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    vue.list(0);
                                                    vue.category={id:0,name:'',sid:0,type:'type_article'};
                                                    $("#addCategoryModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteCategory:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除文章分类',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+vue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');
                                        }
                                        else{
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
                    editCategory:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function(value){
                            vue.category=value.data;
                        });
                        $("#editCategoryModel").modal("show");
                    },
                    updateCategory:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.category).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改文章分类成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                vue.category={id:0,name:'',sid:0,type:'type_article'};
                                                vue.list(0);
                                                $("#editCategoryModel").modal("hide");
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