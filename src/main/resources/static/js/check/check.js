
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: function(info, successCallback, failureCallback) {
            // 서버에서 이벤트 데이터를 가져오는 AJAX 요청
            $.ajax({
                url: '/calendarEvent/check', // 이벤트 데이터를 가져올 엔드포인트 URL
                type: 'GET', // HTTP GET 요청 사용
                success: function(data) {

                    var checkIn = data
                        .filter(function(item) {
                            return item.checkin_time !== null;
                        })
                        .map(function(item) {
                            return {
                                title: item.checkin_time,
                                color: 'blue',
                                start: item.date
                            };
                        });
                        console.log(checkIn)
                    var checkOut = data
                        .filter(function(item) {
                            return item.checkout_time !== null;
                        })
                        .map(function(item) {
                            return {
                                title: item.checkout_time,
                                color: 'red',
                                start: item.date
                            };
                        });

// 데이터를 성공적으로 가져온 경우, checkIn과 checkOut을 합쳐서 한 번만 successCallback에 전달
                    var combinedData = checkIn.concat(checkOut);
                    // console.log(combinedData);
                    successCallback(combinedData);
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