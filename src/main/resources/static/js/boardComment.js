console.log("boardComment.js in");

// cmtAddBtn 버튼을 클릭하면 입력한 댓글고 작성자, bno 값을 controller 전송
document.getElementById('cmtAddBtn').addEventListener('click',() =>{
    const cmtText= document.getElementById('cmtText');
    const cmtWriter= document.getElementById('cmtWriter');

    if(cmtText == null || cmtText.value.trim() == ''){
        alert('댓글을 입력해주세요.');
        cmtText.focus();
        return false;
    }
    let cmtData = {
        bno: bno,
        writer:cmtWriter.innerText,
        content:cmtText.value
    }
    console.log(cmtData);
    registerCommetnToServer(cmtData).then(result => {
        if (result == '1'){
            alert("댓글 등록 완료");
            // 댓글 입력창 비우고 , 포커스 맞추기
            cmtText.value='';
            cmtWriter.focus();
        }else {
            alert("댓글 등록 실패");
            cmtWriter.focus();
        }
        // 댓글 리스트 호출
        spreadCommentList(bno);
    });
});
    function spreadCommentList(bno, page = 1){
        commentListFromServer(bno, page).then(result =>{
            console.log(result);
            const ul = document.getElementById('cmtListArea');
            if(result.content.length > 0){
                // 댓글이 있는 경우
                let li = '';

                for(let comment of result.content){
                li+=`<li class="list-group-item shadow-sm rounded" data-cno="${comment.cno}">`;
                li+=`<div class="ms-2 me-auto">`;
                li+=`<div class="fw-bold">${comment.writer}</div>`;
                li+=`${comment.content}`;
                li+=`</div>`;
                li+=`<span class="badge rounded-pill text-bg-warning">${comment.regDate}</span>`;
                li+=`<button type="button" class="btn btn-outline-success btn-sm mod" data-bs-toggle="modal" data-bs-target="#commentModal">%</button>`;
                li+=`<button type="button" class="btn btn-outline-danger btn-sm del">X</button>`;
                li+=`</li>`;
                }
                ul.innerHTML += li;

                // page 처리 -> moreBtn data-page = +1
                const moreBtn = document.getElementById('moreBtn');
                if(page < result.totalPages){
                    moreBtn.style.visibility = "visible";
                    moreBtn.dataset.page = parseInt(page) + 1;
                }else {
                    moreBtn.style.visibility = "hidden";
                }

            }else {
                // 댓글이 없는 경우
                ul.innerHTML = `<li class="list-group-item shadow-sm rounded">댓글이 없습니다.</li>`
            }

        });
    }

    document.addEventListener('click',(e)=>{
        if(e.target.classList.contains("mod")){
         // mod 버튼을 클릭하면 수정 할 데이터를 modal 에 띄우기
         // modal 화면에 보낼 데이터 cno, writer, content
            let li = e.target.closest('li');
            let cno = li.dataset.cno;
            let cmtWriter = li.querySelector('.fw-bold').innerText;
            // .fw-bold 밑에 있는 다른 형제
            let cmtText = li.querySelector('.fw-bold').nextSibling;
            console.log(cno);
            console.log(cmtWriter);
            console.log(cmtText.nodeValue);

            document.getElementById('cmtWriterMod').innerHTML =
                `No. ${cno} <b>${cmtWriter}</b>`;

            document.getElementById('cmtTextMod').value = cmtText.nodeValue;
            // cmtModBtn => data-cno="" 속성 추가
            document.getElementById('cmtModBtn').setAttribute("data-cno", cno);
        }
        if(e.target.id == 'cmtModBtn'){
            // modal 수정 버튼 => {cno, content} => 비동기 전송
            let moddata = {
                cno: e.target.dataset.cno,
                content: document.getElementById('cmtTextMod').value
            }
            console.log(moddata);
            // 비동기 전송
            commentupdateToServer(moddata).then(result => {
                if(result == '1'){
                    alert('댓글 수정 완료');
                } else {
                    alert('댓글 수정 실패');
                }
                spreadCommentList(bno);
                document.querySelector(".btn-close").click();
            });
        }
        if(e.target.id == 'moreBtn'){
            // 더보기 버튼을 클릭했을때 => 남아있는 게시글 5개를 더 출력
            spreadCommentList(bno, parseInt(e.target.dataset.page));
        }
        if(e.target.classList.contains("del")){
            // delete button 인지
            // cno 값 추출 => closest (내가 속한 부모 값 찾기)
            let li = e.target.closest('li');
            let cno = li.dataset.cno;
            commentRemoveToServer(cno).then(result =>{
                if(result == "1"){
                    alert("삭제 완료");
                    spreadCommentList(bno);
                }else {
                    alert("삭제 오류");
                }
            })
        }
    });


// 전송 async 데이터 보내기
async function registerCommetnToServer(cmtData){
    try {
        const url = "/comment/post";
        const config = {
            method:'post',
            headers: {
                'content-type':'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtData)
        };

        const response = await fetch(url, config);
        const result = await response.text();
        return result
    }catch (e){
        console.log(e);
    }
}

// list
async function commentListFromServer(bno, page = 1){
    try {
        const response = await fetch(`/comment/list/${bno}/${page}`);
        const result = await response.json();
        return result;
    }catch (e){
        console.log(e);
    }
}

// remove
async function commentRemoveToServer(cno) {
    try {
        // fetch(url, config)
        const response = await fetch("/comment/remove/"+cno, {method:"delete"});
        const result = await response.json();
        return result;
    } catch (e) {
        console.log(e);
    }
}

// update
async function commentupdateToServer(moddata){
    try {
        const url = "/comment/update";
        const config = {
            method:'put',
            headers: {
                'content-type':'application/json; charset=utf-8'
            },
            body: JSON.stringify(moddata)
        }
        const response = await fetch(url, config);
        const result = await response.text();
        return result;
    }catch (e) {
        console.log(e);
    }
}
