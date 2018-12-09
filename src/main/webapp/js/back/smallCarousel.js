$(
    function () {
        var carousels={
            uri:"/admin/images/carousels",
            addCarousel:{id:0,sid:0,role:0,type:'type_store',status:1},
            editCarousel:{id:0,sid:0,role:0,type:'type_store',status:1},
            carousels:[],
            image:null
        };
        var carouselVue = new Vue(
            {
                el:".container",
                data:carousels,
                mounted:function () {
                    this.list();
                },
                methods:{
                    list:function () {
                        var url = getPath()+this.uri+"?type=type_store";
                        axios.get(url).then(
                            function (value) {
                                carouselVue.carousels=value.data; }
                        );
                    },
                    getImage: function (id) {
                        if (id == 0)
                            return;
                        var url = getPath() + "/image/carousel_home/" + id + ".jpg";
                        return url;
                    },
                    deleteAllButton:function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除小轮播图',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='carouselCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+carouselVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');                                                  }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    carouselVue.list(0);
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
                    setFile:function(e)
                    {
                        this.image = e.target.files[0];
                    },
                    getFile:function (e) {
                        this.image = e.target.files[0];
                        if (!checkEmpty(this.image, '产品图片')) {
                            return;
                        }
                        var url = getPath()+this.uri;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("type", carouselVue.addCarousel.type);
                        formData.append("sid", carouselVue.addCarousel.sid);
                        formData.append("status", carouselVue.addCarousel.status);
                        axios.post(url, formData).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加小轮播图成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    carouselVue.image = null;
                                                    location.reload();        location.reload();
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    editCarousel_:function(id) {
                        var select = $("#select");
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function (value) {
                            carouselVue.editCarousel = value.data;
                            select.empty();
                            $.each(carouselVue.carousels, function (index, content) {
                                if (content.role == carouselVue.editCarousel.role) {
                                    select.append("<option value=" + content.role + ">" + content.role + "</option>");
                                    select.selectpicker('val', content.role);
                                }
                                else
                                    select.append("<option value=" + content.role + ">" + content.role + "</option>");
                            });
                            select.selectpicker('refresh');
                            select.selectpicker('render');
                            $("#editCarouselModel").modal("show");
                        });
                    },
                    updateCarousel:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        var formData = new FormData();
                        formData.append("image", this.image);
                        formData.append("status", carouselVue.editCarousel.status);
                        formData.append("sid", carouselVue.editCarousel.sid);
                        formData.append("type", carouselVue.editCarousel.type);
                        formData.append("role", carouselVue.editCarousel.role);
                        $("#editCarouselModel").modal("hide");
                        axios.put(url,formData).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改小轮播图成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                location.reload();
                                                carouselVue.image=null;
                                            }
                                        }
                                    }
                                }
                            );

                        })
                    },
                    deleteCarousel:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除小轮播图',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+this.uri+"/"+id;
                                    axios.delete(url).then(function (value) {
                                        if(0!=value.data.length)
                                            $.alert('系统异常，请重试!');
                                        else
                                        {
                                            $.alert('成功删除!');
                                            carouselVue.list();
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