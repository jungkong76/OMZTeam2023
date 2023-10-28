
let checkInBtn = document.querySelector("#checkInBtn");
let checkOutBtn = document.querySelector("#checkOutBtn");
const isLate = 0;

// Check in Btn click 이벤트
checkInBtn.addEventListener("click", function () {
    const url = "/check/in"
    // fetch를 사용하여 POST 요청 보내기
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // JSON 형식으로 데이터를 전송하는 것을 명시
        },
        body: JSON.stringify({ // 객체를 JSON 문자열로 변환하여 전송
            isLate: isLate
        })
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // JSON 형식으로 응답 데이터를 파싱
            } else {
                throw new Error('응답이 실패하였습니다.');
            }
        })
        .then(data => {
            // 응답에서 받은 시간 데이터를 사용하여 input 태그에 값을 설정
            const checkIn  = document.querySelector("#checkIn");
            checkIn.value = data.inTime;

            // controller 에서 전달된 메시지 출력
            var msg = data.msg;

            if (msg === "CHK_IN_OK") {
                alert('출근하였습니다.');
            }
            if (msg === "CHK_IN_ALD") {
                alert("이미 출근 처리가 되었습니다.");
            }
        })
        .catch(error => {
            console.error('에러 발생:', error);
        });
});

// Check out Btn click 이벤트
checkOutBtn.addEventListener("click", function () {
    const url = "/check/out"
    // fetch를 사용하여 POST 요청 보내기
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // JSON 형식으로 데이터를 전송하는 것을 명시
        },
        body: JSON.stringify({ // 객체를 JSON 문자열로 변환하여 전송
        })
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // JSON 형식으로 응답 데이터를 파싱
            } else {
                throw new Error('응답이 실패하였습니다.');
            }
        })
        .then(data => {
            // 응답에서 받은 시간 데이터를 사용하여 input 태그에 값을 설정
            const checkOut  = document.querySelector("#checkOut");
            checkOut.value = data.outTime;

            // controller 에서 전달된 메시지 출력
            var msg = data.msg;

            if (msg === "CHK_OUT_OK") {
                alert('퇴근하였습니다.');
            }
            if (msg === "CHK_OUT_ALD") {
                alert("이미 퇴근 처리가 되었습니다.");
            }
            if (msg === "CHK_IN_YET") {
                alert("출근 이력이 확인되지 않습니다.");
            }
        })
        .catch(error => {
            console.error('에러 발생:', error);
        });
});
