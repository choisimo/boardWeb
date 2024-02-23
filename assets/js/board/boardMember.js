
function Memberupload() {
    const fileInput = document.getElementById('imageInput');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('mem_id', mem_id);
		
        $.ajax({
            type: 'POST',
            url: './MemberFileUpload',
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
            dataType: 'text',
            success: function (data) {
				$(document).ready(function() {
				     updateProfileImg(mem_id)
				    });
    		     const imgElement = document.getElementById('memberEdit_main_img').querySelector('img');
		 	     imgElement.src = contextPath + data;
		  	 	 imgElement.alt = `member_img+${mem_id}`;

/*			    const imgPreview = document.getElementById('imgPreview');
			    imgPreview.innerHTML = `<img src='${contextPath}${data}' alt='member_img${mem_id}'>`;
*/
            },
            error: function (error) {
                console.error('파일 업로드 실패.', error);
            }
        });
    } else {
        console.error('파일을 선택하세요.');
    }
}


function updateProfileImg(mem_id){
	    $.ajax({
        type: "POST",
        url: "./boardSideBar",
        data: { mem_id : mem_id },
        dataType: "json",
        cache: false, //한번 cache 막아봅시다.
        success: function (response) {
            if (response && response.mem_profile_img !== undefined) {

            $("#profileImage").attr("src", response.mem_profile_img);
            } else {
                console.log("올바르지 않은 JSON format 입니다.");
            }
        },
        error: function (xhr, status, error) {
            console.error("이미지 로딩에 실패했습니다.:", status, error);
        }
    });
}

