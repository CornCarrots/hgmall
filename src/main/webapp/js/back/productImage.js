$(
    function () {
        var checkableTree=null;
        var productImages = {
            uri1: '/admin/categories/',
            uri2:'/products/',
            singles:{},
            details:{},
            addBean:{id:0,pid:0,type:''},
            deleteBean:{id:0,pid:0,type:''},
            pid:0,
            product:{},
            cid:0,
            image:null
        };
        var productImageVue = new Vue(
            {
                el:".container",
                data:productImages,
                mounted:function () {
                    var url = getPath()+this.uri1+"products/tree";
                    axios.post(url).then(
                        function (value) {
                            var t = value.data;
                            checkableTree = $("#treeview-checkable").treeview({
                                data: t,
                                showTags: true,
                                highlightSelected: true,    //是否高亮选中
                                emptyIcon: '',    //没有子节点的节点图标
                                onNodeSelected : productImageVue.nodeSelected
                            });
                            var node = checkableTree.treeview('getSelected');
                            productImageVue.nodeSelected('nodeSelected',node[0]);
                        }
                    );
                },
                methods: {
                    nodeSelected: function (event, node) {
                        productImageVue.pid = node.id;
                        productImageVue.addBean.pid = productImageVue.pid;
                        productImageVue.deleteBean.pid = productImageVue.pid;
                        var parentNode = checkableTree.treeview('getParent', node);
                        productImageVue.cid = parentNode.id;
                        productImageVue.get();
                    },
                    get:function () {
                        var url = getPath()+productImageVue.uri1+productImageVue.cid+productImageVue.uri2+productImageVue.pid+"/productImages";
                        axios.get(url).then(
                            function (value) {
                                if(Object.keys(value.data.singles).length > 0)
                                {
                                    $(".productImageSingle-table").show();
                                    $(".notfound_list").hide();
                                    productImageVue.singles = value.data.singles;
                                    productImageVue.product = value.data.singles[0].product;
                                }
                                else if(Object.keys(value.data.details).length > 0)
                                {
                                    $(".productImageDetail-table").show();
                                    $(".notfound_list").hide();
                                    productImageVue.details = value.data.details;
                                }
                                // if(Object.keys(value.data.singles).length > 0&&Object.keys(value.data.details).length > 0)
                                // {
                                //     $(".productImageSingle-table").show();
                                //     $(".productImageDetail-table").show();
                                //     $(".notfound_list").hide();
                                //     productImageVue.singles = value.data.singles;
                                //     productImageVue.details = value.data.details;
                                //     productImageVue.product = value.data.singles[0].product;
                                // }
                                else
                                {
                                    var url = getPath()+productImageVue.uri1+productImageVue.cid+productImageVue.uri2+productImageVue.pid;
                                    axios.get(url).then(function(value){
                                        productImageVue.product = value.data;
                                    });
                                    $(".productImageSingle-table").hide();
                                    $(".productImageDetail-table").hide();
                                    $(".notfound_list").show();
                                }
                            }
                        );
                    },
                    getSingle: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/productSingle_middle/" + id + ".jpg";
                        return url;
                    },
                    getDetail: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/productDetail/" + id + ".jpg";
                        return url;
                    },
                    getImage: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/productSingle/" + id + ".jpg";
                        return url;
                    },
                    getFile: function (e,key) {
                        this.image = e.target.files[0];
                        var type = key;
                        productImageVue.addBean.type ='type_'+type;
                        if (!checkEmpty(this.image, '产品图片')) {
                            return;
                        }
                        var url = getPath()+productImageVue.uri1+productImageVue.cid+productImageVue.uri2+productImageVue.pid+"/productImages";
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("type", productImageVue.addBean.type);
                        formData.append("pid", productImageVue.addBean.pid);
                        axios.post(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加产品图片成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    productImageVue.image = null;
                                                    productImageVue.addBean.type = '';
                                                    location.reload();
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteProductImage_:function (id,type) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除产品图片',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+productImageVue.uri1+productImageVue.cid+productImageVue.uri2+productImageVue.pid+"/productImages/"+id;
                                    axios.delete(url).then(
                                        function (value) {
                                            if(0!=value.data.length)
                                                $.alert('系统异常，请重试!');
                                            else
                                            {
                                                $.alert('成功删除!');
                                                location.reload();
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

                    }
                }
            }
        );
    }
);