function showLeft(path) {
    var alink = $(".list-group a[href*='"+path+"']");
    alink.css("color","red");

    alink.parent().parent().parent().removeClass("tree-closed");
    alink.parent().parent().show();

}

