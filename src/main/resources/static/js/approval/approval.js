function success() {
    var urlParams = new URLSearchParams(window.location.search);
    var boardId = urlParams.get("board_id");
    var comment = document.getElementById("comment").value;
    $.ajax({
        type: "POST",
        url: "/approval/approvalSuccessForm", // 컨트롤러 엔드포인트 URL
        data: {
            board_id: boardId,
            comment: comment
        },
        success: function(response) {

            alert("결재가 성공적으로 완료되었습니다.");
            window.location.href = "/approval/approvalResultForm";

        },
        error: function() {
            // 요청이 실패한 경우 여기에서 처리할 동작을 추가합니다.
            alert("요청을 보내는 중에 오류가 발생했습니다.");
        }
    });

}

function fail() {
    var urlParams = new URLSearchParams(window.location.search);
    var boardId = urlParams.get("board_id");
    var comment = document.getElementById("comment").value;
    $.ajax({
        type: "POST",
        url: "/approval/approvalFailForm", // 컨트롤러 엔드포인트 URL
        data: {
            board_id: boardId,
            comment: comment
        },
        success: function(response) {

            alert("결재가 성공적으로 완료되었습니다.");
            window.location.href = "/approval/approvalResultForm";

        },
        error: function() {
            // 요청이 실패한 경우 여기에서 처리할 동작을 추가합니다.
            alert("요청을 보내는 중에 오류가 발생했습니다.");
        }
    });

}