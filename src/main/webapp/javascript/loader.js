function showLoader() {
    $("#globalLoader").css("display", "flex");
}

function hideLoader() {
    setTimeout(function () {
        $("#globalLoader").hide();
    }, 500); // 
}
