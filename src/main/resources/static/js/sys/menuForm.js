$(function () {

    $("#form").validate();
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/menu/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("操作成功！");
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        parent.reloadGrid();
                    } else {
                        Util.msgError("操作失败！");
                    }
                }
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });

    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

    $("input:radio[name='type']").on('ifChecked', function (event) {
        $(".menu-items")[$(this).val() === "1" ? "hide" : "show"]();
        $("#permissions-div")[$(this).val() === "1" ? "show" : "hide"]();
        if ($(this).val() === "1") {
            $("#menu-show").iCheck('check');
        }
    });
    $("input:radio[name='type']:checked").val() === "1" ? $(".menu-items").hide() : $(".menu-items").show();

    $.post(Util.getPath() + "/menu/treeall/list", function (res) {
        if (res.code === 200) {
            var ztreeDown = $("#parentName").ztreeDown(res.data, {
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        if (treeNode.id === $("#id").val()) {
                            return;
                        }
                        $("#parentId").val(treeNode.id);
                        $("#parentName").val(treeNode.name);
                        ztreeDown.hideMenu();
                    }
                }
            });

            $("#parentName").click(function () {
                ztreeDown.zTree.expandAll(true);
                ztreeDown.showMenu();
            });
        }
    }, "JSON");

});