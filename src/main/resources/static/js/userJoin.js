console.log("userJoin.js in");

document.getElementById('userBtn').addEventListener("click", async () => {

    // 입력한 이메일 가져오기
    const email = document.getElementById("e").value.trim();

    // 입력값 검사
    if(email === ""){
        alert("이메일을 입력하세요.");
        return;
    }

    try{
        const url = `/user/check?email=${encodeURIComponent(email)}`;

        const response = await fetch(url);

        const result = await response.text();
        console.log(result);

        if(result === "OK"){
            alert("사용 가능한 이메일입니다.");
        }else{
            alert("이미 사용 중인 이메일입니다.");
        }

    }catch(err){
        console.log(err);
        alert("서버 오류");
    }
});