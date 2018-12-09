$(
    function () {

        var editor;
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
        var bean = {
            uri:"/foreApplyStore",
            categories:[],
            store: {
                id: 0, name: '', cid: 0, type: 'type_private', applyDate: '', addDate: '',
                applyName: '', mobile: '', telephone: '', identification: '',
                email: '', authentication_credit: 0, authentication_id: 0, authentication_phone: 0, summary_html: '', status: 0
            }
        };
        var storeVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function(){
                  this.initStore();
                },
                methods:{
                    initStore:function(){
                        var url = getPath()+this.uri+"?timeStamp="+new Date().getTime();
                        axios.get(url).then(
                            function (value) {
                                storeVue.categories=value.data.categories;
                                Vue.nextTick(function () {
                                    var html = '';
                                    var first ;
                                    $(storeVue.categories).each(
                                        function (i, data) {
                                            if (i == 0)
                                            {
                                                first = data.id;
                                            }
                                            html += '<option value=\"' + data.id + '\">' + data.name + '</option>';
                                        }
                                    );
                                    $("#select").html(html);
                                    $("#select").selectpicker('val', first);
                                    $("#select").selectpicker('refresh');
                                    $("#select").selectpicker('render');
                                    editor = KindEditor.create("#editor_id", options);
                                    $("#applyForm").validationEngine(
                                        {
                                            promptPosition:'centerRight',
                                            showArrow:true
                                        }
                                    );
                                    valControls( $("#applyForm")) ;
                                });
                            }
                        );
                    },
                    apply:function () {
                        storeVue.store.summary_html = editor.html();
                        //当存在错误信息时返回,找显示错误信息
                        if(controlId.length > 0) {
                            alertinfo(0) ;
                            return false ;
                        }
                        if(!$("#applyForm").validationEngine("validate"))
                            return false;  //验证没有使用ajax验证的控件(不是ajax验证的字段是可以正常验证的, 不通过就返回)

                        var url = getPath()+storeVue.uri+"?timeStamp="+new Date().getTime();
                        axios.post(url,storeVue.store).then(
                            function (value) {
                                location.href=getPath()+"/store_applySuccess";
                            }
                        );
                    },
                    reset:function () {
                        editor.html('');
                        storeVue.store = {
                            id: 0, name: '', cid: 0, type: 'type_private', applyDate: '', addDate: '',
                                applyName: '', mobile: '', telephone: '', identification: '',
                                email: '', authentication_credit: 0, authentication_id: 0, authentication_phone: 0, summary_html: '', status: 0
                        }
                    },
                    getUrl:function (key) {
                        return getPath()+key;
                    }
                }
            }
        );
    }

);