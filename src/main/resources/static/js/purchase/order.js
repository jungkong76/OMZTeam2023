
// 셀렉트 박스의 변경 이벤트 리스너를 추가합니다.
const itemSelect = document.getElementById("itemSelect");
const quantityInput = document.getElementById("quantity");
const USDpriceDisplay = document.getElementById("USDpriceDisplay");
const KRWpriceDisplay = document.getElementById("KRWpriceDisplay");
itemSelect.addEventListener("change", updatePrice);
quantityInput.addEventListener("input", updatePrice);

/*function getExchangeRate() {
    const Eurl = "http://apilayer.net/api/live";
    $.ajax({
        url: Eurl,
        data: {
            access_key: "b581dc7676e0253b578c63854fb3134e",
            currencies: "KRW",
            source: "USD",
            format: 1
        },
        success: function (response) {
            // 성공적으로 데이터를 받아왔을 때 처리
            exchangeRate = response.quotes.USDKRW; // 원하는 환율 데이터 가져오기
            $("#exchangeRate").text(exchangeRate.toFixed(2));

        },
        error: function (xhr, status, error) {
            // 오류 발생 시 처리
            console.error("API 호출 중 오류 발생: " + error);
        }
    });
}*/


function updatePrice() {
    // 선택한 품목의 가격 데이터를 가져옵니다.
    const selectedOption = itemSelect.options[itemSelect.selectedIndex];
    const itemPrice = parseFloat(selectedOption.getAttribute("data-price"));


    // 주문 수량을 가져옵니다.
    const quantity = parseFloat(quantityInput.value);
    const exchangeRate = document.getElementById("exchangeRate").textContent;
    const validQuantity = isNaN(quantity) ? 0 : quantity;
    // 가격을 계산하고 표시합니다.
    const totalPrice = (itemPrice * validQuantity).toFixed(3);
    const KRWtotalPrice = (totalPrice * exchangeRate).toFixed(0);
    const formattedTotalPrice = parseFloat(totalPrice).toLocaleString();
    const formattedKRWTotalPrice = parseFloat(KRWtotalPrice).toLocaleString();
    USDpriceDisplay.textContent = isNaN(totalPrice) ? "0" : formattedTotalPrice;
    KRWpriceDisplay.textContent = isNaN(KRWtotalPrice) ? "0" : formattedKRWTotalPrice;

}

function placeOrder(message) {
    // 선택한 품목의 ItemNum 가져오기
    const itemSelect = document.getElementById("itemSelect");
    const selectedOption = itemSelect.options[itemSelect.selectedIndex];
    const itemNum = selectedOption.value;

    // 주문 수량 가져오기
    const quantityInput = document.getElementById("quantity");
    const quantity = parseInt(quantityInput.value);
    $.ajax({
        url: "/purchase/addOrder", // 컨트롤러 엔드포인트 URL
        type: "Get", // POST 요청
        data: {
            itemNum: itemNum,
            quantity: quantity
        },
        success: function (response) {
            // 성공적으로 데이터를 받아왔을 때 처리
            console.log("주문이 성공적으로 추가되었습니다. 응답 데이터:", response);
            alert(`주문이 성공적으로 추가되었습니다.`);

            // 여기에서 추가적인 로직을 수행할 수 있습니다.
            // 페이지 리다이렉트
            window.location.href = "/purchase/order";
        },
        error: function (xhr, status, error) {
            // 오류 발생 시 처리
            console.error("API 호출 중 오류 발생: " + error);
            // 오류 처리 로직을 추가할 수 있습니다.
        },

    });
}

$(document).ready(function () {

    $('#itemSelect').change(function () {
        var selectedOption = $(this).find('option:selected');
        var itemNum = selectedOption.val();
        // Ajax 요청을 보내는 부분
        $.ajax({
            type: 'POST',  // HTTP 요청 메소드 (GET 또는 POST)
            url: '/purchase/select',  // 컨트롤러 URL을 여기에 입력하세요
            data: { itemNum: itemNum},  // 전송할 데이터
            success: function (data) {
                // Ajax 요청이 성공했을 때 실행할 코드
                // data에는 서버로부터 받은 응답이 포함됩니다.
                const exchangeRate2 = document.getElementById("exchangeRate").textContent;
                $("#USDcostPrice").text(parseFloat(data.usdresult).toLocaleString());
                const krwcost = (parseFloat(data.usdresult) * exchangeRate2).toFixed(0);
                const krwcostResult = parseFloat(krwcost).toLocaleString();
                $("#KRWcostPrice").text(krwcostResult);
                $("#ScrapedPrice").text(parseFloat(data.krwresult).toLocaleString())
                const margin = (((data.krwresult/krwcost)-1)*100).toFixed(2);
                $("#margin").text(margin);
                $("#USDScrapedPrice").text((data.krwresult/exchangeRate2).toFixed(2));
                $("#USDdifference").text((data.krwresult/exchangeRate2 - data.usdresult).toFixed(2));
                $("#KRWdifference").text((data.krwresult - krwcost).toLocaleString());
            },
            error: function (xhr, status, error) {
                // Ajax 요청이 실패했을 때 실행할 코드
                console.log('에러 발생: ' + error);
            }
        });
    });
    $.ajax({
        type: 'GET',
        url: '/purchase/exchange',
        success: function (data){
            $("#exchangeRate").text(data);

            }, error: function (xhr, status, error) {
            // Ajax 요청이 실패했을 때 실행할 코드
            console.log('에러 발생: ' + error);
        }

    })
});
