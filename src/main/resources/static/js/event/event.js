document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: function(info, successCallback, failureCallback) {
            // 서버에서 이벤트 데이터를 가져오는 AJAX 요청
            $.ajax({
                url: '/calendarEvent/events', // 이벤트 데이터를 가져올 엔드포인트 URL
                type: 'GET', // HTTP GET 요청 사용
                success: function(data) {
                    // 데이터를 가져와 FullCalendar 형식에 맞게 가공
                    var events = data.map(function(item) {
                        return {

                            title: item.event_title,
                            start: item.date,
                            // color: 'blue',
                            eventId: item.event_id

                            // 필요에 따라 추가 속성을 설정할 수 있습니다.
                        };
                    });
                    successCallback(events);
                    // 데이터를 성공적으로 가져온 경우 successCallback에 전달
                    // successCallback(events);
                },
                error: function() {
                    // 데이터 가져오기에 실패한 경우
                    failureCallback();
                    alert('이벤트 데이터를 가져오는 중에 오류가 발생했습니다.');
                }
            });
        },
        dateClick: function (info){

            openCalendarModal(info.dateStr);
            // $("#calendarModal").modal("show");

        },
        eventClick: function (info){
            console.log(info)
            openCalendarEventModal(info.event._def.extendedProps.eventId);
        }
    });

    calendar.render();
});
function openCalendarModal(clickedDate) {
    $("#calendarModal").modal("show");

    // 클릭한 날짜를 모달 내부에 추가
    var clickedDateElement = document.getElementById('clickedDate');
    clickedDateElement.textContent = clickedDate;
    var title = document.getElementById('calendar_title');
    title.value = "";
    var description = document.getElementById('calendar_content');
    description.value = "";
    var addButton = document.getElementById('addCalendar'); // 버튼 엘리먼트를 가져옵니다
    var deleteButton = document.getElementById('deleteCalendar'); // 버튼 엘리먼트를 가져옵니다
    deleteButton.style.display = 'none';
    addButton.textContent = "추가";
}

// 버튼 클릭 시 이벤트를 처리하는 함수

function openCalendarEventModal (eventId){
    $.ajax({
        type: 'POST',
        url: "/calendarEvent/eventLoading",
        data: {
            eventId : eventId
        },
        success: function (response) {
            console.log(response)
            // 성공적으로 데이터를 받아왔을 때 처
            $("#calendarModal").modal("show");
            var clickedDateElement = document.getElementById('clickedDate');
            var rawDate = new Date(response.date);
            // 날짜를 "yyyy-mm-dd" 형식으로 포맷팅
            var formattedDate = new Intl.DateTimeFormat('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(rawDate);
            var dateParts = formattedDate.split('/'); // '/'를 기준으로 날짜를 분할
            // "10/13/2023"를 "yyyy-mm-dd" 형식으로 변환
            var formattedDate = dateParts[2] + '-' + dateParts[0] + '-' + dateParts[1];
            // formattedDate를 clickedDateElement의 텍스트 내용으로 설정
            clickedDateElement.textContent = formattedDate;
            var addButton = document.getElementById('addCalendar'); // 버튼 엘리먼트를 가져옵니다
            var deleteButton = document.getElementById('deleteCalendar'); // 버튼 엘리먼트를 가져옵니다
            deleteButton.style.display = 'block';
            deleteButton.style.backgroundColor = 'blue'
            addButton.textContent = "수정";
            addButton.onclick = eventUpdate;
            var title = document.getElementById('calendar_title');
            title.value = response.eventTitle;
            var description = document.getElementById('calendar_content');
            description.value = response.eventDescription;
            var eventId2 = document.getElementById('event_id');
            eventId2.value = eventId;
        },
        error: function (xhr, status, error) {
            // 오류 발생 시 처리
            console.error("오류 발생: " + error);
        }
    });
}
function eventInput() {
    const eventDate = document.getElementById('clickedDate').textContent;
    const eventTitle = document.getElementById('calendar_title').value;
    const eventDescription = document.getElementById('calendar_content').value;
//    console.log(eventDate)
    if (eventDate && eventTitle && eventDescription) {
        const eventData = {
            date: eventDate,
            eventTitle: eventTitle,
            eventDescription: eventDescription
        };

        fetch('/calendarEvent/eventInputForm', {
            method: 'POST',
            body: JSON.stringify(eventData),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                // 서버에서 반환한 데이터를 처리합니다.
                console.log(data);

                // 현재 창을 닫음

                // 리다이렉션 수행
                window.location.href = '/calendarEvent/cal';
            })
            .catch(error => {
                console.error('오류 발생: ', error);
            });
    }

}

function eventDelete() {
    const eventId = document.getElementById('event_id').value;

    fetch(`/calendarEvent/eventDeleteForm/${eventId}`, {
        method: 'POST'
    }).then(response => response.json())
        .then(data => {
            if (data) {

                window.location.href = '/calendarEvent/cal';
            } else {
                alert('삭제에 실패 하였습니다.');
            }
        });
}

function eventUpdate() {
    const eventTitle = document.getElementById('calendar_title').value;
    const eventDescription = document.getElementById('calendar_content').value;
    const eventId = document.getElementById('event_id').value;
    console.log(eventId);
    if (eventTitle && eventDescription) {
        const eventData = {
            eventId : eventId,
            eventTitle: eventTitle,
            eventDescription: eventDescription
        };

        fetch('/calendarEvent/eventUpdateForm', {
            method: 'POST',
            body: JSON.stringify(eventData),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            // .then(response => response.json())
            .then(data => {
                // 서버에서 반환한 데이터를 처리합니다.
                console.log(data);

                // 현재 창을 닫음

                // 리다이렉션 수행
                window.location.href = '/calendarEvent/cal';
            })
            .catch(error => {
                console.error('오류 발생: ', error);
            });
    }

}