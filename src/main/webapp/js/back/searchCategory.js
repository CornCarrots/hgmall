$(
    function () {
        var categories = {
            uri: '/admin/categories',
            key:'',
            searchBean:[],
            editBean:{id:0,name:'',sid:0,type:'type_product',store:{}}
        };

        var searchVue = new Vue(
            {
                el: ".container",
                data: categories,
                mounted: function () {this.get()},
                methods:{
                    get:function () {
                        this.key = getUrlParms("key");
                        var url = getPath()+this.uri+"/search?key="+this.key
                        axios.post(url).then(
                            function (value) {

                                if (value.data.length == 0)
                                {
                                    $(".back_category_list_table").hide();
                                    $(".notfound_search").show();
                                }
                                else
                                {
                                    $(".back_category_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                searchVue.searchBean=value.data;
                            }
                        );
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
                        var url = getPath()+"/admin/category/search?key="+this.key;
                        location.href = url
                    }, addCategoryButton:function()
                    {
                        $("#addCategoryModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除分类',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='categoryCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+searchVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');                                                  }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    // searchVue.list(0);
                                                    location.reload();
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
                    deleteCategory:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除分类',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+searchVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');
                                        }
                                        else{
                                            $.alert('成功删除!');
                                            // searchVue.list(0);
                                            location.reload();
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
                            searchVue.editBean=value.data;
                        });
                        $("#editCategoryModel").modal("show");
                    },
                    updateCategory:function (id) {
                        if(!checkEmpty(this.editBean.name,'分类名字'))
                        {
                            return;
                        }
                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.editBean).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改分类成功',
                                    theme:'modern',
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
                            // searchVue.editBean={id:0,name:'',sid:0,type:'type_product'};
                            // $("#editCategoryModel").modal("hide");
                        });

                    }
                }
            }
        );
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
);