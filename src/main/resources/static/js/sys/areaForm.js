$(function () {

	



    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/area/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("操作成功！");
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        parent.reloadGrid();
                    }else{
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

    $("#parentDiv").click(function () {
        Util.openIframe(Util.getPath() + "/area/panel", function (index, layero) {
            var contentWindow = layero.find('iframe')[0].contentWindow;
            var selectItem = contentWindow.getSelected();
            if (selectItem) {
				if($("#id").val() === selectItem.id){
					Util.msgWarning("不能选择自己成为父类！");
					return;
				}
                $("#parentId").val(selectItem.id);
                $("#parentName").val(selectItem.name);
            } else {
                $("#parentId").val("");
                $("#parentName").val("");
            }
            Util.closeLayer(index);
        });
    });

});