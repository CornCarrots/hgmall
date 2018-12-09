$(
    function () {
        var checkableTree=null;
        var nodeCheckedSilent = false;
        var nodeUncheckedSilent = false;
        var permissions = {
            uri: '/admin/roles/',
            operations:[],
            mid:0,
            oid:[],
            permission:{}
        };
        var permissionVue = new Vue(
            {
                el:".container",
                data:permissions,
                mounted:function () {
                    var rid = getUrlParms("rid");
                    this.uri = this.uri+rid+"/permissions";
                    var url = getPath()+this.uri+"/operations";
                    axios.get(url).then(function (value) {
                        var operation = new Array();
                        $(value.data).each(
                            function (i,data) {
                                operation.push(data);
                                if((i+1)%5==0||i==value.data.length-1)
                                {
                                    permissionVue.operations.push(operation);
                                    operation = new Array();
                                }
                            }
                        );
                    });
                    var url = getPath()+this.uri+"/tree";
                    axios.post(url).then(
                        function (value) {
                            var t = value.data;
                            checkableTree = $("#treeview-checkable").treeview({
                                data: t,
                                showTags: true,
                                showCheckbox: true,   //是否显示复选框
                                highlightSelected: true,    //是否高亮选中
                                emptyIcon: '',    //没有子节点的节点图标
                                checkboxes: true,
                                onNodeChecked:   permissionVue.nodeChecked,
                                onNodeUnchecked: permissionVue.nodeUnchecked,
                                onNodeSelected : permissionVue.nodeSelected
                            });
                            var node = checkableTree.treeview('getSelected');
                            permissionVue.nodeSelected('nodeSelected',node[0]);
                        }
                    );
                },
                methods:{
                    nodeSelected:function  (event, node) {
                        permissionVue.mid = node.id;
                        permissionVue.get();
                    },
                    nodeChecked:function (event, node) {
                        if (nodeCheckedSilent) {
                            return;
                        }
                        nodeCheckedSilent = true;
                        checkAllParent(node);
                        checkAllSon(node);
                        nodeCheckedSilent = false;
                    },
                    nodeUnchecked:function (event, node) {
                        // $('#treeview-checkable').treeview('unselectNode', node);
                        $("input[name='operationCheckBox']").prop("checked",false);
                        if (nodeUncheckedSilent)
                            return;
                        nodeUncheckedSilent = true;
                        uncheckAllParent(node);
                        uncheckAllSon(node);
                        nodeUncheckedSilent = false;
                    },
                    get:function () {
                        $("input[name='operationCheckBox']").prop("checked",false);
                        var url = getPath()+this.uri+"/"+this.mid;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.length > 0)
                                {
                                    $.each(value.data,function (i, data) {
                                        $("#"+data.id).prop("checked",true);
                                    });
                                }
                            }
                        );
                    },
                    update:function (oid,e) {

                        var nodes = $('#treeview-checkable').treeview('getSelected');
                        if(nodes.length<=0)
                        {
                            $.alert("请先选择需要修改的模块!");
                            $(e.target).prop("checked",false);
                            return false;
                        }
                        var node = $(nodes)[0];
                        if (node.state.checked==false)
                        {
                            $.alert("请先勾选需要修改的模块!");
                            $(e.target).prop("checked",false);
                            return false;
                        }
                        var found = null;
                        var parentNode = $('#treeview-checkable').treeview('getParent', node);
                        if(parentNode.id != undefined)
                        {
                            var url = getPath()+this.uri+"/"+parentNode.id;
                            $.ajax(
                                {
                                    type:"get",
                                    url:url,
                                    async:false,
                                    success:function (value) {
                                        if(value.length == 0)
                                        {
                                            found = true;
                                            $.alert("请先为父模块选择权限!");
                                            $(e.target).prop("checked",false);
                                        }
                                    }
                                }
                            );
                        }
                        if(found)
                            return false;
                        var mid =  node.id;
                        url = getPath()+this.uri+"/"+mid;
                        permissionVue.permission = {mid:mid,oid:oid};
                        var checked = $(e.target).prop("checked");
                        if(checked)
                            axios.put(url,permissionVue.permission);
                        else
                        {
                            url +="/"+oid;
                            axios.delete(url);
                        }
                        $.alert(
                            {
                                title: '恭喜你!',
                                content: '修改权限成功',
                                theme:'modern',
                                icon: 'fa fa-smile-o'
                            }
                        );
                    }
                }
            }
        );
    });

//选中全部父节点  
function checkAllParent(node) {
    $('#treeview-checkable').treeview('checkNode', node.nodeId, {silent: true});
    var parentNode = $('#treeview-checkable').treeview('getParent', node.nodeId);
    if (!("nodeId" in parentNode)) {
        return;
    } else {
        checkAllParent(parentNode);
    }
}

//取消全部父节点  
function uncheckAllParent(node) {
    $('#treeview-checkable').treeview('uncheckNode', node.nodeId, {silent: true});
    var siblings = $('#treeview-checkable').treeview('getSiblings', node.nodeId);
    var parentNode = $('#treeview-checkable').treeview('getParent', node.nodeId);
    if (!("nodeId" in parentNode)) {
        return;
    }
    var isAllUnchecked = true;  //是否全部没选中  
    for (var i in siblings) {
        if (siblings[i].state.checked) {
            isAllUnchecked = false;
            break;
        }
    }
    if (isAllUnchecked) {
        uncheckAllParent(parentNode);
    }

}

//级联选中所有子节点  
function checkAllSon(node) {
    $('#treeview-checkable').treeview('checkNode', node.nodeId, {silent: true});
    if (node.nodes != null && node.nodes.length > 0) {
        for (var i in node.nodes) {
            checkAllSon(node.nodes[i]);
        }
    }
}

//级联取消所有子节点  
function uncheckAllSon(node) {
    $('#treeview-checkable').treeview('uncheckNode', node.nodeId, {silent: true});
    if (node.nodes != null && node.nodes.length > 0) {
        for (var i in node.nodes) {
            uncheckAllSon(node.nodes[i]);
        }
    }
}

