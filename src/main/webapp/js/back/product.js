$(
    function () {
        var checkableTree=null;
        var products = {
            uri: '/admin/categories',
            pages:{},
            products:[],
            addProduct:{id:0,name:'',subTitle:'',originalPrice:0,promotePrice:0,stock:0,cid:0,createDate:null,status:0,sales:0,reviews:0},
            editProduct:{id:0,name:'',subTitle:'',originalPrice:0,promotePrice:0,stock:0,cid:0,createDate:null,status:0,sales:0,reviews:0},
            cid:0,
            category:{},
            key:''
        };
        var productVue = new Vue(
            {
                el:".container",
                data:products,
                mounted:function () {
                    var url = getPath()+this.uri+"/tree";
                    axios.post(url).then(
                        function (value) {
                            var t = value.data;
                            checkableTree = $("#treeview-checkable").treeview({
                                data: t,
                                showTags: true,
                                highlightSelected: true,    //是否高亮选中
                                emptyIcon: '',    //没有子节点的节点图标
                                onNodeSelected : productVue.nodeSelected
                            });
                            var node = checkableTree.treeview('getSelected');
                            productVue.nodeSelected('nodeSelected',node[0]);
                        }
                    );
                },
                methods:{
                    nodeSelected:  function  (event, node) {
                        productVue.cid = node.id;
                        productVue.addProduct.cid=productVue.cid;
                        productVue.editProduct.cid=productVue.cid;
                        productVue.get();
                    },
                    get:function () {
                        var url = getPath()+productVue.uri+"/"+productVue.cid+"/products";
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length > 0)
                                {
                                    $(".back_product_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                    productVue.pages=value.data;
                                    productVue.products=value.data.content;
                                    productVue.category=productVue.products[0].category;
                                }
                                else
                                {
                                    var url = getPath()+"/admin/categories/"+productVue.cid;
                                    axios.get(url).then(function(value){
                                        productVue.category=value.data;
                                    });
                                    $(".back_product_list_table").hide();
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
                        var url = getPath()+productVue.uri+"/"+productVue.cid+ "/products?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length > 0)
                                {
                                    $(".back_product_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show()
                                    $(".notfound_search").hide();

                                    productVue.pages=value.data;
                                    productVue.products=value.data.content;
                                    productVue.category=productVue.products[0].category;
                                }
                                else
                                {
                                    var url = getPath()+"/admin/categories/"+productVue.cid;
                                    axios.get(url).then(function(value){
                                        productVue.category=value.data;
                                    });
                                    $(".back_product_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                            }
                        );
                    },
                    jump:function (page) {
                        jump(page,productVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,productVue);
                    },
                    getImage: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/productSingle_small/" + id + ".jpg";
                        return url;
                    },
                    addButton:function(){
                        $("#addProductModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除产品',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='productCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+productVue.uri+"/"+productVue.cid+"/products/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    productVue.list(0);
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
                    addProduct_:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;
                        var url = getPath()+productVue.uri+"/"+productVue.cid+ "/products";
                        axios.post(url,productVue.addProduct).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加产品成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    productVue.addProduct={id:0,name:'',subTitle:'',originalPrice:0,promotePrice:0,stock:0,cid:productVue.cid,createDate:null,sales:0,reviews:0};
                                                    $("#addProductModel").modal("hide");
                                                    productVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteProduct_:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除产品',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+productVue.uri+"/"+productVue.cid+"/products/"+id;
                                    axios.delete(url).then(
                                        function (value) {
                                            if(0!=value.data.length)
                                                $.alert('系统异常，请重试!');
                                            else
                                            {
                                                $.alert('成功删除!');
                                                productVue.list(0);
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
                    editProduct_:function (id) {
                        var url = getPath()+productVue.uri+"/"+productVue.cid+"/products/"+id;
                        axios.get(url).then(
                            function (value) {
                                productVue.editProduct=value.data;
                                $("#editProductModel").modal("show");
                            }
                        );
                    },
                    updateProduct_:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;
                        var url = getPath()+productVue.uri+"/"+productVue.cid+"/products/"+id;
                        axios.put(url,productVue.editProduct).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '修改产品成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    productVue.editProduct={id:0,name:'',subTitle:'',originalPrice:0,promotePrice:0,stock:0,cid:productVue.cid,createDate:null,sales:0,reviews:0};
                                                    $("#editProductModel").modal("hide");
                                                    productVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    searchProduct_:function () {
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
                        var url = getPath()+"/admin/categories/"+this.cid+"/products/search?key="+this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if(value.data.length>0)
                                {
                                    productVue.products=value.data;
                                    $(".back_product_list_table").show();
                                    $(".notfound_search").hide();
                                }
                                else
                                {
                                    $(".back_product_list_table").hide();
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
                    checkOne();
                }
            );
        }

    });