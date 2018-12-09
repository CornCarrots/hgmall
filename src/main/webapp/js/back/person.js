$(function () {

    var person = {
        uri:"/admin/person",
        manager:{},
        image:null
    };
    var personVue = new Vue(
        {
            el:".container",
            data:person,
            mounted:function () {
                this.get();
            },
            filters:{
                sexFilter:function (value) {
                    if(value==0)
                        return '男';
                    if(value==1)
                        return '女';
                }
            },
            methods:{
                get:function () {
                    var url = getPath()+this.uri;
                    axios.get(url).then(function (value) { personVue.manager = value.data });
                },
                getImage:function (id) {
                    if (id == 0||id==undefined)
                        return;
                    else
                        var url= getPath() + "/image/profile_manager/" + id + ".jpg";
                    return url;
                },
                setImage:function (e) {
                    this.image = e.target.files[0];
                },
                upload:function () {
                    if (!checkEmpty(this.image, '图片')) {
                        return;
                    }
                    var url = getPath() + this.uri+"/image";
                    var formData = new FormData();
                    formData.append("image", this.image);
                    formData.append("id", this.manager.id);
                    axios.post(url, formData).then(
                        function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '上传头像成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                personVue.image = null;
                                                location.reload();
                                            }
                                        }
                                    }
                                }
                            );

                        }
                    );
                },
                cancelUpload:function () {
                    this.image = null;
                },
                cancel:function () {
                    personVue.get();
                },
                update:function () {
                    var url = getPath() + this.uri+"/"+personVue.manager.id;
                    axios.put(url, personVue.manager).then(function (value) {
                        $.alert(
                            {
                                title: '恭喜你!',
                                content: '修改个人资料成功',
                                theme:'modern',
                                icon: 'fa fa-smile-o',
                                buttons: {
                                    ok: {
                                        action: function () {
                                            personVue.get();
                                        }
                                    }
                                }
                            }
                        );
                    })
                }
            }
        }
    );
    $(".back_person_list table tr td span:last-child").hide();
    $("#modifyButton").click(
        function () {
            $(".back_person_list table tr td span:first-child").hide();
            $(".back_person_list table tr td span:last-child").show();
            $(this).hide();
        }
    ); $("#saveButton").click(
        function () {
            $(".back_person_list table tr td span:first-child").show();
            $(".back_person_list table tr td span:last-child").hide();
            $("#modifyButton").show();
        }
    );$("#cancelButton").click(
        function () {
            $(".back_person_list table tr td span:first-child").show();
            $(".back_person_list table tr td span:last-child").hide();
            $("#modifyButton").show();
        }
    );
    $("#uploadFile").click(
        function () {
            $(this).parent().hide();
            $("#confirmButton").show();
        }
    );
    $("#confirmButton button").click(
        function () {
            $(this).parent().hide();
            $("#uploadFile").parent().show();
        }
    );
});