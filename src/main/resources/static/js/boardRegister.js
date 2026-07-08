console.log("boardRegister.js Area");

document.getElementById('trigger').addEventListener('click', () => {
    document.getElementById('file').click();
});

const regExp = new RegExp("\.(exe|sg|bat|dll|jar|msi)$");
const maxSize = 1024 * 1024 * 10;

// 실행파일 막기, 10MB 이상 사이즈 제한
function fileVaild(fileName, fileSize) {
    if (regExp.test(fileName)) {
        return 0;
    } else if (fileSize > maxSize) {
        return 0;
    } else {
        return 1;
    }
}

document.getElementById('file').addEventListener('change', (e) => {
    const fileObject = e.target.files;
    console.log(fileObject); // fileList[]

    const div = document.getElementById('fileZone');
    div.innerHTML = '';
    let ul = `<ul class="list-group list-group-flush">`;
    let isOk = 1;
    for(let file of fileObject){
        let vaild = fileVaild(file.name, file.size);
        isOk *= vaild;
        ul+=`<li class="list-group-item">`;
        ul+=`<div class="mb-3">`;
        ul+=`${vaild ? '<div class="fw-bold mb-2">업로드 가능</div>' :
            '<div class="fw-bold mb-2 text-danger">업로드 불가능</div>'}`;
        ul+=`${file.name}`;
        ul+=`<span class="badge rounded-pill text-bg-${vaild ? 'success' : 'danger'}">${file.size}Byte</span>`;
        ul+=`</div></li>`;
    }
    ul+=`</ul>`;
    div.innerHTML = ul;

    if(isOk == 0){
        // file 중 하나라도 검증을 불통과 했다면
        document.getElementById('regBtn').disabled = true;
    }
});