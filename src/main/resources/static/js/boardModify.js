console.log("boardModify.js in");

//수정버튼을 클릭하면 title, content만 readOnly 풀기 (readOnly=false)
document.getElementById('modBtn').addEventListener('click',()=>{
    document.getElementById('t').readOnly = false;
    document.getElementById('c').readOnly = false;
    // Form 태그의 submit 역할을 할 수 있는 버튼 생성
    // 수정, 삭제버튼을 지우고, update 버튼 생성
    // <button type="submit" class="btn btn-success">update</button>

    // <button></button>
    let regBtn = document.createElement('button');
    regBtn.setAttribute('type','submit');
    regBtn.classList.add('btn', 'btn-success');
    regBtn.innerText="update";

    // form 태그의 가장 마지막 요소로 추가
    document.getElementById('modForm').appendChild(regBtn);

    // 수정, 삭제, 리스트 버튼 지우기
    document.getElementById('modBtn').remove();
    document.getElementById('delBtn').remove();
    document.getElementById('listBtn').remove();
})