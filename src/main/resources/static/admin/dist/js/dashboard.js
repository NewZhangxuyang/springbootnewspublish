$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/news/list',
        datatype: "json",
        colModel: [{label: 'id', name: 'newsId', index: 'newsId', width: 50, key: true, hidden: true}, {
            label: '标题', name: 'newsTitle', index: 'newsTitle', width: 140
        }, {
            label: '包含图片',
            name: 'newsCoverImage',
            index: 'newsCoverImage',
            width: 120,
            formatter: coverImageFormatter
        }, {label: '浏览量', name: 'newsViews', index: 'newsViews', width: 60}, {
            label: '赞数',
            name: 'newsViews',
            index: 'newsViews',
            width: 60
        }, {
            label: '详情',
            name: 'newsViews',
            index: 'newsViews',
            width: 60,
            formatter: detailFormatter
        }, {label: '点赞', name: 'newsViews', index: 'newsViews', width: 60, formatter: praiseFormatter},],
        height: 700,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list", page: "data.currPage", total: "data.totalPage", records: "data.totalCount"
        },
        prmNames: {
            page: "page", rows: "limit", order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
    }

    function detailFormatter() {
        return "<button type=\"button\"  onclick=\"detailNews()\"   class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">详情</button>";
    }

    function praiseFormatter() {
        return "<button type=\"button\"  onclick=\"praiseNews()\"   class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">点赞</button>";
    }
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function reloadData() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    var newUrl = '/admin/news/list';
    $("#jqGrid").jqGrid('setGridParam'
        , {url: newUrl, prmNames: {page: page, rows: "limit", order: "order"}}
    ).trigger("reloadGrid");
}


function searchNews() {
    var searchQuery = $("#search-input").val();
    if (searchQuery == null) {
        return;
    }
    console.log("搜索内容: " + searchQuery);
    var newUrl = '/admin/detail/news/search?keyword=' + encodeURIComponent(searchQuery);
    console.log("newUrl: " + newUrl);
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    console.log("page: " + page);
    $("#jqGrid").jqGrid('setGridParam'
        , {url: newUrl, prmNames: {page: page, rows: "limit", order: "order"}}
    ).trigger("reloadGrid");

}

function detailNews() {
    var data = getNowRow();
    var id = data.newsId;
    if (id == null) {
        return;
    }
    window.open("/admin/detail/" + id, "_blank");
}

function praiseNews() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    $.ajax({
        type: "POST",
        url: "/admin/detail/news/praise",
        contentType: "application/json",
        data: JSON.stringify(ids),
        success: function (r) {
            if (r.resultCode == 200) {
                swal("点赞成功", {
                    icon: "success",
                });
                $("#jqGrid").trigger("reloadGrid");
            } else {
                swal(r.message, {
                    icon: "error",
                });
            }
        }
    });
}

function getNowRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("请选择一条记录", {
            icon: "warning",
        });
        return;
    }
    var rowData = grid.getRowData(rowKey);
    return rowData;
}


function exportExcel() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    axios({
        method: 'POST', url: "/admin/detail/news/export", timeout: 5000, headers: {
            'Content-Type': 'application/json'
        }, data: JSON.stringify(ids), responseType: 'blob'
    }).then(function (res) {
        var data = res.data;
        var blob = new Blob([data], {type: 'application/octet-stream'});
        var url = URL.createObjectURL(blob);
        var exportLink = document.createElement('a');
        exportLink.setAttribute("download", "export.xlsx");
        exportLink.href = url;
        document.body.appendChild(exportLink);
        exportLink.click();
    })
}

