$('#commentSubmit').click(function () {
    var newsId = $('#newsId').val();
    var verifyCode = $('#verifyCode').val();
    var commentator = $('#commentator').val();
    var commentBody = $('#commentBody').val();
    if (isNull(newsId)) {
        swal("参数异常", {
            icon: "warning",
        });
        return;
    }
    if (isNull(verifyCode)) {
        swal("请输入验证码", {
            icon: "warning",
        });
        return;
    }
    if (!validCN_ENString2_100(commentator)) {
        swal("请输入符合规范的名称(不要输入特殊字符)", {
            icon: "warning",
        });
        return;
    }
    if (!validCN_ENString2_100(commentBody)) {
        swal("请输入符合规范的评论内容(不要输入特殊字符)", {
            icon: "warning",
        });
        return;
    }
    var data = {
        "newsId": newsId, "verifyCode": verifyCode, "commentator": commentator, "commentBody": commentBody
    };
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: '/admin/detail/news/comment',
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                swal("保存成功", {
                    icon: "success",
                });
                $('#commentBody').val('');
                $('#verifyCode').val('');
            } else {
                swal(result.message, {
                    icon: "error",
                });
            };
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});


$(document).ready(function () {
    var praiseButton = $('#praise-button');
    var praised = praiseButton.data("praise");
    if (praised === 1) {
        praiseButton.text("已点赞");
    } else {
        praiseButton.text("未点赞");
    }
});


function praiseNews() {
    var praiseButton = $('#praise-button');
    var newsId = praiseButton.val();
    var praised = praiseButton.data('praise');
    //做按钮反转
    if (praised === 1) {
        praiseButton.text("未点赞");
        praiseButton.data('praise', 0);
    } else {
        praiseButton.text("已点赞");
        praiseButton.data('praise', 1);
    }
    //  创建对象
    var newsInfo = {
        "newsId": newsId,
        "praised": praised
    };
    console.log(newsInfo);
    $.ajax({
        type: "POST",
        url: "/admin/detail/news/praise",
        contentType: "application/json",
        data: JSON.stringify(newsInfo),
        success: function (r) {
            if (r.resultCode == 200) {
                if (praised === 1) {
                    swal("取消点赞", {
                        icon: "success",
                    });
                } else {
                    swal("点赞成功", {
                        icon: "success",
                    });
                }
            } else {
                swal(r.message, {
                    icon: "error",
                });
            }
        }
    });
}