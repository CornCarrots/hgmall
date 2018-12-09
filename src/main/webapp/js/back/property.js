$(
    function () {
        var checkableTree=null;
        var properties = {
            uri: '/admin/categories',
            pages:{},
            properties:[],
            addProperty:{id:0,cid:0,name:''},
            editProperty:{id:0,cid:0,name:'',category:{}},
            cid:0,
            category:{},
            key:''
        };
        var propertyVue = new Vue(
            {
                el:".container",
                data:properties,
                mounted:function () {
                    this.init();

                },
                methods:{
                    init:function(){
                        var url = getPath()+this.uri+"/tree";
                        axios.post(url).then(
                            function (value) {
                                var t = value.data;
                                checkableTree = $("#treeview-checkable").treeview({
                                    data: t,
                                    showTags: true,
                                    highlightSelected: true,    //是否高亮选中
                                    emptyIcon: '',    //没有子节点的节点图标
                                    onNodeSelected : propertyVue.nodeSelected
                                    // onNodeSelected : nodeSelected ,
                                    // onNodeUnSelected : nodeUnselected
                                });

                                var node = checkableTree.treeview('getSelected');
                                propertyVue.nodeSelected('nodeSelected',node[0]);

                            }
                        );

                    },
                    nodeSelected:  function  (event, node) {
                        propertyVue.cid = node.id;
                        propertyVue.addProperty.cid=propertyVue.cid;
                        propertyVue.editProperty.cid=propertyVue.cid;
                        propertyVue.get();
                    },
                    get:function () {
                        var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+"/properties";
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    $(".back_property_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                    propertyVue.pages=value.data;
                                    propertyVue.properties=value.data.content;
                                    propertyVue.category=propertyVue.properties[0].category;
                                }
                                else
                                {
                                    var url = getPath()+"/admin/categories/"+propertyVue.cid;
                                    axios.get(url).then(function(value){
                                        propertyVue.category=value.data;
                                    });
                                    $(".back_property_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    checkListener();
                                });
                            }
                        );
                    },
                    list: function (start) {
                        var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+ "/properties?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    $(".back_property_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show()
                                    $(".notfound_search").hide();

                                    propertyVue.pages=value.data;
                                    propertyVue.properties=value.data.content;
                                    propertyVue.category=propertyVue.properties[0].category;
                                }
                                else
                                {
                                    var url = getPath()+"/admin/categories/"+propertyVue.cid;
                                    axios.get(url).then(function(value){
                                        propertyVue.category=value.data;
                                    });
                                    $(".back_property_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();


                                }
                            }
                        );
                    },
                    jump:function (page) {
                        jump(page,propertyVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,propertyVue);
                    },
                    addButton:function(){
                        $("#addPropertyModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除属性',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='propertyCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+"/properties/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    propertyVue.list(0);
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
                    addProperty_:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;
                        var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+ "/properties";
                        axios.post(url,propertyVue.addProperty).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加属性成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    propertyVue.addProperty={id:0,cid:propertyVue.cid,name:''};
                                                    $("#addPropertyModel").modal("hide");
                                                    propertyVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteProperty_:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除属性',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+"/properties/"+id;
                                    axios.delete(url).then(
                                        function (value) {
                                            if(0!=value.data.length)
                                                $.alert('系统异常，请重试!');
                                            else
                                            {
                                                $.alert('成功删除!');
                                                propertyVue.list(0);
                                            }
                                        })
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });

                    },
                    editProperty_:function (id) {
                        var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+"/properties/"+id;
                        axios.get(url).then(
                            function (value) {
                                propertyVue.editProperty=value.data;
                                $("#editPropertyModel").modal("show");
                            }
                        );
                    },
                    updateProperty_:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;
                        var url = getPath()+propertyVue.uri+"/"+propertyVue.cid+"/properties/"+id;
                        axios.put(url,propertyVue.editProperty).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '修改属性成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    propertyVue.editProperty={id:0,cid:0,name:'',category:{}};
                                                    $("#editPropertyModel").modal("hide");
                                                    propertyVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    searchProperty_:function () {
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
                        var url = getPath()+"/admin/categories/"+this.cid+"/properties/search?key="+this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if(value.data.length>0)
                                {
                                    propertyVue.properties=value.data;
                                    $(".back_property_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                else
                                {
                                    $(".back_property_list_table").hide();
                                    $(".notfound_search").show();
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
                    console.log(1)
                    checkOne();
                }
            );
        }

    });