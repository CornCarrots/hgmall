$(
    function () {
        var checkableTree = null;
        var propertyValues = {
            uri1: '/admin/categories/',
            uri2: '/products/',
            propertyValues: [],
            addBean:{id:0,pid:0,ptid:0,value:''},
            pid: 0,
            cid: 0
        };
        var propertyValueVue = new Vue(
            {
                el: ".container",
                data: propertyValues,
                mounted: function () {
                    var url = getPath() + this.uri1 + "products/tree";
                    axios.post(url).then(
                        function (value) {
                            var t = value.data;
                            checkableTree = $("#treeview-checkable").treeview({
                                data: t,
                                showTags: true,
                                highlightSelected: true,    //是否高亮选中
                                emptyIcon: '',    //没有子节点的节点图标
                                onNodeSelected: propertyValueVue.nodeSelected
                            });
                            var node = checkableTree.treeview('getSelected');
                            propertyValueVue.nodeSelected('nodeSelected', node[0]);
                        }
                    );
                },
                methods: {
                    nodeSelected: function (event, node) {
                        propertyValueVue.pid = node.id;
                        var parentNode = checkableTree.treeview('getParent', node);
                        propertyValueVue.cid = parentNode.id;
                        propertyValueVue.get();
                    },
                    get: function () {
                        var url = getPath() + propertyValueVue.uri1 + propertyValueVue.cid + propertyValueVue.uri2 + propertyValueVue.pid + "/propertyValues";
                        axios.get(url).then(
                            function (value) {
                                if(value.data.length !=0)
                                {
                                    propertyValueVue.propertyValues = value.data;
                                    $(".propertyValue_list").show();
                                    $(".notfound_list").hide();
                                }
                                else
                                {
                                    $(".propertyValue_list").hide();
                                    $(".notfound_list").show();
                                }

                            }
                        );
                    },
                    updatePropertyValue:function (e,id,ptid,pid) {
                        var data = e.target.value;
                        propertyValueVue.addBean.value=data;
                        propertyValueVue.addBean.id = id;
                        propertyValueVue.addBean.ptid = ptid;
                        propertyValueVue.addBean.pid = pid;
                        var url = getPath() + propertyValueVue.uri1 + propertyValueVue.cid + propertyValueVue.uri2 + propertyValueVue.pid + "/propertyValues/"+id;
                        axios.put(url,propertyValueVue.addBean).then(function (value) { })
                    }
                }
            }
        );
    }
);